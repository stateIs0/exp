package cn.think.in.java.open.exp.adapter.springboot1;

import cn.think.in.java.open.exp.client.PluginFilter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/28
 **/
@Slf4j
public class PluginFilterImpl implements PluginFilter {
    @Override
    public <T> List<FModel<T>> filter(List<FModel<T>> list) {
        log.info(list.toString());
        return list;
    }
}
