package cn.think.in.java.open.exp.adapter.springboot2;

import cn.think.in.java.open.exp.classloader.support.UniqueNameUtil;
import cn.think.in.java.open.exp.client.ObjectStore;
import cn.think.in.java.open.exp.client.TenantObjectProxyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.util.Assert;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 **/
@Slf4j
@SuppressWarnings("unchecked")
public class SpringBootObjectStore implements ObjectStore {

    private final Map<String, RestUrlScanComponent> cache = new HashMap<>();

    private final BeanDefinitionRegistry beanDefinitionRegistry;

    private ConfigurableListableBeanFactory beanFactory;

    private final Map<String, List<Class<?>>> classesCache = new HashMap<>();

    private Supplier<String> pluginsSpringUrlReplaceKey;

    private final SpringBootTenantObjectPostProcessorFactory tenantObjectProxy = new SpringBootTenantObjectPostProcessorFactory();

    public SpringBootObjectStore(BeanDefinitionRegistry beanDefinitionRegistry, Supplier<String> pluginsSpringUrlReplaceKey) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
        this.pluginsSpringUrlReplaceKey = pluginsSpringUrlReplaceKey;
    }

    public void setBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        if (beanFactory == null) {
            throw new RuntimeException("beanFactory can not be null");
        }
        this.beanFactory = beanFactory;
        this.beanFactory.addBeanPostProcessor(tenantObjectProxy);
    }

    @Override
    public void startRegister(List<Class<?>> list, String pluginId) throws Exception {

        List<Class<?>> rollbackList = new ArrayList<>();
        for (Class<?> aClass : list) {
            try {
                String name = UniqueNameUtil.getName(aClass, pluginId);
                BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(aClass);
                BeanDefinition beanDefinition = builder.getBeanDefinition();
                beanDefinitionRegistry.registerBeanDefinition(name, beanDefinition);
                log.info("注册 spring bean {} {}", aClass.getName(), name);
                rollbackList.add(aClass);
            } catch (Exception e) {
                for (Class<?> a : rollbackList) {
                    unRegisterCallback(a, pluginId);
                }
                throw e;
            }
        }


        handleMappings(pluginId, list);

        classesCache.put(pluginId, list);
    }

    private void handleMappings(String pluginId, List<Class<?>> classes) {
        RequestMappingHandlerMapping mapping = (RequestMappingHandlerMapping) beanFactory.getBean("requestMappingHandlerMapping");
        RequestMappingHandlerAdapter adapter = (RequestMappingHandlerAdapter) beanFactory.getBean("requestMappingHandlerAdapter");
        for (Class<?> aClass : classes) {
            try {
                beanFactory.getBean(UniqueNameUtil.getName(aClass, pluginId));
            } catch (Exception e) {
                // ignore
            }
        }
        for (Class<?> aClass : classes) {
            Object bean = beanFactory.getBean(UniqueNameUtil.getName(aClass, pluginId));

            RestUrlScanComponent r = new RestUrlScanComponent(bean, mapping, adapter, pluginsSpringUrlReplaceKey.get());
            r.register();
            cache.put(UniqueNameUtil.getName(aClass, pluginId), r);
        }
    }

    @Override
    public void unRegister(String pluginId) {
        List<Class<?>> classes = classesCache.get(pluginId);
        if (classes == null) {
            log.warn("no class need un register");
            return;
        }
        for (Class<?> aClass : classes) {
            unRegisterCallback(aClass, pluginId);
        }
        classesCache.remove(pluginId);
    }

    private void unRegisterCallback(Class<?> aClass, String pluginId) {
        String name = UniqueNameUtil.getName(aClass, pluginId);
        RestUrlScanComponent urlComponent = cache.get(name);
        if (urlComponent != null) {
            urlComponent.unRegister();
            cache.remove(name);
        }
    }

    @Override
    public <T> T getObject(Class<?> c, String pluginId) {
        if (beanFactory == null) {
            return null;
        }
        String name = UniqueNameUtil.getName(c.getName(), pluginId);
        return (T) beanFactory.getBean(name);
    }

    @Override
    public TenantObjectProxyFactory getTenantObjectProxyFactory() {
        return tenantObjectProxy;
    }
}
