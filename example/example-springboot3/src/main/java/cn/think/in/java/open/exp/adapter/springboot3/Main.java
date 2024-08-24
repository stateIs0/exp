package cn.think.in.java.open.exp.adapter.springboot3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * spring boot 3 需要jdk的版本至少为17
 * VM --add-opens java.base/java.lang=ALL-UNNAMED
 **/
@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @RestController
    @RequestMapping("/hello")
    class Cont {

        @RequestMapping("/hi")
        public User s() {
            return new User();
        }
    }
}
