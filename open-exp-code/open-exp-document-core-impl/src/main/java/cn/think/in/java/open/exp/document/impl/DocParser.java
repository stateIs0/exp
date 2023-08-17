package cn.think.in.java.open.exp.document.impl;

import cn.think.in.java.open.exp.document.api.model.ExtDocInterface;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/6
 **/
public interface DocParser {

    ExtDocInterface parse(Class<?> aClass);
}
