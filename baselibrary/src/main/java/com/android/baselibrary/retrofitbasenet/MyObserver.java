package com.android.baselibrary.retrofitbasenet;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

import com.android.baselibrary.R;
import com.android.baselibrary.baseutil.LoggerUtil;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;

/**
 * Author: WangHao
 * Created On: 2018/9/27 0027 16:54
 * Description:
 */
public abstract class MyObserver<T> extends DisposableObserver<T>{

    /**
     * 是否需要显示默认Loading
     */
    private boolean showloading = true;

    private Context context;
    protected Dialog loadingPop;

    public MyObserver() {
    }

    public MyObserver(Context context) {
        this.context = context;
        initLoading();
    }

    private void showLoading() {
        if (showloading && null != loadingPop) {
            loadingPop.show();
        }
    }

    public synchronized void initLoading() {
        if (loadingPop == null) {
            loadingPop = new Dialog(context, R.style.NoTitleDialogStyle);
            loadingPop.setContentView(R.layout.popup_loading);
            loadingPop.setCanceledOnTouchOutside(false);
        }
    }

    public synchronized void hideLoading() {
        if (loadingPop != null) {
            loadingPop.dismiss();
        }
    }

    @Override
    protected void onStart() {
        showLoading();
    }

    @Override
    public void onError(Throwable e) {
        LoggerUtil.i("onError", "请求失败");
        try {

            if (e instanceof SocketTimeoutException) {//请求超时
            } else if (e instanceof ConnectException) {//网络连接超时
                //                mOnSuccessAndFaultListener.onFault("网络连接超时");
            } else if (e instanceof SSLHandshakeException) {//安全证书异常
                //                mOnSuccessAndFaultListener.onFault("安全证书异常");
            } else if (e instanceof HttpException) {//请求的地址不存在
                int code = ((HttpException) e).code();
                if (code == 504) {
                    //                    mOnSuccessAndFaultListener.onFault("网络异常，请检查您的网络状态");
                } else if (code == 404) {
                    //                    mOnSuccessAndFaultListener.onFault("请求的地址不存在");
                } else {
                    //                    mOnSuccessAndFaultListener.onFault("请求失败");
                }
            } else if (e instanceof UnknownHostException) {//域名解析失败
                //                mOnSuccessAndFaultListener.onFault("域名解析失败");
            } else {
                //                mOnSuccessAndFaultListener.onFault("error:" + e.getMessage());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        } finally {
            //            mOnSuccessAndFaultListener.onFault("error:" + e.getMessage());
            hideLoading();
        }
    }

    @Override
    public void onComplete() {
        LoggerUtil.i("onComplete", "请求结束");
        hideLoading();
    }

    /**
     * 取消对observable的订阅，同时也取消了http请求
     */
    final public void disposed() {
        if (!this.isDisposed()) {
            this.dispose();
        }
    }
}
