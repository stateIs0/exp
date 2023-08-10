package cn.think.in.java.open.exp.adapter.springboot2.tenant;

import cn.think.in.java.open.exp.adapter.springboot2.example.UserService;
import cn.think.in.java.open.exp.core.tenant.impl.TenantExpAppContext;
import cn.think.in.java.open.exp.core.tenant.impl.TenantExpAppContextSpiFactory;
import cn.think.in.java.open.exp.core.tenant.impl.TenantObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@SpringBootApplication
public class TenantMainBoot2 {
    public static void main(String[] args) {
        SpringApplication.run(TenantMainBoot2.class, args);

        TenantExpAppContext tenantExpAppContext = TenantExpAppContextSpiFactory.getFirst();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                tenantExpAppContext.updateSort("example-plugin2_2.0.0", 11);
                Optional<UserService> sortFirst = tenantExpAppContext.getSortFirst(UserService.class, "999");
                List<UserService> list = tenantExpAppContext.getList(UserService.class, "999");
                sortFirst.ifPresent(userService -> userService.createUserExt());
                System.out.println(list);
            }
        }).start();
    }
}