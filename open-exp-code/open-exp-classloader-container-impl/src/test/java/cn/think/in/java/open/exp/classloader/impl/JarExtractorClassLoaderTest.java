package cn.think.in.java.open.exp.classloader.impl;

public class JarExtractorClassLoaderTest {

    @org.junit.Test
    public void findClass() throws Exception {

        String pathToJar
                = "/Users/cxs/github/open-exp/open-exp-code/open-exp-core-impl/target/open-exp-core-impl-1.0-SNAPSHOT.jar";
        String extractDir = "test-tmp/";

        JarExtractorClassLoader loader = new JarExtractorClassLoader(pathToJar, extractDir);

        // Test loading a class
        Class<?> clazz = loader.loadClass("org.think.in.java.open.exp.core.impl.ExpAppContextSpi");
        System.out.println("Loaded: " + clazz);
        System.out.println("Loaded: " + clazz.getClassLoader());
    }
}