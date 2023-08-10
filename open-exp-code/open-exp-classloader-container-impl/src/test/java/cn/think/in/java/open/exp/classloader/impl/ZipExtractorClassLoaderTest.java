package cn.think.in.java.open.exp.classloader.impl;

import org.junit.Test;

import java.io.File;

public class ZipExtractorClassLoaderTest {

    @Test
    public void findClass() throws Exception {
        String pathToZip = "/Users/cxs/github/open-exp/spring-adapter/open-exp-adapter-common/target/open-exp-adapter-common-1.0-SNAPSHOT.zip";
        String extractDir = "test-tmp-zip/";

        String name = new File(pathToZip).getName();
        System.out.println(name);

        ZipExtractorClassLoader loader = new ZipExtractorClassLoader(pathToZip, extractDir);

        // Test loading a class
        Class<?> clazz = loader.loadClass("org.think.in.java.open.exp.adapter.springboot2.Main");
        System.out.println("Loaded: " + clazz);
        System.out.println("Loaded: " + clazz.getClassLoader());
    }
}