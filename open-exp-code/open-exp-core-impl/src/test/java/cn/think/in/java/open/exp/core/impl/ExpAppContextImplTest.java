package cn.think.in.java.open.exp.core.impl;

import cn.think.in.java.open.exp.classloader.impl.PluginMetaServiceImpl;
import cn.think.in.java.open.exp.client.Plugin;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class ExpAppContextImplTest {

    @Test
    public void preLoad() {
        ExpAppContextImpl expAppContext = new ExpAppContextImpl(new PluginMetaServiceImpl());
        Plugin plugin = expAppContext.preLoad(new File("/Users/cxs/github/open-exp/exp-plugins/example-plugin-a-v2-1.0-SNAPSHOT-1.0-SNAPSHOT.zip"));
        System.out.println(plugin);
    }
}