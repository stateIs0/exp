package open.exp.adapter.springboot3.starter.cn.think.in.java.open.exp.client;

import cn.think.in.java.open.exp.client.PluginConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/25
 **/
@Component
@Slf4j
public class PluginConfigImpl implements PluginConfig, EnvironmentAware {

    static Environment environment;

    @Override
    public String getProperty(String s, String s1, String s2) {
        log.info("plugin id {}", s);
        if (environment == null) {
            return "hello";
        }
        return environment.getProperty(s1, s2);
    }

    @Override
    public void setEnvironment(Environment environment) {
        PluginConfigImpl.environment = environment;
    }
}
