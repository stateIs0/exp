package cn.think.in.java.open.exp.client;

/**
 * @Author cxs
 **/
public class StringUtil {
    public static boolean isEmpty(String value) {
        return null == value || value.isEmpty() || value.trim().isEmpty();
    }
}
