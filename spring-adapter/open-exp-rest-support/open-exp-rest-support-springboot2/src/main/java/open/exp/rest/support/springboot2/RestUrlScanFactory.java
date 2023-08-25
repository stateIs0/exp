package open.exp.rest.support.springboot2;

import cn.think.in.java.open.exp.client.SpiFactory;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.function.Supplier;

public interface RestUrlScanFactory {

    static RestUrlScanFactory getInstance() {
        return SpiFactory.get(RestUrlScanFactory.class);
    }

    AloneRestUrlScan create(Object obj,
                            RequestMappingHandlerMapping handlerMapping,
                            RequestMappingHandlerAdapter handlerAdapter,
                            String pluginId,
                            Supplier<String> replaceEnabled);
}
