package cn.think.in.java.open.exp.adapter.springboot1;

import cn.think.in.java.open.exp.client.PluginConfig;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/25
 **/
@Slf4j
@Component
public class ConfigServ implements PluginConfig , EnvironmentAware {

    static Logger logger = LoggerFactory.getLogger(ConfigServ.class);

    static Environment environment;
    @Override
    public String getProperty(String pluginId, String key, String defaultValue) {
        logger.info("{} {} {}", pluginId, key, defaultValue);
        return environment.getProperty(key, defaultValue);
    }

    @Override
    public void setEnvironment(Environment e) {
        environment = e;
    }
}
