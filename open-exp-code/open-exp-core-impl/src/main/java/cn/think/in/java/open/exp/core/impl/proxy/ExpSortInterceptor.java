package cn.think.in.java.open.exp.core.impl.proxy;

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
public class ExpSortInterceptor implements MethodInterceptor {

    String pluginId;
    Object target;

    public ExpSortInterceptor(String pluginId, Object target) {
        this.pluginId = pluginId;
        this.target = target;
    }

    @Override
    public Object intercept(Object origin, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        try {
            return method.invoke(target, objects);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

}
