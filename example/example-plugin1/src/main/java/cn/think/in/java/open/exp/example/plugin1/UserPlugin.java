package cn.think.in.java.open.exp.example.plugin1;

import cn.think.in.java.open.exp.adapter.springboot2.example.UserService;
import cn.think.in.java.open.exp.client.ExPBean;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author cxs
 * @Description
 * @date 2023/8/9
 * @version 1.0
 **/
@Slf4j
public class UserPlugin implements UserService, ExPBean {

    String name = "plugin1";

    @Override
    public void createUserExt() {
        System.out.println("name = " + name);
        log.info("name = {}", name);
    }
}
