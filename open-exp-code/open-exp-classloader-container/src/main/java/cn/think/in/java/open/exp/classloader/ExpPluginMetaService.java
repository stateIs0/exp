package cn.think.in.java.open.exp.classloader;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @Author cxs
 * @Description
 * @date 2023/8/9
 * @version 1.0
 **/
public interface ExpPluginMetaService {

    void setConfig(PluginMetaConfig pluginMetaConfig);

    PluginMetaFat install(File file) throws Throwable;

    void unInstall(String pluginId) throws IOException;

    <T> List<ExpClass<T>> get(Class<T> extClass) throws ClassNotFoundException;

    <T> List<ExpClass<T>> get(String extCode) throws ClassNotFoundException;

    <T> ExpClass<T> get(String extCode, String pluginId) throws ClassNotFoundException;
}
