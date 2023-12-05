package cn.think.in.java.open.exp.core.impl;

import cn.think.in.java.open.exp.client.ExpAppContext;
import cn.think.in.java.open.exp.client.ExpAppContextSpiFactory;
import cn.think.in.java.open.exp.client.ObjectStore;
import cn.think.in.java.open.exp.client.PluginLifeCycleHook;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class PluginLifeCycleHookManager {

    private static final ExpAppContext APP_CONTEXT = ExpAppContextSpiFactory.getFirst();

    static Map<String, PluginLifeCycleHook> cache = new HashMap<>();

    public static void postInit(ObjectStore objectStore, Class<?> clazz, Supplier<Object> bean, String beanName) {
        cache.values().forEach(c -> c.postInit(objectStore, clazz, bean, beanName));
    }


    public static void postDestroy(ObjectStore objectStore, Class<?> aC, String beanName) {
        cache.values().forEach(c -> c.postDestroy(objectStore, aC, beanName));
    }

    /**
     * 插件安装时添加钩子
     */
    public static void addHook(String pluginId) {
        PluginLifeCycleHook hook = (PluginLifeCycleHook) APP_CONTEXT.get(PluginLifeCycleHook.class.getName(), pluginId).orElse(null);
        if (hook != null) {
            cache.put(pluginId, hook);
        }
    }

    /**
     * 插件卸载时删除钩子
     */
    public static void removeHook(String pluginId) {
        cache.remove(pluginId);
    }
}
