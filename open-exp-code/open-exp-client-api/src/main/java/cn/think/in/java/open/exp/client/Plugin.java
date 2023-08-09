package cn.think.in.java.open.exp.client;

import lombok.Data;

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

}
