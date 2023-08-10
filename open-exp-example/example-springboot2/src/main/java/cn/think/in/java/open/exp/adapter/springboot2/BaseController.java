package cn.think.in.java.open.exp.adapter.springboot2;

import cn.think.in.java.open.exp.adapter.springboot2.example.UserService;
import cn.think.in.java.open.exp.client.ExpAppContext;
import cn.think.in.java.open.exp.client.ExpAppContextSpiFactory;
import cn.think.in.java.open.exp.client.Plugin;
import cn.think.in.java.open.exp.client.TenantCallback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
    static ThreadLocal<String> context = new InheritableThreadLocal<>();
    ExpAppContext expAppContext = ExpAppContextSpiFactory.getFirst();
    Map<String, Integer> sortMap = new HashMap<>();
    Map<String, String> pluginIdTenantIdMap = new HashMap<>();

    public BaseController() {
        sortMap.put("example-plugin1_1.0.0", 1);
        sortMap.put("example-plugin2_2.0.0", 2);
        pluginIdTenantIdMap.put("example-plugin2_2.0.0", "12345");
        pluginIdTenantIdMap.put("example-plugin1_1.0.0", "11111");

        expAppContext.setTenantCallback(new TenantCallback() {
            @Override
            public Integer getSort(String pluginId) {
                // 获取这个插件的排序
                return sortMap.get(pluginId);
            }

            @Override
            public Boolean isOwnCurrentTenant(String pluginId) {
                // 判断当前租户是不是这个匹配这个插件
                return context.get().equals(pluginIdTenantIdMap.get(pluginId));
            }
        });
    }

    @RequestMapping("/hi")
    public String hi() {
        return "hi";
    }


    @RequestMapping("/run")
    public String run(String tenantId) {
        // 上下文设置租户 id
        context.set(tenantId);

        List<UserService> userServices = expAppContext.get(UserService.class);

        List<UserService> collect = userServices.stream().sorted().collect(Collectors.toList());

        Optional<UserService> first = collect.stream().findFirst();
        if (first.isPresent()) {
            first.get().createUserExt();
        } else {
            return "not found";
        }
        // 上下文删除租户 id
        context.remove();
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
