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
public class ExtDocInterface implements Serializable {

    String interfaceName;

    String desc;

    String createTime;

    List<ExtDocMethod> methods;

    public ExtDocInterface(String interfaceName, String desc, String createTime) {
        this.interfaceName = interfaceName;
        this.desc = desc;
        this.createTime = createTime;
    }
}
