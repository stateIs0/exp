package cn.think.in.java.open.exp.classloader.impl;

import cn.think.in.java.open.exp.classloader.ExpClass;
import cn.think.in.java.open.exp.classloader.ExpPluginMetaService;
import cn.think.in.java.open.exp.classloader.PluginMetaConfig;
import cn.think.in.java.open.exp.classloader.PluginMetaFat;
import cn.think.in.java.open.exp.classloader.support.DirectoryCleaner;
import cn.think.in.java.open.exp.classloader.support.MetaConfigReader;
import cn.think.in.java.open.exp.classloader.support.PluginMetaModel;
import cn.think.in.java.open.exp.client.ExpBoot;
import cn.think.in.java.open.exp.client.PluginObjectRegister;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @Author cxs
 * @Description
 * @date 2023/8/9
 * @version 1.0
 **/
@Slf4j
@SuppressWarnings("unchecked")
public class ExpPluginMetaServiceImpl implements ExpPluginMetaService {

    private PluginMetaConfig pluginMetaConfig = new PluginMetaConfig();

    private Map<String/* pluginId */, PluginMetaFat> cache = new HashMap<>();

    private Map<String/* extCode */, Set<String/* pluginId */>> extCache = new HashMap<>();


    @Override
    public void setConfig(PluginMetaConfig pluginMetaConfig) {
        this.pluginMetaConfig = pluginMetaConfig;
    }

    @Override
    public PluginMetaFat install(File file) throws Throwable {
        PluginMetaModel meta = MetaConfigReader.getMeta(file);
        Map<String, String> mapping = MetaConfigReader.getMapping(file);

        String dir = pluginMetaConfig.getWorkDir() + "/" + meta.getPluginId();
        if (new File(dir).exists()) {
            log.info("exists, delete {}", dir);
            new File(dir).delete();
        }

        PluginMetaFat pluginMetaFat = new PluginMetaFat();
        ClassLoader classLoader = null;
        if (file.getName().endsWith(".jar")) {

            classLoader =
                    new JarExtractorClassLoader(file.getPath(), dir);
            pluginMetaFat.setClassLoader(classLoader);
        } else if (file.getName().endsWith(".zip")) {
            classLoader =
                    new JarExtractorClassLoader(file.getPath(), dir);
            pluginMetaFat.setClassLoader(classLoader);
        }

        if (classLoader == null) {
            throw new RuntimeException("classLoader is null...file " + file.getName());
        }

        Class<ExpBoot> aClass = (Class<ExpBoot>) classLoader.loadClass(meta.getPluginBootClass());
        PluginObjectRegister register = aClass.newInstance().boot();

        pluginMetaFat.setPluginBeanRegister(register);
        pluginMetaFat.setExtensionMappings(mapping);
        pluginMetaFat.setPluginId(meta.getPluginId());
        pluginMetaFat.setPluginCode(meta.getPluginCode());
        pluginMetaFat.setPluginDesc(meta.getPluginDesc());
        pluginMetaFat.setPluginVersion(meta.getPluginVersion());
        pluginMetaFat.setPluginExt(meta.getPluginExt());
        pluginMetaFat.setPluginConfig(meta.getPluginConfig());
        pluginMetaFat.setPluginBootClass(meta.getPluginBootClass());
        pluginMetaFat.setLocation(new File(dir));

        cache.put(pluginMetaFat.getPluginId(), pluginMetaFat);

        for (String extCode : pluginMetaFat.getExtensionMappings().keySet()) {
            Set<String> set = extCache.computeIfAbsent(extCode, k -> new HashSet<>());
            set.add(pluginMetaFat.getPluginId());
        }

        return pluginMetaFat;
    }

    @Override
    public void unInstall(String pluginId) throws IOException {
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
    }

    @Override
    public <T> List<ExpClass<T>> get(Class<T> extClass) throws ClassNotFoundException {
        return get(extClass.getName());
    }

    @Override
    public <T> List<ExpClass<T>> get(String extCode) throws ClassNotFoundException {

        List<ExpClass<T>> re = new ArrayList<>();

        Set<String> set = extCache.get(extCode);
        if (set == null) {
            return new ArrayList<>();
        }
        for (String pluginId : set) {
            re.add(get(extCode, pluginId));
        }
        return re;
    }

    @Override
    public <T> ExpClass<T> get(String extCode, String pluginId) throws ClassNotFoundException {
        PluginMetaFat pluginMetaFat = cache.get(pluginId);
        String extImpl = pluginMetaFat.getExtensionMappings().get(extCode);
        Class<T> aClass = (Class<T>) pluginMetaFat.getClassLoader().loadClass(extImpl);
        return new ExpClass<>(aClass, pluginId);
    }
}
