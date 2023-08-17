package cn.think.in.java.open.exp.document.api;


import cn.think.in.java.open.exp.client.SpiFactory;
import cn.think.in.java.open.exp.document.api.model.ExtDocInterface;

import java.util.List;

/**
 *
 **/
public interface ExpDocContext {

    static ExpDocContext getSpi() {
        return SpiFactory.get(ExpDocContext.class);
    }

    List<ExtDocInterface> getExtDocList(String path);
}
