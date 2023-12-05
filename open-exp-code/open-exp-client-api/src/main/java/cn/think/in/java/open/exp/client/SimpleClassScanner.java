package cn.think.in.java.open.exp.client;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 *
 **/
@Slf4j
public class SimpleClassScanner {

    public static List<Class<?>> doScan(String scanPath,
                                        Class aClass,
                                        ClassLoader pluginClassLoader,
                                        String classLocation) throws Exception {
        List<Class<?>> classes = new ArrayList<>();
        File file = new File(classLocation);
        if (file.getName().endsWith(".jar")) {
            classes.addAll(doScanJar(file, scanPath, aClass, pluginClassLoader));
        } else {
            classes.addAll(doScanClasses(file, scanPath, aClass, pluginClassLoader));
        }
        return classes;

    }

    public static List<Class<?>> doScanJar(File file, String scanPath, Class extenderService,
                                           ClassLoader classLoader) throws IOException, ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        try (JarFile localJarFile = new JarFile(file)) {
            Enumeration<JarEntry> entries = localJarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                String entryName = jarEntry.getName();
                String classPath = entryName.replaceAll("/", ".");
                if (classPath.startsWith(scanPath) && entryName.endsWith(".class") && !entryName.endsWith("$")) {
                    String className = entryName.substring(0, entryName.length() - 6).replaceAll("/", ".");
                    listAddClass(classes, extenderService, classLoader, className);
                }
            }
        }
        return classes;

    }

    public static List<Class<?>> doScanClasses(File file, String scanPath, Class extenderService,
                                               ClassLoader classLoader) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        File scanFile = new File(file, scanPath.replaceAll("\\.", "/"));
        doScanClasses(classes, scanFile, scanPath, extenderService, classLoader);
        return classes;
    }


    public static void doScanClasses(List<Class<?>> list, File scanFile, String scanPath,
                                     Class<?> extenderService, ClassLoader classLoader) throws ClassNotFoundException {

        File[] files = scanFile.listFiles();

        if (files == null) {
            log.warn("---->> files is null, scanFile = {}", scanFile);
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                doScanClasses(list, file, scanPath + "." + file.getName(), extenderService, classLoader);
            } else if (file.getName().endsWith(".class") && !file.getName().endsWith("$")) {

                String className = file.getName().substring(0, file.getName().length() - 6);
                String fullClassName = scanPath + "." + className;

                listAddClass(list, extenderService, classLoader, fullClassName);
            }
        }
    }

    private static void listAddClass(List<Class<?>> list, Class<?> extenderService, ClassLoader classLoader, String fullClassName) throws ClassNotFoundException {
        Class<?> clazz = classLoader.loadClass(fullClassName);
        boolean flag = false;
        if (!clazz.isInterface()) {
            Class<?>[] impls = clazz.getInterfaces();
            for (Class<?> impl : impls) {
                if (impl.getName().equals(extenderService.getName())) {
                    list.add(clazz);
                    flag = true;
                    break;
                }
            }
        }
        // 不再处理
        if (flag) {
            return;
        }

        for (Annotation declaredAnnotation : clazz.getDeclaredAnnotations()) {
            String name = declaredAnnotation.annotationType().getName();
            if ("org.springframework.web.bind.annotation.RestController".equals(name)) {
                list.add(clazz);
                break;
            }
            if ("org.springframework.stereotype.Service".equals(name)) {
                list.add(clazz);
                break;
            }
            if ("org.springframework.stereotype.Component".equals(name)) {
                list.add(clazz);
                break;
            }
            if ("org.springframework.stereotype.Controller".equals(name)) {
                list.add(clazz);
                break;
            }
        }
    }

}
