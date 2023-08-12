package cn.think.in.java.open.exp.client;

import java.util.List;

/**
 * @Author cxs
 **/
public interface PluginObjectRegister {

    void setScanPath(String scanPath);

    void setPluginClassLoader(ClassLoader pluginClassLoader);

    void setLocation(String location);

    /**
     * 注册
     * @param callback 注册回调
     */
    List<Class<?>> register(Callback callback) throws Exception;

    interface Callback {
        /**
         *
         * @param aClass 当前的 class
         */
        void register(Class<?> aClass) throws Exception;
    }
}
