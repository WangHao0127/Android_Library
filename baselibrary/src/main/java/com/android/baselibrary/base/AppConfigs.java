package com.android.baselibrary.base;

import com.android.baselibrary.BuildConfig;

import okhttp3.logging.HttpLoggingInterceptor.Level;

/**
 * Author: WangHao
 * Created On: 2018/9/19 0019 15:54
 * Description:
 */
public interface AppConfigs {

    boolean enableDebugLog = !BuildConfig.BUILD_TYPE.contains("production");

    Level HTTP_LOG_LEVEL = enableDebugLog ? Level.BODY : Level.NONE;
}
