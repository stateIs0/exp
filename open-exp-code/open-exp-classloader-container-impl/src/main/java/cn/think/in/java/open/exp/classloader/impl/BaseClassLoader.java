package cn.think.in.java.open.exp.classloader.impl;

import cn.think.in.java.open.exp.client.PluginClassLoader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.ProtectionDomain;
import java.security.cert.Certificate;

/**
 * @Author cxs
 * @Description
 * @date 2023/8/9
 * @version 1.0
 **/
public class BaseClassLoader extends ClassLoader implements PluginClassLoader {

    private final Path extractPath;
    boolean definePackage = false;

    public BaseClassLoader(String extractDir, ClassLoader parent) throws Exception {
        super(parent);
        this.extractPath = Paths.get(extractDir);
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
                definePackage(name.substring(0, name.lastIndexOf("."))
                        , null, null, null, null, null, null, null);
                definePackage = true;
            }
            return defineClass(name, classData, 0, classData.length, protectionDomain);
        } catch (Exception e) {
            throw new ClassNotFoundException(name, e);
        }
    }


    @Override
    public String getPath() {
        return extractPath.toAbsolutePath().toString();
    }
}
