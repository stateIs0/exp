package cn.think.in.java.open.exp.document.api;

import java.lang.annotation.*;

/**
 * 用于描述 扩展点接口.
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ExtApiInterface {

    String desc();

    String createTime();

}
