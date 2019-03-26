package com.sun.lib.net.mvp.delegate;

import android.app.Activity;
import android.os.Bundle;

import com.sun.lib.net.mvp.root.IMvpPresenter;
import com.sun.lib.net.mvp.root.IMvpView;


public class ActivityMvpDelegateImpl<V extends IMvpView, P extends IMvpPresenter<V>> implements ActivityMvpDelegate {
    protected Activity activity;

    private MvpDelegateCallback<V, P> delegateCallback;

    public ActivityMvpDelegateImpl(Activity activity, MvpDelegateCallback<V, P> delegateCallback) {
        if (activity == null) {
            throw new NullPointerException("警告！！！ Activity is null!");
        }
        if (delegateCallback == null) {
            throw new NullPointerException("警告！！！MvpDelegateCallback is null!");
        }
        this.activity = activity;
        this.delegateCallback = delegateCallback;
    }

    /**
     * 是否保留V&P实例
     * isChangingConfigurations()检测当前的Activity是否因为Configuration的改变被销毁了
     *
     * @param activity 视图
     * @return true: 保留   false：不保留
     */
    private static boolean isRetainVPInstance(Activity activity) {
        return activity.isChangingConfigurations() || !activity.isFinishing();
    }

    @Override
    public void onCreate(Bundle bundle) {
        /*针对有多个presenter情况下，循环关联View*/
        P[] pArray = delegateCallback.getPresenter();
        if (pArray != null && pArray.length > 0) {
            V[] vArray = delegateCallback.getMvpView();
            P presenter;
            V view;
            for (int i = 0; i < pArray.length; i++) {
                presenter = pArray[i];
                view = vArray[i];
                if (presenter != null && view != null) {
                    //关联view
                    presenter.attachView(view);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        P[] pArray = delegateCallback.getPresenter();
        if (pArray != null) {
            for (P item : pArray) {
                if (item != null) {
                    //解除View
                    item.detachView();
                    if (!isRetainVPInstance(activity)) {
                        //销毁 V & P 实例
                        item.destroy();
                    }
                }
            }
        }
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onContentChanged() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {

    }
}
