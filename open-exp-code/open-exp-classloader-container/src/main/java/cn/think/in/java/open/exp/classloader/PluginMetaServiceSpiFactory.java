package cn.think.in.java.open.exp.classloader;

import java.util.ServiceLoader;

/**
 * @Author cxs
 **/
public class PluginMetaServiceSpiFactory {

    private static final Object LOCK = new Object();

    private static ExpPluginMetaService expPluginMetaService;

    public static ExpPluginMetaService getFirst() {
        if (expPluginMetaService != null) {
            return expPluginMetaService;
        }
        synchronized (LOCK) {
            if (expPluginMetaService != null) {
                return expPluginMetaService;
            }

            ServiceLoader<ExpPluginMetaService> load = ServiceLoader.load(ExpPluginMetaService.class);
            for (ExpPluginMetaService service : load) {
                PluginMetaServiceSpiFactory.expPluginMetaService = service;
                return expPluginMetaService;
            }
            throw new RuntimeException("SPI 缺失.");
        }
    }
}
