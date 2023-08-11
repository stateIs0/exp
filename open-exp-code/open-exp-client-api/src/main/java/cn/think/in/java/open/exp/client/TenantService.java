package cn.think.in.java.open.exp.client;

/**
 * @Author cxs
 * @Description
 * @date 2023/8/11
 * @version 1.0
 **/
public interface TenantService {


    /**
     * 获取 TenantCallback 扩展逻辑;
     */
    default TenantCallback getTenantCallback() {
        return TenantCallback.TenantCallbackMock.instance;
    }

    /**
     * 设置 callback;
     */
    default void setTenantCallback(TenantCallback callback) {
    }
}
