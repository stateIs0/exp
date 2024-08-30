package cn.think.in.java.open.exp.example.bv3;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @jakarta.annotation.PostConstruct
    public void init() {
        log.info("init...bv3");
    }

    @GetMapping("/bv3")
    public String hello() {

//        MyMapper myMapper = mybatisSupport.doGetMapper(MyMapper.class);
//
//        MyTable myTable = myMapper.selectById(1L);

        return "myTable.name";
    }
}
