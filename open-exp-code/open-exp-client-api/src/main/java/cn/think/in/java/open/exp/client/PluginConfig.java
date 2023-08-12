package cn.think.in.java.open.exp.client;

/**
 * @Author cxs
 * @Description
 * @date 2023/8/12
 * @version 1.0
 **/
public interface PluginConfig {

    static PluginConfig getSpi() {
        return SpiFactory.get(PluginConfig.class);
    }

    String getProperty(String pluginId, String key, String defaultValue);

}
