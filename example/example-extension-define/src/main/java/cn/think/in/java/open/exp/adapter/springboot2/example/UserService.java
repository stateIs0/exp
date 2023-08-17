package cn.think.in.java.open.exp.adapter.springboot2.example;

import cn.think.in.java.open.exp.document.api.ExtApiInterface;

/**
 * @Author cxs
 **/
@ExtApiInterface(desc = "UserService 服务", createTime = "2023/08/14")
public interface UserService {


    void createUserExt();

    default String getName() {
        return "";
    }

}
