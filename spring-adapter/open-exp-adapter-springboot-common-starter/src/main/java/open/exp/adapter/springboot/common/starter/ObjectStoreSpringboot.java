package open.exp.adapter.springboot.common.starter;

import cn.think.in.java.open.exp.client.ObjectStore;
import cn.think.in.java.open.exp.client.SpiFactory;
import cn.think.in.java.open.exp.client.StringUtil;
import cn.think.in.java.open.exp.core.impl.PluginLifeCycleHookManager;
import lombok.extern.slf4j.Slf4j;
import open.exp.adapter.springboot.common.starter.spi.ObjectScan;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 /**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/8
 **/
@Slf4j
@SuppressWarnings("unchecked")
public class ObjectStoreSpringboot implements ObjectStore {

    private final BeanDefinitionRegistry beanDefinitionRegistry;

    private final ConfigurableListableBeanFactory beanFactory;

    private final Map<String/* pluginId */, List<Class<?>>> classesCache = new HashMap<>();

    private final List<ObjectScan> objectScans;

    public ObjectStoreSpringboot(BeanDefinitionRegistry beanDefinitionRegistry, ConfigurableListableBeanFactory beanFactory) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
        this.beanFactory = beanFactory;
        objectScans = SpiFactory.getList(ObjectScan.class);
        Runtime.getRuntime().addShutdownHook(new GraceShutDownThread(() -> new HashMap<>(classesCache).keySet().forEach(this::unRegisterCallback)));
    }

    @Override
    public void registerCallback(List<Class<?>> classes, String pluginId) throws Exception {
        List<Class<?>> rollbackList = new ArrayList<>();
        objectScans.forEach(e -> e.init(this));
        for (Class<?> aClass : classes) {
            String name = PluginNameBuilder.build(aClass, pluginId);
            try {
                BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(aClass);
                BeanDefinition beanDefinition = builder.getBeanDefinition();
                beanDefinitionRegistry.registerBeanDefinition(name, beanDefinition);
                rollbackList.add(aClass);
                log.info("------->>>> 插件 register beanName:{}", name);
            } catch (Exception e) {
                log.error("插件安装失败, 回滚中......" + e.getMessage(), e);
                unRegisterCallbackAll(pluginId, e, rollbackList);
                throw e;
            }
        }

        objectScans.forEach(e -> e.registerApis(classes, pluginId));
        postInit(pluginId, classes);
        classesCache.put(pluginId, classes);
    }

    private void unRegisterCallbackAll(String pluginId, Exception ex, List<Class<?>> rollbackList) {
        for (Class<?> a : rollbackList) {
            try {
                unRegisterCallback(a, pluginId);
            } catch (Exception exception) {
                log.error("unRegister 失败...." + ex.getMessage(), ex);
            }
        }
    }


    @Override
    public void unRegisterCallback(String pluginId) {

        List<Class<?>> classes = classesCache.get(pluginId);
        if (classes == null) {
            return;
        }

        postDestroy(pluginId, classes);
        objectScans.forEach(e -> e.unregisterApis(pluginId));
        classesCache.remove(pluginId);
    }


    @Override
    public <T> T getObject(Class<?> aClass, String pluginId) {
        if (StringUtil.isEmpty(pluginId)) {
            return (T) beanFactory.getBean(aClass);
        }
        String name = PluginNameBuilder.build(aClass, pluginId);
        if (beanFactory == null) {
            return null;
        }
        Object t = beanFactory.getBean(name);
        return (T) t;
    }

    @Override
    public Object getOrigin() {
        return beanFactory;
    }


    private void unRegisterCallback(Class<?> aClass, String pluginId) {
        String name = PluginNameBuilder.build(aClass, pluginId);
        beanDefinitionRegistry.removeBeanDefinition(name);
        log.warn("------->>>> 插件 unRegister beanName:{}", name);
    }


    private void postInit(String pluginId, List<Class<?>> classes) {
        for (Class<?> aClass : classes) {
            String name = PluginNameBuilder.build(aClass, pluginId);
            try {
                // un lazy
                beanFactory.getBean(name);
            } catch (Exception ignore) {
                //log.warn(ex.getMessage());
            }
        }
        for (Class<?> aClass : classes) {
            String name = PluginNameBuilder.build(aClass, pluginId);
            PluginLifeCycleHookManager.postInit(this, aClass, () -> beanFactory.getBean(name), name);
        }
    }

    private void postDestroy(String pluginId, List<Class<?>> classes) {
        for (Class<?> aClass : classes) {
            unRegisterCallback(aClass, pluginId);
            PluginLifeCycleHookManager.postDestroy(this, aClass, PluginNameBuilder.build(aClass, pluginId));
        }
    }

    static class GraceShutDownThread extends Thread {

        public GraceShutDownThread(Runnable runnable) {
            super(runnable, "eepGraceStop");
        }
    }
}
