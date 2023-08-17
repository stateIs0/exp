package cn.think.in.java.open.exp.client;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/11
 **/
public interface TenantObjectProxyFactory {

    Object getProxy(Object origin, String pluginId);
}
