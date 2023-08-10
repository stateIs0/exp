package cn.think.in.java.open.exp.client;

/**
 * @Author cxs
 * @Description
 * @date 2023/8/10
 * @version 1.0
 **/
public interface Sort extends Comparable<Sort> {

    int getSort();

    @Override
    default int compareTo(Sort o) {
        return o.getSort() - getSort();
    }
}
