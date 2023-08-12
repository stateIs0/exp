package cn.think.in.java.open.exp.classloader.support;

import cn.think.in.java.open.exp.classloader.impl.JarClassLoader;
import cn.think.in.java.open.exp.classloader.impl.ZipClassLoader;

import java.io.File;

/**
 * @Author cxs
 **/
public class ClassLoaderFinder {

    public static ClassLoader find(File file, String dir) {
        ClassLoader classLoader = null;
        try {

            if (file.getName().endsWith(".jar")) {
                classLoader =
                        new JarClassLoader(file.getPath(), dir);
            } else if (file.getName().endsWith(".zip")) {
                classLoader =
                        new ZipClassLoader(file.getPath(), dir);
            }

            if (classLoader == null) {
                throw new RuntimeException("not support that file...  fileName = " + file.getName());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return classLoader;
    }
}
