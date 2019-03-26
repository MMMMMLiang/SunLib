package com.sun.lib.net.mvp.delegate;

import android.app.Activity;
import android.os.Bundle;

import com.sun.lib.net.mvp.root.IMvpPresenter;
import com.sun.lib.net.mvp.root.IMvpView;


/**
 * activity生命周期
 * @param <V>
 * @param <P>
 */
public interface ActivityMvpDelegate<V extends IMvpView, P extends IMvpPresenter<V>> {

    /**
     * 此方法必须在activity的onCreate方法调用
     * {@link Activity#onCreate(Bundle)}
     * 此方法在内部创建Presenter并将View附加到Presenter
     * @param bundle bundle
     */
    void onCreate(Bundle bundle);

    /**
     * 此方法必须在activity的onDestroy方法调用
     * {@link Activity#onDestroy()}
     * 此方法在内部从Presenter分离View
     */
    void onDestroy();

    /**
     * 此方法必须在activity的onPause方法调用
     * {@link Activity#onPause()}
     */
    void onPause();

    /**
     * 此方法必须在activity的onResume方法调用
     * {@link Activity#onResume()}
     */
    void onResume();

    /**
     * 此方法必须在activity的onStart方法调用
     * {@link Activity#onStart()}
     */
    void onStart();

    /**
     * 此方法必须在activity的onStop方法调用
     * {@link Activity#onStop()}
     */
    void onStop();

    /**
     * 此方法必须在activity的onRestart方法调用
     * {@link Activity#onRestart()}
     */
    void onRestart();

    /**
     * 此方法必须在activity的onContentChanged方法调用
     * {@link Activity#onContentChanged()}
     */
    void onContentChanged();

    /**
     * 此方法必须在activity的onSaveInstanceState方法调用
     * {@link Activity#onSaveInstanceState(Bundle)}
     * @param outState 状态
     */
    void onSaveInstanceState(Bundle outState);

    /**
     * 此方法必须在activity的onPostCreate方法调用
     * {@link Activity#onPostCreate(Bundle)}
     * @param savedInstanceState 状态
     */
    void onPostCreate(Bundle savedInstanceState);

}
