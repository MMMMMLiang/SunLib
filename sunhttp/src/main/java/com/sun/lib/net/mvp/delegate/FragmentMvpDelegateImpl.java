package com.sun.lib.net.mvp.delegate;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.sun.lib.net.mvp.root.IMvpPresenter;
import com.sun.lib.net.mvp.root.IMvpView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class FragmentMvpDelegateImpl<V extends IMvpView, P extends IMvpPresenter<V>> implements FragmentMvpDelegate {
    private Fragment fragment;

    private MvpDelegateCallback<V, P> callback;

    public FragmentMvpDelegateImpl(Fragment fragment, MvpDelegateCallback<V, P> callback) {
        if (fragment == null) {
            throw new NullPointerException("警告！！！ Activity is null!");
        }
        if (callback == null) {
            throw new NullPointerException("警告！！！MvpDelegateCallback is null!");
        }
        this.fragment = fragment;
        this.callback = callback;
    }

    /**
     * 是否保留V&P实例
     * isChangingConfigurations()检测当前的Activity是否因为Configuration的改变被销毁了
     *
     * @param activity 视图
     * @return true: 保留   false：不保留
     */
    private static boolean isRetainVPInstance(Activity activity, Fragment fragment) {
        if (activity.isChangingConfigurations()) {
            return false;
        }
        if (activity.isFinishing()) {
            return false;
        }
        return !fragment.isRemoving();
    }

    /**
     * 获取Activity
     *
     * @return activity
     */
    private Activity getActivity() {
        Activity activity = fragment.getActivity();
        if (activity == null) {
            throw new NullPointerException("警告！！！Activity returned by Fragment.getActivity() is null. Fragment is " + fragment);
        }
        return activity;
    }

    @Override
    public void onCreate(Bundle saved) {

    }

    @Override
    public void onDestroy() {
        Activity activity = getActivity();
        P[] pArray = callback.getPresenter();
        if (pArray != null) {
            for (P item : pArray) {
                if (item != null) {
                    if (!isRetainVPInstance(activity, fragment)) {
                        //销毁 V & P 实例
                        item.destroy();
                    }
                }
            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        P[] pArray = callback.getPresenter();
        if (pArray != null) {
            V[] vArray = callback.getMvpView();
            P p;
            V v;
            for (int i = 0; i < pArray.length; i++) {
                p = pArray[i];
                v = vArray[i];
                if (p != null && v != null) {
                    //关联view
                    p.attachView(v);
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        P[] pArray = callback.getPresenter();
        if (pArray != null) {
            for (P item : pArray) {
                if (item != null) {
                    //解除View
                    item.detachView();
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
    public void onActivityCreated(Bundle savedInstanceState) {

    }

    @Override
    public void onAttach(Activity activity) {

    }

    @Override
    public void onDetach() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }
}
