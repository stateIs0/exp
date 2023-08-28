package cn.think.in.java.open.exp.classloader;

import cn.think.in.java.open.exp.client.Plugin;
import cn.think.in.java.open.exp.client.PluginObjectScanner;
import lombok.Data;

import java.io.File;
import java.util.Map;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/9
 **/
@Data
public class PluginMetaFat extends PluginMetaThin {

    private PluginObjectScanner scanner;
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
        plugin.setPluginExt(this.pluginExt);
        plugin.setConfigSupportList(configSupportList);

        return plugin;
    }

}
