package cn.think.in.java.open.exp.adapter.springboot2;

import cn.think.in.java.open.exp.client.ExpAppContext;
import cn.think.in.java.open.exp.client.ExpAppContextSpiFactory;
import cn.think.in.java.open.exp.client.TenantObject;
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
        ExpAppContext expAppContext = ExpAppContextSpiFactory.getFirst();
        String pluginId;
        Integer sort;

        public ExpMethodInterceptor(Object bean, String pluginId) {
            this.bean = bean;
            this.pluginId = pluginId;
        }

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            method.setAccessible(true);
            try {
                Integer sortNum = sortHandle((TenantObject) o, method, objects);
                if (sortNum != null) {
                    return sortNum;
                }

                return method.invoke(bean, objects);
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw e;
            }
        }

        private Integer sortHandle(TenantObject o, Method method, Object[] objects) {
            if ("getSort".equals(method.getName())) {
                sort = expAppContext.getTenantCallback().getSort(pluginId);
                if (sort == null) {
                    sort = 0;
                }
                return sort;
            }

            if ("compareTo".equals(method.getName())) {
                return ((TenantObject) objects[0]).getSort() - o.getSort();
            }
            return null;
        }
    }
}
