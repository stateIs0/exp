package cn.think.in.java.open.exp.document.api.model;

import lombok.*;

import java.util.List;

/**
 *
 **/
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@ToString
public class ExtDocParam extends ExtDocResult {

    int index;

    public ExtDocParam(String type, String desc, List<ExtDocProperty> propertyList, int index) {
        super(type, desc, propertyList);
        this.index = index;
    }
}
