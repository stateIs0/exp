package cn.think.in.java.open.exp.example.plugin1;

import cn.think.in.java.open.exp.plugin.depend.AbstractBoot;

/**
 * @Author cxs
 * @Description
 * @date 2023/8/9
 * @version 1.0
 **/
public class Boot extends AbstractBoot {

    @Override
    protected String getScanPath() {
        return Boot.class.getPackage().getName();
    }
}
