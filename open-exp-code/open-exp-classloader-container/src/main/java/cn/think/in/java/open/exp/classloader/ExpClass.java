package cn.think.in.java.open.exp.classloader;

import lombok.Data;

/**
 * @Author cxs
 * @Description
 * @date 2023/8/9
 * @version 1.0
 **/
@Data
public class ExpClass<T> {

    Class<T> aClass;

    String pluginId;

    public ExpClass(Class<T> aClass, String pluginId) {
        this.aClass = aClass;
        this.pluginId = pluginId;
    }
}
