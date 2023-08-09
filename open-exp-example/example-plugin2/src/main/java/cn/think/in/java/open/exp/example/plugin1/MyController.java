package cn.think.in.java.open.exp.example.plugin1;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author cxs
 * @Description
 * @date 2023/8/9
 * @version 1.0
 **/
@RestController
@RequestMapping("/hello")
public class MyController {

    @RequestMapping("/bb2")
    public String hello() {
        return "hello2 " + getClass().getClassLoader() + getClass().getProtectionDomain()
                .getCodeSource().getLocation();
    }
}
