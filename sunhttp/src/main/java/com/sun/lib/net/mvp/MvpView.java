package com.sun.lib.net.mvp;



import com.sun.lib.net.mvp.root.IMvpView;

import androidx.annotation.UiThread;

/**
 * 基础View接口
 * 备注:loading/data/error
 * 1. lde 思想: 页面通用  加载中/展示数据/错误处理
 * 2. action 方式: 考虑多个请求时 根据 action 区分处理
 */
public interface MvpView extends IMvpView {

    /**
     * 加载中
     * @param action 标示
     * @param show 是否显示
     */
    @UiThread
    void mvpLoading(String action, boolean show);

    /**
     * 成功获取数据
     * @param action 标示
     * @param data 数据
     * @param <M> 数据类型
     */
    @UiThread
    <M> void mvpData(String action, M data);

    /**
     * 错误处理
     * @param action 标示
     * @param code 错误码
     * @param msg 错误信息
     */
    @UiThread
    void mvpError(String action, int code, String msg);
}
