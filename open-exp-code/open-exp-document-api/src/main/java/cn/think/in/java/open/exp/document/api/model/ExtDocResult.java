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
public class ExtDocResult implements Serializable {

    String type;

    String desc;

    List<ExtDocProperty> propertyList;
}
