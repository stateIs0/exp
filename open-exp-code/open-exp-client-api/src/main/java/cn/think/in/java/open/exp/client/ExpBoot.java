package cn.think.in.java.open.exp.client;

/**
 * @Author cxs
 **/
public interface ExpBoot {

    PluginObjectScanner getRegister() throws Throwable;

    default void start(String pluginId) {

    }

    default void stop() {

    }
}
