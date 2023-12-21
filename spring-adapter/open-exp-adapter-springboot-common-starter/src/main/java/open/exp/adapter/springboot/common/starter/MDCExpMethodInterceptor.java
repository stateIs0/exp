package open.exp.adapter.springboot.common.starter;

import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.core.DefaultNamingPolicy;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.MDC;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Stack;

@Slf4j
public class MDCExpMethodInterceptor implements MethodInterceptor {

    public static final String MDC_KEY = "pluginId";
    private static final ThreadLocal<Stack<String>> MDC_STACK = ThreadLocal.withInitial(Stack::new);

    private final String pluginId;
    private final Object target;


    public MDCExpMethodInterceptor(String pluginId, Object target) {
        this.pluginId = pluginId;
        this.target = target;
    }

    @Override
    public Object intercept(Object origin, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        try {
            if ("toString".equals(method.getName())) {
                return target.toString();
            }
            if ("hashcode".equals(method.getName())) {
                return target.hashCode();
            }
            Object r;
            try {
                pushPluginId(pluginId);
                method.setAccessible(true);
                r = method.invoke(target, objects);
            } finally {
                popPluginId();
            }
            return r;
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    private static void pushPluginId(String pluginId) {
        MDC_STACK.get().push(pluginId);
        MDC.put(MDC_KEY, pluginId);
    }

    private static void popPluginId() {
        MDC_STACK.get().pop();
        if (!MDC_STACK.get().isEmpty()) {
            String previousPluginId = MDC_STACK.get().peek();
            MDC.put(MDC_KEY, previousPluginId);
        } else {
            MDC.remove(MDC_KEY);
        }
    }



    public static boolean needEnhancer(Object o) {
        return !(o instanceof ExPIntercept);
    }

    public static Object getEnhancer(Object b, String pluginId) {
        if (!needEnhancer(b)) {
            return b;
        }
        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(new Class[]{ExPIntercept.class});
        enhancer.setSuperclass(b.getClass());
        enhancer.setNamingPolicy(new DefaultNamingPolicy() {
            @Override
            protected String getTag() {
                return super.getTag() + "$$MDC$$" + pluginId;
            }
        });
        enhancer.setCallback(new MDCExpMethodInterceptor(pluginId, b));
        return enhancer.create();
    }
}