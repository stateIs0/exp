package cn.think.in.java;

import cn.think.in.java.open.exp.client.PluginFilter;

import java.util.List;

/**
 * @author cxs
 * @Description TODO
 * @Date 2024/8/27
 * @Version 1.0.0
 */
public class PluginFilterSpiImpl implements PluginFilter {
    @Override
    public <T> List<FModel<T>> filter(List<FModel<T>> list) {
        return list;
    }
}
