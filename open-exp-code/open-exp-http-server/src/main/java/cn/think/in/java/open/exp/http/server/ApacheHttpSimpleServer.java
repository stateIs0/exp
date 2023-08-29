package cn.think.in.java.open.exp.http.server;

import cn.think.in.java.open.exp.client.ConfigSpi;
import cn.think.in.java.open.exp.client.SpiFactory;
import cn.think.in.java.open.exp.client.StringUtil;
import cn.think.in.java.open.exp.http.server.han.GetAllHandler;
import cn.think.in.java.open.exp.http.server.han.HttpFileHandler;
import cn.think.in.java.open.exp.http.server.han.InstallHandler;
import cn.think.in.java.open.exp.http.server.han.UnInstallHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpServerConnection;
import org.apache.http.impl.DefaultBHttpServerConnection;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.protocol.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ApacheHttpSimpleServer {
    private static ConfigSpi configSpi = SpiFactory.get(ConfigSpi.class, new ConfigSpi.MockConfigSpi());
    volatile static boolean running;
    static ServerSocket serversocket;

    public static void stop() {
        running = false;
        try {
            serversocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void start(String contextPath, int port) throws Exception {
        if (running) {
            log.warn("ApacheHttpSimpleServer is running....");
            return;
        }
        HttpProcessor httpProcessor = HttpProcessorBuilder.create()
                .add(new ResponseDate())
                .add(new ResponseServer("ExP-Server/1.1"))
                .add(new ResponseContent())
                .add(new ResponseConnControl()).build();

        UriHttpRequestHandlerMapper handlerMapper = new UriHttpRequestHandlerMapper();

        String uploadAndInstall;
        String install;
        String uninstall;
        String getAll;

        if (!StringUtil.isEmpty(contextPath)) {
            uploadAndInstall = contextPath + "-uploadAndInstall";
            install = contextPath + "-install";
            uninstall = contextPath + "-uninstall";
            getAll = contextPath + "-getAll";
        } else {
            uploadAndInstall = "/uploadAndInstall";
            install = "/install";
            uninstall = "/uninstall";
            getAll = "/getAll";
        }

        handlerMapper.register(uploadAndInstall, new HttpFileHandler());
        handlerMapper.register(install, new InstallHandler());
        handlerMapper.register(uninstall, new UnInstallHandler());
        handlerMapper.register(getAll, new GetAllHandler());

        Executors.newScheduledThreadPool(1).schedule(() -> {
            String url = "http://localhost:" + Integer.parseInt(configSpi.getProperty("exp.plugin.manager.port", "8888"));
            String uploadAndInstall2 = (url + uploadAndInstall);
            String install2 = url + install + "?path={path}";
            String uninstall2 = url + uninstall + "?pluginId={pluginId}";
            String getAll2 = url + getAll;
            log.info("插件管理模块:先卸载后安装 URL : {}", uploadAndInstall2);
            log.info("插件管理模块: 安装 URL : {}", install2);
            log.info("插件管理模块: 卸载 URL : {}", uninstall2);
            log.info("插件管理模块: 获取所有 URL : {}", getAll2);
        }, 1, TimeUnit.SECONDS);

        HttpService httpService = new HttpService(
                httpProcessor,
                new DefaultConnectionReuseStrategy(),
                new DefaultHttpResponseFactory(),
                handlerMapper,
                null);

        serversocket = new ServerSocket(port);
        log.info("server start");
        running = true;
        try {
            while (running) {
                Socket socket = serversocket.accept();
                socket.setSoTimeout(10000);
                DefaultBHttpServerConnection conn = new DefaultBHttpServerConnection(8 * 1024);
                conn.bind(socket);

                Thread t = new RequestListenerThread(httpService, conn);
                t.setName("exp-http-server-" + conn.getRemoteAddress() + ":" + conn.getRemotePort());
                t.setDaemon(true);
                t.start();
            }
            log.info("server stop end");
        } catch (Exception e) {
            log.warn("server stop end");
        }
    }

    static class RequestListenerThread extends Thread {
        private final HttpService httpService;
        private final HttpServerConnection conn;

        public RequestListenerThread(final HttpService httpService, final HttpServerConnection conn) {
            super();
            this.httpService = httpService;
            this.conn = conn;
        }

        @Override
        public void run() {
            try {
                HttpContext context = new BasicHttpContext();
                this.httpService.handleRequest(this.conn, context);
            } catch (ConnectionClosedException ignore) {
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            } finally {
                try {
                    this.conn.shutdown();
                } catch (IOException ignore) {
                }
            }
        }
    }
}
