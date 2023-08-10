package cn.think.in.java.open.exp.core.impl;

import cn.think.in.java.open.exp.classloader.ExpPluginMetaService;
import cn.think.in.java.open.exp.classloader.PluginMetaConfig;
import cn.think.in.java.open.exp.classloader.PluginMetaServiceSpiFactory;
import cn.think.in.java.open.exp.client.ExpAppContext;
import cn.think.in.java.open.exp.client.ExpAppContextSpiFactory;
import cn.think.in.java.open.exp.client.ObjectStore;
import cn.think.in.java.open.exp.client.Plugin;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * @Author cxs
 **/
@Slf4j
public class Bootstrap {

    /**
     * 自动安装 path 下的所有 jar.
     */
    public static void bootstrap(ObjectStore callback, String path, String workDir) throws Throwable {

        ExpAppContext expAppContext = ExpAppContextSpiFactory.getFirst();
        if (expAppContext instanceof ExpAppContextImpl) {
            ExpAppContextImpl spi = (ExpAppContextImpl) expAppContext;
            spi.setObjectStore(callback);
            ExpPluginMetaService metaService = PluginMetaServiceSpiFactory.getFirst();
            metaService.setConfig(new PluginMetaConfig(workDir));
            spi.setPluginMetaService(metaService);
        }

        File[] files = new File(path).listFiles();
        if (files == null) {
            log.warn("在目录里没有找到 jar 包或 zip 包, 目录 = {}", path);
        } else {
            for (File file : files) {
                Plugin load = expAppContext.load(file);
                log.info(load.toString());
            }
        }

    }
}
