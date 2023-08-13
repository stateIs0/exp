package com.timevale.eep.plugin.object.field.ext;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.Loader;
import lombok.Data;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class DemoTest {


    @Test
    public void ttt() throws Exception {
        Student student = new Student();
        student.setStudentName("ltm");
        student.setAge(99);

        // 需要新增的属性
        Map<String, Object> newFieldMap = new HashMap<>();
        newFieldMap.put("className", "1班");

        ClassPool pool = ClassPool.getDefault();
        // 注意: 不能用pool.get(Student.class.getName())，否则报错attempted  duplicate class definition for name，原因是同个Class不能在同个ClassLoader中加载两次
        CtClass ctClass = pool.get(student.getClass().getName());

        // 给class新增属性
        for (String fieldName : newFieldMap.keySet()) {
            // 字段类型
            String fieldType = newFieldMap.get(fieldName).getClass().getName();
            // 创建新字段
            CtField ctField = new CtField(pool.get(fieldType), fieldName, ctClass);
            ctField.setModifiers(Modifier.PRIVATE);
            ctClass.addField(ctField);
        }

        // 同个Class不能在同个ClassLoader中加载两次，所以需要使用javassist提供的ClassLoader
        Loader classLoader = new Loader(pool);
        // 生成新的字节码
        Class<?> newClass = classLoader.loadClass(ctClass.getName());
        // 用新的字节码创建新的对象
        Object newObject = newClass.newInstance();

        // 设置原有属性的值
        for (Field oldField : student.getClass().getDeclaredFields()) {
            // 跳过final属性，因为final属性无法修改
            if (java.lang.reflect.Modifier.isFinal(oldField.getModifiers())) {
                continue;
            }
            oldField.setAccessible(true);
            Field newField = newClass.getDeclaredField(oldField.getName());
            newField.setAccessible(true);
            newField.set(newObject, oldField.get(student));
        }

        // 设置新增属性的值
        for (String fieldName : newFieldMap.keySet()) {
            Field field = newClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(newObject, newFieldMap.get(fieldName));
        }

        for (Field field : newClass.getDeclaredFields()) {
            field.setAccessible(true);
            System.out.println(field.getName() + ": " + field.get(newObject));
        }
    }
}

