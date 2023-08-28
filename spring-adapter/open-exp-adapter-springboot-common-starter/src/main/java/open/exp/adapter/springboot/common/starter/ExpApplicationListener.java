package open.exp.adapter.springboot.common.starter;

import cn.think.in.java.open.exp.classloader.support.UniqueNameUtil;
import cn.think.in.java.open.exp.client.Constant;
import cn.think.in.java.open.exp.client.PluginManager;
import cn.think.in.java.open.exp.client.SpiFactory;
import cn.think.in.java.open.exp.client.StringUtil;
import cn.think.in.java.open.exp.core.impl.Bootstrap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @Author cxs
 **/
@Slf4j
public class ExpApplicationListener implements ApplicationListener<ApplicationReadyEvent> {

    private SpringBootObjectStore objectStore;
    private ConfigurableApplicationContext context;
    private static final BeanPostProcessor BEAN_POST_PROCESSOR = new BeanPostProcessor() {
        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            String pluginId = UniqueNameUtil.getPluginId(beanName);
            if (StringUtil.isEmpty(pluginId)) {
                return bean;
            }
            log.debug("====>>>> beanName = {}", bean);
            return MDCExpMethodInterceptor.getEnhancer(bean, pluginId);
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            return bean;
        }
    };

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            context = event.getApplicationContext();
            ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
            beanFactory.addBeanPostProcessor(BEAN_POST_PROCESSOR);
            BeanDefinitionRegistry registry = (BeanDefinitionRegistry) context;
            objectStore = new SpringBootObjectStore(registry, beanFactory);
            String pluginPath = event.getApplicationContext().getEnvironment().getProperty(Constant.PLUGINS_PATH_KEY, "exp-plugins");
            String workDir = event.getApplicationContext().getEnvironment().getProperty(Constant.PLUGINS_WORK_DIE_PATH_KEY, "exp-workDir");
            String extPluginAutoDelete = event.getApplicationContext().getEnvironment().getProperty(Constant.PLUGINS_AUTO_DELETE_KEY, "true");

            Bootstrap.bootstrap(objectStore, pluginPath, workDir, extPluginAutoDelete);
            SpiFactory.get(PluginManager.class, new PluginManager.PluginManagerMock() {
            }).start();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
