package com.feiyu.common.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author feiyu127
 * @date 2018/8/29 21:34
 * @desc
 */

public class FileUtils {
    private static final String DEFAULT_CHARSET_NAME = "UTF-8";

    /**
     * 将字符串内容写入文件
     *
     * @param content 文本内容
     * @param path    文件
     */
    public static void write(String content, String charsetName, String path) {
        try {
            byte[] bytes = content.getBytes(charsetName);
            write(bytes, path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将字符串内容写入文件
     *
     * @param content 文本内容
     * @param path    文件
     */
    public static void write(String content, String path) {
        write(content, DEFAULT_CHARSET_NAME, path);
    }

    /**
     * 将字节流写入文件
     *
     * @param bytes 文本内容
     * @param path  文件
     */
    public static void write(byte[] bytes, String path) {
        File file = createNewFile(path);
        try {
            Files.write(file.toPath(), bytes, StandardOpenOption.WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建新文件
     *
     * @param filePath 文件全路径
     * @return
     */
    public static File createNewFile(String filePath) {
        File file = new File(filePath);
        createNewFile(file);
        return file;
    }

    /**
     * 创建新闻界
     *
     * @param filePath 文件路径
     * @param fileName 文件名
     * @return
     */
    public static File createNewFile(String filePath, String fileName) {
        File file = new File(filePath, fileName);
        createNewFile(file);
        return file;
    }

    /**
     * 创建文件，如果存在则直接返回，否则创建新闻界
     *
     * @param file
     */
    public static boolean createNewFile(File file) {
        if (!file.exists()) {
            File dir = file.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            try {
                return file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 创建目录
     *
     * @param dirPath
     */
    public static void createDir(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();
        }
    }
}
