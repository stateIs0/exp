package open.exp.adapter.springboot.common.starter;

import cn.think.in.java.open.exp.classloader.support.UniqueNameUtil;

/**
 * @Author cxs
 **/
public class PluginNameBuilder {

    public static String build(Class<?> c, String pluginId) {
        return UniqueNameUtil.getName(c, pluginId);
    }
}
