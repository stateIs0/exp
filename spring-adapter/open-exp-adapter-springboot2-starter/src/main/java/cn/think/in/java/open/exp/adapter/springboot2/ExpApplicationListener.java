package cn.think.in.java.open.exp.adapter.springboot2;

import cn.think.in.java.open.exp.client.Constant;
import cn.think.in.java.open.exp.core.impl.Bootstrap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

/**
 * @Author cxs
 **/
@Slf4j
public class ExpApplicationListener implements ApplicationListener<ApplicationReadyEvent> {

    private SpringBootObjectStore objectStore;

    public SpringBootObjectStore getObjectStore() {
        return objectStore;
    }

    public void setObjectStore(SpringBootObjectStore objectStore) {
        this.objectStore = objectStore;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            String pluginPath = event.getApplicationContext().getEnvironment().getProperty(Constant.PLUGINS_PATH_KEY, "exp-plugins");
            String workDir = event.getApplicationContext().getEnvironment().getProperty(Constant.PLUGINS_WORK_DIE_PATH_KEY, "exp-workDir");

            Bootstrap.bootstrap(objectStore, pluginPath, workDir);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
