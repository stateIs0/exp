package cn.think.in.java.open.exp.client;

/**
 * @Author cxs
 **/
public interface ExpBoot {

    PluginObjectRegister getRegister() throws Throwable;

    default void setPluginId(String pluginId) {
    }
}
