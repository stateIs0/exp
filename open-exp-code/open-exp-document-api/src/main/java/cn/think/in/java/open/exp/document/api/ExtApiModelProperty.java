package cn.think.in.java.open.exp.document.api;

import java.lang.annotation.*;

/**
 * 实体类的属性
 **/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ExtApiModelProperty {

    String desc();

}
