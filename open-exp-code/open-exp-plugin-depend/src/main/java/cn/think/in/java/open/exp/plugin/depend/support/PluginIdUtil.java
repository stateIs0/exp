package cn.think.in.java.open.exp.plugin.depend.support;


import cn.think.in.java.open.exp.client.Constant;
import cn.think.in.java.open.exp.client.ExpBoot;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Properties;

/**
 * @Author cxs
 * @Description
 * @date 2023/8/12
 * @version 1.0
 **/
public class PluginIdUtil {

    public static String getId(String location) {
        InputStream inputStream = null;
        try {
            inputStream = Files.newInputStream(new File(
                    location + "/" + Constant.PLUGIN_META_FILE_NAME).toPath());
            Properties properties = IOUtils.get(inputStream);
            Object o1 = properties.get(Constant.PLUGIN_CODE_KEY);
            Object o2 = properties.get(Constant.PLUGIN_VERSION_KEY);
            return o1.toString() + "_" + o2.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }
}
