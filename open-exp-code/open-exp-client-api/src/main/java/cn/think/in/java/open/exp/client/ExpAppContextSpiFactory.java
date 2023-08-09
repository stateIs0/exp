package cn.think.in.java.open.exp.client;

import java.util.ServiceLoader;

/**
 * @Author cxs
 **/
public class ExpAppContextSpiFactory {

    private static final Object LOCK = new Object();

    private static ExpAppContext expAppContext;

    public static ExpAppContext getFirst() {
        if (expAppContext != null) {
            return expAppContext;
        }
        synchronized (LOCK) {
            if (expAppContext != null) {
                return expAppContext;
            }

            ServiceLoader<ExpAppContext> load = ServiceLoader.load(ExpAppContext.class);
            for (ExpAppContext kernel : load) {
                ExpAppContextSpiFactory.expAppContext = kernel;
                return kernel;
            }
            throw new RuntimeException("SPI 缺失.");
        }
    }
}
