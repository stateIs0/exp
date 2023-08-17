package cn.think.in.java.open.exp.object.field.ext;

import lombok.Data;

import java.util.LinkedList;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/13
 **/
@Data
public class ExtFieldModelDesc {

    String fieldType;

    String modifiers;

    String fieldName;

    boolean hasGetter = true;

    boolean hasSetter = true;

    LinkedList<ExtFieldAnnotationModelDesc> annotationsDesc;
}
