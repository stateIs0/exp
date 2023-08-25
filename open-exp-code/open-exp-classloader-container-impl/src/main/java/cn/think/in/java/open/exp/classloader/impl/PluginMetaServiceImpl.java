package cn.think.in.java.open.exp.classloader.impl;

import cn.think.in.java.open.exp.classloader.*;
import cn.think.in.java.open.exp.classloader.support.ClassLoaderFinder;
import cn.think.in.java.open.exp.classloader.support.DirectoryCleaner;
import cn.think.in.java.open.exp.classloader.support.MetaConfigReader;
import cn.think.in.java.open.exp.classloader.support.PluginMetaInnerModel;
import cn.think.in.java.open.exp.client.ConfigSupport;
import cn.think.in.java.open.exp.client.ExpBoot;
import cn.think.in.java.open.exp.client.PluginObjectScanner;
import cn.think.in.java.open.exp.client.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author cxs
 **/
@Slf4j
@SuppressWarnings("unchecked")
public class PluginMetaServiceImpl implements PluginMetaService {

    private PluginMetaConfig pluginMetaConfig = new PluginMetaConfig();

    private Map<String/* pluginId */, PluginMetaFat> cache = new HashMap<>();

    private Map<String/* extCode */, Set<String/* pluginId */>> extCache = new HashMap<>();

    private Set<String> ids = new HashSet<>();

    public static List<ConfigSupport> getConfigSupportFields(Class<?> clazz, String pluginId) {
        return Stream.of(clazz.getDeclaredFields())
                .filter(field -> java.lang.reflect.Modifier.isStatic(field.getModifiers()))
                .filter(field -> field.getType().equals(ConfigSupport.class))
                .peek(field -> field.setAccessible(true))
                .map(field -> {
                    try {
                        ConfigSupport c = (ConfigSupport) field.get(clazz);
                        c.setPluginId(pluginId);
                        return c;
                    } catch (IllegalAccessException e) {
                        log.error(e.getMessage(), e);
                        return null;
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public void setConfig(PluginMetaConfig pluginMetaConfig) {
        this.pluginMetaConfig = pluginMetaConfig;
    }

    @Override
    public PluginMetaThin parse(File file) {
        PluginMetaInnerModel meta = MetaConfigReader.getMeta(file);
        return meta.conv();
    }


    @Override
    public PluginMetaFat install(File file) throws Throwable {
        PluginMetaInnerModel meta = MetaConfigReader.getMeta(file);
        if (ids.contains(meta.getPluginId())) {
            throw new RuntimeException("不要重复安装插件 " + meta.getPluginId());
        }
        Map<String, String> mapping = MetaConfigReader.getMapping(file);

        String dir = pluginMetaConfig.getWorkDir() + "/" + meta.getPluginId();
        if (new File(dir).exists()) {
            log.warn("---->>>>> 插件目录已经存在, 删除 = {}", dir);
            if (Boolean.parseBoolean(pluginMetaConfig.getAutoDelete())) {
                new File(dir).delete();
            } else {
                throw new RuntimeException("插件目录已经存在, 请先卸载再安装.");
            }
        }

        PluginMetaFat pluginMetaFat = new PluginMetaFat();
        ClassLoader classLoader = ClassLoaderFinder.find(file, dir);
        pluginMetaFat.setClassLoader(classLoader);

        Class<ExpBoot> aClass = (Class<ExpBoot>) classLoader.loadClass(meta.getPluginBootClass());
        ExpBoot expBoot = aClass.newInstance();
        PluginObjectScanner register = expBoot.getRegister();

        pluginMetaFat.setScanner(register);
        pluginMetaFat.setExtensionMappings(mapping);
        pluginMetaFat.setPluginId(meta.getPluginId());
        pluginMetaFat.setPluginCode(meta.getPluginCode());
        pluginMetaFat.setPluginDesc(meta.getPluginDesc());
        pluginMetaFat.setPluginVersion(meta.getPluginVersion());
        pluginMetaFat.setPluginExt(meta.getPluginExt());
        pluginMetaFat.setPluginBootClass(meta.getPluginBootClass());
        pluginMetaFat.setLocation(new File(dir));
        pluginMetaFat.setConfigSupportList(getConfigSupportFields(aClass, meta.getPluginId()));

        cache.put(pluginMetaFat.getPluginId(), pluginMetaFat);

        for (String extCode : pluginMetaFat.getExtensionMappings().keySet()) {
            Set<String> set = extCache.computeIfAbsent(extCode, k -> new HashSet<>());
            set.add(pluginMetaFat.getPluginId());
        }

        ids.add(pluginMetaFat.getPluginId());

        return pluginMetaFat;
    }

    @Override
    public void unInstall(String pluginId) {
        PluginMetaFat pluginMetaFat = cache.get(pluginId);
        if (pluginMetaFat == null) {
            log.warn("请检查你的 pluginId 是否正确 {} ", pluginId);
            return;
        }
        File location = pluginMetaFat.getLocation();

        DirectoryCleaner.deleteDirectoryContents(location);
        cache.remove(pluginId);
        for (Map.Entry<String, Set<String>> entry : extCache.entrySet()) {
            entry.getValue().removeIf(s -> s.equals(pluginId));
        }

        ids.remove(pluginId);
    }

    @Override
    public <T> List<ExpClass<T>> get(Class<T> extClass) throws ClassNotFoundException {
        return get(extClass.getName());
    }

    @Override
    public <T> List<ExpClass<T>> get(String extCode) throws ClassNotFoundException {

        List<ExpClass<T>> re = new ArrayList<>();

        Set<String> pluginIdSet = extCache.get(extCode);
        if (pluginIdSet == null) {
            return new ArrayList<>();
        }
        for (String pluginId : pluginIdSet) {
            re.add(get(extCode, pluginId));
        }
        return re;
    }

    @Override
    public <T> ExpClass<T> get(String extCode, String pluginId) throws ClassNotFoundException {
        PluginMetaFat pluginMetaFat = cache.get(pluginId);
        if (pluginMetaFat == null) {
            return null;
        }
        String extImpl = pluginMetaFat.getExtensionMappings().get(extCode);
        if (StringUtil.isEmpty(extImpl)) {
            return null;
        }
        Class<T> aClass = (Class<T>) pluginMetaFat.getClassLoader().loadClass(extImpl);
        return new ExpClass<>(aClass, pluginId);
    }

}
