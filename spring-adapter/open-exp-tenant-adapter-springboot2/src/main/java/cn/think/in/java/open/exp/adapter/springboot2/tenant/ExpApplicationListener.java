package cn.think.in.java.open.exp.adapter.springboot2.tenant;

import cn.think.in.java.open.exp.classloader.ExpPluginMetaService;
import cn.think.in.java.open.exp.classloader.PluginMetaConfig;
import cn.think.in.java.open.exp.classloader.PluginMetaServiceSpiFactory;
import cn.think.in.java.open.exp.client.Constant;
import cn.think.in.java.open.exp.client.ExpAppContext;
import cn.think.in.java.open.exp.client.ExpAppContextSpiFactory;
import cn.think.in.java.open.exp.client.Plugin;
import cn.think.in.java.open.exp.core.impl.Bootstrap;
import cn.think.in.java.open.exp.core.impl.ExpAppContextImpl;
import cn.think.in.java.open.exp.core.tenant.impl.TenantExpAppContext;
import cn.think.in.java.open.exp.core.tenant.impl.TenantExpAppContextSpiFactory;
import cn.think.in.java.open.exp.core.tenant.impl.TenantObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import java.io.File;

/**
 * @Author cxs
 **/
@Slf4j
public class ExpApplicationListener implements ApplicationListener<ApplicationReadyEvent> {

    ObjectStoreForSpringBoot objectStore;

    public void setObjectStore(ObjectStoreForSpringBoot objectStore) {
        this.objectStore = objectStore;
    }

    public ObjectStoreForSpringBoot getObjectStore() {
        return objectStore;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            String pluginPath = event.getApplicationContext().getEnvironment().getProperty(Constant.PLUGINS_PATH_KEY, "exp-plugins");
            String workDir = event.getApplicationContext().getEnvironment().getProperty(Constant.PLUGINS_WORK_DIE_PATH_KEY, "exp-workDir");

            TenantExpAppContext tenantExpAppContext = TenantExpAppContextSpiFactory.getFirst();
            ExpAppContext expAppContext = ExpAppContextSpiFactory.getFirst();
            if (expAppContext instanceof ExpAppContextImpl) {
                ExpAppContextImpl spi = (ExpAppContextImpl) expAppContext;
                spi.setObjectStore(objectStore);
                ExpPluginMetaService metaService = PluginMetaServiceSpiFactory.getFirst();
                metaService.setConfig(new PluginMetaConfig(workDir));
                spi.setPluginMetaService(metaService);
            }

            File[] files = new File(pluginPath).listFiles();
            if (files == null) {
                log.warn("在目录里没有找到 jar 包或 zip 包, 目录 = {}", pluginPath);
            } else {
                for (File file : files) {
                    Plugin load = tenantExpAppContext.load(file, "999");
                    tenantExpAppContext.updateSort(load.getPluginId(), 10);
                }
            }

        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
