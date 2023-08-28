package cn.think.in.java.open.exp.core.impl;

import cn.think.in.java.open.exp.adapter.springboot2.example.UserService;
import cn.think.in.java.open.exp.client.ExpAppContext;
import cn.think.in.java.open.exp.client.ObjectStore;
import cn.think.in.java.open.exp.client.PluginFilter;
import cn.think.in.java.open.exp.client.TenantObjectProxyFactory;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.core.NamingPolicy;
import net.sf.cglib.core.Predicate;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class BootstrapTest {


    Map<String, Map<String, Object>> pluginIdMapping = new HashMap<>();

    @org.junit.Test
    public void bootstrap() throws Throwable {
        ExpAppContext expAppContext = Bootstrap.bootstrap(new ObjectStore() {
            @Override
            public void startRegister(List<Class<?>> list, String pluginId) throws Exception {
                Map<String, Object> store = new HashMap<>();
                for (Class<?> aClass : list) {
                    Object proxy = null;
                    //getTenantObjectProxyFactory().getProxy(aClass.newInstance(), pluginId);
                    store.put(aClass.getName(), proxy);
                }
                pluginIdMapping.put(pluginId, store);
            }

            @Override
            public void unRegister(String pluginId) {
                pluginIdMapping.remove(pluginId);
            }

            @Override
            public <T> T getObject(Class<?> c, String pluginId) {
                return (T) pluginIdMapping.get(pluginId).get(c.getName());
            }

            @Override
            public Object getOrigin() {
                return null;
            }


            //@Override
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
                        }, origin.getClass(), pluginId);
                    }


                    public <P> P getProxy(final MethodInterceptor callback, Class<P> c, String pluginId) {
                        Enhancer enhancer = new Enhancer();
                        enhancer.setInterfaces(new Class[]{});
                        enhancer.setSuperclass(c);
                        enhancer.setNamingPolicy(new NamingPolicy() {
                            @Override
                            public String getClassName(String s, String s1, Object o, Predicate predicate) {
                                return s + "$$" + pluginId + "$$";
                            }
                        });
                        enhancer.setCallback(callback);
                        return (P) enhancer.create();
                    }
                };
            }
        }, "../../exp-plugins", "exp-workdir", "true");


        // 业务逻辑实现
        PluginFilter callback = new PluginFilter() {
            @Override
            public <T> List<FModel<T>> filter(List<FModel<T>> list) {
                return list;
            }
        };

        // 调用逻辑
        expAppContext.get(UserService.class).stream().findFirst().ifPresent(userService -> {
            System.out.println("---->>> " + userService.getClass().getName());
            userService.createUserExt();
            // Assert
        });

    }
}