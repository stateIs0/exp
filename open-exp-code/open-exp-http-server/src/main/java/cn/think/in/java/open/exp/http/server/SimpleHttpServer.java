package cn.think.in.java.open.exp.http.server;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/24
 **/

import cn.think.in.java.open.exp.client.ExpAppContext;
import cn.think.in.java.open.exp.client.Plugin;
import cn.think.in.java.open.exp.client.SpiFactory;
import cn.think.in.java.open.exp.client.StringUtil;
import cn.think.in.java.open.exp.json.ExpJson;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static cn.think.in.java.open.exp.http.server.ManagerPluginManager.blocking;

@Slf4j
public class SimpleHttpServer {

    static ExpJson expJson = SpiFactory.get(ExpJson.class);

    static ExpAppContext expAppContext = SpiFactory.get(ExpAppContext.class);
    static String responseFormat = "HTTP/1.1 200 OK\r\n" +
            "Content-Type: application/json\r\n" +
            "\r\n" +
            "%s";
    static String response = "HTTP/1.1 200 OK\r\n" +
            "Content-Type: application/json\r\n" +
            "\r\n";

    public static void handleRequest(Socket clientSocket) throws Throwable {
        if (blocking()) {
            return;
        }

        clientSocket.setSoTimeout(10000);

        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        String line = in.readLine();
        if (line != null && line.startsWith("GET")) {
            // 解析URL和参数
            String[] parts = line.split(" ");
            if (parts.length > 1) {
                String url = parts[1];
                if (url.contains("favicon.ico")) {
                    in.close();
                    return;
                }
                Map<String, String> params = parseQueryString(url);
                log.info("url ={}, params={}", url, params);
                if ("/getAll".equals(url)) {
                    getAll(clientSocket);
                    return;
                }

                if (params.get("path") != null) {
                    install(clientSocket, params);
                    return;
                }
                String pluginId = params.get("pluginId");
                if (!StringUtil.isEmpty(pluginId)) {
                    handlerUnInstall(clientSocket, pluginId);
                    return;
                }

                write(clientSocket, "输入错误, 支持 URL : /getAll ; /install?path=xxxx ; /uninstall?pluginId=xxxx(http 需转义)");
            }

        }

        in.close();
    }

    private static void getAll(Socket clientSocket) throws Exception {
        List<String> allPluginId = expAppContext.getAllPluginId();
        write(clientSocket, allPluginId);
    }

    private static void install(Socket clientSocket, Map<String, String> params) throws Throwable {
        try {
            String path = params.get("path");
            if (path.startsWith("http")) {
                File tempFile = File.createTempFile("exp-" + UUID.randomUUID(), ".jar");
                HttpFileDownloader.download(path, tempFile.getAbsolutePath());
                path = tempFile.getAbsolutePath();
            }
            Plugin load = expAppContext.load(new File(path));
            write(clientSocket, load);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            write(clientSocket, e);
        }
    }

    private static void handlerUnInstall(Socket clientSocket, String pluginId) throws Exception {
        try {
            if (expAppContext.getAllPluginId().contains(pluginId)) {
                expAppContext.unload(pluginId);
                write(clientSocket, "success");
            } else {
                write(clientSocket, pluginId + " pluginId 不存在.....");
            }
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            write(clientSocket, e);
        }
    }

    public static void write(Socket clientSocket, Object o) throws Exception {
        if (o instanceof Throwable) {
            Throwable t = (Throwable) o;
            clientSocket.getOutputStream().write(response.getBytes(StandardCharsets.UTF_8));
            t.printStackTrace(new PrintStream(clientSocket.getOutputStream()));
            return;
        }
        String format = String.format(responseFormat, expJson.toJson(o));
        clientSocket.getOutputStream().write(format.getBytes(StandardCharsets.UTF_8));
    }

    private static Map<String, String> parseQueryString(String url) {
        Map<String, String> params = new HashMap<>();
        int questionMarkIndex = url.indexOf("?");
        if (questionMarkIndex != -1) {
            String queryString = url.substring(questionMarkIndex + 1);
            String[] paramPairs = queryString.split("&");
            for (String paramPair : paramPairs) {
                String[] keyValue = paramPair.split("=");
                if (keyValue.length == 2) {
                    params.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return params;
    }


}
