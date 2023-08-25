package cn.think.in.java.open.exp.classloader.support;

import cn.think.in.java.open.exp.client.ConfigSupport;
import cn.think.in.java.open.exp.client.Constant;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/25
 **/
@Slf4j
public class ConfigSupportClassLoader extends URLClassLoader {


    public ConfigSupportClassLoader(File url) throws MalformedURLException {
        super(new URL[]{url.toURL()}, Thread.currentThread().getContextClassLoader());
    }

    public List<ConfigSupport> get() {

        List<ConfigSupport> list = new ArrayList<>();
        try (InputStream resourceAsStream = getResourceAsStream(Constant.PLUGIN_META_FILE_NAME)) {
            Properties properties = new Properties();
            properties.load(resourceAsStream);
            Object name = properties.get(Constant.PLUGIN_BOOT_CLASS);
            Class<?> aClass = loadClass(name.toString());
            if (aClass != null) {
                for (Field field : aClass.getDeclaredFields()) {
                    if (field.getType().equals(ConfigSupport.class)) {
                        list.add((ConfigSupport) field.get(aClass));
                    }
                }
            } else {
                log.warn(Constant.PLUGIN_BOOT_CLASS + " 是空");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return list;
    }
}
