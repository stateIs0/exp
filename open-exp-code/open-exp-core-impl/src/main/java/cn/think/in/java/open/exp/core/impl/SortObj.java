package cn.think.in.java.open.exp.core.impl;

import cn.think.in.java.open.exp.client.Sort;

/**
 * @version 1.0
 * @Author cxs
 * @date 2023/8/12
 **/
public class SortObj<T> implements Sort {

    T obj;
    int sort;

    public SortObj(T obj, int sort) {
        this.obj = obj;
        this.sort = sort;
    }

    @Override
    public int getSort() {
        return sort;
    }
}
