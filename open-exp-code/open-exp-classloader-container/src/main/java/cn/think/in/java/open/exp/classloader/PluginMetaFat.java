package cn.think.in.java.open.exp.classloader;

import cn.think.in.java.open.exp.client.ConfigSupport;
import cn.think.in.java.open.exp.client.Plugin;
import cn.think.in.java.open.exp.client.PluginObjectScanner;
import lombok.Data;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/9
 **/
@Data
public class PluginMetaFat {

    private String pluginId;

    private String pluginCode;

    private String pluginDesc;

    private String pluginVersion;

    private String pluginExt;

    private String pluginBootClass;

    private PluginObjectScanner scanner;

    /**
     * 扩展点映射关系
     */
    private Map<String, String> extensionMappings;

    private File location;

    private ClassLoader classLoader;

    private List<ConfigSupport> configSupportList;

    public Plugin conv() {
        Plugin plugin = new Plugin();
        plugin.setPluginId(this.pluginId);
        plugin.setPluginCode(this.pluginCode);
        plugin.setPluginDesc(this.pluginDesc);
        plugin.setPluginVersion(this.pluginVersion);
        plugin.setPluginBootClass(this.pluginBootClass);
        plugin.setPluginExt(this.pluginExt);
        plugin.setConfigSupportList(configSupportList);

        return plugin;
    }

}
