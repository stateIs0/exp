package cn.think.in.java.open.exp.plugin.depend;

import cn.think.in.java.open.exp.client.ExPBean;
import cn.think.in.java.open.exp.client.PluginObjectRegister;

import java.util.List;

/**
 * @Author cxs
 **/
public class DefaultRegister implements PluginObjectRegister {

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
    public List<Class<?>> register() {
        try {
            return SimpleClassScanner.doScan(scanPath, ExPBean.class, pluginClassLoader, location);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
