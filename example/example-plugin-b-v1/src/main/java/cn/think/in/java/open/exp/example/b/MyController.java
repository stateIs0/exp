package cn.think.in.java.open.exp.example.b;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

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

    @PostConstruct
    public void init() {
        log.info("access http://localhost:8080/hello/bv1");
    }

    @RequestMapping("/bv1")
    public String hello() {

        return Boot.configSupport.getProperty();
    }
}
