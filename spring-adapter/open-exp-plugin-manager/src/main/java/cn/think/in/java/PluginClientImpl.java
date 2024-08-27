package cn.think.in.java;

import cn.think.in.java.model.HeartbeatRequest;
import cn.think.in.java.model.SyncStateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author cxs
 * @Description TODO
 * @Date 2024/8/27
 * @Version 1.0.0
 */
@Slf4j
@Component
public class PluginClientImpl implements PluginClient {

    private final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);


    @Resource
    PluginServer pluginServer;

    @Value("${spring.application.name}")
    String appName;

    @Value("${server.port}")
    String serverPort;

    @Override
    public void start() {
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            try {
                String heartbeat = pluginServer.heartbeat(HeartbeatRequest.builder()
                        .ip(IPUtil.getLocalIpAddress())
                        .appName(appName)
                        .port(Integer.parseInt(serverPort))
                        .build());
                log.info("heartbeat: {}", heartbeat);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }, 10, 10, TimeUnit.SECONDS);
    }

    @Override
    public void stop() {
        scheduledExecutorService.shutdown();
    }

    @Override
    public String syncState(SyncStateRequest syncStateRequest) {
        return "success";
    }
}
