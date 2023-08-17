package cn.think.in.java.open.exp.document.impl;


import cn.think.in.java.open.exp.document.api.ExpDocContext;
import cn.think.in.java.open.exp.document.api.model.ExtDocInterface;

import java.util.List;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/6
 **/
public class DefaultEEPExtDocContext implements ExpDocContext {

    private volatile List<ExtDocInterface> list;

    @Override
    public synchronized List<ExtDocInterface> getExtDocList(String path) {
        if (list == null) {
            list = Boot.boot(path);
        }
        return list;
    }

}
