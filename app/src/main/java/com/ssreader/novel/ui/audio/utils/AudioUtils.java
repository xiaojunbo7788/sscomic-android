package com.ssreader.novel.ui.audio.utils;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * 用于解析.wav文件
 * @author admin
 * @version 2020/02/13
 */
public class AudioUtils {

    /**
     * 根据本地文件地址获取wav音频时长
     * @param filePath
     * @return
     */
    public static long getWavLength(String filePath) {
        byte[] wavData = getBytes(filePath);
        if (wavData != null && wavData.length > 44) {
            int byteRate = byteArrayToInt(wavData, 28, 31);
            int waveSize = byteArrayToInt(wavData, 40, 43);
            // 毫秒
//            return waveSize * 1000 / byteRate;
            // 秒
            return waveSize / byteRate;
        }
        return 0;
    }

    /**
     * file 2 byte数组
     * @param filePath
     * @return
     */
    private static byte[] getBytes(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * byte - int
     * @param b
     * @param start
     * @param end
     * @return
     */
    private static int byteArrayToInt(byte[] b, int start, int end) {
        return ByteBuffer.wrap(b, start, end).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }
}
