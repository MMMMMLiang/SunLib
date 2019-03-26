package com.sun.lib.net.cancel;

import io.reactivex.disposables.Disposable;

public interface RequestManager<T> {
    /**
     * 添加
     *
     * @param tag 标签
     * @param disposable 请求
     */
    void add(T tag, Disposable disposable);

    /**
     * 移除
     *
     * @param tag 标签
     */
    void remove(T tag);

    /**
     * 取消
     *
     * @param tag 标签
     */
    void cancel(T tag);

    /**
     * 取消全部
     */
    void cancelAll();
}
