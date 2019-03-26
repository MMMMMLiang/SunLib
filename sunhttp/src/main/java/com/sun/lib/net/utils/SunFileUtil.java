package com.sun.lib.net.utils;

import android.text.TextUtils;

import java.io.File;

/**
 * 文件相关工具类
 */
public class SunFileUtil {

    /**
     * 计算进度值
     * @param current 当前进度
     * @param total 总进度
     * @return 百分比
     */
    public static float getProgress(long current, long total) {
        return (float) current / (float) total;
    }

    /**
     * 判断路径下的文件是否存在
     * @param fileUrl 文件路径
     * @return 是否
     */
    public static boolean isFileExists(String fileUrl) {
        boolean flag = false;
        File file = new File(fileUrl);
        if (file.exists() && file.isFile()) {
            flag = true;
        }
        return flag;
    }

    /**
     * 根据文件路径，删除文件
     * @param filePath 文件路径
     * @return 是否删除
     */
    public static boolean deleteFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) return false;

        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + filePath + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + filePath + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + filePath + "不存在！");
            return false;
        }
    }
}
