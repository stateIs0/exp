package cn.think.in.java.open.exp.example.b;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String hello(@RequestParam(name = "key") String key) {

        return Boot.get(key, "default") + " ---->> hello2 " + getClass() + "--->>." + getClass().getProtectionDomain()
                .getCodeSource().getLocation();
    }
}
