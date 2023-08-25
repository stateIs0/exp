package cn.think.in.java.open.exp.classloader;

import cn.think.in.java.open.exp.client.SpiFactory;

import java.io.File;
import java.util.List;

/**
 * @Author cxs
 **/
public interface PluginMetaService {

    static PluginMetaService getSpi() {
        return SpiFactory.get(PluginMetaService.class);
    }

    void setConfig(PluginMetaConfig pluginMetaConfig);

    PluginMetaThin parse(File file);

    PluginMetaFat install(File file) throws Throwable;

    void unInstall(String pluginId) throws Exception;

    <T> List<ExpClass<T>> get(Class<T> extClass) throws ClassNotFoundException;

    <T> List<ExpClass<T>> get(String extCode) throws ClassNotFoundException;

    <T> ExpClass<T> get(String extCode, String pluginId) throws ClassNotFoundException;
}
