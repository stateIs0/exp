package cn.think.in.java.open.exp.classloader.support;

import cn.think.in.java.open.exp.classloader.PluginMetaThin;
import cn.think.in.java.open.exp.client.ConfigSupport;
import lombok.Data;

import java.util.List;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/9
 **/
@Data
public class PluginMetaInnerModel {

    String pluginId;

    String pluginCode;

    String pluginDesc;

    String pluginVersion;

    String pluginExt;

    String pluginBootClass;

    List<ConfigSupport> configSupportList;


    public PluginMetaInnerModel(String pluginCode, String pluginDesc, String pluginVersion, String pluginExt, String pluginBootClass, List<ConfigSupport> configSupportList) {

        this.pluginId = pluginCode + UniqueNameUtil.getSplit() + pluginVersion;
        this.pluginCode = pluginCode;
        this.pluginDesc = pluginDesc;
        this.pluginVersion = pluginVersion;
        this.pluginExt = pluginExt;
        this.pluginBootClass = pluginBootClass;
        this.configSupportList = configSupportList;
    }

    public PluginMetaThin conv() {
        return new PluginMetaThin(this.pluginId, this.pluginCode, this.pluginDesc, this.pluginVersion, this.pluginExt, this.pluginBootClass, this.configSupportList);
    }

}
