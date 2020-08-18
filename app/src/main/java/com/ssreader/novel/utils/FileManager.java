package com.ssreader.novel.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;


import com.ssreader.novel.base.BWNApplication;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.model.BaseComicImage;
import com.ssreader.novel.model.BookChapter;
import com.ssreader.novel.model.ChapterContent;
import com.ssreader.novel.ui.utils.MyToash;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;

/**
 * 文件操作管理类
 */
public class FileManager {

    /**
     * 文件存储根目录
     */
    public static String FILEROOT;


    public static final void initialize(Context context) {
        FILEROOT = context.getFilesDir().getPath().concat("/");
    }

    /**
     * <p>
     * 判断SDCard是否存在。
     * </p>
     *
     * @return SD卡存在与否。
     */
    public static final boolean isSDCardExists() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * @return SD卡路径。
     */
    public static final String getSDCardRoot() {
        String root = "";
        if (isSDCardExists()) {
            root = Environment.getExternalStorageDirectory().toString() + "/ssreader/";
        }
        return root;
    }

    public static String getLocalBookTxtPath(BookChapter chapter) {
        return getLocalBookTxtPath(chapter.book_id, chapter.chapter_id, chapter.is_preview, chapter.update_time);
    }

    public static String getLocalBookTxtPath(long book_id, ChapterContent chapter) {
        return getLocalBookTxtPath(book_id, chapter.getChapter_id(), chapter.getIs_preview(), chapter.getUpdate_time());
    }

    public static String getLocalBookTxtPath(long book_id, long chapter_id, int Is_preview, String Update_time) {
        return FileManager.getSDCardRoot()
                .concat("Reader/book/")
                .concat(book_id + "/")
                .concat(Update_time + "/")
                .concat(chapter_id + "/")
                .concat(Is_preview + ".bwTxt");
    }

    public static boolean isExistLocalBookTxtPath(BookChapter chapter) {
        if (chapter == null) {
            return false;
        }
        if (chapter.book_id > Constant.LOCAL_BOOKID) {
            return true;
        }
        if (FileManager.isExist(chapter.chapter_path)) {
            int length = chapter.chapter_path.length();
            if (length > 7) {
                String pr = chapter.chapter_path.substring(length - 7, length - 6);
                if (pr.equals(chapter.is_preview + "")) {
                    return true;
                }
            }
        }
        String newPath = getLocalBookTxtPath(chapter);
        boolean isExist2 = FileManager.isExist(newPath);
        if (isExist2) {
            if (!newPath.equals(chapter.chapter_path)) {
                chapter.chapter_path = newPath;
                ObjectBoxUtils.addData(chapter, BookChapter.class);
            }
        }
        return isExist2;
    }

    // ben APP 根目录
    public static final String getImgs() {
        String images = FileManager.getSDCardRoot() + "Imgs/";
        File file = new File(images);
        if (!file.exists()) {
            file.mkdirs();
        }
        return images;
    }

    // ben APP 根目录
    public static final String getVideos() {
        return FileManager.getSDCardRoot() + "videos/";
    }

    public static final String getAudioLocal(ChapterContent chapterContent) {
        String path = FileManager.getSDCardRoot().concat("Reader/audio/").concat(chapterContent.getAudio_id() + "/");
        File file;
        if (!(file = new File(path)).exists()) {
            file.mkdirs();
        }
        return path + chapterContent.getChapter_id();
    }

    public static final String isExistAudioLocal(long audio_id, long chapter_id) {
        String path = FileManager.getSDCardRoot().concat("Reader/audio/").concat(audio_id + "/") + chapter_id + ".mp3";
        File file = new File(path);
        if (file.exists()) {
            return path;
        } else {
            path = FileManager.getSDCardRoot().concat("Reader/audio/").concat(audio_id + "/") + chapter_id + ".wav";
            file = new File(path);
            if (file.exists()) {
                return path;
            }
        }
        return null;
    }

    // ben APP 根目录
    public static final String getAPP() {
        return FileManager.getSDCardRoot() + "app/";
    }

    // ben APP 根目录
    public static final File getSquareBigImgs(String name) {
        File pathFile = new File(getImgs() + "imgs/");
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        if (name == null) {
            return new File(pathFile, System.currentTimeMillis() + ".jpg");
        } else {
            return new File(pathFile, name + ".jpg");
        }
    }

    public static final String getHeadImgs() {
        return getImgs() + "HeadImgs/";
    }

    public static final File getVideo(String name) {
        File pathFile = new File(getImgs() + "Videos/");
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        if (name == null) {
            return new File(pathFile, System.currentTimeMillis() + ".jpg");
        } else {
            return new File(pathFile, name + ".jpg");
        }
    }

    /**
     * 漫画下载更目录
     *
     * @return
     */
    public static final String getManhuaSDCardRoot() {
        return FileManager.getSDCardRoot() + "image/comic/";
    }

    public static final String getAudioSDCardRoot() {
        return FileManager.getSDCardRoot() + "audio/";
    }

    public static final byte[] readFile(String filePath) {
        byte[] buffer = null;
        FileInputStream fis = null;
        try {
            File file = new File(filePath);
            if (file.exists()) {
                fis = new FileInputStream(file);
                int len = fis.available();
                buffer = new byte[len];
                fis.read(buffer);
            }
        } catch (Exception e) {
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                }
            }
        }
        return buffer;
    }

    /**
     * 创建文件
     *
     * @param path   文件路径
     * @param buffer 比特流缓冲区
     */
    public static final File createFile(String path, byte[] buffer) {
        return createFile(new File(path), buffer);
    }

    public static final File createFile(File file, byte[] buffer) {
        boolean b;
        if (file.exists()) {
            return file;
        } else {
            b = file.getParentFile().mkdirs();
        }
        FileOutputStream fstream = null;
        try {
            file.createNewFile();
            fstream = new FileOutputStream(file);
            fstream.write(buffer);
        } catch (IOException e) {
        } finally {
            if (fstream != null) {
                try {
                    fstream.close();
                } catch (IOException e) {
                }
            }
        }
        return file;
    }

    /**
     * 创建目录。
     *
     * @param path 目录
     */
    public static final void createPath(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * <p>
     * 保存文件，采用普通流方式保存。<br>
     * 如果是路径则创建（不存在时）；如果是文件则保存。
     * </p>
     *
     * @param filePath 文件路径
     * @param buffer   内容
     */
    public static final void saveFile(String filePath, byte[] buffer) {
        if (null == filePath || filePath.equals("")) {
            return;
        }
        if (buffer == null) {
            return;
        }

        if (filePath.endsWith("/")) {
            // 数据以/结尾（例：sygh/css/），表示是路径，此时应建立文件夹
            createPath(filePath);
        } else {
            createFile(filePath, buffer);
        }
    }


    /**
     * <p>
     * 删除文件或目录。
     * </p>
     *
     * @param filePath 文件路径
     * @return 删除成功返回true，否则返回false。
     */
    public static final boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        }
        if (file.isFile()) {
            return file.delete();
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File tempF : files) {
                    deleteFile(tempF.getPath());
                }
            }
            // 删除file 本身
            return file.delete();
        }
        return true;
    }

    public static boolean isExist(String filePath) {
        return filePath != null && new File(filePath).exists();
    }

    public static String txt2String(File file) {
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                result.append(System.lineSeparator() + s);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }


    /**
     * 根据java.nio.*的流获取文件大小
     *
     * @param file
     */
    public static long getFileSize(File file) {
        FileChannel fc = null;
        try {
            if (file.exists() && file.isFile()) {
                String fileName = file.getName();
                FileInputStream fis = new FileInputStream(file);
                fc = fis.getChannel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != fc) {
                try {
                    fc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            return fc.size();
        } catch (IOException e) {
            return 0;
        }
    }

    public static File getLocalComicImageFile(BaseComicImage baseComicImage) {
        String ImgName = "";
        String localPath = FileManager.getManhuaSDCardRoot().concat(baseComicImage.comic_id + "/").concat(baseComicImage.chapter_id + "/");
        if (baseComicImage.image.contains(".jpg")) {
            ImgName = baseComicImage.image_id + ".jpg";
        } else if (baseComicImage.image.contains(".jpeg")) {
            ImgName = baseComicImage.image_id + ".jpeg";
        } else if (baseComicImage.image.contains(".png")) {
            ImgName = baseComicImage.image_id + ".png";
        } else if (baseComicImage.image.contains(".webp")) {
            ImgName = baseComicImage.image_id + ".webp";
        }
        if (ImgName.equals("")) {
            return null;
        }
        File localPathFile = new File(localPath.concat(ImgName) + "bw");
        return localPathFile;
    }

    public static void GlideCopy(File source, File target) {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileInputStream = new FileInputStream(source);
            fileOutputStream = new FileOutputStream(target);
            byte[] buffer = new byte[1024];
            while (fileInputStream.read(buffer) > 0) {
                fileOutputStream.write(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileInputStream.close();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean writeToXML(String fileName, String content) {
        String rootXMLPath = getSDCardRoot() + "comic/down";
        FileOutputStream fileOutputStream;
        BufferedWriter bufferedWriter;
        createDirectory(rootXMLPath);
        File file = new File(rootXMLPath + "/" + fileName + ".bw");
        try {
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            bufferedWriter.write(content);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 读取XML内容
     *
     * @param filePath
     * @return
     */
    public static String readFromXML(String filePath, boolean cimicDown) {
        String rootXMLPath;
        if (cimicDown) {
            rootXMLPath = getSDCardRoot() + "comic/down/" + filePath + ".bw";
        } else {
            rootXMLPath = filePath;
        }
        FileInputStream fileInputStream;
        BufferedReader bufferedReader;
        StringBuilder stringBuilder = new StringBuilder();
        File file = new File(rootXMLPath);
        if (file.exists()) {
            try {
                fileInputStream = new FileInputStream(file);
                bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                bufferedReader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 创建文件夹
     *
     * @param fileDirectory
     */
    public static void createDirectory(String fileDirectory) {
        File file = new File(fileDirectory);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 获取总内存
     *
     * @return
     */
    public static long getSize() {
        final StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        long size = statFs.getBlockSizeLong();
        long availableCounts = statFs.getAvailableBlocksLong();
        return availableCounts * size;
    }

    public static byte[] getBytesByFile(File file) {
        try {

            //获取输入流
            FileInputStream fis = new FileInputStream(file);

            //新的 byte 数组输出流，缓冲区容量1024byte
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
            //缓存
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            //改变为byte[]
            byte[] data = bos.toByteArray();
            //
            bos.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
