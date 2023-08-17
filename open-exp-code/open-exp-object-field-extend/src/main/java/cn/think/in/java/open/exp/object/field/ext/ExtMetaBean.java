package cn.think.in.java.open.exp.object.field.ext;

import lombok.Data;

import java.util.List;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/13
 **/
@Data
public class ExtMetaBean {

    String className;

    List<ExtFieldModelDesc> fieldModelsDesc;

}
