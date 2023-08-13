package cn.think.in.java.open.exp.object.field.ext;

import javassist.*;

import java.util.List;

/**
 * @Author cxs
 * @Description
 * @date 2023/8/13
 * @version 1.0
 **/
public class JavassistObjectFieldExt implements ObjectFieldExt {

    ClassPool pool = ClassPool.getDefault();

    @Override
    public void addField(List<ExtMetaBean> list) throws Exception {
        for (ExtMetaBean extFieldAnnotationModelDesc : list) {
            addField0(extFieldAnnotationModelDesc);
        }
    }

    void addField0(ExtMetaBean desc) throws Exception {
        CtClass ctClass = pool.get(desc.getClassName());
        for (ExtFieldModelDesc iter : desc.getFieldModelsDesc()) {
            // 字段类型
            String fieldType = iter.getFieldType();
            String fieldName = iter.getFieldName();
            // 创建新字段
            CtField ctField = new CtField(pool.get(fieldType), fieldName, ctClass);
            fieldName = capitalizeFirstLetter(fieldName);
            ctClass.addMethod(CtNewMethod.setter("set" + fieldName, ctField));
            ctClass.addMethod(CtNewMethod.getter("get" + fieldName, ctField));
            ctField.setModifiers(Modifier.PRIVATE);
            ctClass.addField(ctField);
        }

        ctClass.toClass(getClass().getClassLoader());

    }

    public static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

}
