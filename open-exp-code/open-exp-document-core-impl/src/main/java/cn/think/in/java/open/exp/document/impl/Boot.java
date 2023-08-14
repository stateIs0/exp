package cn.think.in.java.open.exp.document.impl;


import cn.think.in.java.open.exp.document.api.model.ExtDocInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author cxs
 * @Description
 * @date 2023/8/6
 * @version 1.0
 **/
public class Boot {

    public static synchronized List<ExtDocInterface> boot(String path) {

        ExtScan scan = new DefaultExtScan();
        List<Class<?>> classList = scan.scan(path);

        DocParser docParser = new DefaultDocParser();

        List<ExtDocInterface> result = new ArrayList<>();

        classList.forEach(clazz -> result.add(docParser.parse(clazz)));

        return result;
    }
}
