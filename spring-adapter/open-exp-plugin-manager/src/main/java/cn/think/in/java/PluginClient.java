package cn.think.in.java;

import cn.think.in.java.model.SyncStateRequest;

/**
 * @author cxs
 * @Description TODO
 * @Date 2024/8/27
 * @Version 1.0.0
 */
public interface PluginClient {

    void start();

    void stop();

    String syncState(SyncStateRequest syncStateRequest);
}
