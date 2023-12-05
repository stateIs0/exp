package open.exp.adapter.springboot.common.starter;

/**
 * @Author cxs
 **/
public class PluginNameBuilder {

    public static String build(Class<?> c, String pluginId) {
        return c.getName() + "_____" + pluginId;
    }

    public static String getPluginId(String name) {
        String[] split = name.split("_____");
        if (split.length >= 2) {
            return split[split.length - 1];
        }
        return null;
    }

    public static String buildId(String code, String version) {
        return code + "_" + version;
    }

    public static String[] splitId(String pluginId) {
        return pluginId.split("_");
    }
}
