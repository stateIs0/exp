package cn.think.in.java.open.exp.core.tenant.impl;

import cn.think.in.java.open.exp.client.Plugin;
import lombok.Data;

import java.util.Objects;

/**
 * @Author cxs
 * @Description
 * @date 2023/8/10
 * @version 1.0
 **/
@Data
public class TenantPlugin {

    private String tenantId;

    private int sort;

    private Plugin plugin;

    public TenantPlugin(String tenantId, Plugin plugin) {
        this.tenantId = tenantId;
        this.plugin = plugin;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TenantPlugin that = (TenantPlugin) o;

        if (!Objects.equals(tenantId, that.tenantId)) {
            return false;
        }
        return Objects.equals(plugin, that.plugin);
    }

    @Override
    public int hashCode() {
        int result = tenantId != null ? tenantId.hashCode() : 0;
        result = 31 * result + (plugin != null ? plugin.hashCode() : 0);
        return result;
    }
}
