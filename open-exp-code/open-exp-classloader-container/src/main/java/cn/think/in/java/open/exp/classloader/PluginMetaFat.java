package cn.think.in.java.open.exp.classloader;

import cn.think.in.java.open.exp.client.Plugin;
import cn.think.in.java.open.exp.client.PluginObjectRegister;
import lombok.Data;

import java.io.File;
import java.util.Map;

/**
 * @Author cxs
 * @Description
 * @date 2023/8/9
 * @version 1.0
 **/
@Data
public class PluginMetaFat {

    private String pluginId;

    private String pluginCode;

    private String pluginDesc;

    private String pluginVersion;

    private String pluginExt;

    private String pluginConfig;

    private String pluginBootClass;

    private PluginObjectRegister register;

    /**
     * 扩展点映射关系
     */
    private Map<String, String> extensionMappings;

    private File location;

    private ClassLoader classLoader;

    public Plugin conv() {
        Plugin plugin = new Plugin();
        plugin.setPluginId(this.pluginId);
        plugin.setPluginCode(this.pluginCode);
        plugin.setPluginDesc(this.pluginDesc);
        plugin.setPluginVersion(this.pluginVersion);
        plugin.setPluginBootClass(this.pluginBootClass);
        plugin.setPluginConfig(this.pluginConfig);
        plugin.setPluginExt(this.pluginExt);

        return plugin;
    }

}
