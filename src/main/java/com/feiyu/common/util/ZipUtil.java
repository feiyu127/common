package com.feiyu.common.util;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * zip 压缩解压工具类
 *
 * @author feiyu127
 * @date 2018/9/11 21:48
 * @desc
 */

public class ZipUtil {
    public static void main(String[] args) throws IOException {
        String pathD = "D:\\torrent//";
        zip(pathD, "D:\\torrent1.zip");
    }

    /**
     * 缓冲区大小
     */
    private static final int BUFFER_LENGTH = 2048;

    /**
     * 解压文件到当前目录，目录名为当前文件名称
     *
     * @param filePath zip文件
     */
    public static void unzip(String filePath) {
        String dir = filePath.substring(0, filePath.lastIndexOf("."));
        unzip(filePath, dir);
    }

    /**
     * 解压文件到指定目录
     *
     * @param filePath    zip文件
     * @param destDirPath 解压路径
     */
    public static void unzip(String filePath, String destDirPath) {
        File file = new File(filePath);
        try (ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(file)));) {
            ZipEntry zipEntry;
            byte[] buffer = new byte[BUFFER_LENGTH];
            while ((zipEntry = zis.getNextEntry()) != null) {
                if (zipEntry.isDirectory()) {
                    continue;
                }
                File fileout = FileUtils.createNewFile(destDirPath, zipEntry.getName());
                System.out.println("解压 : " + fileout.getAbsolutePath());
                FileOutputStream fileOutputStream = new FileOutputStream(fileout);
                int readLength = 0;
                while ((readLength = zis.read(buffer, 0, BUFFER_LENGTH)) != -1) {
                    fileOutputStream.write(buffer, 0, readLength);
                }
                fileOutputStream.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 压缩文件夹到指定文件
     *
     * @param sourceFolder 文件夹
     * @param zipFilePath  指定的zip文件
     */
    public static void zip(String sourceFolder, String zipFilePath) {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFilePath)));) {
            Path sourcePath = Paths.get(sourceFolder);
            int sourceForderPathLength = sourcePath.toString().length() + 1;
            System.out.println("length : " + sourceFolder);
            byte[] buffer = new byte[BUFFER_LENGTH];
            Files.walk(sourcePath, FileVisitOption.FOLLOW_LINKS).forEach(path -> {
                File file = path.toFile();
                if (file.isDirectory()) {
                    return;
                }
                String pathName = file.getAbsolutePath().substring(sourceForderPathLength);
                try (InputStream inputStream = new BufferedInputStream(new FileInputStream(file))) {
                    zipOutputStream.putNextEntry(new ZipEntry(pathName));
                    int len;
                    while ((len = inputStream.read(buffer)) != -1) {
                        zipOutputStream.write(buffer, 0, len);
                    }
                    zipOutputStream.closeEntry();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 压缩stringMap 到指定文件 默认使用utf8字符集
     *
     * @param stringMap   存储<fileName, content> 结构文件，key为文件名，可以包含父文件夹；value为字符串
     * @param zipFilePath 压缩到目标文件目录
     */
    public static void zipStringMap(Map<String, String> stringMap, String zipFilePath) {
        zipStringMap(stringMap, zipFilePath, "UTF8");
    }

    /**
     * 压缩stringMap 到指定文件
     *
     * @param stringMap   存储<fileName, content> 结构文件，key为文件名，可以包含父文件夹；value为字符串
     * @param zipFilePath 压缩到目标文件目录
     * @param charsetName 指定字符集
     */
    public static void zipStringMap(Map<String, String> stringMap, String zipFilePath, String charsetName) {
        if (stringMap == null || stringMap.isEmpty()) {
            return;
        }
        Map<String, byte[]> byteMap = new HashMap<>(stringMap.size());
        Charset charset = Charset.forName(charsetName);
        stringMap.forEach((k, v) -> {
            byteMap.put(k, v.getBytes(charset));
        });
        zip(byteMap, zipFilePath);
    }

    /**
     * 压缩 byteMap 到指定文件 默认使用utf8字符集
     *
     * @param byteMap     存储<fileName, byte[]> 结构文件，key为文件名，可以包含父文件夹；value为字节数组
     * @param zipFilePath 压缩到目标文件目录
     */
    public static void zip(Map<String, byte[]> byteMap, String zipFilePath) {
        if (byteMap == null || byteMap.isEmpty()) {
            return;
        }
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFilePath)));) {
            byteMap.forEach((pathName, bytes) -> {
                try {
                    zipOutputStream.putNextEntry(new ZipEntry(pathName));
                    zipOutputStream.write(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
