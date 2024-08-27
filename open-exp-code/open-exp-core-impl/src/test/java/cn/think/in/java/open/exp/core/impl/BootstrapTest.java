package cn.think.in.java.open.exp.core.impl;

import cn.think.in.java.open.exp.adapter.springboot2.example.UserService;
import cn.think.in.java.open.exp.client.ExpAppContext;
import cn.think.in.java.open.exp.client.ObjectStore;
import lombok.extern.slf4j.Slf4j;

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
            public void registerCallback(List<Class<?>> list, String pluginId) throws Exception {
                Map<String, Object> store = new HashMap<>();
                for (Class<?> aClass : list) {
                    Object proxy = null;
                    store.put(aClass.getName(), proxy);
                }
                pluginIdMapping.put(pluginId, store);
            }

            @Override
            public void unRegisterCallback(String pluginId) {
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
        }, "../../exp-plugins", "exp-workdir", "true");


        // 调用逻辑
        expAppContext.list(UserService.class).stream().findFirst().ifPresent(userService -> {
            System.out.println("---->>> " + userService.getClass().getName());
            userService.createUserExt();
            // Assert
        });

    }
}