package cn.think.in.java.open.exp.adapter.springboot1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class);
    }


    @RestController
    @RequestMapping("/base")
    class Con {

        @RequestMapping("/main")
        public String hello() {
            return "hi";
        }
    }
}

