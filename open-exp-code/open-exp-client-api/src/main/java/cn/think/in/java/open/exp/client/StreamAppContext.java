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
    <P> List<P> streamOne(Class<P> pClass);

    /**
     * 针对有返回值的 api, 需要支持流式调用
     */
    <R, P> R streamList(Class<P> pClass, Ec<R, List<P>> ecs);

    /**
     * 针对有返回值的 api, 需要支持流式调用
     */
    <R, P> R stream(Class<P> clazz, String pluginId, Ec<R, P> ec);
}
