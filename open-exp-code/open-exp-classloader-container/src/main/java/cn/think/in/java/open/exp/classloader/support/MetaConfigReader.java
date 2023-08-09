package cn.think.in.java.open.exp.classloader.support;

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

    public static String pluginInfoFileName = "pluginMeta.properties";
    public static String pluginExtFileName = "extension.properties";

    public static String pluginCodeName = "plugin.code";
    public static String pluginDescName = "plugin.desc";
    public static String pluginVersionName = "plugin.version";
    public static String pluginExtName = "plugin.ext";
    public static String pluginConfigName = "plugin.config";
    public static String pluginBootClassName = "plugin.boot.class";


    public static PluginMetaModel getMeta(File file) {
        Properties properties = loadProperties(file.getAbsolutePath(), pluginInfoFileName);
        String code = properties.getProperty(pluginCodeName);
        String desc = properties.getProperty(pluginDescName);
        String version = properties.getProperty(pluginVersionName);
        String ext = properties.getProperty(pluginExtName);
        String config = properties.getProperty(pluginConfigName);
        String boot = properties.getProperty(pluginBootClassName);
        return new PluginMetaModel(code, desc, version, ext, config, boot);
    }

    public static Map<String, String> getMapping(File file) {
        Properties properties = loadProperties(file.getAbsolutePath(), pluginExtFileName);
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
