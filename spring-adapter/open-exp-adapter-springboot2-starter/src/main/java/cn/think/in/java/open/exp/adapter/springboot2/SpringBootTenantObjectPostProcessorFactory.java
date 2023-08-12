package cn.think.in.java.open.exp.adapter.springboot2;

import cn.think.in.java.open.exp.client.TenantObjectProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @Author cxs
 * @Description
 * @date 2023/8/10
 * @version 1.0
 **/
public class SpringBootTenantObjectPostProcessorFactory implements BeanPostProcessor, TenantObjectProxyFactory {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        String[] split = beanName.split("_");

        if (split.length >= 2) {
            String pluginId = split[split.length - 2] + "_" + split[split.length - 1];
            return getProxy(bean, pluginId);
        }

        return bean;
    }

    @Override
    public Object getProxy(Object bean, String pluginId) {
        return TenantExpAppContextProxyFactory.getProxy(
                new TenantExpAppContextProxyFactory.ExpMethodInterceptor(bean, pluginId), bean.getClass(), pluginId);
    }
}
