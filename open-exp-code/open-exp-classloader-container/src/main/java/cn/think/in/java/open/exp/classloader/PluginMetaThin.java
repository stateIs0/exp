package cn.think.in.java.open.exp.classloader;

import cn.think.in.java.open.exp.client.ConfigSupport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/25
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PluginMetaThin {

    protected String pluginId;

    protected String pluginCode;

    protected String pluginDesc;

    protected String pluginVersion;

    protected String pluginExt;

    protected String pluginBootClass;

    protected List<ConfigSupport> configSupportList;

    protected String classLoaderMode;
}
