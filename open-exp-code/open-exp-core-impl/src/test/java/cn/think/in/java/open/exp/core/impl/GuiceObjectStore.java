package cn.think.in.java.open.exp.core.impl;

import cn.think.in.java.open.exp.client.ObjectStore;
import cn.think.in.java.open.exp.client.TenantObjectProxyFactory;

import java.util.List;

/**
 * todo
 *
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/12
 **/
public class GuiceObjectStore implements ObjectStore {

    private ObjectStore objectStore;

    @Override
    public void startRegister(List<Class<?>> list, String pluginId) throws Exception {

    }

    @Override
    public void unRegister(String pluginId) {

    }

    @Override
    public <T> T getObject(Class<?> c, String pluginId) {
        return null;
    }

    @Override
    public Object getOrigin() {
        return null;
    }

}
