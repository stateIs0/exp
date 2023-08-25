package open.exp.rest.support.springboot.common;

import cn.think.in.java.open.exp.client.SpiFactory;

import java.util.function.Supplier;

public interface RestUrlScanFactory {

    static RestUrlScanFactory getInstance() {
        return SpiFactory.get(RestUrlScanFactory.class);
    }

    AloneRestUrlScan create(Object obj,
                            Object requestMappingHandlerMapping,
                            Object requestMappingHandlerAdapter,
                            String pluginId,
                            Supplier<String> replaceEnabled);
}
