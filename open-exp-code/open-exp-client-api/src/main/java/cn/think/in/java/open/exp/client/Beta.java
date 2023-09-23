package cn.think.in.java.open.exp.client;

import java.lang.annotation.*;

/**
 * @Author cxs
 * @Description
 * @date 2023/9/23
 * @version 1.0
 **/
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Beta {
}
