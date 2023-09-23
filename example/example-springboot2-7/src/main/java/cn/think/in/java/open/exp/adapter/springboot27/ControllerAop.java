package cn.think.in.java.open.exp.adapter.springboot27;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author cxs
 * @Description
 * @date 2023/9/23
 * @version 1.0
 **/
@Aspect
@Component
public class ControllerAop {

    @Pointcut("@annotation(controller)")
    public void loggableMethods(RestController controller) {}

    @Before("loggableMethods(controller)")
    public void logBeforeControllerMethodCall(JoinPoint joinPoint, RestController controller) {
        String methodName = joinPoint.getSignature().toShortString();
        System.out.println("调用Controller方法：" + methodName);
    }
}
