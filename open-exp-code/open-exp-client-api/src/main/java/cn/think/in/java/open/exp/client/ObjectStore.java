package cn.think.in.java.open.exp.client;

import java.util.List;

/**
 * 对象存储仓库, 默认是存在 spring 容器里.
 * 可以实现为 map 之类的.
 **/
public interface ObjectStore {

    /**
     * 注册插件到对象仓库
     *
     * @param list     插件里的核心 class, expBean 标记的 class.
     * @param pluginId 插件 id;
     */
    void startRegister(List<Class<?>> list, String pluginId) throws Exception;

    /**
     * 从对象仓库将这个插件的东西全部删除.
     */
    void unRegister(String pluginId);

    /**
     * 从对象仓库获取某个对象
     */
    <T> T getObject(Class<?> c, String pluginId);

    Object getOrigin();
}
