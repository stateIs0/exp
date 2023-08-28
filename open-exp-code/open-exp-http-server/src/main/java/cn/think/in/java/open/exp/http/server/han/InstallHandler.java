package cn.think.in.java.open.exp.http.server.han;

import cn.think.in.java.open.exp.client.ExpAppContext;
import cn.think.in.java.open.exp.client.Plugin;
import cn.think.in.java.open.exp.client.SpiFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class InstallHandler implements HttpRequestHandler {
    static ExpAppContext expAppContext = SpiFactory.get(ExpAppContext.class);

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse response, HttpContext httpContext) throws HttpException, IOException {
        String uri = httpRequest.getRequestLine().getUri();
        String path = uri.substring(uri.indexOf("path=") + 5, uri.length());
        try {
            Plugin pluginInfo = expAppContext.load(new File(path));
            response.setStatusCode(HttpStatus.SC_OK);
            response.setEntity(new StringEntity(pluginInfo.toString(), StandardCharsets.UTF_8));
            log.info("安装插件 ------>> {}", pluginInfo);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            response.setStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            response.setEntity(new StringEntity(e.getMessage(), StandardCharsets.UTF_8));
        }
    }
}
