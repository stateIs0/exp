package cn.think.in.java.open.exp.client;

/**
 * @Author cxs
 * @Description
 * @date 2023/8/13
 * @version 1.0
 **/
public interface TenantObjectStore {

    /**
     * 获取代理租户对象工厂.
     */
    TenantObjectProxyFactory getTenantObjectProxyFactory();
}
