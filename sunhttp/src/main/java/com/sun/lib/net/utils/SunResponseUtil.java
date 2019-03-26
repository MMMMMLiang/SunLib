package com.sun.lib.net.utils;


import com.sun.lib.net.model.Download;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import okhttp3.ResponseBody;

/**
 * 网络请求响应工具类
 */
public class SunResponseUtil {
    private static SunResponseUtil instance = null;

    private SunResponseUtil() {
    }

    public static SunResponseUtil get() {
        if (instance == null) {
            synchronized (SunResponseUtil.class) {
                if (instance == null) {
                    instance = new SunResponseUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 下载并创建文件写入数据
     * @param responseBody 下载的数据
     * @param file         创建的文件
     * @param download     下载实体类
     * @throws IOException 异常信息
     */
    public void download2LocalFile(ResponseBody responseBody, File file, Download download) throws IOException {
        try {
            RandomAccessFile randomAccessFile = null;
            FileChannel channelOut = null;
            InputStream inputStream = null;
            try {
                //创建文件夹
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                //初始化
                inputStream = responseBody.byteStream();
                randomAccessFile = new RandomAccessFile(file, "rwd");
                channelOut = randomAccessFile.getChannel();
                //总长度
                long allLength = download.getTotalSize() == 0 ? responseBody.contentLength() : download.getCurrentSize() + responseBody.contentLength();

                MappedByteBuffer mappedBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE, download.getCurrentSize(), allLength - download.getCurrentSize());

                byte[] buffer = new byte[1024 * 4];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    mappedBuffer.put(buffer, 0, length);
                }
            } catch (IOException e) {
                throw e;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (channelOut != null) {
                    channelOut.close();
                }
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            }
        } catch (IOException e) {
            throw e;
        }
    }
}
