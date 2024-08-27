package cn.think.in.java;

import cn.think.in.java.model.HeartbeatRequest;
import cn.think.in.java.model.SyncStateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cxs
 * @Description TODO
 * @Date 2024/8/27
 * @Version 1.0.0
 */
@Slf4j
@Component
public class PluginServerImpl implements PluginServer {

    @Override
    public String heartbeat(HeartbeatRequest request) {
        return "";
    }

    @Override
    public String preInstall(String filePath) {
        return "";
    }

    @Override
    public String installPlugin(String pluginId) {

        List<PluginClient> list = new ArrayList<>();

        for (PluginClient pluginClient : list) {
            String result = pluginClient.syncState(SyncStateRequest.builder()
                    .state("")
                    .pluginId("")
                    .build());
        }

        return "";
    }

    @Override
    public void unInstallPlugin(String pluginId) {
        List<PluginClient> list = new ArrayList<>();

        for (PluginClient pluginClient : list) {
            String result = pluginClient.syncState(SyncStateRequest.builder()
                    .state("")
                    .pluginId("")
                    .build());
        }
    }
}
