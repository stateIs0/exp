package cn.think.in.java.open.exp.example.bv3;

import cn.think.in.java.open.exp.adapter.springboot2.example.UserService;
import cn.think.in.java.open.exp.client.ExPBean;
import lombok.extern.slf4j.Slf4j;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/9
 **/
@Slf4j
public class UserPlugin implements UserService, ExPBean {

    String name = "plugin3";

    @Override
    public void createUserExt() {
        System.out.println("name = " + name);
        log.info("name = {}", name);
    }
}
