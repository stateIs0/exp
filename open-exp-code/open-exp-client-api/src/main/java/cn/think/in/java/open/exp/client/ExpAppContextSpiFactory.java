package cn.think.in.java.open.exp.client;

/**
 * @Author cxs
 **/
public class ExpAppContextSpiFactory {

    public static ExpAppContext getFirst() {
        return SpiFactory.get(ExpAppContext.class);
    }
}
