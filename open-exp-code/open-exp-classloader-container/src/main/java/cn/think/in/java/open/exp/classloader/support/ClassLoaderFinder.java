package cn.think.in.java.open.exp.classloader.support;

import cn.think.in.java.open.exp.classloader.impl.JarExtractorClassLoader;
import cn.think.in.java.open.exp.classloader.impl.ZipExtractorClassLoader;

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
                        new JarExtractorClassLoader(file.getPath(), dir);
            } else if (file.getName().endsWith(".zip")) {
                classLoader =
                        new ZipExtractorClassLoader(file.getPath(), dir);
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
