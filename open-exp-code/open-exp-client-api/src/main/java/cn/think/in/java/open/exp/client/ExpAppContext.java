package cn.think.in.java.open.exp.client;

import java.io.File;
import java.util.List;
import java.util.Optional;

/**
 * @author cxs
 */
public interface ExpAppContext extends StreamAppContext {

    /**
     * 获取当前所有的插件 id
     */
    List<String> getAllPluginId();

    /**
     * 预加载, 只读取元信息和 load boot class 和配置, 不做 bean 加载.
     */
    Plugin preLoad(File file);

    /**
     * 加载插件
     */
    Plugin load(File file) throws Throwable;

    /**
     * 卸载插件
     */
    void unload(String pluginId) throws Exception;

    /**
     * 获取多个扩展点的插件实例
     */
    <P> List<P> get(String extCode);

    /**
     * 获取多个扩展点的插件实例
     */
    <P> List<P> get(Class<P> pClass);

    /**
     * 获取单个插件实例.
     */
    <P> Optional<P> get(String extCode, String pluginId);

}
