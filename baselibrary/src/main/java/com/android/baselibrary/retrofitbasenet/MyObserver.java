package com.android.baselibrary.retrofitbasenet;

import com.android.baselibrary.baseutil.LoggerUtil;

import io.reactivex.observers.DisposableObserver;

/**
 * Author: WangHao
 * Created On: 2018/9/27 0027 16:54
 * Description:
 */
public abstract class MyObserver<T> extends DisposableObserver<T> {

    @Override
    protected void onStart() {
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {
        LoggerUtil.i("onComplete","请求结束");
    }

}
