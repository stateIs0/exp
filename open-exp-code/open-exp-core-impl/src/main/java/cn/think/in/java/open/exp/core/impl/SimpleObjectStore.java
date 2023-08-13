package cn.think.in.java.open.exp.core.impl;

import cn.think.in.java.open.exp.client.ObjectStore;
import cn.think.in.java.open.exp.client.TenantObjectProxyFactory;
import cn.think.in.java.open.exp.core.impl.proxy.PluginIdNetSfCglibProxyEnhancer;

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
    public void startRegister(List<Class<?>> list, String pluginId) throws Exception {
        Map<String, Object> store = new HashMap<>();
        list.forEach(aClass -> {
            Object proxy;
            try {
                proxy = getTenantObjectProxyFactory().getProxy(aClass.newInstance(), pluginId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            store.put(aClass.getName(), proxy);
        });
        pluginIdMapping.put(pluginId, store);
    }

    @Override
    public void unRegister(String pluginId) {
        pluginIdMapping.remove(pluginId);
    }

    @Override
    public <T> T getObject(Class<?> c, String pluginId) {
        Map<String, Object> map = pluginIdMapping.get(pluginId);
        if (map == null) {
            return null;
        }
        return (T) map.get(c.getName());
    }

    @Override
    public TenantObjectProxyFactory getTenantObjectProxyFactory() {
        return PluginIdNetSfCglibProxyEnhancer::getEnhancer;
    }
}
