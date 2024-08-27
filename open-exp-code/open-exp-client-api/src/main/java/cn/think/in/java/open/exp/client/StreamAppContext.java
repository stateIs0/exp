package cn.think.in.java.open.exp.client;

import java.util.List;

/**
 * 流式 API, 优雅处理.
 *
 * @Author cxs
 **/
public interface StreamAppContext {

    /**
     * 简化操作, code 就是全路径类名
     */
    <P> List<P> list(Class<P> pClass);
}
