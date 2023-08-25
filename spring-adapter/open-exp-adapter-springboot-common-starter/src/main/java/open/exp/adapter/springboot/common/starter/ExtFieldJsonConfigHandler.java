package open.exp.adapter.springboot.common.starter;

import cn.think.in.java.open.exp.client.SpiFactory;
import cn.think.in.java.open.exp.client.StringUtil;
import cn.think.in.java.open.exp.json.ExpJson;
import cn.think.in.java.open.exp.object.field.ext.ExtMetaBean;
import cn.think.in.java.open.exp.object.field.ext.JavassistObjectFieldExt;
import cn.think.in.java.open.exp.object.field.ext.ObjectFieldExt;
import lombok.Builder;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/13
 **/
@Builder
public class ExtFieldJsonConfigHandler {
    final static String EXT_FIELD_CONFIG_JSON = "exp_object_field_config_json";
    final ObjectFieldExt objectFieldExt = new JavassistObjectFieldExt();
    ConfigurableEnvironment environment;


    public void run() {
        ExpJson expJson = SpiFactory.get(ExpJson.class);
        String property = environment.getProperty(EXT_FIELD_CONFIG_JSON);
        if (!StringUtil.isEmpty(property)) {
            List<ExtMetaBean> extMetaBean;
            try {
                extMetaBean = expJson.toObj(property, ArrayList.class, ExtMetaBean.class);
                objectFieldExt.addField(extMetaBean);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
