package cn.think.in.java.open.exp.example.b;

import cn.think.in.java.open.exp.client.PluginConfig;
import cn.think.in.java.open.exp.plugin.depend.AbstractBoot;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/9
 **/
public class Boot extends AbstractBoot {
    private static String selfPluginId;

    public static String get(String key, String value) {
        return PluginConfig.getSpi().getProperty(selfPluginId, key, value);
    }

    @Override
    protected String getScanPath() {
        return Boot.class.getPackage().getName();
    }

    @Override
    public void setPluginId(String pluginId) {
        selfPluginId = pluginId;
    }

}
