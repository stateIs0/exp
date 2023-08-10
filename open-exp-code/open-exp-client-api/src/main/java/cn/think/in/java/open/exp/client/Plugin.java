package cn.think.in.java.open.exp.client;

import lombok.Data;

import java.util.Objects;

/**
 * @Author cxs
 * @Description
 * @date 2023/8/8
 * @version 1.0
 **/
@Data
public class Plugin {

    /**
     * build: pluginCode||pluginVersion
     */
    String pluginId;

    String pluginCode;

    String pluginDesc;

    String pluginVersion;

    String pluginExt;

    String pluginConfig;

    String pluginBootClass;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Plugin plugin = (Plugin) o;

        return Objects.equals(pluginId, plugin.pluginId);
    }

    @Override
    public int hashCode() {
        return pluginId != null ? pluginId.hashCode() : 0;
    }
}
