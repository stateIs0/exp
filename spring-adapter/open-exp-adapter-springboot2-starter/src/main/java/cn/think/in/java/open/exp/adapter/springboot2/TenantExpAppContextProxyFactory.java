package cn.think.in.java.open.exp.adapter.springboot2;

import cn.think.in.java.open.exp.client.Sort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.SpringNamingPolicy;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author cxs
 **/
@Slf4j
@SuppressWarnings("unchecked")
public class TenantExpAppContextProxyFactory {


    public static class ExpMethodInterceptor implements MethodInterceptor {
        Object bean;
        String pluginId;
        boolean setAccessibleFlag = false;

        public ExpMethodInterceptor(Object bean, String pluginId) {
            this.bean = bean;
            this.pluginId = pluginId;
        }

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            if (!setAccessibleFlag) {
                method.setAccessible(true);
                setAccessibleFlag = true;
            }

            try {
                return method.invoke(bean, objects);
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw e;
            }
        }
    }

    public static <P> P getProxy(final MethodInterceptor callback, Class<P> c, String pluginId) {
        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(new Class[]{Sort.class});
        enhancer.setSuperclass(c);
        enhancer.setNamingPolicy(new SpringNamingPolicy() {
            @Override
            protected String getTag() {
                return super.getTag() + "$$EXP$$" + pluginId;
            }
        });
        enhancer.setCallback(callback);
        return (P) enhancer.create();
    }
}
