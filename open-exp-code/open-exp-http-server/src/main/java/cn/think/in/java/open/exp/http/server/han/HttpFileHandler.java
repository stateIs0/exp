package cn.think.in.java.open.exp.http.server.han;

import cn.think.in.java.open.exp.client.ExpAppContext;
import cn.think.in.java.open.exp.client.Plugin;
import cn.think.in.java.open.exp.client.SpiFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.*;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/26
 **/
@Slf4j
public class HttpFileHandler implements HttpRequestHandler {

    static ExpAppContext expAppContext = SpiFactory.get(ExpAppContext.class);

    public static byte[] getBytesAfterEmptyLine(byte[] data) {
        for (int i = 0; i < data.length - 3; i++) {
            // Check for \r\n\r\n (Windows style)
            if (data[i] == '\r' && data[i + 1] == '\n' && data[i + 2] == '\r' && data[i + 3] == '\n') {
                byte[] result = new byte[data.length - i - 4];
                System.arraycopy(data, i + 4, result, 0, result.length);
                return result;
            }
            // Check for \n\n (Unix style)
            else if (data[i] == '\n' && data[i + 1] == '\n') {
                byte[] result = new byte[data.length - i - 2];
                System.arraycopy(data, i + 2, result, 0, result.length);
                return result;
            }
        }
        return new byte[0];  // Return empty array if no empty line found
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response,
                       HttpContext context) throws HttpException, IOException {
        try {
            if (!request.getRequestLine().getMethod().contains("POST")) {
                response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
                response.setEntity(new StringEntity("need post method", StandardCharsets.UTF_8));
                return;
            }
            if (request instanceof BasicHttpRequest) {
                HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
                if (entity != null && !entity.isRepeatable()) {
                    byte[] byteArray = EntityUtils.toByteArray(entity);
                    String fileName = parseFileName(byteArray);
                    if (fileName != null) {
                        saveUploadedFile(getBytesAfterEmptyLine(byteArray), fileName);
                        Plugin pluginInfo = expAppContext.load(new File(fileName));
                        response.setStatusCode(HttpStatus.SC_OK);
                        response.setEntity(new StringEntity(pluginInfo.toString(), StandardCharsets.UTF_8));
                        log.info("------>> {}", pluginInfo);
                    } else {
                        response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
                        response.setEntity(new StringEntity("Invalid form data"));
                    }
                }
            }
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
            response.setEntity(new StringEntity(e.getMessage(), StandardCharsets.UTF_8));
        }
    }

    private String parseFileName(byte[] content) {
        // TODO: Implement a method to parse the filename from the form data content.
        // This is a placeholder and may not work for all cases.
        String contentStr = new String(content);
        String[] parts = contentStr.split("filename=\"");
        if (parts.length > 1) {
            return parts[1].split("\"")[0];
        }
        return null;
    }

    private void saveUploadedFile(byte[] content, String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            log.warn("创建新文件 {}", fileName);
            file.createNewFile();
        } else {
            log.warn("删除老文件 {}", fileName);
            file.delete();
        }
        try (FileOutputStream out = new FileOutputStream(file)) {
            out.write(content, 0, content.length);
        }
    }

}
