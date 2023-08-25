package cn.think.in.java.open.exp.json;

import java.util.Collection;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/25
 **/
public interface ExpJson {

    String toJson(Object o);


    <T> T toObj(String json, Class<T> tClass);

    <T, R> R toObj(String json, Class<? extends Collection> c, Class<T> tClass);
}
