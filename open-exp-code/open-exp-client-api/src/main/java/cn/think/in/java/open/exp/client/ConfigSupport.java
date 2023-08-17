package cn.think.in.java.open.exp.client;

import lombok.Setter;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/17
 **/
public class ConfigSupport {

    @Setter
    private String keyName;

    @Setter
    private String defaultValue;

    @Setter
    private String desc;

    @Setter
    private boolean required;

    @Setter
    private String pluginId;

    public ConfigSupport(String keyName) {
        this.keyName = keyName;
    }

    public ConfigSupport(String keyName, String defaultValue) {
        this.keyName = keyName;
        this.defaultValue = defaultValue;
    }

    public String getProperty() {
        return PluginConfig.getSpi().getProperty(pluginId, keyName, defaultValue);
    }

    @Override
    public String toString() {
        return "ConfigSupport{" +
                "keyName='" + keyName + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                ", desc='" + desc + '\'' +
                ", required=" + required +
                ", pluginId='" + pluginId + '\'' +
                '}';
    }
}
