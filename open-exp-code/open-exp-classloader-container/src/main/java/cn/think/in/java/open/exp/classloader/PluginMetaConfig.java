package cn.think.in.java.open.exp.classloader;

import lombok.Data;

/**
 * @Author cxs
 * @Description
 * @date 2023/8/9
 * @version 1.0
 **/
@Data
public class PluginMetaConfig {

    String workDir = "exp-tmp-dir/";


    public PluginMetaConfig() {
    }

    public PluginMetaConfig(String workDir) {
        this.workDir = workDir;
    }
}
