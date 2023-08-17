package cn.think.in.java.open.exp.classloader.support;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/9
 **/
public class UniqueNameUtil {


    public static String getSplit() {
        return "_";
    }

    public static String getName(Class<?> c, String pluginId) {
        return c.getName() + getSplit() + pluginId;
    }

    public static String getName(String c, String pluginId) {
        return c + getSplit() + pluginId;
    }

}
