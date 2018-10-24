package com.android.baselibrary.base;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.android.baselibrary.BuildConfig;
import com.android.baselibrary.baseutil.LoggerUtil;
import com.android.baselibrary.baseutil.MyCrashUtils;
import com.android.baselibrary.retrofitbasenet.OKHttpUpdateHttpService;

import com.blankj.utilcode.util.ToastUtils;
import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate.entity.UpdateError;
import com.xuexiang.xupdate.listener.OnUpdateFailureListener;
import com.xuexiang.xupdate.utils.UpdateUtils;

/**
 * Author: WangHao
 * Created On: 2018/9/18 0018 16:58
 * Description:
 */
public class BaseLibraryApplication extends MultiDexApplication {

    public static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        appContext = this;
        MultiDex.install(this);
        LoggerUtil.init(BuildConfig.DEBUG);
        //=== 崩溃日志相关
        MyCrashUtils.getInstance().init(appContext);

        XUpdate.get()
            .isWifiOnly(true)     //默认设置只在wifi下检查版本更新
            .isGet(true)          //默认设置使用get请求检查版本
            .isAutoMode(false)    //默认设置非自动模式，可根据具体使用配置
            .param("versionCode", UpdateUtils.getVersionCode(this)) //设置默认公共请求参数
            .param("appKey", getPackageName())
            //                .debug(true)
            .setOnUpdateFailureListener(new OnUpdateFailureListener() { //设置版本更新出错的监听
                @Override
                public void onFailure(UpdateError error) {
                    ToastUtils.showShort(error.toString());
                }
            })
            .setIUpdateHttpService(new OKHttpUpdateHttpService()) //这个必须设置！实现网络请求功能。
            .init(this);   //这个必须初始化
    }

}
