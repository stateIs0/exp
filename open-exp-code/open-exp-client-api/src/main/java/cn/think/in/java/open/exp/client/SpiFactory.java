package cn.think.in.java.open.exp.client;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * @Author cxs
 **/
@SuppressWarnings("unchecked")
public class SpiFactory {

    private static Map<Class<?>, Object> cache = new HashMap<>();

    public static <T> T get(Class<?> c) {
        if (cache.get(c) != null) {
            return (T) cache.get(c);
        }
        synchronized (SpiFactory.class) {
            if (cache.get(c) != null) {
                return (T) cache.get(c);
            }

            ServiceLoader<T> load = (ServiceLoader<T>) ServiceLoader.load(c);
            for (T obj : load) {
                cache.put(c, obj);
                return obj;
            }
            return null;
        }
    }
}
