package cn.think.in.java.simple.app.example;

import cn.think.in.java.open.exp.adapter.springboot2.example.UserService;
import cn.think.in.java.open.exp.client.ExpAppContext;
import cn.think.in.java.open.exp.client.PluginFilter;
import cn.think.in.java.open.exp.core.impl.Bootstrap;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Slf4j
public class SimpleJavaAppMain {
    static ExpAppContext expAppContext;
    static PluginFilter callback = new PluginFilter() {

        @Override
        public <T> List<FModel<T>> filter(List<FModel<T>> list) {
            return list;
        }
    };

    public static void main(String[] args) throws Throwable {
        Class<UserService> extensionClass = UserService.class;
        expAppContext = Bootstrap.bootstrap("exp-plugins/", "workdir-simple-java-app");

        expAppContext.get(extensionClass, callback).stream().findFirst().ifPresent(userService -> {
            System.out.println(userService.getClass());
            System.out.println(userService.getClass().getClassLoader());
            userService.createUserExt();
        });

        // UserService.class.getName(), 参数是字符串, 无法使用流式.
        Optional<UserService> optional = expAppContext.get(UserService.class.getName(), "example.plugin.a_1.0.0");
        optional.ifPresent(userService -> {
            log.info(userService.getClass().getName());
            userService.createUserExt();
        });

        String name = expAppContext.stream(UserService.class, "example.plugin.a_1.0.0", UserService::getName);
        System.out.println(name);

        String listName = expAppContext.streamList(UserService.class, SimpleJavaAppMain::run);
        System.out.println(listName);

        List<String> allPluginId = expAppContext.getAllPluginId();
        System.out.println(allPluginId);
    }

    private static String run(List<UserService> userServices) {
        return userServices.stream().findFirst().get().getName();
    }
}