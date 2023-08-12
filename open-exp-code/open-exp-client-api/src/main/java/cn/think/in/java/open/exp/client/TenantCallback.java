package cn.think.in.java.open.exp.client;

/**
 * @Author cxs
 **/
public interface TenantCallback {

    /**
     * 返回这个插件的序号, 默认 0;
     * {@link  cn.think.in.java.open.exp.client.ExpAppContext#get(java.lang.Class)} 函数返回的List 的第一位就是 sort 最高的.
     */
    int getSort(String pluginId);

    /**
     * 插件过滤, 返回为 true, 则保留; 通常是这个插件是否属于当前租户, 默认是;
     * 这个返回值, 会影响 {@link  cn.think.in.java.open.exp.client.ExpAppContext#get(java.lang.Class)} 的结果
     * 即进行过滤, 返回为 true 的 plugin 实现, 才会被返回.
     */
    boolean filter(String pluginId);

    /**
     * 最简单的, 随机排序, 全都过.
     */
    class TenantCallbackMock implements TenantCallback {
        public static TenantCallbackMock DEFAULT = new TenantCallbackMock();

        @Override
        public int getSort(String pluginId) {
            return 0;
        }

        @Override
        public boolean filter(String pluginId) {
            return true;
        }
    }
}
