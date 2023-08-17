package cn.think.in.java.open.exp.classloader.support;

import lombok.Data;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/9
 **/
@Data
public class PluginMetaInnerModel {

    /**
     *
     */
    String pluginId;

    String pluginCode;

    String pluginDesc;

    String pluginVersion;

    String pluginExt;

    String pluginBootClass;


    public PluginMetaInnerModel(String pluginCode, String pluginDesc,
                                String pluginVersion, String pluginExt,
                                String pluginBootClass) {

        this.pluginId = pluginCode + UniqueNameUtil.getSplit() + pluginVersion;
        this.pluginCode = pluginCode;
        this.pluginDesc = pluginDesc;
        this.pluginVersion = pluginVersion;
        this.pluginExt = pluginExt;
        this.pluginBootClass = pluginBootClass;
    }
}
