package cn.think.in.java.model;

import lombok.Builder;
import lombok.Data;

/**
 * @author cxs
 * @Description TODO
 * @Date 2024/8/27
 * @Version 1.0.0
 */
@Data
@Builder
public class HeartbeatRequest {

    String ip;
    int port;
    String appName;
}
