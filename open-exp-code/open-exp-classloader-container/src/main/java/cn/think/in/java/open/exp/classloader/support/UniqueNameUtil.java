package cn.think.in.java.open.exp.classloader.support;

/**
 * @Author cxs
 * @Description
 * @date 2023/8/9
 * @version 1.0
 **/
public class UniqueNameUtil {


    public static String getSplit() {
        return "_";
    }

    public static String getName(Class<?> c, String pluginId) {
        return c.getName() + getSplit() + pluginId;
    }

}
