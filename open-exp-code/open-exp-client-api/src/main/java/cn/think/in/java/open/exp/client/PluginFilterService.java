package cn.think.in.java.open.exp.client;

import java.util.List;

/**
 * @Author cxs
 **/
public interface PluginFilterService {

    <P> List<P> get(String extCode, PluginFilter filter);

    <P> List<P> get(Class<P> pClass, PluginFilter callback);
}
