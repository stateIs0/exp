package cn.think.in.java.open.exp.adapter.springboot2;

import cn.think.in.java.open.exp.client.PluginConfig;

/**
 * @Author cxs
 * @Description
 * @date 2023/8/12
 * @version 1.0
 **/
public class PluginConfigImpl implements PluginConfig{
    @Override
    public String getProperty(String pluginId, String key, String defaultValue) {
        if (key.equals("bb2-config")) {
            return "1";
        }
        return "9999";
    }

}
