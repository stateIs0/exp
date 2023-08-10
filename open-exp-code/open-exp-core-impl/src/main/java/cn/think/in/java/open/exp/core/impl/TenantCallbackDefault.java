package cn.think.in.java.open.exp.core.impl;

import cn.think.in.java.open.exp.client.TenantCallback;

/**
 * @Author cxs
 * @Description
 * @date 2023/8/10
 * @version 1.0
 **/
public class TenantCallbackDefault implements TenantCallback {

    @Override
    public Integer getSort(String pluginId) {
        return 0;
    }

    @Override
    public Boolean isOwnCurrentTenant(String pluginId) {
        return true;
    }

}

