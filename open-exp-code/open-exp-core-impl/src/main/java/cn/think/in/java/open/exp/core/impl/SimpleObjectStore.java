package cn.think.in.java.open.exp.core.impl;

import cn.think.in.java.open.exp.client.ObjectStore;

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
    public void registerCallback(List<Class<?>> list, String pluginId) throws Exception {
        Map<String, Object> store = new HashMap<>();
        list.forEach(aClass -> {
            try {
                store.put(aClass.getName(), aClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
        pluginIdMapping.put(pluginId, store);
    }

    @Override
    public void unRegisterCallback(String pluginId) {
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
    public Object getOrigin() {
        return pluginIdMapping;
    }
}
