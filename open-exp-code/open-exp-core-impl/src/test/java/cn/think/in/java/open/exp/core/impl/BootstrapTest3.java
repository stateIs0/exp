package cn.think.in.java.open.exp.core.impl;

import cn.think.in.java.open.exp.adapter.springboot2.example.UserService;
import cn.think.in.java.open.exp.client.ExpAppContext;
import org.junit.Test;

import java.io.File;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/12
 **/
public class BootstrapTest3 {

    @Test
    public void test() throws Throwable {

        String absolutePath = new File("../../exp-plugins/").getAbsolutePath();
        ExpAppContext bootstrap = Bootstrap.bootstrap(absolutePath, "exp-workdir3");
        bootstrap.list(UserService.class).stream().findFirst().ifPresent(userService -> {
            System.out.println("exist");
            System.out.println(userService.getClass());
            System.out.println(userService.getClass().getClassLoader());
            userService.createUserExt();
        });
    }
}
