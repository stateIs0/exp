package cn.think.in.java.open.exp.adapter.springboot2.tenant;

import cn.think.in.java.open.exp.adapter.springboot2.example.UserService;
import cn.think.in.java.open.exp.client.Plugin;
import cn.think.in.java.open.exp.core.tenant.impl.TenantExpAppContext;
import cn.think.in.java.open.exp.core.tenant.impl.TenantExpAppContextSpiFactory;
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

    TenantExpAppContext tenantExpAppContext = TenantExpAppContextSpiFactory.getFirst();

    @RequestMapping("/run")
    public String run(String tenantId) {
        Optional<UserService> sortFirst = tenantExpAppContext.getSortFirst(UserService.class, tenantId);
        if (sortFirst.isPresent()) {
            sortFirst.get().createUserExt();
        } else {
            return "not found";
        }
        return "success";
    }


    @RequestMapping("/install")
    public String install(String path, String tenantId) throws Throwable {
        Plugin load = tenantExpAppContext.load(new File(path), tenantId);
        return load.getPluginId();
    }

    @RequestMapping("/unInstall")
    public String unInstall(String id) throws Exception {
        log.info("plugin id {}", id);
        tenantExpAppContext.unload(id);
        return "ok";
    }
}
