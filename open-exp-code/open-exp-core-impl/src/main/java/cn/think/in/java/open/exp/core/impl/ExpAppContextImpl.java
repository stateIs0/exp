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

    private PluginMetaService metaService;
    private ObjectStore objectStore;


    @Override
    public Plugin load(File file) throws Throwable {
        PluginMetaFat fat = metaService.install(file);
        objectStore.startRegister(fat.getRegister(), fat.getPluginId());
        log.info("安装加载插件, 插件 ID = [{}]", fat.getPluginId());
        return fat.conv();
    }

    @Override
    public void unload(String pluginId) throws Exception {
        objectStore.unRegister(pluginId);
        metaService.unInstall(pluginId);
        log.info("卸载插件 {}", pluginId);
    }

    @Override
    public <P> List<P> get(String extCode) {
        return get(extCode, TenantCallback.TenantCallbackMock.DEFAULT);
    }

    @Override
    public <P> List<P> get(String extCode, TenantCallback callback) {
        try {
            List<ExpClass<P>> classes = metaService.get(extCode);

            List<SortObj<P>> sortObjList = new ArrayList<>();

            for (ExpClass<P> aClass : classes) {
                if (callback == null) {
                    P bean = objectStore.getObject(aClass.getAClass().getName(), aClass.getPluginId());
                    sortObjList.add(new SortObj<>(bean, 0));
                    continue;
                }
                if (!callback.filter(aClass.getPluginId())) {
                    continue;
                }

                P bean = objectStore.getObject(aClass.getAClass().getName(), aClass.getPluginId());
                sortObjList.add(new SortObj<>(bean, callback.getSort(aClass.getPluginId())));
            }

            return sortObjList.stream().sorted().map(pSortObj -> pSortObj.obj).collect(Collectors.toList());
        } catch (
                Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public <P> List<P> get(Class<P> pClass) {
        return get(pClass.getName());
    }

    @Override
    public <P> List<P> get(Class<P> pClass, TenantCallback callback) {
        return get(pClass.getName(), callback);
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

    public void setPluginMetaService(PluginMetaService pluginMetaService) {
        this.metaService = pluginMetaService;
    }

    public void setObjectStore(ObjectStore objectStore) {
        this.objectStore = objectStore;
    }
}
