package cn.think.in.java.open.exp.example.empty;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.think.in.java.open.exp.example.empty.Boot.configSupport;

/**
 * @version 1.0
 **/
@RestController
@RequestMapping("/hello")
public class MyController {

    @RequestMapping("/empty")
    public String hello() {
        return configSupport.getProperty();
    }
}
