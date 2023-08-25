package cn.think.in.java.open.exp.core.impl.support;

import cn.think.in.java.open.exp.classloader.PluginMetaThin;
import cn.think.in.java.open.exp.client.Plugin;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/25
 **/
@Mapper
public interface PluginModelMapper {

    PluginModelMapper instance = Mappers.getMapper(PluginModelMapper.class);

    Plugin conv(PluginMetaThin pluginMetaThin);

}
