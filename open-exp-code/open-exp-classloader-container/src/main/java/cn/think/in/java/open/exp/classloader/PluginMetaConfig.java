package cn.think.in.java.open.exp.classloader;

import lombok.Data;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/9
 **/
@Data
public class PluginMetaConfig {

    String workDir = "exp-tmp-dir";
    String autoDelete = "true";


    public PluginMetaConfig() {
    }

    public PluginMetaConfig(String workDir) {
        this.workDir = workDir;
    }
}
