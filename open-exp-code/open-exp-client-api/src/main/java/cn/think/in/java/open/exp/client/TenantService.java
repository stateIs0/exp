package cn.think.in.java.open.exp.client;

import java.util.List;

/**
 * @Author cxs
 **/
public interface TenantService {

    <P> List<P> get(String extCode, TenantCallback callback);

    <P> List<P> get(Class<P> pClass, TenantCallback callback);
}
