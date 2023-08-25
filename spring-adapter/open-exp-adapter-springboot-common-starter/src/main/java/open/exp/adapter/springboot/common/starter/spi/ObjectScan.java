package open.exp.adapter.springboot.common.starter.spi;

import cn.think.in.java.open.exp.client.ObjectStore;

import java.util.List;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/25
 **/
public interface ObjectScan {

    void init(ObjectStore objectStore);

    void registerApis(List<Class<?>> classes, String pluginId);

    void unregisterApis(String pluginId);
}
