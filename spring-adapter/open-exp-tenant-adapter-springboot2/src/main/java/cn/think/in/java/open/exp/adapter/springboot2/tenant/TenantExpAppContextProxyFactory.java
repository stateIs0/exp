package cn.think.in.java.open.exp.adapter.springboot2.tenant;

import cn.think.in.java.open.exp.core.tenant.impl.TenantExpAppContext;
import cn.think.in.java.open.exp.core.tenant.impl.TenantExpAppContextSpiFactory;
import cn.think.in.java.open.exp.core.tenant.impl.TenantObject;
import org.springframework.cglib.core.SpringNamingPolicy;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author cxs
 * @Description
 * @date 2023/8/10
 * @version 1.0
 **/
public class TenantExpAppContextProxyFactory {

    private static final SpringNamingPolicy SPRING_NAMING_POLICY = new SpringNamingPolicy() {
        @Override
        protected String getTag() {
            return super.getTag() + "$$EXP";
        }
    };

    public static <P> P getProxy(final MethodInterceptor callback, Class<P> c) {
        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(new Class[]{TenantObject.class});
        enhancer.setSuperclass(c);
        enhancer.setNamingPolicy(SPRING_NAMING_POLICY);
        enhancer.setCallback(callback);
        return (P) enhancer.create();
    }

    public static class ExpMethodInterceptor implements MethodInterceptor {


        Object bean;

        TenantExpAppContext tenantExpAppContext = TenantExpAppContextSpiFactory.getFirst();

        String pluginId;

        int sort;

        public ExpMethodInterceptor(Object bean, String pluginId) {

            this.bean = bean;
            this.pluginId = pluginId;
        }

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            method.setAccessible(true);
            try {
                if (method.getName().equals("getTenantId")) {
                    return tenantExpAppContext.getTenantId(pluginId);
                }
                if ("getSort".equals(method.getName())) {
                    sort = tenantExpAppContext.getSort(pluginId);
                    return sort;
                }
                if (method.getName().equals("compareTo")) {
                    return ((TenantObject) objects[0]).getSort() - ((TenantObject) o).getSort();
                }

                return method.invoke(bean, objects);
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            } catch (Exception e) {
                System.out.println(method.getName());
                throw e;
            }
        }
    }
}
