package cn.think.in.java.open.exp.client;

import java.util.List;

/**
 * @Author cxs
 **/
public class DefaultScaner implements PluginObjectScanner {

    private String scanPath;
    private ClassLoader pluginClassLoader;
    private String location;


    public void setScanPath(String scanPath) {
        this.scanPath = scanPath;
    }

    public void setPluginClassLoader(ClassLoader pluginClassLoader) {
        this.pluginClassLoader = pluginClassLoader;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public List<Class<?>> scan() {
        try {
            return SimpleClassScanner.doScan(scanPath, ExPBean.class, pluginClassLoader, location);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
