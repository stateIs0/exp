package cn.think.in.java.open.exp.client;

/**
 * @Author cxs
 **/
public interface TenantCallback {

    /**
     * 返回这个插件的序号, 默认 0;
     */
    Integer getSort(String pluginId);

    /**
     * 这个插件是否属于当前租户, 默认是;
     */
    Boolean isOwnCurrentTenant(String pluginId);

    class TenantCallbackMock implements TenantCallback {
        public static TenantCallbackMock instance = new TenantCallbackMock();

        @Override
        public Integer getSort(String pluginId) {
            return 0;
        }

        @Override
        public Boolean isOwnCurrentTenant(String pluginId) {
            return true;
        }
    }
}
