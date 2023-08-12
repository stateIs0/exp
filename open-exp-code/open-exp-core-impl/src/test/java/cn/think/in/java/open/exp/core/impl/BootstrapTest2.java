package cn.think.in.java.open.exp.core.impl;

import cn.think.in.java.open.exp.adapter.springboot2.example.UserService;
import cn.think.in.java.open.exp.client.ExpAppContext;
import cn.think.in.java.open.exp.client.TenantCallback;
import org.junit.Test;

/**
 * @Author cxs
 **/
public class BootstrapTest2 {

    @Test
    public void bootstrap() throws Throwable {
        ExpAppContext expAppContext = Bootstrap.bootstrap(new SimpleObjectStore(), "../../exp-plugins", "exp-workdir");

        String asser = "2";

        // 业务逻辑实现
        TenantCallback callback = new TenantCallback() {
            @Override
            public int getSort(String pluginId) {
                // 测试用, 随便写的.
                if (pluginId.endsWith(asser + ".0.0")) {
                    return 2;
                }
                return 1;
            }

            @Override
            public boolean filter(String pluginId) {
                return true;
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
