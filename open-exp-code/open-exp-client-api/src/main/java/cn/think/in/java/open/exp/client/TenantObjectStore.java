package cn.think.in.java.open.exp.client;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/13
 **/
public interface TenantObjectStore {

    /**
     * 获取代理租户对象工厂.
     */
    TenantObjectProxyFactory getTenantObjectProxyFactory();
}
