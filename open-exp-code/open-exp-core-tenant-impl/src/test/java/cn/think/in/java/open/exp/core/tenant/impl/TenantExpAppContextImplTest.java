package cn.think.in.java.open.exp.core.tenant.impl;

import cn.think.in.java.open.exp.adapter.springboot2.example.UserService;
import cn.think.in.java.open.exp.client.Plugin;

import java.io.File;
import java.util.Optional;
import java.util.function.Consumer;

public class TenantExpAppContextImplTest {

    @org.junit.Test
    public void getSortFirst() throws Throwable {

        TenantExpAppContext tenantExpAppContext = new TenantExpAppContextImpl();

        Plugin load = tenantExpAppContext.load(new File("/Users/cxs/github/open-exp/exp-plugins/example-plugin1-1.0-SNAPSHOT.jar"), "123");

        Optional<UserService> sortFirst = tenantExpAppContext.getSortFirst(UserService.class, "123");

        sortFirst.ifPresent(UserService::createUserExt);


    }
}