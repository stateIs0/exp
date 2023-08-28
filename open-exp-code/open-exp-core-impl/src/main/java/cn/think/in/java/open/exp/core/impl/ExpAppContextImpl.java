package cn.think.in.java.open.exp.core.impl;

import cn.think.in.java.open.exp.classloader.ExpClass;
import cn.think.in.java.open.exp.classloader.PluginMetaFat;
import cn.think.in.java.open.exp.classloader.PluginMetaService;
import cn.think.in.java.open.exp.client.*;
import cn.think.in.java.open.exp.core.impl.support.PluginModelMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author cxs
 **/
@Slf4j
@SuppressWarnings("unchecked")
public class ExpAppContextImpl implements ExpAppContext {

    private PluginMetaService metaService;
    private ObjectStore objectStore = new SimpleObjectStore();
    private final List<String> all = new ArrayList<>();
    private PluginFilter filter = SpiFactory.get(PluginFilter.class);

    public ExpAppContextImpl() {
    }

    public ExpAppContextImpl(PluginMetaService metaService) {
        this.metaService = metaService;
    }

    @Override
    public List<String> getAllPluginId() {
        return new ArrayList<>(all);
    }

    @Override
    public Plugin preLoad(File file) {
        return PluginModelMapper.instance.conv(metaService.parse(file));
    }

    @Override
    public Plugin load(File file) throws Throwable {
        PluginMetaFat fat = metaService.install(file);
        List<Class<?>> classes = fat.getScanner().scan();
        objectStore.startRegister(classes, fat.getPluginId());
        all.add(fat.getPluginId());
        log.info("安装加载插件, 插件 ID = [{}], 配置={}", fat.getPluginId(), fat.getConfigSupportList());
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
        try {
            List<ExpClass<P>> classes = metaService.get(extCode);

            if (classes == null) {
                return new ArrayList<>();
            }

            if (filter == null) {
                return (List<P>) classes.stream().map(c -> objectStore.getObject(c.getAClass(), c.getPluginId())).collect(Collectors.toList());
            }

            return filter.filter(classes.stream().map(c -> {
                P bean = objectStore.getObject(c.getAClass(), c.getPluginId());
                return new PluginFilter.FModel<>(bean, c.getPluginId());
            }).collect(Collectors.toList())).stream().map(PluginFilter.FModel::getT).collect(Collectors.toList());
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
    public <R, P> R streamList(Class<P> pClass, Ec<R, List<P>> ecs) {
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
