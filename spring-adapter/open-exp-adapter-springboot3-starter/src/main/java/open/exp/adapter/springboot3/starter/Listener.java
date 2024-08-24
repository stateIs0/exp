package open.exp.adapter.springboot3.starter;

import open.exp.adapter.springboot.common.starter.DocHandler;
import open.exp.adapter.springboot.common.starter.ExpApplicationListener;
import open.exp.adapter.springboot.common.starter.ExtFieldJsonConfigHandler;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Listener implements SpringApplicationRunListener {

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        if (!(context instanceof AnnotationConfigApplicationContext)) {
            context.addApplicationListener(new ExpApplicationListener());
            ExtFieldJsonConfigHandler.builder().environment(context.getEnvironment()).build().run();
            DocHandler.builder().environment(context.getEnvironment()).build().init();
        }
    }
}
