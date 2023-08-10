package cn.think.in.java.open.exp.adapter.springboot2;

import cn.think.in.java.open.exp.adapter.springboot2.example.UserService;
import cn.think.in.java.open.exp.core.tenant.impl.TenantExpAppContext;
import cn.think.in.java.open.exp.core.tenant.impl.TenantExpAppContextSpiFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainBoot2 {
    public static void main(String[] args) {
        SpringApplication.run(MainBoot2.class, args);

        TenantExpAppContext first = TenantExpAppContextSpiFactory.getFirst();
        first.getSortFirst(UserService.class, "123");
    }
}