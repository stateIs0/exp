package cn.think.in.java.open.exp.adapter.springboot2;

import cn.think.in.java.open.exp.classloader.support.UniqueNameUtil;
import cn.think.in.java.open.exp.client.PluginObjectRegister;
import cn.think.in.java.open.exp.client.ObjectStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 **/
@Slf4j
@SuppressWarnings("unchecked")
public class ObjectStoreForSpringBoot implements ObjectStore {

    private final Map<String, RestUrlScanComponent> cache = new HashMap<>();

    private final BeanDefinitionRegistry beanDefinitionRegistry;

    private BeanFactory beanFactory;

    private Map<String, List<Class<?>>> classesMap = new HashMap<>();

    public ObjectStoreForSpringBoot(BeanDefinitionRegistry beanDefinitionRegistry) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public List<Class<?>> registerCallback(PluginObjectRegister register, String pluginId) throws Exception {

        List<Class<?>> rollbackList = new ArrayList<>();

        List<Class<?>> classes = register.register((aClass) -> {
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
        });

        if (beanFactory != null) {
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

                RestUrlScanComponent r = new RestUrlScanComponent(bean, mapping, adapter);
                r.register();
                cache.put(UniqueNameUtil.getName(aClass, pluginId), r);
            }
        }
        classesMap.put(pluginId, classes);
        return classes;
    }

    @Override
    public void unRegisterCallback(String pluginId) {
        List<Class<?>> classes = classesMap.get(pluginId);
        if (classes == null) {
            log.warn("no class need un register");
            return;
        }
        for (Class<?> aClass : classes) {
            unRegisterCallback(aClass, pluginId);
        }
        classesMap.remove(pluginId);
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
    public <T> T getObject(String name) {
        if (beanFactory == null) {
            return null;
        }
        return (T) beanFactory.getBean(name);
    }
}
