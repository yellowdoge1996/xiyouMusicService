/*
 * Copyright 2015 Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yellowdoge.app.xiyoumusic.util;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.flac.FlacInfoReader;
import org.jaudiotagger.audio.generic.GenericAudioHeader;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.audio.wav.util.WavInfoReader;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class FileTools {

    public static final int BUFSIZE = 1024 * 8;
    private static final String TAG = "FileTools";

    public static String getJarRootPath(){
        try {
            String path = ResourceUtils.getURL("classpath:").getPath();
            //=> file:/root/tmp/demo-springboot-0.0.1-SNAPSHOT.jar!/BOOT-INF/classes!/
            //创建File时会自动处理前缀和jar包路径问题  => /root/tmp
            File rootFile = new File(path);
            if (!rootFile.exists()) {
                rootFile = new File("");
            }
            return rootFile.getAbsolutePath();
        }catch (FileNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }

    public static int getMP3Lenth(String dir){
        File file = new File(dir);
        File[] content = file.listFiles();
        for (File file1 : content) {
            String filepath = file1.getPath().toLowerCase();
            if (filepath.endsWith(".mp3")||filepath.endsWith(".flac")||filepath.endsWith(".wav")) {
                return (int) file1.length();
            }
        }
        return 0;
    }
    public static File getMp3File(String dir){
        File file = new File(dir);
        File[] content = file.listFiles();
        for (File file1 : content) {
            if (file1.getPath().endsWith(".mp3")) {
                return file1;
            }
        }
        return null;
    }
    public static long getMP3Time(String dir){
        File file = new File(dir);
        File[] content = file.listFiles();
        for (File file1 : content) {
            String filepath = file1.getPath().toLowerCase();
            if (filepath.endsWith(".mp3")) {
                try {
                    MP3File f = (MP3File) AudioFileIO.read(file1);
                    MP3AudioHeader audioHeader = (MP3AudioHeader) f.getAudioHeader();
                    return audioHeader.getTrackLength();
                } catch (Exception e) {
                    return -1;
                }
            }else if (filepath.endsWith(".wav")){
                try {
                    RandomAccessFile randomAccessFile = new RandomAccessFile(file1, "rw");
                    WavInfoReader wavInfoReader = new WavInfoReader();
                    GenericAudioHeader read = wavInfoReader.read(randomAccessFile);
                    return read.getTrackLength();
                }catch (IOException e) {
                    return -1;
                }catch (CannotReadException e) {
                    return -1;
                }
            }else if (filepath.endsWith(".flac")){
                try {
                    RandomAccessFile randomAccessFile = new RandomAccessFile(file1,"rw");
                    FlacInfoReader flacInfoReader = new FlacInfoReader();
                    try {
                        GenericAudioHeader read = flacInfoReader.read(randomAccessFile);
                        return read.getTrackLength();
                    } catch (CannotReadException e) {
                        return -1;
                    }
                }catch (IOException e) {
                    return -1;
                }
            }
        }
        return 0;
    }
    public static String StringFilter(String str)throws PatternSyntaxException {

        // String   regEx  =  "[^a-zA-Z0-9]"; // 只允许字母和数字
        // 清除掉所有特殊字符(除了~之外)
        String regEx="[\\[{【<].*[\\]}】>]|[`!@#$%^&*+=|{}':;,/<>?！￥…（）—【】‘；：”“’。，、？]";
        Pattern pattern   =   Pattern.compile(regEx);
        Matcher matcher   =   pattern.matcher(str);
        return   matcher.replaceAll("").trim();
    }

    /**
     * 文件或者文件夹是否存在.
     */
    public static boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * 删除指定文件夹下所有文件, 不保留文件夹.
     */
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (file.isFile()) {
            file.delete();
            return true;
        }
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            File exeFile = files[i];
            if (exeFile.isDirectory()) {
                delAllFile(exeFile.getAbsolutePath());
            } else {
                exeFile.delete();
            }
        }
        file.delete();

        return flag;
    }
    /**
     * 获取文件或者文件夹大小.
     */
    public static long getFileAllSize(String path) {
        File file = new File(path);
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] childrens = file.listFiles();
                long size = 0;
                for (File f : childrens) {
                    size += getFileAllSize(f.getPath());
                }
                return size;
            } else {
                return file.length();
            }
        } else {
            return 0;
        }
    }

    /**
     * 创建一个文件.
     */
    public static boolean initFile(String path) {
        boolean result = false;
        try {
            File file = new File(path);
            if (!file.exists()) {
                result = file.createNewFile();
            } else if (file.isDirectory()) {
                file.delete();
                result = file.createNewFile();
            } else if (file.exists()) {
                result = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 创建一个文件夹.
     */
    public static boolean initDirectory(String path) {
        boolean result = false;
        File file = new File(path);
        if (!file.exists()) {
            result = file.mkdir();
        } else if (!file.isDirectory()) {
            file.delete();
            result = file.mkdir();
        } else if (file.exists()) {
            result = true;
        }
        return result;
    }

    /**
     * 判断目录是否存在，不存在则判断是否创建成功
     *
     * @param file 文件
     * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
     */
    public static boolean createOrExistsDir(File file) {
        // 如果存在，是目录则返回true，是文件则返回false，不存在则返回是否创建成功
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    /**
     * 判断文件是否存在，不存在则判断是否创建成功
     *
     * @param file 文件
     * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
     */
    public static boolean createOrExistsFile(File file) {
        if (file == null) return false;
        // 如果存在，是文件则返回true，是目录则返回false
        if (file.exists()) return file.isFile();
        if (!createOrExistsDir(file.getParentFile())) return false;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断文件是否存在，存在则在创建之前删除
     *
     * @param file 文件
     * @return {@code true}: 创建成功<br>{@code false}: 创建失败
     */
    public static boolean createFileByDeleteOldFile(File file) {
        if (file == null) return false;
        // 文件存在并且删除失败返回false
        if (file.exists() && file.isFile() && !file.delete()) return false;
        // 创建目录失败返回false
        if (!createOrExistsDir(file.getParentFile())) return false;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除文件
     *
     * @param file 文件
     * @return {@code true}: 删除成功<br>{@code false}: 删除失败
     */
    public static boolean deleteFile(File file) {
        return file != null && (!file.exists() || file.isFile() && file.delete());
    }
}