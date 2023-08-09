package cn.think.in.java.open.exp.adapter.springboot2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

/**
 * @Author cxs
 * @Description
 * @date 2023/8/8
 * @version 1.0
 **/
@Slf4j
public class Listener implements SpringApplicationRunListener, BeanDefinitionRegistryPostProcessor {
    public Listener(SpringApplication springApplication, String... args) throws IOException {
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        if (!isEnabled(context)) {
            return;
        }
        ExpApplicationListener listener = new ExpApplicationListener();
        context.addApplicationListener(listener);
        context.addBeanFactoryPostProcessor(new ExpBeanDefinitionRegistryPostProcessor(listener));
    }

    private boolean isEnabled(ConfigurableApplicationContext context) {
        return !(context instanceof AnnotationConfigApplicationContext);
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }
}
