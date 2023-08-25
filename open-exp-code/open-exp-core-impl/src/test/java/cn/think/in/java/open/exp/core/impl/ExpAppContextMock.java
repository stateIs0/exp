package cn.think.in.java.open.exp.core.impl;

import cn.think.in.java.open.exp.client.Ec;
import cn.think.in.java.open.exp.client.ExpAppContext;
import cn.think.in.java.open.exp.client.Plugin;
import cn.think.in.java.open.exp.client.TenantCallback;

import java.io.File;
import java.util.List;
import java.util.Optional;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/12
 **/
public class ExpAppContextMock implements ExpAppContext {

    @Override
    public List<String> getAllPluginId() {
        return null;
    }

    @Override
    public Plugin preLoad(File file) {
        return null;
    }

    @Override
    public Plugin load(File file) throws Throwable {
        return null;
    }

    @Override
    public void unload(String pluginId) throws Exception {

    }

    @Override
    public <P> List<P> get(String extCode) {
        return null;
    }

    @Override
    public <P> List<P> get(Class<P> pClass) {
        return null;
    }

    @Override
    public <P> Optional<P> get(String extCode, String pluginId) {
        return Optional.ofNullable(null);
    }

    @Override
    public <P> List<P> get(String extCode, TenantCallback callback) {
        return null;
    }

    @Override
    public <P> List<P> get(Class<P> pClass, TenantCallback callback) {
        return null;
    }

    @Override
    public <R, P> R streamList(Class<P> pClass, Ec<R, List<P>> ecs) {
        return null;
    }

    @Override
    public <R, P> R stream(Class<P> clazz, String pluginId, Ec<R, P> ec) {
        return null;
    }
}
