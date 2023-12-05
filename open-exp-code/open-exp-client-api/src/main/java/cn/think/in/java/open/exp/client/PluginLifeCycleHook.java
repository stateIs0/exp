package cn.think.in.java.open.exp.client;

import java.util.function.Supplier;

/**
 * @Author cxs
 * @Description
 * @date 2023/7/31
 * @version 1.0
 **/
public interface PluginLifeCycleHook {

    void postInit(ObjectStore objectStore, Class<?> clazz, Supplier<Object> bean, String beanName);

    void postDestroy(ObjectStore objectStore, Class<?> aClass, String beanName);

}
