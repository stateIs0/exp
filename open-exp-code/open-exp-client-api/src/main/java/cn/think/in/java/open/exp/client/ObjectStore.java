package cn.think.in.java.open.exp.client;

import java.util.List;

/**
 * 对象存储仓库, 默认是存在 spring 容器里.
 * 可以实现为 map 之类的.
 **/
public interface ObjectStore {

    /**
     * 注册插件到对象仓库
     * @param register 插件注册器
     * @param pluginId 插件 id;
     * @return 有哪些 class
     * @throws Exception 可能的异常
     */
    List<Class<?>> startRegister(PluginObjectRegister register, String pluginId) throws Exception;

    /**
     * 从对象仓库反注册插件
     */
    void unRegister(String pluginId);

    /**
     * 从对象仓库获取某个对象
     */
    <T> T getObject(String name, String pluginId);


    /**
     * 获取代理租户对象工厂.
     */
    TenantObjectProxyFactory getTenantObjectProxyFactory();
}
