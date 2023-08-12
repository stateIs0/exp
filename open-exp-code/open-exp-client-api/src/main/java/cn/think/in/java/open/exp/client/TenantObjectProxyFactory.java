package cn.think.in.java.open.exp.client;

/**
 * @Author cxs
 * @Description
 * @date 2023/8/11
 * @version 1.0
 **/
public interface TenantObjectProxyFactory {

    Object getProxy(Object origin, String pluginId);
}
