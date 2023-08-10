package cn.think.in.java.open.exp.adapter.springboot2.tenant;

import cn.think.in.java.open.exp.adapter.springboot2.example.UserService;
import cn.think.in.java.open.exp.client.ExpAppContext;
import cn.think.in.java.open.exp.client.ExpAppContextSpiFactory;
import cn.think.in.java.open.exp.client.Plugin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Optional;

/**
 * @Author cxs
 * @Description
 * @date 2023/8/9
 * @version 1.0
 **/
@RestController
@RequestMapping("/base")
@Slf4j
public class BaseController {

    @RequestMapping("/hi")
    public String hi() {
        return "hi";
    }

    ExpAppContext expAppContext = ExpAppContextSpiFactory.getFirst();


    @RequestMapping("/run")
    public String run() {
        Optional<UserService> first = expAppContext.get(UserService.class).stream().findFirst();
        if (first.isPresent()) {
            first.get().createUserExt();
        } else {
            return "not found";
        }
        return "success";
    }


    @RequestMapping("/install")
    public String install(String path) throws Throwable {
        Plugin load = expAppContext.load(new File(path));


        return load.getPluginId();
    }

    @RequestMapping("/unInstall")
    public String unInstall(String id) throws Exception {
        log.info("plugin id {}", id);
        expAppContext.unload(id);
        return "ok";
    }
}
