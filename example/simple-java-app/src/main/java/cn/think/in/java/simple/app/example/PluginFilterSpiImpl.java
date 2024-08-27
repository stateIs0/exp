package cn.think.in.java.simple.app.example;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author cxs
 * @Description TODO
 * @Date 2024/8/27
 * @Version 1.0.0
 */
@Slf4j
public class PluginFilterSpiImpl implements cn.think.in.java.open.exp.client.PluginFilter {
    @Override
    public <T> List<FModel<T>> filter(List<FModel<T>> list) {
        list.forEach(i ->
                log.warn("filter={}", i.getPluginId()));
        return list;
    }
}
