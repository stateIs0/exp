package cn.think.in.java.open.exp.adapter.springboot27;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

public class HttpFileDownloader {

    public static void download(String fileURL, String savePath) {
        if (!savePath.startsWith("/") && !fileURL.startsWith("http")) {
            throw new RuntimeException("path  错误" + savePath);
        }

        try {
            if (fileURL.contains("%")) {
                fileURL = URLDecoder.decode(fileURL, "UTF-8");
            }
            URL url = new URL(fileURL);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            int responseCode = httpConn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = httpConn.getInputStream();
                FileOutputStream outputStream = new FileOutputStream(savePath);

                int bytesRead;
                byte[] buffer = new byte[4096];

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.close();
                inputStream.close();
            }
            httpConn.disconnect();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
