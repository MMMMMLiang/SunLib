package com.sun.lib.net.mvp;



import com.sun.lib.net.mvp.root.IMvpPresenter;
import com.sun.lib.net.mvp.root.IMvpView;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;


public class MvpPresenter<V extends IMvpView> implements IMvpPresenter<V> {
    /*View弱引用*/
    private WeakReference<V> viewRef;

    /**
     * 获取View
     * @return view
     */
    @UiThread
    public V getView() {
        return viewRef == null ? null : viewRef.get();
    }

    /**
     * 判断view是否添加
     * @return true：添加
     */
    @UiThread
    public boolean isViewAttached() {
        return viewRef != null && viewRef.get() != null;
    }

    /**
     * 绑定
     * @param view 视图
     */
    @Override
    public void attachView(@NonNull V view) {
        viewRef = new WeakReference<>(view);
    }

    /**
     * 移除
     */
    @Override
    public void detachView() {
        if (viewRef != null) {
            viewRef.clear();
            viewRef = null;
        }
    }

    @Override
    public void destroy() {

    }
}
