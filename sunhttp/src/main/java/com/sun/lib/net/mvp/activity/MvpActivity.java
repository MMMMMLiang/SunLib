package com.sun.lib.net.mvp.activity;


import android.os.Bundle;

import com.sun.lib.net.mvp.delegate.ActivityMvpDelegate;
import com.sun.lib.net.mvp.delegate.ActivityMvpDelegateImpl;
import com.sun.lib.net.mvp.delegate.MvpDelegateCallback;
import com.sun.lib.net.mvp.root.IMvpPresenter;
import com.sun.lib.net.mvp.root.IMvpView;
import com.trello.rxlifecycle3.components.RxActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * MVPActivity
 * 备注:
 * 1.XXActivity 继承 MvpActivity,当页面存在 Presenter 时，具体 Activity 需要调用 setPresenter(P... presenter)
 * 2.由于此框架集合了 RxLifecycle 因此本 Activity 继承自 RxActivity (开发者也可以直接继承 Activity)
 * 3.支持一个 Activity 存在多个 Presenter
 * @param <V>
 * @param <P>
 */
public abstract class MvpActivity<V extends IMvpView, P extends IMvpPresenter<V>> extends RxActivity implements IMvpView, MvpDelegateCallback<V, P> {

    protected ActivityMvpDelegate mvpDelegate;

    protected abstract P[] getPresenterArray();

    @Override
    public P[] getPresenter() {
        return getPresenterArray();
    }

    @Override
    public V[] getMvpView() {
        V[] view = null;
        P[] pArray = getPresenter();
        if (pArray != null) {
            view = (V[]) new IMvpView[pArray.length];
            for (int i = 0; i < pArray.length; i++) {
                view[i] = (V) this;
            }
        }
        return view;
    }

    @NonNull
    public ActivityMvpDelegate getMvpDelegate() {
        if (mvpDelegate == null) {
            mvpDelegate = new ActivityMvpDelegateImpl(this, this);
        }
        return mvpDelegate;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMvpDelegate().onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getMvpDelegate().onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getMvpDelegate().onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getMvpDelegate().onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMvpDelegate().onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getMvpDelegate().onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        getMvpDelegate().onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getMvpDelegate().onRestart();
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        getMvpDelegate().onContentChanged();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getMvpDelegate().onPostCreate(savedInstanceState);
    }
}
