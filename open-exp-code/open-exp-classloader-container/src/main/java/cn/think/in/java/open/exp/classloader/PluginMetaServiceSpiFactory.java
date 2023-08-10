package cn.think.in.java.open.exp.classloader;

import java.util.ServiceLoader;

/**
 * @Author cxs
 **/
public class PluginMetaServiceSpiFactory {

    private static final Object LOCK = new Object();

    private static PluginMetaService pluginMetaService;

    public static PluginMetaService getFirst() {
        if (pluginMetaService != null) {
            return pluginMetaService;
        }
        synchronized (LOCK) {
            if (pluginMetaService != null) {
                return pluginMetaService;
            }

            ServiceLoader<PluginMetaService> load = ServiceLoader.load(PluginMetaService.class);
            for (PluginMetaService service : load) {
                PluginMetaServiceSpiFactory.pluginMetaService = service;
                return pluginMetaService;
            }
            throw new RuntimeException("SPI 缺失.");
        }
    }
}
