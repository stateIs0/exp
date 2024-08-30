package cn.think.in.java.open.exp.example.a;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.think.in.java.open.exp.example.a.Boot.configSupport;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/9
 **/
@RestController
@RequestMapping("/hello")
public class MyController {

    @jakarta.annotation.PostConstruct
    public void init() {
        System.out.println("--->>");
    }

    @RequestMapping("/av1")
    public String hello() {
        return configSupport.getProperty();
    }
}
