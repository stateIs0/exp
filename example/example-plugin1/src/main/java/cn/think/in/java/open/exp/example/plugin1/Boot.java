package cn.think.in.java.open.exp.example.plugin1;

import cn.think.in.java.open.exp.client.PluginConfig;
import cn.think.in.java.open.exp.plugin.depend.AbstractBoot;

/**
 * @Author cxs
 * @Description
 * @date 2023/8/9
 * @version 1.0
 **/
public class Boot extends AbstractBoot {

    static String pluginId;

    @Override
    protected String getScanPath() {
        return Boot.class.getPackage().getName();
    }

    @Override
    public void setPluginId(String pluginId) {
        Boot.pluginId = pluginId;
    }

    public static String getProperty(String key, String value) {
        if (PluginConfig.getSpi() == null) {
            return null;
        }
        return PluginConfig.getSpi().getProperty(pluginId, key, value);
    }
}
