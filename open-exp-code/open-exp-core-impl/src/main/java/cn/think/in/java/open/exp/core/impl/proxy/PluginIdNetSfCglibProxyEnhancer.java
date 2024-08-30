//package cn.think.in.java.open.exp.core.impl.proxy;
//
//import net.sf.cglib.core.DefaultNamingPolicy;
//import net.sf.cglib.proxy.Enhancer;
//
///**
// * @Author cxs
// **/
//public class PluginIdNetSfCglibProxyEnhancer {
//
//    public static Object getEnhancer(Object b, String pluginId) {
//        Enhancer enhancer = new Enhancer();
//        enhancer.setInterfaces(new Class[]{});
//        enhancer.setSuperclass(b.getClass());
//        enhancer.setNamingPolicy(new DefaultNamingPolicy() {
//            @Override
//            protected String getTag() {
//                return super.getTag() + "$$EXP$$SORT$$" + pluginId;
//            }
//        });
//        enhancer.setCallback(new ExpPluginIdInterceptor(pluginId, b));
//        return enhancer.create();
//    }
//
//}
