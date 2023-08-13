package cn.think.in.java.open.exp.adapter.springboot2;

import cn.think.in.java.open.exp.client.StringUtil;
import cn.think.in.java.open.exp.object.field.ext.ExtMetaBean;
import cn.think.in.java.open.exp.object.field.ext.JavassistObjectFieldExt;
import cn.think.in.java.open.exp.object.field.ext.ObjectFieldExt;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.Builder;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author cxs
 * @Description
 * @date 2023/8/13
 * @version 1.0
 **/
@Builder
public class ExtFieldJsonConfigHandler {
    final static String EXT_FIELD_CONFIG_JSON = "exp_object_field_config_json";
    final ObjectMapper objectMapper = new ObjectMapper();
    final ObjectFieldExt objectFieldExt = new JavassistObjectFieldExt();
    ConfigurableEnvironment environment;

    public void run() {
        String property = environment.getProperty(EXT_FIELD_CONFIG_JSON);
        if (!StringUtil.isEmpty(property)) {
            CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, ExtMetaBean.class);
            List<ExtMetaBean> extMetaBean;
            try {
                extMetaBean = objectMapper.readValue(property, listType);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            try {
                objectFieldExt.addField(extMetaBean);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
