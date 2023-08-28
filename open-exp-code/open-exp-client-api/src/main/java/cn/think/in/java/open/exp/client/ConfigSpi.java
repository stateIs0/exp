package cn.think.in.java.open.exp.client;

/**
 * @Author cxs
 * @Description
 * @date 2023/8/28
 * @version 1.0
 **/
public interface ConfigSpi {

    String getProperty(String key, String def);

    class MockConfigSpi implements ConfigSpi {

        @Override
        public String getProperty(String key, String def) {
            return def;
        }

    }
}
