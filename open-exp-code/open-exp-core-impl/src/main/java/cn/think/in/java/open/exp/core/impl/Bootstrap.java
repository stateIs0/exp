package cn.think.in.java.open.exp.core.impl;

import cn.think.in.java.open.exp.classloader.PluginMetaConfig;
import cn.think.in.java.open.exp.classloader.PluginMetaService;
import cn.think.in.java.open.exp.client.ExpAppContext;
import cn.think.in.java.open.exp.client.ExpAppContextSpiFactory;
import cn.think.in.java.open.exp.client.ObjectStore;
import cn.think.in.java.open.exp.client.Plugin;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author cxs
 **/
@Slf4j
public class Bootstrap {

    public static ExpAppContext bootstrap(String path, String workDir) throws Throwable {
        return bootstrap(new SimpleObjectStore(), path, workDir, "true");
    }

    /**
     * 默认实现. ObjectStore 仅仅是个 hashmap;
     * 通常, ObjectStore 是个 spring 容器.
     */
    public static ExpAppContext bootstrap(String path, String workDir, String autoDelete) throws Throwable {
        return bootstrap(new SimpleObjectStore(), path, workDir, autoDelete);
    }


    /**
     * 自动安装 path 下的所有 jar.
     */
    public static ExpAppContext bootstrap(ObjectStore callback, String path, String workDir, String extPluginAutoDelete) throws Throwable {

        ExpAppContext expAppContext = ExpAppContextSpiFactory.getFirst();
        if (expAppContext instanceof ExpAppContextImpl) {
            ExpAppContextImpl spi = (ExpAppContextImpl) expAppContext;
            spi.setObjectStore(callback);
            PluginMetaService metaService = PluginMetaService.getSpi();
            PluginMetaConfig pluginMetaConfig = new PluginMetaConfig(workDir);
            pluginMetaConfig.setAutoDelete(extPluginAutoDelete);
            metaService.setConfig(pluginMetaConfig);
            spi.setPluginMetaService(metaService);
        }

        File[] files = new File(path).listFiles();
        List<String> tmp = new ArrayList<>();
        if (files == null) {
            log.warn("在目录里没有找到 jar 包或 zip 包, 目录 = {}", path);
            return expAppContext;
        }

        for (File file : files) {
            try {
                if (!file.exists()) {
                    continue;
                }
                log.info("准备安装插件, 压缩包路径: " + file.getAbsolutePath());
                Plugin plugin = expAppContext.load(file);
                tmp.add(plugin.getPluginId());
            } catch (Exception e) {
                log.error(e.getMessage() + " ---->>>> " + file.getAbsolutePath(), e);
                throw e;
            }
        }

        log.info("安装结束, 插件列表:{}", tmp);
        return expAppContext;

    }
}
