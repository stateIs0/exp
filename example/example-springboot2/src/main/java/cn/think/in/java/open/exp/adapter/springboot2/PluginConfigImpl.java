package cn.think.in.java.open.exp.adapter.springboot2;

import cn.think.in.java.open.exp.client.PluginConfig;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/12
 **/
public class PluginConfigImpl implements PluginConfig {
    @Override
    public String getProperty(String pluginId, String key, String defaultValue) {
        return pluginId + " --->>> " + key;
    }

}
