package cn.think.in.java.open.exp.adapter.springboot1;


import lombok.extern.slf4j.Slf4j;
import open.exp.rest.support.springboot.common.AloneRestUrlScan;
import open.exp.rest.support.springboot.common.RestUrlScanFactory;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;
import java.util.function.Supplier;

@Slf4j
public class RestUrlScanFactoryImpl implements RestUrlScanFactory {
    @Override
    public AloneRestUrlScan create(Object obj, Object requestMappingHandlerMapping,
                                   Object handlerAdapter,
                                   String pluginId, Supplier<String> replaceEnabled) {
        return new AloneRestUrlScanImpl(requestMappingHandlerMapping, handlerAdapter, obj, replaceEnabled, pluginId);
    }

    static class AloneRestUrlScanImpl implements AloneRestUrlScan {

        private final RequestMappingHandlerMapping handlerMapping;
        private final RequestMappingHandlerAdapter handlerAdapter;
        private final Object obj;
        private List<ScanProcessor.RequestMappingInfoWrapper> requestMappingInfoWrappers;
        private final Supplier<String> replaceEnabled;
        private final String pluginId;


        public AloneRestUrlScanImpl(Object handlerMapping,
                                    Object handlerAdapter,
                                    Object obj, Supplier<String> replaceEnabled,
                                    String pluginId) {
            this.handlerMapping = (RequestMappingHandlerMapping) handlerMapping;
            this.handlerAdapter = (RequestMappingHandlerAdapter) handlerAdapter;
            this.obj = obj;
            this.replaceEnabled = replaceEnabled;
            this.pluginId = pluginId;
        }

        @Override
        public void register() {
            requestMappingInfoWrappers = ScanProcessor.builder().build().scan(obj.getClass());
            for (ScanProcessor.RequestMappingInfoWrapper mapping : requestMappingInfoWrappers) {
                if (Boolean.parseBoolean(replaceEnabled.get())) {
                    // 覆盖, 删除老的 URL;
                    log.info("覆盖, 删除老的 URL {} {}", pluginId, (mapping.path));
                    handlerMapping.unregisterMapping(mapping.requestMappingInfo);
                    handlerAdapter.afterPropertiesSet();
                }
                // 将新的Controller方法添加到handlerMapping
                handlerMapping.registerMapping(mapping.requestMappingInfo, obj, mapping.method);
                log.info("注册 url, mapping = {}", (mapping.path));
            }
            // 刷新handlerAdapter以应用新的映射
            handlerAdapter.afterPropertiesSet();
        }

        @Override
        public void unRegister() {
            for (ScanProcessor.RequestMappingInfoWrapper mapping : requestMappingInfoWrappers) {
                handlerMapping.unregisterMapping(mapping.requestMappingInfo);
                log.warn("反注册 url, mapping = {}", (mapping.path));
            }
            handlerAdapter.afterPropertiesSet();
        }
    }
}
