package cn.think.in.java.open.exp.adapter.springboot27;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class MainBoot27 {
    public static void main(String[] args) {
        SpringApplication.run(MainBoot27.class, args);
    }
}