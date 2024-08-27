package cn.think.in.java.simple.app.example;

import cn.think.in.java.open.exp.adapter.springboot2.example.UserService;
import cn.think.in.java.open.exp.client.ExpAppContext;
import cn.think.in.java.open.exp.core.impl.Bootstrap;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
public class SimpleJavaAppMain {
    static ExpAppContext expAppContext;

    public static void main(String[] args) throws Throwable {
        Class<UserService> extensionClass = UserService.class;
        expAppContext = Bootstrap.bootstrap("exp-plugins/", "workdir-simple-java-app");

        expAppContext.list(extensionClass).stream().findFirst().ifPresent(userService -> {
            log.info("userService.getClass={}", userService.getClass());
            log.info("userService.getClass().getClassLoader()={}", userService.getClass().getClassLoader());
            userService.createUserExt();
        });

        Optional<UserService> optional = expAppContext.get(UserService.class.getName(), "example.plugin.a_1.0.0");
        optional.ifPresent(userService -> {
            log.info("example.plugin.a_1.0.0--->userService.getClass().getName()={}", userService.getClass().getName());
            userService.createUserExt();
        });

        List<String> allPluginId = expAppContext.getAllPluginId();
        log.info("allPluginId={}", allPluginId);
        log.info("allPluginId.size={}", allPluginId.size());
        log.info("end --------------->>>>>>>>>>>");
    }

}