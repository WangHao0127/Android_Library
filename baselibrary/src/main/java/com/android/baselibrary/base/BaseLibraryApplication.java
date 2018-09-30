package com.android.baselibrary.base;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.android.baselibrary.BuildConfig;
import com.android.baselibrary.baseutil.LoggerUtil;
import com.android.baselibrary.baseutil.MyCrashUtils;

import io.realm.Realm;
import io.realm.RealmConfiguration;

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

        Realm.init(this);
        setRealmConfig();
    }

    private void setRealmConfig() {
        RealmConfiguration config = new RealmConfiguration
            .Builder()
            .name("AndroidLibrary.realm")
            .deleteRealmIfMigrationNeeded()
            .schemaVersion(1)
            .build();
        Realm.setDefaultConfiguration(config);
    }
}
