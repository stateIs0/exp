package cn.think.in.java.open.exp.adapter.springboot2;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

/**
 * @Author cxs
 * @Description
 * @date 2023/8/9
 * @version 1.0
 **/
public class ExpBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {


    private final ExpApplicationListener exPApplicationListener;

    public ExpBeanDefinitionRegistryPostProcessor(ExpApplicationListener expApplicationListener) {
        this.exPApplicationListener = expApplicationListener;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        exPApplicationListener.setObjectStore(new ObjectStoreForSpringBoot(registry));
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        exPApplicationListener.getObjectStore().setBeanFactory(beanFactory);
    }
}
