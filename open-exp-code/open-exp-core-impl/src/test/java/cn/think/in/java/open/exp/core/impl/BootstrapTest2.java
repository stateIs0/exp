package cn.think.in.java.open.exp.core.impl;

import cn.think.in.java.open.exp.adapter.springboot2.example.UserService;
import cn.think.in.java.open.exp.client.ExpAppContext;
import cn.think.in.java.open.exp.client.PluginFilter;
import org.junit.Test;

import java.util.List;

/**
 * @Author cxs
 **/
public class BootstrapTest2 {

    @Test
    public void bootstrap() throws Throwable {
        ExpAppContext expAppContext = Bootstrap.bootstrap(new SimpleObjectStore(),
                "../../exp-plugins", "exp-workdir", "true");

        String asser = "2";

        // 业务逻辑实现
        PluginFilter callback = new PluginFilter() {
            @Override
            public <T> List<FModel<T>> filter(List<FModel<T>> list) {
                return list;
            }
        };

        // 调用逻辑
        expAppContext.get(UserService.class, callback).stream().findFirst().ifPresent(userService -> {
            System.out.println("---->>> " + userService.getClass().getName());
            userService.createUserExt();
            // Assert
        });
    }
}
