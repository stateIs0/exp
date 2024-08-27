package cn.think.in.java;

import cn.think.in.java.model.SyncStateRequest;
import cn.think.in.java.open.exp.client.ExpAppContext;
import cn.think.in.java.open.exp.client.ExpAppContextSpiFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cxs
 * @Description TODO
 * @Date 2024/8/27
 * @Version 1.0.0
 */
@RestController
@RequestMapping("/plugin/manager")
public class ClientEntryController {

    ExpAppContext expAppContext = ExpAppContextSpiFactory.getFirst();

    @PostMapping("/syncState")
    public String syncState(@RequestBody SyncStateRequest syncStateRequest) {
        return "syncState";
    }
}
