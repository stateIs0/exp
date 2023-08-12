package cn.think.in.java.open.exp.core.impl.proxy;

import cn.think.in.java.open.exp.client.Sort;
import net.sf.cglib.core.DefaultNamingPolicy;
import net.sf.cglib.proxy.Enhancer;

/**
 * @Author cxs
 **/
public class SortNetSfCglibProxyEnhancer {

    public static Object getEnhancer(Object b, String pluginId) {
        Enhancer enhancer = new Enhancer();
        // 实现 sort 接口, 用于排序
        enhancer.setInterfaces(new Class[]{Sort.class});
        enhancer.setSuperclass(b.getClass());
        enhancer.setNamingPolicy(new DefaultNamingPolicy() {
            @Override
            protected String getTag() {
                return super.getTag() + "$$EXP$$SORT";
            }
        });
        enhancer.setCallback(new ExpSortInterceptor(pluginId, b));
        return enhancer.create();
    }

}
