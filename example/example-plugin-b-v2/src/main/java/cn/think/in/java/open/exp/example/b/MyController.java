package cn.think.in.java.open.exp.example.b;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/9
 **/
@RestController
@RequestMapping("/hello")
public class MyController {

    @Resource
    MybatisSupport mybatisSupport;

    @GetMapping("/bv2")
    public String hello() {

        MyMapper myMapper = mybatisSupport.doGetMapper(MyMapper.class);

        MyTable myTable = myMapper.selectById(1L);

        return myTable.name;
    }
}
