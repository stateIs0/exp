package cn.think.in.java.open.exp.plugin.depend;

import cn.think.in.java.open.exp.client.ExPBean;
import cn.think.in.java.open.exp.client.PluginObjectRegister;

import java.util.List;

/**
 * @Author cxs
 **/
public class DefaultRegister implements PluginObjectRegister {

    List<Class<?>> classes;
    /**
     */
    private String scanPath;
    /**
     */
    private ClassLoader pluginClassLoader;
    /**
     */
    private String location;


    @Override
    public void setScanPath(String scanPath) {
        this.scanPath = scanPath;
    }

    @Override
    public void setPluginClassLoader(ClassLoader pluginClassLoader) {
        this.pluginClassLoader = pluginClassLoader;
    }

    @Override
    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public List<Class<?>> register(Callback callback) {
        try {
            classes = SimpleClassScanner.doScan(scanPath,
                    ExPBean.class, pluginClassLoader, location);
            for (Class<?> aClass : classes) {
                callback.register(aClass);
            }
            return classes;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
