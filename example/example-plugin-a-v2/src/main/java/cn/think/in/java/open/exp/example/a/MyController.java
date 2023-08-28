package cn.think.in.java.open.exp.example.a;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/9
 **/
@Slf4j
@RestController
@RequestMapping("/hello")
public class MyController {

    @RequestMapping("/av2")
    public String hello() {
        log.info(MyController.class.getName());
        return Boot.configSupport.getProperty();
    }
}
