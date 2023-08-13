package cn.think.in.java.open.exp.document.api.model;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 **/
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@EqualsAndHashCode
@ToString
public class ExtDocMethod implements Serializable {

    String methodName;

    String desc;

    String createTime;

    List<ExtDocParam> params;

    ExtDocResult result;

    public ExtDocMethod(String methodName, String desc, String createTime) {
        this.methodName = methodName;
        this.desc = desc;
        this.createTime = createTime;
    }
}
