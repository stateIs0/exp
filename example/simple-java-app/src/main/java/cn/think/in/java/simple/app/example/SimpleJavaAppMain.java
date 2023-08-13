package cn.think.in.java.simple.app.example;

import cn.think.in.java.open.exp.adapter.springboot2.example.UserService;
import cn.think.in.java.open.exp.client.ExpAppContext;
import cn.think.in.java.open.exp.client.TenantCallback;
import cn.think.in.java.open.exp.core.impl.Bootstrap;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;

@Slf4j
public class SimpleJavaAppMain {

    public static void main(String[] args) throws Throwable {
        Class<UserService> extensionClass = UserService.class;
        ExpAppContext expAppContext = Bootstrap.bootstrap("exp-plugins/", "workdir-simple-java-app");
        TenantCallback callback = new TenantCallback() {
            @Override
            public int getSort(String pluginId) {
                int sort = new Random().nextInt(10);
                log.info(pluginId + " >>>>>>> " + sort);
                return sort;
            }

            @Override
            public boolean filter(String pluginId) {
                return true;
            }
        };

        Optional<UserService> first = expAppContext.get(extensionClass, callback).stream().findFirst();
        first.ifPresent(userService -> {
            System.out.println(userService.getClass());
            System.out.println(userService.getClass().getClassLoader());
            userService.createUserExt();
        });

        Optional<UserService> optional = expAppContext.get(UserService.class.getName(), "example.plugin.a_1.0.0");
        optional.ifPresent(userService -> {
            log.info(userService.getClass().getName());
            userService.createUserExt();
        });
    }
}