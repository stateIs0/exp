package cn.think.in.java.open.exp.classloader.impl;

import org.junit.Test;
import cn.think.in.java.open.exp.classloader.ExpClass;
import cn.think.in.java.open.exp.classloader.PluginMetaConfig;
import cn.think.in.java.open.exp.classloader.PluginMetaFat;

import java.io.File;
import java.util.List;

public class ExpPluginMetaServiceImplTest {

    @Test
    public void install() throws Throwable {
        String pathToJar
                = "/Users/cxs/github/open-exp/open-exp-example/example-plugin1/target/example-plugin1-1.0-SNAPSHOT.jar";
        ExpPluginMetaServiceImpl pluginMetaService = new ExpPluginMetaServiceImpl();
        pluginMetaService.setConfig(new PluginMetaConfig("junit-test"));
        PluginMetaFat fat = pluginMetaService.install(new File(pathToJar));
        System.out.println(fat);


        List<ExpClass<Object>> expClasses = pluginMetaService.get("org.think.in.java.open.exp.adapter.springboot2.example.UserService");

        for (ExpClass<Object> expClass : expClasses) {
            System.out.println(expClass.getPluginId());
            System.out.println(expClass.getAClass());
            System.out.println(expClass.getAClass().getClassLoader());
        }

        pluginMetaService.unInstall(fat.getPluginId());
    }
}