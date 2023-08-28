package cn.think.in.java.open.exp.classloader.impl;

import cn.think.in.java.open.exp.client.PluginClassLoader;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author cxs
 */
public class JarClassLoader extends BaseClassLoader implements PluginClassLoader {

    public JarClassLoader(String jarFilePath, String extractDir, ClassLoader parent, boolean isParentMode) throws Exception {
        super(extractDir, parent, isParentMode);
        extractJar(jarFilePath, extractDir);
    }

    private void extractJar(String jarFilePath, String extractDir) throws Exception {
        try (JarFile jar = new JarFile(jarFilePath)) {
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                Path outputPath = Paths.get(extractDir, entry.getName());

                if (entry.isDirectory()) {
                    Files.createDirectories(outputPath);
                } else {
                    Files.createDirectories(outputPath.getParent());
                    try (InputStream in = new BufferedInputStream(jar.getInputStream(entry));
                         OutputStream out = new BufferedOutputStream(Files.newOutputStream(outputPath.toFile().toPath()))) {

                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = in.read(buffer)) != -1) {
                            out.write(buffer, 0, bytesRead);
                        }
                    }
                }
            }
        }
    }

}
