package cn.think.in.java.simple.app.example;

import cn.think.in.java.open.exp.adapter.springboot2.example.UserService;
import cn.think.in.java.open.exp.client.ExpAppContext;
import cn.think.in.java.open.exp.client.TenantCallback;
import cn.think.in.java.open.exp.core.impl.Bootstrap;

import java.util.Optional;
import java.util.Random;

public class SimpleJavaAppMain {

    public static void main(String[] args) throws Throwable {
        ExpAppContext bootstrap = Bootstrap.bootstrap("exp-plugins/", "workdir-simple-java-app");
        bootstrap.setTenantCallback(new TenantCallback() {
            @Override
            public Integer getSort(String pluginId) {
                return new Random().nextInt(10);
            }

            @Override
            public Boolean isOwnCurrentTenant(String pluginId) {
                return null;
            }
        });
        Optional<UserService> first = bootstrap.get(UserService.class).stream().findFirst();
        first.ifPresent(userService -> {
            System.out.println(userService.getClass());
            System.out.println(userService.getClass().getClassLoader());
            userService.createUserExt();
        });
    }
}