package cn.think.in.java.open.exp.http.server.han;

import cn.think.in.java.open.exp.client.ExpAppContext;
import cn.think.in.java.open.exp.client.SpiFactory;
import cn.think.in.java.open.exp.json.ExpJson;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;

import java.io.IOException;
import java.util.Collection;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/26
 **/
@Slf4j
public class GetAllHandler implements HttpRequestHandler {

    ExpJson expJson = SpiFactory.get(ExpJson.class);
    ExpAppContext expAppContext = SpiFactory.get(ExpAppContext.class);

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse response, HttpContext httpContext) throws HttpException, IOException {
        log.info("getAll =======>>>>>>>> ");
        response.setStatusCode(HttpStatus.SC_OK);
        response.setEntity(new StringEntity(expJson.toJson(expAppContext.getAllPluginId())));
    }
}
