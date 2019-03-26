package com.sun.lib.net.mvp.delegate;


import com.sun.lib.net.mvp.root.IMvpPresenter;
import com.sun.lib.net.mvp.root.IMvpView;

/**
 * V/P 媒介
 * @param <V>
 * @param <P>
 */
public interface MvpDelegateCallback<V extends IMvpView, P extends IMvpPresenter<V>> {

    /**
     * 获取presenter
     * @return presenter[]
     */
    P[] getPresenter();

    /**
     * 获取presenter的MVPView
     *
     * @return 与presenter关联的View
     */
    V[] getMvpView();
}
