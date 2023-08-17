package cn.think.in.java.open.exp.document.impl;

import cn.think.in.java.open.exp.document.api.ExtApiInterface;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/6
 **/
public class DefaultExtScan implements ExtScan {

    @Override
    public List<Class<?>> scan(String path) {
        List<Class<?>> entityClasses = new ArrayList<>();
        try (ScanResult scanResult = new ClassGraph().enableAllInfo().
                acceptPackages(path).scan()) {
            for (ClassInfo classInfo : scanResult.getClassesWithAnnotation(ExtApiInterface.class.getName())) {
                Class<?> classObj = classInfo.loadClass();
                entityClasses.add(classObj);
            }
        }
        return entityClasses;
    }
}
