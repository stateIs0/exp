package cn.think.in.java.open.exp.classloader;

import cn.think.in.java.open.exp.classloader.impl.ExpPluginMetaServiceImpl;

/**
 * @Author cxs
 **/
public class PluginMetaServiceSpiFactory {

    private static final ExpPluginMetaService DEFAULT_IMPL = new ExpPluginMetaServiceImpl();

    public static ExpPluginMetaService getSpi() {
        return DEFAULT_IMPL;
    }
}
