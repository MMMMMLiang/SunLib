package com.sun.lib.net.utils;

import android.os.Looper;

public class SunThreadUtil {

    /**
     * 是否主线程
     * @return 是：主线程  否：非主线程
     */
    public static boolean isMainThread() {
        return Looper.getMainLooper().getThread().getId() == Thread.currentThread().getId();
    }
}
