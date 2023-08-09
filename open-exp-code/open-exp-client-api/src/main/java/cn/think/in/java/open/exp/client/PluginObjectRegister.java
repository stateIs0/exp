package cn.think.in.java.open.exp.client;

import java.util.List;

/**
 * @Author cxs
 **/
public interface PluginObjectRegister {

    void setScanPath(String scanPath);

    void setPluginClassLoader(ClassLoader pluginClassLoader);

    void setLocation(String location);

    List<Class<?>> register(Callback callback) throws Exception;

    interface Callback {
        void register(Class<?> aClass) throws Exception;
    }
}
