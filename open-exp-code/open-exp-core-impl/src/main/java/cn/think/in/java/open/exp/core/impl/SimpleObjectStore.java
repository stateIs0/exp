package cn.think.in.java.open.exp.core.impl;

import cn.think.in.java.open.exp.client.ObjectStore;
import cn.think.in.java.open.exp.client.PluginObjectRegister;
import cn.think.in.java.open.exp.client.TenantObjectProxyFactory;
import cn.think.in.java.open.exp.core.impl.proxy.SortNetSfCglibProxyEnhancer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author cxs
 **/
@SuppressWarnings("unchecked")
public class SimpleObjectStore implements ObjectStore {

    Map<String, Map<String, Object>> pluginIdMapping = new HashMap<>();

    @Override
    public List<Class<?>> startRegister(PluginObjectRegister register, String pluginId) throws Exception {
        Map<String, Object> store = new HashMap<>();
        List<Class<?>> classList = register.register(aClass -> {
            Object proxy = getTenantObjectProxyFactory().getProxy(aClass.newInstance(), pluginId);
            store.put(aClass.getName(), proxy);
        });
        pluginIdMapping.put(pluginId, store);
        return classList;
    }

    @Override
    public void unRegister(String pluginId) {
        pluginIdMapping.remove(pluginId);
    }

    @Override
    public <T> T getObject(String name, String pluginId) {
        Map<String, Object> map = pluginIdMapping.get(pluginId);
        if (map == null) {
            return null;
        }
        return (T) map.get(name);
    }

    @Override
    public TenantObjectProxyFactory getTenantObjectProxyFactory() {
        return SortNetSfCglibProxyEnhancer::getEnhancer;
    }
}
