package cn.think.in.java.open.exp.core.tenant.impl;

import cn.think.in.java.open.exp.client.ExpAppContext;
import cn.think.in.java.open.exp.client.ExpAppContextSpiFactory;
import cn.think.in.java.open.exp.client.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author cxs
 **/
public class TenantExpAppContextImpl implements TenantExpAppContext {

    private final ExpAppContext appContext = ExpAppContextSpiFactory.getFirst();

    private final Map<String/* 租户 id */, List<TenantPlugin/* 插件集合 */>> tenantPluginMappings = new HashMap<>();

    private final Map<String/* plugin id */, String/* 租户 id */> cache = new HashMap<>();

    @Override
    public Plugin load(File file, String tenantId) throws Throwable {

        Plugin load = appContext.load(file);

        TenantPlugin tenantPlugin = new TenantPlugin(tenantId, load);

        List<TenantPlugin> list = tenantPluginMappings.computeIfAbsent(tenantId, k -> new ArrayList<>());

        list.add(tenantPlugin);

        cache.put(load.getPluginId(), tenantId);

        return load;
    }

    @Override
    public void unLoad(String pluginId) throws Exception {
        appContext.unload(pluginId);
        for (Map.Entry<String, List<TenantPlugin>> entry : tenantPluginMappings.entrySet()) {
            entry.getValue().removeIf(t -> t.getPlugin().getPluginId().equals(pluginId));
        }
        cache.remove(pluginId);
    }

    @Override
    public <P> P getSortFirst(Class<P> pClass, String tenantId) {
        List<P> list = appContext.get(pClass);
        return list.stream().filter(p -> ((TenantObject) p).getTenantId().equals(tenantId)).sorted().findFirst().orElse(null);
    }

    @Override
    public <P> List<P> getList(Class<P> pClass, String tenantId) {
        List<P> list = appContext.get(pClass);
        return list.stream().filter(p -> ((TenantObject) p).getTenantId().equals(tenantId)).collect(Collectors.toList());
    }

    @Override
    public int getSort(String pluginId) {
        for (List<TenantPlugin> value : tenantPluginMappings.values()) {
            for (TenantPlugin tenantPlugin : value) {
                if (tenantPlugin.getPlugin().getPluginId().equals(pluginId)) {
                    return tenantPlugin.getSort();
                }
            }
        }
        return 0;
    }

    @Override
    public String getTenantId(String pluginId) {
        return cache.get(pluginId);
    }

    @Override
    public void updateSort(String pluginId, int sort) {
        for (List<TenantPlugin> value : tenantPluginMappings.values()) {
            for (TenantPlugin tenantPlugin : value) {
                if (tenantPlugin.getPlugin().getPluginId().equals(pluginId)) {
                    tenantPlugin.setSort(sort);
                }
            }
        }
    }
}
