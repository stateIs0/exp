package cn.think.in.java.open.exp.classloader.support;

import cn.think.in.java.open.exp.client.Constant;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


/**
 * @Author cxs
 * @Description
 * @date 2023/8/9
 * @version 1.0
 **/
public class MetaConfigReader {

    public static PluginMetaInnerModel getMeta(File file) {
        Properties properties = loadProperties(file.getAbsolutePath(), Constant.PLUGIN_META_FILE_NAME);
        String code = properties.getProperty(Constant.PLUGIN_CODE_KEY);
        String desc = properties.getProperty(Constant.PLUGIN_DESC_KEY);
        String version = properties.getProperty(Constant.PLUGIN_VERSION_KEY);
        String ext = properties.getProperty(Constant.PLUGIN_EXT_KEY);
        String config = properties.getProperty(Constant.PLUGIN_CONFIG_KEY);
        String boot = properties.getProperty(Constant.PLUGIN_BOOT_CLASS);
        return new PluginMetaInnerModel(code, desc, version, ext, config, boot);
    }

    public static Map<String, String> getMapping(File file) {
        Properties properties = loadProperties(file.getAbsolutePath(), Constant.EXTENSION_FILE_NAME);
        Map<String, String> map = new HashMap<>();

        for (Map.Entry<Object, Object> i : properties.entrySet()) {
            map.put(i.getKey().toString(), i.getValue().toString());
        }

        return map;
    }


    private static Properties loadProperties(String file, String fileName) {
        Properties properties = new Properties();

        if (file.endsWith(".jar")) {
            try (JarFile jarFile = new JarFile(file)) {
                JarEntry entry = jarFile.getJarEntry(fileName);
                if (entry != null) {
                    try (InputStream inputStream = jarFile.getInputStream(entry)) {
                        properties.load(inputStream);
                    }
                } else {
                    throw new RuntimeException(file + " not found " + fileName);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            return properties;
        }

        if (file.endsWith(".zip")) {
            try (ZipFile zipFile = new ZipFile(fileName)) {
                ZipArchiveEntry entry = zipFile.getEntry(fileName);
                if (entry != null) {
                    try (InputStream inputStream = zipFile.getInputStream(entry)) {
                        properties.load(inputStream);
                    }
                } else {
                    throw new IOException("Properties file not found: " + fileName);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return properties;
        }

        return properties;
    }
}
