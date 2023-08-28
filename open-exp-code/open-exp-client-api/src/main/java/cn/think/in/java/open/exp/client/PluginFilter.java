package cn.think.in.java.open.exp.client;

import lombok.Data;

import java.util.List;

/**
 * @Author cxs
 **/
public interface PluginFilter {

    PluginFilter MOCK = new PluginFilterMock();

    <T> List<FModel<T>> filter(List<FModel<T>> list);

    @Data
    class FModel<T> {
        T t;
        String pluginId;

        public FModel(T t, String pluginId) {
            this.t = t;
            this.pluginId = pluginId;
        }
    }

    class PluginFilterMock implements PluginFilter {
        @Override
        public <T> List<FModel<T>> filter(List<FModel<T>> list) {
            return list;
        }
    }
}
