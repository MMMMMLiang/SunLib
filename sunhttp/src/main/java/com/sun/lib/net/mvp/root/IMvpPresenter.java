package com.sun.lib.net.mvp.root;


import androidx.annotation.NonNull;
import androidx.annotation.UiThread;

/**
 * 根Presenter
 * @param <V>
 */
public interface IMvpPresenter<V extends IMvpView> {

    /**
     * 将view添加到当前Presenter
     * @param view 视图
     */
    @UiThread
    void attachView(@NonNull V view);

    /**
     * 从Presenter移除View
     */
    @UiThread
    void detachView();

    /**
     * 销毁View实例
     */
    @UiThread
    void destroy();
}
