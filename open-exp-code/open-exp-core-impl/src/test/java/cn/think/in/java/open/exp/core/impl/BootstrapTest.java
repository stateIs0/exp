package cn.think.in.java.open.exp.core.impl;

import cn.think.in.java.open.exp.adapter.springboot2.example.UserService;
import cn.think.in.java.open.exp.client.*;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.core.DefaultNamingPolicy;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
public class BootstrapTest {


    Map<String, Map<String, Object>> pluginIdMapping = new HashMap<>();

    @org.junit.Test
    public void bootstrap() throws Throwable {
        ExpAppContext expAppContext = Bootstrap.bootstrap(new ObjectStore() {
            @Override
            public List<Class<?>> startRegister(PluginObjectRegister pluginObjectRegisters, String pluginId) throws Exception {
                Map<String, Object> store = new HashMap<>();
                List<Class<?>> classList = pluginObjectRegisters.register(new PluginObjectRegister.Callback() {
                    @Override
                    public void register(Class<?> aClass) throws Exception {
                        Object proxy = getTenantObjectProxyFactory().getProxy(aClass.newInstance(), pluginId);
                        store.put(aClass.getName(), proxy);
                    }
                });
                pluginIdMapping.put(pluginId, store);
                return classList;
            }

            @Override
            public void unRegister(String pluginId) {
                pluginIdMapping.remove(pluginId);
            }

            @Override
            public <T> T getObject(String name, String pluginId) {
                return (T) pluginIdMapping.get(pluginId).get(name);
            }


            @Override
            public TenantObjectProxyFactory getTenantObjectProxyFactory() {
                return new TenantObjectProxyFactory() {

                    @Override
                    public Object getProxy(Object origin, String pluginId) {
                        return getProxy((o, method, objects, methodProxy) -> {
                            try {
                                return method.invoke(origin, objects);
                            } catch (InvocationTargetException e) {
                                throw e.getTargetException();
                            } catch (Exception e) {
                                log.error(e.getMessage(), e);
                                throw e;
                            }
                        }, origin.getClass());
                    }


                    public <P> P getProxy(final MethodInterceptor callback, Class<P> c) {
                        Enhancer enhancer = new Enhancer();
                        enhancer.setInterfaces(new Class[]{Sort.class});
                        enhancer.setSuperclass(c);
                        enhancer.setNamingPolicy(new DefaultNamingPolicy());
                        enhancer.setCallback(callback);
                        return (P) enhancer.create();
                    }
                };
            }
        }, "../../exp-plugins", "exp-workdir");


        // 业务逻辑实现
        TenantCallback callback = new TenantCallback() {
            @Override
            public int getSort(String pluginId) {
                // 测试用, 随便写的.
                if (pluginId.endsWith("1.0.0")) {
                    return 2;
                }
                return 1;
            }

            @Override
            public boolean filter(String pluginId) {
                return true;
            }
        };

        // 调用逻辑
        expAppContext.get(UserService.class, callback).stream().findFirst().ifPresent(new Consumer<UserService>() {
            @Override
            public void accept(UserService userService) {
                System.out.println("---->>> " + userService.getClass().getName());
                userService.createUserExt();
                // Assert
            }
        });

    }
}