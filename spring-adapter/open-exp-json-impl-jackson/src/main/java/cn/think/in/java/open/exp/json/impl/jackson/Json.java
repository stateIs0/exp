package cn.think.in.java.open.exp.json.impl.jackson;

import cn.think.in.java.open.exp.json.ExpJson;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/25
 **/
public class Json implements ExpJson {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String toJson(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T toObj(String json, Class<T> tClass) {
        try {
            return objectMapper.readValue(json, tClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T, C> C toObj(String o, Class<? extends Collection> c, Class<T> tClass) {
        CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(c, tClass);
        try {
            return objectMapper.readValue(o, listType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
