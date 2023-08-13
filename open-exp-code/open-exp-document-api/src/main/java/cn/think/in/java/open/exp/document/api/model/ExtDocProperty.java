package cn.think.in.java.open.exp.document.api.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 **/
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@EqualsAndHashCode
@ToString
public class ExtDocProperty {

    String type;

    String name;

    String desc;

    List<ExtDocProperty> genericTypeList = new ArrayList<>();

    List<ExtDocProperty> propertyList;

    public ExtDocProperty(String type, String name, String desc) {
        this.type = type;
        this.name = name;
        this.desc = desc;
    }
}
