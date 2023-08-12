package cn.think.in.java.open.exp.core.impl;

import cn.think.in.java.open.exp.classloader.ExpClass;
import cn.think.in.java.open.exp.classloader.PluginMetaFat;
import cn.think.in.java.open.exp.classloader.PluginMetaService;
import cn.think.in.java.open.exp.client.ExpAppContext;
import cn.think.in.java.open.exp.client.ObjectStore;
import cn.think.in.java.open.exp.client.Plugin;
import cn.think.in.java.open.exp.client.TenantCallback;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author cxs
 **/
@Slf4j
public class ExpAppContextImpl implements ExpAppContext {

    PluginMetaService metaService;
    ObjectStore objectStore;
    TenantCallback tenantCallback;


    public void setPluginMetaService(PluginMetaService pluginMetaService) {
        this.metaService = pluginMetaService;
    }

    public void setObjectStore(ObjectStore objectStore) {
        this.objectStore = objectStore;
    }

    @Override
    public Plugin load(File file) throws Throwable {
        PluginMetaFat install = metaService.install(file);
        objectStore.startRegister(install.getRegister(), install.getPluginId());
        log.info("安装加载插件 {}", install.getPluginId());
        return install.conv();
    }

    @Override
    public void unload(String pluginId) throws Exception {
        objectStore.unRegister(pluginId);
        metaService.unInstall(pluginId);
        log.info("卸载插件 {}", pluginId);
    }

    @Override
    public <P> List<P> get(String extCode) {
        try {
            List<ExpClass<P>> classes = metaService.get(extCode);
            List<P> result = new ArrayList<>();
            for (ExpClass<P> aClass : classes) {
                Boolean ownCurrentTenant = getTenantCallback().isOwnCurrentTenant(aClass.getPluginId());
                if (ownCurrentTenant == null) {
                    ownCurrentTenant = true;
                }
                if (!ownCurrentTenant) {
                    continue;
                }
                P bean = objectStore.getObject(aClass.getAClass().getName(), aClass.getPluginId());
                if (bean != null) {
                    result.add(bean);
                }
            }
            return result.stream().sorted().collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <P> List<P> get(Class<P> pClass) {
        return get(pClass.getName());
    }

    @Override
    public <P> P get(String extCode, String pluginId) {
        try {
            ExpClass<P> classZ = metaService.get(extCode, pluginId);
            return objectStore.getObject(classZ.getAClass().getName(), classZ.getPluginId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TenantCallback getTenantCallback() {
        if (this.tenantCallback == null) {
            return TenantCallback.TenantCallbackMock.instance;
        }
        return this.tenantCallback;
    }

    @Override
    public void setTenantCallback(TenantCallback callback) {
        if (callback == null) {
            throw new RuntimeException("callback can not be null");
        }
        if (this.tenantCallback != null) {
            throw new RuntimeException("不能重复设置 tenantCallback");
        }
        this.tenantCallback = callback;
    }
}
