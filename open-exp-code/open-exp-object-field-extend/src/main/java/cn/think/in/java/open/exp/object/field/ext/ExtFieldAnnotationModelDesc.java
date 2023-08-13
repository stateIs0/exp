package cn.think.in.java.open.exp.object.field.ext;

import lombok.Data;

import java.util.List;

/**
 * @Author cxs
 * @Description
 * @date 2023/8/13
 * @version 1.0
 **/
@Data
public class ExtFieldAnnotationModelDesc {

    String annotationClassName;

    List<Pair> list;

}

@Data
class Pair {
    String key;
    String value;
}
