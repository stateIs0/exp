package cn.think.in.java.open.exp.plugin.depend.support;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Author cxs
 * @Description
 * @date 2023/8/12
 * @version 1.0
 **/
public class IOUtils {

    public static Properties get(InputStream inputStream) {
        Properties properties = new Properties();

        try {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                // ignore
            }
        }

        return properties;

    }
}
