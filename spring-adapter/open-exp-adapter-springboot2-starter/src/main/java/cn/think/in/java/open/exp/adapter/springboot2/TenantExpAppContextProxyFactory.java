package cn.think.in.java.open.exp.adapter.springboot2;

import cn.think.in.java.open.exp.client.ExpAppContext;
import cn.think.in.java.open.exp.client.ExpAppContextSpiFactory;
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

    private static final SpringNamingPolicy SPRING_NAMING_POLICY = new SpringNamingPolicy() {
        @Override
        protected String getTag() {
            return super.getTag() + "$$EXP";
        }
    };

    public static <P> P getProxy(final MethodInterceptor callback, Class<P> c) {
        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(new Class[]{Sort.class});
        enhancer.setSuperclass(c);
        enhancer.setNamingPolicy(SPRING_NAMING_POLICY);
        enhancer.setCallback(callback);
        return (P) enhancer.create();
    }

    public static class ExpMethodInterceptor implements MethodInterceptor, Sort {
        Object bean;
        ExpAppContext expAppContext = ExpAppContextSpiFactory.getFirst();
        String pluginId;
        Integer sort;
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
                // 一个扩展点多个实现时, 会触发排序.
                if ("getSort".equals(method.getName())) {
                    return getSort();
                }

                if ("compareTo".equals(method.getName())) {
                    return ((Sort) objects[0]).getSort() - ((Sort) o).getSort();
                }

                return method.invoke(bean, objects);
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw e;
            }
        }

        @Override
        public int getSort() {
            sort = expAppContext.getTenantCallback().getSort(pluginId);
            if (sort == null) {
                sort = 0;
            }
            return sort;
        }

    }
}
