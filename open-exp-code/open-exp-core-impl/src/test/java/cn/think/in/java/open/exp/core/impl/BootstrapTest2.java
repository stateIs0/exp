package cn.think.in.java.open.exp.core.impl;

import cn.think.in.java.open.exp.adapter.springboot2.example.UserService;
import cn.think.in.java.open.exp.client.ExpAppContext;
import org.junit.Test;

/**
 * @Author cxs
 **/
public class BootstrapTest2 {

    @Test
    public void bootstrap() throws Throwable {
        ExpAppContext expAppContext = Bootstrap.bootstrap(new SimpleObjectStore(),
                "../../exp-plugins", "exp-workdir", "true");

        // 调用逻辑
        expAppContext.list(UserService.class).stream().findFirst().ifPresent(userService -> {
            System.out.println("---->>> " + userService.getClass().getName());
            userService.createUserExt();
            // Assert
        });
    }
}
