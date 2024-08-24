package open.exp.adapter.springboot3.starter;

import lombok.extern.slf4j.Slf4j;
import open.exp.rest.support.springboot.common.AloneRestUrlScan;
import open.exp.rest.support.springboot.common.RestUrlScanFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/25
 **/
@Slf4j
public class RestUrlScanFactoryImpl implements RestUrlScanFactory {

    @Override
    public AloneRestUrlScan create(Object obj, Object handlerMapping,
                                   Object handlerAdapter,
                                   String pluginId, Supplier<String> replaceEnabled) {
        return new AloneRestUrlScanImpl(
                (RequestMappingHandlerMapping) handlerMapping,
                (RequestMappingHandlerAdapter) handlerAdapter,
                obj, replaceEnabled, pluginId);
    }

    static class AloneRestUrlScanImpl implements AloneRestUrlScan {

        private RequestMappingInfo.BuilderConfiguration config = new RequestMappingInfo.BuilderConfiguration();

        private final RequestMappingHandlerMapping handlerMapping;
        private final RequestMappingHandlerAdapter handlerAdapter;
        private final Object obj;
        private List<RequestMappingInfoWrapper> scan;
        private String pluginId;


        public AloneRestUrlScanImpl(RequestMappingHandlerMapping handlerMapping,
                                    RequestMappingHandlerAdapter handlerAdapter,
                                    Object obj, Supplier<String> replaceEnabled,
                                    String pluginId) {
            this.handlerMapping = handlerMapping;
            this.handlerAdapter = handlerAdapter;
            this.obj = obj;
            this.pluginId = pluginId;
            this.config.setTrailingSlashMatch(handlerMapping.useTrailingSlashMatch());
            this.config.setContentNegotiationManager(handlerMapping.getContentNegotiationManager());

            if (handlerMapping.getPatternParser() != null) {
                this.config.setPatternParser(handlerMapping.getPatternParser());
                Assert.isTrue(!handlerMapping.useSuffixPatternMatch() && !handlerMapping.useRegisteredSuffixPatternMatch(),
                        "Suffix pattern matching not supported with PathPatternParser.");
            } else {
                this.config.setSuffixPatternMatch(handlerMapping.useSuffixPatternMatch());
                this.config.setRegisteredSuffixPatternMatch(handlerMapping.useRegisteredSuffixPatternMatch());
                this.config.setPathMatcher(handlerMapping.getPathMatcher());
            }
        }

        @Override
        public void register() {
            scan = scan(obj.getClass());
            for (RequestMappingInfoWrapper mapping : scan) {
                // 覆盖, 删除老的 URL;
                log.info("覆盖, 删除老的 URL {} {}", pluginId, (mapping.path));
                handlerMapping.unregisterMapping(mapping.requestMappingInfo);
                handlerAdapter.afterPropertiesSet();
                // 将新的Controller方法添加到handlerMapping
                handlerMapping.registerMapping(mapping.requestMappingInfo, obj, mapping.method);
                log.info("注册 url, mapping = {}", (mapping.path));
            }
            // 刷新handlerAdapter以应用新的映射
            handlerAdapter.afterPropertiesSet();
        }

        @Override
        public void unRegister() {
            for (RequestMappingInfoWrapper mapping : scan) {
                handlerMapping.unregisterMapping(mapping.requestMappingInfo);
                log.warn("反注册 url, mapping = {}", (mapping.path));
            }
            handlerAdapter.afterPropertiesSet();
        }

        public List<RequestMappingInfoWrapper> scan(Class<?> aClass) {
            List<RequestMappingInfoWrapper> result = new ArrayList<>();
            Annotation[] annotations = aClass.getDeclaredAnnotations();
            if (aClass.getName().contains("$$EnhancerBySpringCGLIB$$")) {
                // spring cglib
                annotations = aClass.getSuperclass().getDeclaredAnnotations();
                aClass = aClass.getSuperclass();
            }
            for (Annotation annotation : annotations) {
                String[] parentPath = new String[]{""};
                if (annotation.annotationType().equals(RestController.class) || annotation.annotationType().equals(Controller.class)) {
                    //
                    if (aClass.isAnnotationPresent(RequestMapping.class)) {
                        RequestMapping mapping = aClass.getAnnotation(RequestMapping.class);
                        parentPath = mapping.value();
                    }

                    Method[] declaredMethods = aClass.getDeclaredMethods();
                    for (Method declaredMethod : declaredMethods) {
                        boolean isPublic = Modifier.isPublic(declaredMethod.getModifiers());
                        if (isPublic) {
                            Annotation[] an = declaredMethod.getDeclaredAnnotations();
                            for (Annotation ann : an) {
                                if (ann.annotationType().equals(RequestMapping.class)) {
                                    RequestMapping mapping = declaredMethod.getAnnotation(RequestMapping.class);
                                    RequestMethod[] method = mapping.method();
                                    String path = RequestMappingInfoWrapper.buildPath(parentPath, mapping.value());
                                    result.add(new RequestMappingInfoWrapper(RequestMappingInfo
                                            .paths(path) // 设置URL路径
                                            .methods(method) // 设置HTTP方法
                                            .params(mapping.params())
                                            .options(this.config)
                                            .headers(mapping.headers())
                                            .build(), declaredMethod, path));
                                }
                                if (ann.annotationType().equals(GetMapping.class)) {
                                    GetMapping mapping = declaredMethod.getAnnotation(GetMapping.class);
                                    String path = RequestMappingInfoWrapper.buildPath(parentPath, mapping.value());
                                    result.add(new RequestMappingInfoWrapper(RequestMappingInfo
                                            .paths(path) // 设置URL路径
                                            .methods(RequestMethod.GET) // 设置HTTP方法
                                            .params(mapping.params())
                                            .options(this.config)
                                            .headers(mapping.headers())
                                            .build(), declaredMethod, path));
                                }

                                if (ann.annotationType().equals(PostMapping.class)) {
                                    PostMapping mapping = declaredMethod.getAnnotation(PostMapping.class);
                                    String path = RequestMappingInfoWrapper.buildPath(parentPath, mapping.value());
                                    result.add(new RequestMappingInfoWrapper(RequestMappingInfo
                                            .paths(path) // 设置URL路径
                                            .methods(RequestMethod.POST) // 设置HTTP方法
                                            .params(mapping.params())
                                            .options(this.config)
                                            .headers(mapping.headers())
                                            .build(), declaredMethod, path));
                                }

                                if (ann.annotationType().equals(PutMapping.class)) {
                                    PutMapping mapping = declaredMethod.getAnnotation(PutMapping.class);
                                    String path = RequestMappingInfoWrapper.buildPath(parentPath, mapping.value());
                                    result.add(new RequestMappingInfoWrapper(RequestMappingInfo
                                            .paths(path) // 设置URL路径
                                            .methods(RequestMethod.PUT) // 设置HTTP方法
                                            .params(mapping.params())
                                            .options(this.config)
                                            .headers(mapping.headers())
                                            .build(), declaredMethod, path));
                                }

                                if (ann.annotationType().equals(DeleteMapping.class)) {
                                    DeleteMapping mapping = declaredMethod.getAnnotation(DeleteMapping.class);
                                    String path = RequestMappingInfoWrapper.buildPath(parentPath, mapping.value());
                                    result.add(new RequestMappingInfoWrapper(RequestMappingInfo
                                            .paths(path) // 设置URL路径
                                            .options(this.config)
                                            .methods(RequestMethod.DELETE) // 设置HTTP方法
                                            .params(mapping.params())
                                            .headers(mapping.headers())
                                            .build(), declaredMethod, path));
                                }
                            }
                        }
                    }
                }
            }
            return result;
        }
    }

    static class RequestMappingInfoWrapper {
        public RequestMappingInfo requestMappingInfo;
        public Method method;
        public String path;

        public RequestMappingInfoWrapper(RequestMappingInfo requestMappingInfo, Method method, String path) {
            this.requestMappingInfo = requestMappingInfo;
            this.method = method;
            this.path = path;
        }

        public static String buildPath(String[] parent, String[] subPath) {
            if (subPath[0].startsWith("/")) {
                return parent[0] + subPath[0];
            }
            return parent[0] + "/" + subPath[0];
        }
    }
}
