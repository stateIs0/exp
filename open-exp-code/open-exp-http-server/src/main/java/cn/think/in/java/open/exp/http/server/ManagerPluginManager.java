package cn.think.in.java.open.exp.http.server;

import cn.think.in.java.open.exp.client.ConfigSpi;
import cn.think.in.java.open.exp.client.PluginManager;
import cn.think.in.java.open.exp.client.SpiFactory;
import cn.think.in.java.open.exp.client.StringUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/23
 **/
@Slf4j
public class ManagerPluginManager implements PluginManager {
    private static ConfigSpi configSpi = SpiFactory.get(ConfigSpi.class, new ConfigSpi.MockConfigSpi());
    private boolean init;

    public static boolean blocking() {

        String enabled = configSpi.getProperty("exp.plugin.manager.enabled", "true");
        return !Boolean.parseBoolean(enabled);
    }

    @Override
    public void start() {
        if (init) {
            return;
        }

        init = true;

        if (blocking()) {
            return;
        }

        new PMThread(() -> {
            try {
                Runtime.getRuntime().addShutdownHook(new Thread(ManagerPluginManager.this::stop));
                String portStr = configSpi.getProperty("exp.plugin.manager.port", "8888");
                String contextPath = configSpi.getProperty("server.servlet.context-path", "");
                if (StringUtil.isEmpty(portStr)) {
                    portStr = "8888";
                }
                log.info("Server listening on port " + Integer.parseInt(portStr) + ", contextPath = " + contextPath);
                ApacheHttpSimpleServer.start(contextPath, Integer.parseInt(portStr));
            } catch (Throwable e) {
                log.warn(e.getMessage(), e);
            }
        }).start();
    }

    @Override
    public void stop() {
        ApacheHttpSimpleServer.stop();
    }

    static class PMThread extends Thread {
        public PMThread(Runnable target) {
            super(target, "ExP-Plugin-Manager");
        }
    }

}
