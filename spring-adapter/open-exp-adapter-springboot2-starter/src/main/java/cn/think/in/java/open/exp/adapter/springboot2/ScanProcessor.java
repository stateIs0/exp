package cn.think.in.java.open.exp.adapter.springboot2;

import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Builder
public class ScanProcessor {

    private final List<Handler> list = new CopyOnWriteArrayList<>();

    {
        list.add(new RequestMappingHandler());
        list.add(new GetMappingHandler());
        list.add(new PostMappingHandler());
        list.add(new DeleteMappingHandler());
        list.add(new PutMappingHandler());
    }

    public List<RequestMappingInfoWrapper> scan(Class<?> aClass) {
        List<RequestMappingInfoWrapper> result = new ArrayList<>();
        Annotation[] annotations = aClass.getDeclaredAnnotations();
        if (aClass.getName().contains("$$Enhancer")) {
            // spring cglib
            annotations = aClass.getSuperclass().getDeclaredAnnotations();
            aClass = aClass.getSuperclass();
        }

        for (Annotation annotation : annotations) {
            processAnnotation(aClass, annotation, result);
        }

        return result;
    }

    private void processAnnotation(Class<?> aClass, Annotation annotation, List<RequestMappingInfoWrapper> result) {
        String[] parentPath = new String[]{""};
        if (annotation.annotationType().equals(RestController.class) || annotation.annotationType().equals(Controller.class)) {
            if (aClass.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping mapping = aClass.getAnnotation(RequestMapping.class);
                parentPath = mapping.value().length == 0 ? mapping.path() : mapping.value();
            }
            Method[] declaredMethods = aClass.getDeclaredMethods();
            for (Method declaredMethod : declaredMethods) {
                doScan(declaredMethod, result, parentPath);
            }
        }
    }

    private void doScan(Method declaredMethod,
                        List<RequestMappingInfoWrapper> resultList,
                        String[] parentPath) {

        for (Handler handler : list) {
            Optional<Result> result = handler.handler(declaredMethod);
            result.ifPresent(r -> {
                RequestMethod[] methods = r.methods;
                String[] params = r.params;
                String[] headers = r.headers;
                String[] path = r.path;
                String[] pathFinal = RequestMappingInfoWrapper.buildPath(parentPath, path);
                resultList.add(new RequestMappingInfoWrapper(RequestMappingInfo
                        .paths(pathFinal)
                        .methods(methods)
                        .params(params)
                        .headers(headers)
                        .build(), declaredMethod, pathFinal));

            });
        }

    }


    @Builder
    @Data
    static class Result {
        RequestMethod[] methods;
        String[] params;
        String[] headers;
        String[] path;
    }

    interface Handler {
        Optional<Result> handler(Method declaredMethod);
    }

    public static class RequestMappingInfoWrapper {
        public RequestMappingInfo requestMappingInfo;
        public Method method;
        public String[] path;

        public RequestMappingInfoWrapper(RequestMappingInfo requestMappingInfo, Method method, String[] path) {
            this.requestMappingInfo = requestMappingInfo;
            this.method = method;
            this.path = path;
        }

        public static String[] buildPath(String[] parent, String[] subPath) {
            List<String> paths = new ArrayList<>();

            if (parent == null || parent.length == 0) {
                return subPath;
            }

            if (subPath == null || subPath.length == 0) {
                return parent;
            }

            for (String parentPath : parent) {
                for (String subPathValue : subPath) {
                    String combinedPath = (parentPath.endsWith("/") ? parentPath : parentPath + "/") +
                            (subPathValue.startsWith("/") ? subPathValue.substring(1) : subPathValue);
                    paths.add(combinedPath);
                }
            }
            return paths.toArray(new String[0]);
        }
    }

    private static Optional<Result> getResult(RequestMethod[] methods, String[] params, String[] headers, String[] value, String[] path2) {
        String[] path = value.length == 0 ? path2 : value;
        return Optional.of(Result.builder()
                .methods(methods)
                .params(params)
                .headers(headers)
                .path(path)
                .build());
    }

    static class RequestMappingHandler implements Handler {

        @Override
        public Optional<Result> handler(Method declaredMethod) {
            RequestMapping mapping = declaredMethod.getAnnotation(RequestMapping.class);
            if (mapping != null) {
                RequestMethod[] methods = mapping.method();
                return getResult(methods, mapping.params(), mapping.headers(), mapping.value(), mapping.path());
            }
            return Optional.empty();
        }
    }

    static class GetMappingHandler implements Handler {

        @Override
        public Optional<Result> handler(Method declaredMethod) {
            GetMapping mapping = declaredMethod.getAnnotation(GetMapping.class);
            if (mapping != null) {
                RequestMethod[] methods = new RequestMethod[]{RequestMethod.GET};
                return getResult(methods, mapping.params(), mapping.headers(), mapping.value(), mapping.path());
            }
            return Optional.empty();
        }
    }

    static class PostMappingHandler implements Handler {

        @Override
        public Optional<Result> handler(Method declaredMethod) {
            PostMapping mapping = declaredMethod.getAnnotation(PostMapping.class);
            if (mapping != null) {
                RequestMethod[] methods = new RequestMethod[]{RequestMethod.POST};
                return getResult(methods, mapping.params(), mapping.headers(), mapping.value(), mapping.path());
            }
            return Optional.empty();
        }
    }

    static class DeleteMappingHandler implements Handler {

        @Override
        public Optional<Result> handler(Method declaredMethod) {
            DeleteMapping mapping = declaredMethod.getAnnotation(DeleteMapping.class);
            if (mapping != null) {
                RequestMethod[] methods = new RequestMethod[]{RequestMethod.DELETE};
                return getResult(methods, mapping.params(), mapping.headers(), mapping.value(), mapping.path());
            }
            return Optional.empty();
        }
    }

    static class PutMappingHandler implements Handler {

        @Override
        public Optional<Result> handler(Method declaredMethod) {
            PutMapping mapping = declaredMethod.getAnnotation(PutMapping.class);
            if (mapping != null) {
                RequestMethod[] methods = new RequestMethod[]{RequestMethod.PUT};
                return getResult(methods, mapping.params(), mapping.headers(), mapping.value(), mapping.path());
            }
            return Optional.empty();
        }
    }
}
