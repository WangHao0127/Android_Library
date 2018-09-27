package com.android.baselibrary.base;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.android.baselibrary.BuildConfig;
import com.android.baselibrary.baseutil.LoggerUtil;

/**
 * Author: WangHao
 * Created On: 2018/9/18 0018 16:58
 * Description:
 */
public class BaseLibraryApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        LoggerUtil.init(BuildConfig.DEBUG);
    }
}
