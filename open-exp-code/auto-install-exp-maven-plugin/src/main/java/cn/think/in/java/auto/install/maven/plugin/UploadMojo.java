package cn.think.in.java.auto.install.maven.plugin;

import cn.think.in.java.open.exp.client.Constant;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;

import static cn.think.in.java.open.exp.client.Constant.PLUGIN_META_FILE_NAME;

@Mojo(name = "upload", defaultPhase = LifecyclePhase.PACKAGE)
public class UploadMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project.build.directory}", property = "outputDir", required = true)
    private File outputDirectory;

    @Parameter(property = "jarName", required = true)
    private String jarName;

    @Parameter(property = "uploadUrl", required = true)
    private String uploadUrl;

    @Parameter(property = "uninstallUrl", required = false)
    private String uninstallUrl;

    @Parameter(defaultValue = "false", property = "skipUpload", required = true)
    private boolean skipUpload;

    public void execute() throws MojoExecutionException {
        if (skipUpload) {
            getLog().info("Skipping upload.");
            return;
        }

        File jarFile = new File(outputDirectory, jarName);
        if (!jarFile.exists()) {
            throw new MojoExecutionException("Jar file does not exist: " + jarFile);
        }

        URL resource;
        try (URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{jarFile.toURI().toURL()}, Thread.currentThread().getContextClassLoader())) {
            resource = urlClassLoader.findResource(PLUGIN_META_FILE_NAME);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Properties properties = new Properties();
        try(InputStream resourceStream = resource.openStream()) {
            properties.load(resourceStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Object o = properties.get(Constant.PLUGIN_CODE_KEY);
        Object o2 = properties.get(Constant.PLUGIN_VERSION_KEY);

        String id = o + "_" + o2;

        getLog().info("插件 id = " + id);
        tryUninstall(id);

        uploadFile(jarFile);
    }

    void tryUninstall(String pluginId) throws MojoExecutionException {
        if (StringUtils.isEmpty(uninstallUrl)) {
            return;
        }
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet uploadFile = new HttpGet(uninstallUrl + "?pluginId=" + pluginId);

            CloseableHttpResponse response = httpClient.execute(uploadFile);
            HttpEntity responseEntity = response.getEntity();

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                String reasonPhrase = response.getStatusLine().getReasonPhrase();
                getLog().info("卸载失败. Server response: " + EntityUtils.toString(responseEntity));
                throw new MojoExecutionException("Failed to uninstall plugin. Server returned status code: " + statusCode
                        + ", reasonPhrase = " + reasonPhrase + ", pluginId = " + pluginId);
            }

            getLog().info("卸载结束. Server response: " + EntityUtils.toString(responseEntity));
        } catch (Exception e) {
            throw new MojoExecutionException("Error uninstall plugin", e);
        }

    }

    private void uploadFile(File file) throws MojoExecutionException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost uploadFile = new HttpPost(uploadUrl);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addBinaryBody("file", file);
            HttpEntity multipart = builder.build();
            uploadFile.setEntity(multipart);

            CloseableHttpResponse response = httpClient.execute(uploadFile);
            HttpEntity responseEntity = response.getEntity();

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                String reasonPhrase = response.getStatusLine().getReasonPhrase();
                throw new MojoExecutionException("Failed to upload file. Server returned status code: " + statusCode
                        + ", reasonPhrase = " + reasonPhrase);
            }

            getLog().info("File uploaded successfully. Server response: " + EntityUtils.toString(responseEntity));
        } catch (Exception e) {
            throw new MojoExecutionException("Error uploading file", e);
        }
    }
}
