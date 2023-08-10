package cn.think.in.java.open.exp.client;

/**
 * @Author cxs
 * @Description
 * @date 2023/8/10
 * @version 1.0
 **/
public interface TenantObject extends Comparable<TenantObject> {

    int getSort();

    String getTenantId();

    @Override
    default int compareTo(TenantObject o) {
        return o.getSort() - getSort();
    }
}
