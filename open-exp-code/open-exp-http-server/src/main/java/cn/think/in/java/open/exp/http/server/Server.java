package cn.think.in.java.open.exp.http.server;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/28
 **/
public interface Server {

    void start(String context, int port);

    void stop();
}
