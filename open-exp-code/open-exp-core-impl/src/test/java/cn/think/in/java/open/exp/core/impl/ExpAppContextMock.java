package cn.think.in.java.open.exp.core.impl;

import cn.think.in.java.open.exp.client.ExpAppContext;
import cn.think.in.java.open.exp.client.Plugin;
import cn.think.in.java.open.exp.client.TenantCallback;

import java.io.File;
import java.util.List;

/**
 * @Author cxs
 * @Description
 * @date 2023/8/12
 * @version 1.0
 **/
public class ExpAppContextMock implements ExpAppContext {

    @Override
    public Plugin load(File file) throws Throwable {
        return null;
    }

    @Override
    public void unload(String id) throws Exception {

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
    public <P> P get(String extCode, String pluginId) {
        return null;
    }

    @Override
    public <P> List<P> get(String extCode, TenantCallback callback) {
        return null;
    }

    @Override
    public <P> List<P> get(Class<P> pClass, TenantCallback callback) {
        return null;
    }
}
