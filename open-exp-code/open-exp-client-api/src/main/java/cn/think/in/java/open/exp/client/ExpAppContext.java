package cn.think.in.java.open.exp.client;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author cxs
 */
public interface ExpAppContext extends TenantService {

    Set<String> getAllPluginId();

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
     * 简化操作, code 就是全路径类名
     */
    <P> List<P> get(Class<P> pClass);


    /**
     * 获取单个插件实例.
     */
    <P> Optional<P> get(String extCode, String pluginId);

}
