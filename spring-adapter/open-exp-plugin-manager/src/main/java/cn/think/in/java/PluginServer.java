package cn.think.in.java;

import cn.think.in.java.model.HeartbeatRequest;

/**
 * @author cxs
 * @Description TODO
 * @Date 2024/8/27
 * @Version 1.0.0
 */
public interface PluginServer {

    String heartbeat(HeartbeatRequest request);

    String preInstall(String filePath);

    String installPlugin(String pluginId);

    void unInstallPlugin(String pluginId);
}
