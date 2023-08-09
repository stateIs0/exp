package cn.think.in.java.open.exp.classloader.support;

import java.io.File;

public class DirectoryCleaner {

    public static void deleteDirectoryContents(File directory) {
        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("指定的路径不是一个有效的文件夹!");
            return;
        }

        File[] files = directory.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectoryContents(file);
                    file.delete();  // 删除子文件夹
                } else {
                    file.delete();  // 删除文件
                }
            }
        }

        directory.delete();
    }

}
