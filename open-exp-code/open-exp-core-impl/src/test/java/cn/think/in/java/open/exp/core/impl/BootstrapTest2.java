package cn.think.in.java.open.exp.core.impl;

import cn.think.in.java.open.exp.adapter.springboot2.example.UserService;
import cn.think.in.java.open.exp.client.ExpAppContext;
import cn.think.in.java.open.exp.client.TenantCallback;
import org.junit.Test;

import java.util.function.Consumer;

/**
 * @Author cxs
 **/
public class BootstrapTest2 {

    @Test
    public void bootstrap() throws Throwable {
        ExpAppContext expAppContext = Bootstrap.bootstrap("../../exp-plugins", "exp-workdir");

        String asser = "2";

        // 业务逻辑实现
        expAppContext.setTenantCallback(new TenantCallback() {
            @Override
            public Integer getSort(String pluginId) {
                // 测试用, 随便写的.
                if (pluginId.endsWith(asser + ".0.0")) {
                    return 2;
                }
                return 1;
            }

            @Override
            public Boolean isOwnCurrentTenant(String pluginId) {
                return true;
            }
        });

        // 调用逻辑
        expAppContext.get(UserService.class).stream().findFirst().ifPresent(new Consumer<UserService>() {
            @Override
            public void accept(UserService userService) {
                System.out.println("---->>> " + userService.getClass().getName());
                userService.createUserExt();
                // Assert
            }
        });
    }
}
