package cn.think.in.java.open.exp.example.b;

import cn.think.in.java.open.exp.client.ObjectStore;
import cn.think.in.java.open.exp.client.PluginLifeCycleHook;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

/**
 * @Description PluginLifeCycleHook example
 * @Author jujiale
 * @Date 2023/12/24
 */
@Component
public class UserPluginLifeCycleHook implements PluginLifeCycleHook {
    @Override
    public void postInit(ObjectStore objectStore, Class<?> clazz, Supplier<Object> bean, String beanName) {
        System.out.println("example-plugin-b-v1#UserPluginLifeCycleHook#postInit invoked...");
    }

    @Override
    public void postDestroy(ObjectStore objectStore, Class<?> aClass, String beanName) {
        System.out.println("example-plugin-b-v1#UserPluginLifeCycleHook#postDestroy invoked...");
    }
}
