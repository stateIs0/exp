package cn.think.in.java.open.exp.classloader.support;

import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class DirectoryCleaner {

    public static void deleteDirectoryContents(File directory) {
        if (!directory.exists() || !directory.isDirectory()) {
            log.warn("指定的路径不是一个有效的文件夹! {}", directory.getAbsolutePath());
            return;
        }

        File[] files = directory.listFiles();
        if (files != null) {
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
