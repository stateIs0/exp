package cn.think.in.java.open.exp.http.server.han;

import cn.think.in.java.open.exp.client.ExpAppContext;
import cn.think.in.java.open.exp.client.SpiFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/26
 **/
@Slf4j
public class UnInstallHandler implements HttpRequestHandler {
    static ExpAppContext expAppContext = SpiFactory.get(ExpAppContext.class);

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse response, HttpContext httpContext) throws HttpException, IOException {
        String uri = httpRequest.getRequestLine().getUri();
        String pluginId = uri.substring(uri.lastIndexOf("=") + 1, uri.length());
        try {
            List<String> allPluginId = expAppContext.getAllPluginId();
            if (allPluginId.contains(pluginId)) {
                expAppContext.unload(pluginId);
                response.setStatusCode(HttpStatus.SC_OK);
                response.setEntity(new StringEntity("卸载成功", StandardCharsets.UTF_8));
            } else {
                response.setStatusCode(HttpStatus.SC_OK);
                response.setEntity(new StringEntity("没有这个插件 " + pluginId, StandardCharsets.UTF_8));
            }

            log.info("卸载插件 ------>> {}", pluginId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            response.setStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            response.setEntity(new StringEntity(e.getMessage(), StandardCharsets.UTF_8));
        }
    }
}
