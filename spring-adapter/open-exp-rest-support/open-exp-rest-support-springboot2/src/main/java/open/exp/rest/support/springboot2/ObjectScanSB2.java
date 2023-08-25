package open.exp.rest.support.springboot2;

import cn.think.in.java.open.exp.classloader.support.UniqueNameUtil;
import cn.think.in.java.open.exp.client.ObjectStore;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/25
 **/
public class ObjectScanSB2 implements open.exp.adapter.springboot.common.starter.spi.ObjectScan{

    private Map<String, List<Class<?>>> cache = new HashMap<>();

    private Map<String, AloneRestUrlScan> restUrlScanCache = new HashMap<>();

    private BeanFactory beanFactory;
    private RequestMappingHandlerMapping mapping;
    private RequestMappingHandlerAdapter adapter;

    private boolean init;

    @Override
    public void init(ObjectStore objectStore) {
        if (init) {
            return;
        }
        beanFactory = (BeanFactory) objectStore.getOrigin();
        mapping = (RequestMappingHandlerMapping) beanFactory.getBean("requestMappingHandlerMapping");
        adapter = (RequestMappingHandlerAdapter) beanFactory.getBean("requestMappingHandlerAdapter");
        init = true;
    }

    @Override
    public void registerApis(List<Class<?>> classes, String pluginId) {
        for (Class<?> aClass : classes) {
            try {
                String name = UniqueNameUtil.getName(aClass, pluginId);
                beanFactory.getBean(name);
            } catch (Exception e) {
                // ignore
            }
        }
        for (Class<?> aClass : classes) {
            String name = UniqueNameUtil.getName(aClass, pluginId);
            Object bean = beanFactory.getBean(name);
            AloneRestUrlScan restUrlScan = RestUrlScanFactory.getInstance().create(bean, mapping, adapter, pluginId, () -> "true");
            restUrlScan.register();
            restUrlScanCache.put(name, restUrlScan);
        }

        cache.put(pluginId, classes);
    }

    @Override
    public void unregisterApis(String pluginId) {

        for (Class<?> aClass : cache.get(pluginId)) {
            String name = UniqueNameUtil.getName(aClass, pluginId);
            AloneRestUrlScan r = restUrlScanCache.get(name);
            if (r != null) {
                // handler controller
                r.unRegister();
                restUrlScanCache.remove(name);
            }
        }

        cache.remove(pluginId);
    }
}
