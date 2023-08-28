package cn.think.in.java.open.exp.classloader.impl;

import cn.think.in.java.open.exp.classloader.LogSpi;
import cn.think.in.java.open.exp.client.PluginClassLoader;
import cn.think.in.java.open.exp.client.SpiFactory;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.ProtectionDomain;
import java.security.cert.Certificate;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/9
 **/
public class BaseClassLoader extends URLClassLoader implements PluginClassLoader {

    private final Path extractPath;
    boolean definePackage = false;
    private boolean isParentMode;
    private LogSpi logSpi = SpiFactory.get(LogSpi.class, c -> {

    });

    public BaseClassLoader(String extractDir, ClassLoader parent, boolean isParentMode) throws Exception {
        super(new URL[]{Paths.get(extractDir).toUri().toURL()}, parent);
        this.extractPath = Paths.get(extractDir);
        this.isParentMode = isParentMode;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if (isParentMode) {
            return super.loadClass(name);
        }
        Class<?> loadedClass = findLoadedClass(name);
        if (loadedClass != null) {
            return loadedClass;
        }
        try {
            return findClass(name);
        } catch (Exception e) {
            return getParent().loadClass(name);
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        Path classFilePath = extractPath.resolve(name.replace('.', '/') + ".class");
        if (!Files.exists(classFilePath)) {
            throw new ClassNotFoundException(name);
        }

        try {
            byte[] classData = Files.readAllBytes(classFilePath);
            CodeSource codeSource = new CodeSource(classFilePath.toUri().toURL(), (Certificate[]) null);
            PermissionCollection permissions = new Permissions();
            ProtectionDomain protectionDomain = new ProtectionDomain(codeSource, permissions);

            if (!definePackage) {
                definePackage(name.substring(0, name.lastIndexOf(".")), null, null, null, null, null, null, null);
                definePackage = true;
            }
            Class<?> aClass = defineClass(name, classData, 0, classData.length, protectionDomain);
            logSpi.enhance(aClass);
            return aClass;
        } catch (Exception e) {
            throw new ClassNotFoundException(name, e);
        }
    }


    @Override
    public String getPath() {
        return extractPath.toAbsolutePath().toString();
    }
}
