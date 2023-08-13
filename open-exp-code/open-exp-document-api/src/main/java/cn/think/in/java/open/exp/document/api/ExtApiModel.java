package cn.think.in.java.open.exp.document.api;

import java.lang.annotation.*;

/**
 * 实体类的描述
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ExtApiModel {

    String desc();
}
