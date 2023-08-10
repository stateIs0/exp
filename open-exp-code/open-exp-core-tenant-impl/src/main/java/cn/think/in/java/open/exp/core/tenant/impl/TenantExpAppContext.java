package cn.think.in.java.open.exp.core.tenant.impl;

import cn.think.in.java.open.exp.client.Plugin;

import java.io.File;
import java.util.List;
import java.util.Optional;

/**
 * @Author cxs
 * @Description
 * @date 2023/8/10
 * @version 1.0
 **/
public interface TenantExpAppContext {


    /**
     * 装载
     */
    Plugin load(File file, String tenantId) throws Throwable;

    /**
     * 卸载
     */
    void unload(String pluginId) throws Exception;

    /**
     * 获取这个租户的优先级最高的插件实现.
     */
    <P> Optional<P> getSortFirst(Class<P> pClass, String tenantId);

    /**
     * 获取集合
     */
    <P> List<P> getList(Class<P> pClass, String tenantId);

    /**
     * 获取这个插件的顺序
     */
    int getSort(String pluginId);

    /**
     * 获取这个插件的租户.
     */
    String getTenantId(String pluginId);

    /**
     * 更新这个插件的顺序
     */
    void updateSort(String pluginId, int sort);
}
