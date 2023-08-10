package cn.think.in.java.open.exp.adapter.springboot2;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @Author cxs
 * @Description
 * @date 2023/8/10
 * @version 1.0
 **/
public class TenantBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.startsWith("cn.think")) {
            return TenantExpAppContextProxyFactory.getProxy(
                            new TenantExpAppContextProxyFactory.ExpMethodInterceptor(bean), bean.getClass());
        }

        return bean;
    }
}
