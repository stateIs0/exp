package cn.think.in.java.open.exp.client;

/**
 * @Author cxs
 **/
public interface TenantCallback {

    /**
     * 返回这个插件的序号, 默认 0;
     * {@link  cn.think.in.java.open.exp.client.ExpAppContext#get(java.lang.Class)} 函数返回的List 的第一位就是 sort 最高的.
     */
    Integer getSort(String pluginId);

    /**
     * 这个插件是否属于当前租户, 默认是;
     * 这个返回值, 会影响 {@link  cn.think.in.java.open.exp.client.ExpAppContext#get(java.lang.Class)} 的结果
     * 即进行过滤, 返回为 true 的 plugin 实现, 才会被返回.
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
