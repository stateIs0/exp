package cn.think.in.java.open.exp.core.impl;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Author cxs
 * @Description
 * @date 2023/8/11
 * @version 1.0
 **/
public class CgLibClient {

    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(HelloImpl.class);
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                System.out.println("before method run...");
                Object result = methodProxy.invokeSuper(o, args);
                System.out.println("after method run...");
                return result;
            }
        });

        HelloImpl hello = (HelloImpl) enhancer.create();
        hello.sayHello();
    }
}

interface IHello {
    void sayHello();
}

class HelloImpl implements IHello {
    @Override
    public void sayHello() {
        System.out.println("hello");
    }
}
