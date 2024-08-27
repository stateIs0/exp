package cn.think.in.java.open.exp.adapter.springboot27;

import cn.think.in.java.open.exp.adapter.springboot2.example.UserService;
import cn.think.in.java.open.exp.client.ExpAppContext;
import cn.think.in.java.open.exp.client.ExpAppContextSpiFactory;
import cn.think.in.java.open.exp.client.Plugin;
import cn.think.in.java.open.exp.client.PluginFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.*;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/9
 **/
@RestController
@RequestMapping("/base")
@Slf4j
public class BaseController {
    static ThreadLocal<String> context = new InheritableThreadLocal<>();
    ExpAppContext expAppContext = ExpAppContextSpiFactory.getFirst();
    Map<String, Integer> sortMap = new HashMap<>();
    Map<String, String> pluginIdTenantIdMap = new HashMap<>();

    PluginFilter callback;

    public BaseController() {
        sortMap.put("example-plugin1_1.0.0", 1);
        sortMap.put("example-plugin2_2.0.0", 2);
        pluginIdTenantIdMap.put("example-plugin2_2.0.0", "12345");
        pluginIdTenantIdMap.put("example-plugin1_1.0.0", "12345");

        callback = new PluginFilter() {

            @Override
            public <T> List<FModel<T>> filter(List<FModel<T>> list) {
                return list;
            }
        };

    }

    @RequestMapping("/hello")
    public ResModel hello() {
        return new ResModel();
    }


    @RequestMapping("/run")
    public String run(String tenantId) {
        // 上下文设置租户 id
        context.set(tenantId);
        try {
            List<UserService> userServices = expAppContext.list(UserService.class);
            // first 第一个就是这个租户优先级最高的.
            Optional<UserService> optional = userServices.stream().findFirst();
            if (optional.isPresent()) {
                optional.get().createUserExt();
            } else {
                return "not found";
            }
            return "success";
        } finally {
            // 上下文删除租户 id
            context.remove();
        }
    }

    @RequestMapping("/preload")
    public Plugin preload(String path) throws Throwable {
        if (path.startsWith("http")) {
            File tempFile = File.createTempFile("exp-" + UUID.randomUUID(), ".jar");
            HttpFileDownloader.download(path, tempFile.getAbsolutePath());
            path = tempFile.getAbsolutePath();
        }

        return expAppContext.preLoad(new File(path));
    }



    @RequestMapping("/install")
    public String install(String path, String tenantId) throws Throwable {
        if (path.startsWith("http")) {
            File tempFile = File.createTempFile("exp-" + UUID.randomUUID(), ".jar");
            HttpFileDownloader.download(path, tempFile.getAbsolutePath());
            path = tempFile.getAbsolutePath();
        }

        Plugin plugin = expAppContext.load(new File(path));

        sortMap.put(plugin.getPluginId(), Math.abs(new Random().nextInt(100)));
        pluginIdTenantIdMap.put(plugin.getPluginId(), tenantId);

        return plugin.getPluginId();
    }

    @RequestMapping("/unInstall")
    public String unInstall(String pluginId) throws Exception {
        log.info("plugin id {}", pluginId);
        expAppContext.unload(pluginId);
        pluginIdTenantIdMap.remove(pluginId);
        sortMap.remove(pluginId);
        return "ok";
    }
}
