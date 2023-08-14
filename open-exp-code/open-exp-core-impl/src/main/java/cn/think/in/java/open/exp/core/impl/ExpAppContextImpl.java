package cn.think.in.java.open.exp.core.impl;

import cn.think.in.java.open.exp.classloader.ExpClass;
import cn.think.in.java.open.exp.classloader.PluginMetaFat;
import cn.think.in.java.open.exp.classloader.PluginMetaService;
import cn.think.in.java.open.exp.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author cxs
 **/
@Slf4j
public class ExpAppContextImpl implements ExpAppContext {

    private PluginMetaService metaService;
    private ObjectStore objectStore;
    private Set<String> all = new HashSet<>();


    @Override
    public Set<String> getAllPluginId() {
        return all;
    }

    @Override
    public Plugin load(File file) throws Throwable {
        PluginMetaFat fat = metaService.install(file);
        List<Class<?>> classes = fat.getScanner().scan();
        objectStore.startRegister(classes, fat.getPluginId());
        all.add(fat.getPluginId());
        log.info("安装加载插件, 插件 ID = [{}]", fat.getPluginId());
        return fat.conv();
    }

    @Override
    public void unload(String pluginId) throws Exception {
        objectStore.unRegister(pluginId);
        metaService.unInstall(pluginId);
        all.remove(pluginId);
        log.info("卸载插件 {}", pluginId);
    }

    @Override
    public <P> List<P> get(String extCode) {
        return get(extCode, TenantCallback.DEFAULT);
    }

    @Override
    public <P> List<P> get(String extCode, TenantCallback callback) {
        try {
            List<ExpClass<P>> classes = metaService.get(extCode);

            if (classes == null) {
                return new ArrayList<>();
            }

            List<SortObj<P>> sortObjList = new ArrayList<>();

            classes.forEach(c -> {
                if (callback == null) {
                    P bean = objectStore.getObject(c.getAClass(), c.getPluginId());
                    sortObjList.add(new SortObj<>(bean, 0));
                    return;
                }
                if (!callback.filter(c.getPluginId())) {
                    return;
                }

                P bean = objectStore.getObject(c.getAClass(), c.getPluginId());
                sortObjList.add(new SortObj<>(bean, callback.getSort(c.getPluginId())));
            });

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
    public <P> Optional<P> get(String extCode, String pluginId) {
        try {
            ExpClass<P> classZ = metaService.get(extCode, pluginId);
            if (classZ == null) {
                return Optional.empty();
            }
            return Optional.ofNullable(objectStore.getObject(classZ.getAClass(), classZ.getPluginId()));
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

    @Override
    public <R, P> R listStream(Class<P> pClass, Ec<R, List<P>> ecs) {
        List<P> list = get(pClass);
        if (list == null) {
            return null;
        }
        return ecs.run(list);
    }

    @Override
    public <R, P> R stream(Class<P> clazz, String pluginId, Ec<R, P> ec) {
        Optional<P> o = get(clazz.getName(), pluginId);
        return o.map(ec::run).orElse(null);
    }
}
