package cn.think.in.java.open.exp.document.api;

import java.lang.annotation.*;

/**
 * 用于描述 扩展点接口的方法
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ExtApiMethod {

    String desc();

    String createTime();
}
