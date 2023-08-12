package cn.think.in.java.open.exp.core.impl.proxy;

import cn.think.in.java.open.exp.client.ExpAppContext;
import cn.think.in.java.open.exp.client.ExpAppContextSpiFactory;
import cn.think.in.java.open.exp.client.Sort;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author cxs
 * @Description
 * @date 2023/8/11
 * @version 1.0
 **/
@Slf4j
public class ExpSortInterceptor implements MethodInterceptor, Sort {

    ExpAppContext expAppContext = ExpAppContextSpiFactory.getFirst();
    Integer sort;
    String pluginId;
    Object target;

    public ExpSortInterceptor(String pluginId, Object target) {
        this.pluginId = pluginId;
        this.target = target;
    }

    @Override
    public Object intercept(Object origin, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        try {
            // 一个扩展点多个实现时, 会触发排序.
            if ("getSort".equals(method.getName())) {
                return getSort();
            }

            if ("compareTo".equals(method.getName())) {
                return ((Sort) objects[0]).getSort() - ((Sort) origin).getSort();
            }

            return method.invoke(target, objects);
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
