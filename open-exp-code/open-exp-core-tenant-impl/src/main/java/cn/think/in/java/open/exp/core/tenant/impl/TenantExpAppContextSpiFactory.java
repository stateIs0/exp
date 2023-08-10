package cn.think.in.java.open.exp.core.tenant.impl;

/**
 * @Author cxs
 * @Description
 * @date 2023/8/10
 * @version 1.0
 **/
public class TenantExpAppContextSpiFactory {

    static TenantExpAppContext tenantExpAppContext = new TenantExpAppContextImpl();

    public static TenantExpAppContext getFirst() {
        return tenantExpAppContext;
    }
}
