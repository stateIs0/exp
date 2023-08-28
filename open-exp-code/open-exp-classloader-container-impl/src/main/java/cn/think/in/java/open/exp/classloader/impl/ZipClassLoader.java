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
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipClassLoader extends BaseClassLoader implements PluginClassLoader {

    public ZipClassLoader(String zipFilePath, String extractDir, ClassLoader parent, boolean isParentMode) throws Exception {
        super(extractDir, parent, isParentMode);
        extractZip(zipFilePath, extractDir);
    }

    private void extractZip(String zipFilePath, String extractDir) throws Exception {
        try (ZipFile zip = new ZipFile(zipFilePath)) {
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                Path outputPath = Paths.get(extractDir, entry.getName());

                if (entry.isDirectory()) {
                    Files.createDirectories(outputPath);
                } else {
                    Files.createDirectories(outputPath.getParent());
                    try (InputStream in = new BufferedInputStream(zip.getInputStream(entry));
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
