package cn.think.in.java.simple.app.example;

import cn.think.in.java.open.exp.adapter.springboot2.example.UserService;
import cn.think.in.java.open.exp.client.ExpAppContext;
import cn.think.in.java.open.exp.client.TenantCallback;
import cn.think.in.java.open.exp.core.impl.Bootstrap;

import java.util.Optional;
import java.util.Random;

public class SimpleJavaAppMain {

    public static void main(String[] args) throws Throwable {
        Class<UserService> extensionClass = UserService.class;
        ExpAppContext expAppContext = Bootstrap.bootstrap("exp-plugins/", "workdir-simple-java-app");
        TenantCallback callback = new TenantCallback() {
            @Override
            public int getSort(String pluginId) {
                int sort = new Random().nextInt(10);
                System.out.println(pluginId + ",    " + sort);
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
    }
}