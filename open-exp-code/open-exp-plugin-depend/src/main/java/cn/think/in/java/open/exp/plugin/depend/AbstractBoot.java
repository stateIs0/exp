package cn.think.in.java.open.exp.plugin.depend;

import cn.think.in.java.open.exp.client.ExpBoot;
import cn.think.in.java.open.exp.client.PluginClassLoader;
import cn.think.in.java.open.exp.client.PluginObjectRegister;
import cn.think.in.java.open.exp.client.StringUtil;
import cn.think.in.java.open.exp.plugin.depend.support.PluginIdUtil;

/**
 * @Author cxs
 **/
public abstract class AbstractBoot implements ExpBoot {

    private final ClassLoader pluginClassLoader;
    private final String classLocation;
    private final DefaultRegister pluginBeanRegister;
    private String pluginId;

    public AbstractBoot() {
        if (getClass().getClassLoader() instanceof PluginClassLoader) {
            this.classLocation = ((PluginClassLoader) getClass().getClassLoader()).getPath();
        } else {
            throw new RuntimeException("classLocation 非法");
        }
        this.pluginClassLoader = getClass().getClassLoader();
        this.pluginBeanRegister = new DefaultRegister();
        this.pluginBeanRegister.setPluginClassLoader(this.pluginClassLoader);
        this.pluginBeanRegister.setLocation(this.classLocation);
        this.pluginId = PluginIdUtil.getId(this.classLocation);
        setPluginId(this.pluginId);
        String scanPath = getScanPath();
        if (StringUtil.isEmpty(scanPath)) {
            throw new RuntimeException("AbstractBoot getScanPath 不能为空.");
        }
        this.pluginBeanRegister.setScanPath(getScanPath());
    }

    @Override
    public PluginObjectRegister getRegister() throws Throwable {
        return this.pluginBeanRegister;
    }

    protected abstract String getScanPath();

}
