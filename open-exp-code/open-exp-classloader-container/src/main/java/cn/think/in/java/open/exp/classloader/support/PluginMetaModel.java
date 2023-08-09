package cn.think.in.java.open.exp.classloader.support;

import lombok.Data;

/**
 * @Author cxs
 * @Description
 * @date 2023/8/9
 * @version 1.0
 **/
@Data
public class PluginMetaModel {

    /**
     * build: pluginCode||||pluginVersion
     */
    String pluginId;

    String pluginCode;

    String pluginDesc;

    String pluginVersion;

    String pluginExt;

    String pluginConfig;

    String pluginBootClass;


    public PluginMetaModel() {
    }

    public PluginMetaModel(String pluginCode, String pluginDesc,
                           String pluginVersion, String pluginExt,
                           String pluginConfig, String pluginBootClass) {

        this.pluginId = pluginCode + UniqueNameUtil.getSplit() + pluginVersion;
        this.pluginCode = pluginCode;
        this.pluginDesc = pluginDesc;
        this.pluginVersion = pluginVersion;
        this.pluginExt = pluginExt;
        this.pluginConfig = pluginConfig;
        this.pluginBootClass = pluginBootClass;
    }
}
