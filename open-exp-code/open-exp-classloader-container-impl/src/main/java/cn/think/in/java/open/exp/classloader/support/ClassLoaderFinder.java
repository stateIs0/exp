package cn.think.in.java.open.exp.classloader.support;

import cn.think.in.java.open.exp.classloader.impl.BaseClassLoader;
import cn.think.in.java.open.exp.classloader.impl.JarClassLoader;
import cn.think.in.java.open.exp.classloader.impl.ZipClassLoader;
import cn.think.in.java.open.exp.client.Constant;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * @Author cxs
 **/
@Slf4j
public class ClassLoaderFinder {

    public static BaseClassLoader find(File file, String dir, String modeString) {
        BaseClassLoader classLoader = null;
        try {
            // springboot launcher classloader
            ClassLoader parent = Thread.currentThread().getContextClassLoader();

            boolean isParentMode = !Constant.PLUGIN_CLASS_LOADER_MODE_SELF.equals(modeString);
            log.info("dir {} isParentMode = {}", dir, modeString);

            if (file.getName().endsWith(".jar")) {
                classLoader = new JarClassLoader(file.getPath(), dir, parent, isParentMode);
            } else if (file.getName().endsWith(".zip")) {
                classLoader = new ZipClassLoader(file.getPath(), dir, parent, isParentMode);
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
