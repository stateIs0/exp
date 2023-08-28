package cn.think.in.java.open.exp.classloader.support;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/9
 **/
public class UniqueNameUtil {

    public static String getPluginIdSplit() {
        return "_";
    }

    public static String getNameSplit() {
        return "___";
    }

    public static String getName(Class<?> c, String pluginId) {
        return c.getName() + getNameSplit() + pluginId;
    }

    public static String getPluginId(String name) {
        if (!name.contains(getNameSplit())) {
            return null;
        }
        return name.split(getNameSplit())[1];
    }

}
