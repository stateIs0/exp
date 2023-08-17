package cn.think.in.java.open.exp.document.impl;

import cn.think.in.java.open.exp.document.api.ExtApiInterface;
import cn.think.in.java.open.exp.document.api.ExtApiMethod;
import cn.think.in.java.open.exp.document.api.ExtApiModel;
import cn.think.in.java.open.exp.document.api.ExtApiModelProperty;
import cn.think.in.java.open.exp.document.api.model.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/6
 **/
@SuppressWarnings("unchecks")
public class DefaultDocParser implements DocParser {

    public static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();

        for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        }

        return fields;
    }

    public static List<Method> getAllMethods(Class<?> clazz) {
        List<Method> methods = new ArrayList<>();

        for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
            methods.addAll(Arrays.asList(c.getDeclaredMethods()));
        }

        for (Class<?> iface : clazz.getInterfaces()) {
            methods.addAll(getAllMethods(iface));
        }

        return methods;
    }

    @Override
    public ExtDocInterface parse(Class<?> aClass) {

        // 处理接口
        String name = aClass.getName();
        ExtApiInterface annotation = aClass.getAnnotation(ExtApiInterface.class);
        if (annotation == null) {
            throw new RuntimeException("扩展点接口必须使用 ExtApiInterface 注解");
        }
        String time = annotation.createTime();
        String desc = annotation.desc();
        ExtDocInterface result = new ExtDocInterface(name, desc, time);

        List<ExtDocMethod> extDocMethodList = new ArrayList<>();

        // 处理方法
        List<Method> methods = getAllMethods(aClass);
        for (Method method : methods) {
            String methodName = method.getName();
            ExtApiMethod extApiMethod = method.getAnnotation(ExtApiMethod.class);
            if (extApiMethod == null) {
                continue;
            }
            String methodTime = extApiMethod.createTime();
            String methodDesc = extApiMethod.desc();
            ExtDocMethod extDocMethod = new ExtDocMethod(methodName, methodDesc, methodTime);

            // 处理返回值
            Class<?> returnType = method.getReturnType();
            ExtDocResult dc = parseModel(returnType);
            extDocMethod.setResult(dc);


            // 处理参数
            List<ExtDocParam> params = new ArrayList<>();
            for (int i = 0; i < method.getParameters().length; i++) {
                if (method.getParameters()[i].getType().getAnnotation(ExtApiModel.class) == null) {
                    throw new RuntimeException(aClass.getName() + "#" + method.getName() + "#" +
                            method.getParameters()[i].getName() + "参数必须使用 ExtApiModel 注解");
                }
                ExtDocResult res = parseModel(method.getParameters()[i].getType());
                params.add(new ExtDocParam(res.getType(), res.getDesc(), res.getPropertyList(), i));
            }

            extDocMethod.setParams(params);
            extDocMethodList.add(extDocMethod);
        }

        result.setMethods(extDocMethodList);
        return result;
    }

    public ExtDocResult parseModel(Class<?> a) {

        ExtDocResult result = new ExtDocResult();

        ExtApiModel annotation = a.getAnnotation(ExtApiModel.class);
        if (annotation == null) {
            throw new RuntimeException(a.getName() + " 没有添加注解");
        }
        String name = a.getName();
        result.setType(name);
        String desc = annotation.desc();
        result.setDesc(desc);
        result.setPropertyList(new ArrayList<>());

        List<Field> declaredFields = getAllFields(a);
        for (Field field : declaredFields) {
            fieldParse(a, field, result);
        }

        return result;
    }

    private void fieldParse(Class<?> a, Field field, ExtDocResult result) {
        ExtApiModelProperty property = field.getAnnotation(ExtApiModelProperty.class);
        if (property == null) {
            throw new RuntimeException(a.getName() + " 属性 " + field.getName() + " 没有添加注解");
        }
        String typeName = field.getType().getName();
        String desced = property.desc();
        ExtDocProperty edp = new ExtDocProperty(typeName, field.getName(), desced);

        //handleGeneric(a, field, p);

        result.getPropertyList().add(edp);

        ExtApiModel extApiModel = field.getType().getAnnotation(ExtApiModel.class);
        if (extApiModel != null) {
            ExtDocResult parse = parseModel(field.getType());
            edp.setPropertyList(parse.getPropertyList());
        }
    }

    private void handleGeneric(Class<?> a, Field field, ExtDocProperty p) {
        Type genericType = field.getGenericType();


        if (genericType instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) genericType;
            for (Type typeArg : pType.getActualTypeArguments()) {
                ExtDocResult re = null;
                try {
                    Class<?> genericClass = a.getClassLoader().loadClass(typeArg.getTypeName());
                    re = parseModel(genericClass);
                } catch (Exception e) {
                    // INGORE
                }
                if (re != null) {
                    p.getGenericTypeList().add(
                            new ExtDocProperty(re.getType(),
                                    typeArg.getTypeName(),
                                    re.getDesc(),
                                    null, re.getPropertyList()));
                } else {
                    p.getGenericTypeList().add(
                            new ExtDocProperty(typeArg.getTypeName(), typeArg.getTypeName(), "null"));
                }
            }
        }
    }
}
