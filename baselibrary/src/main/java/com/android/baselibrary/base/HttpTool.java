package com.android.baselibrary.base;

import android.app.Activity;

import java.lang.ref.WeakReference;
import java.util.Map;

import okhttp3.OkHttpClient;

/**
 * Author: WangHao
 * Created On: 2018/9/19 0019 11:22
 * Description:
 */
public class HttpTool {

    private static final int DEFAULT_TIMEOUT = 30;

    private WeakReference<Activity> mWeakReference;

    private OkHttpClient okHttpClient;
    private static Map headers = null;

    public static HttpTool newInstance(Activity context){
        HttpTool httpTool = new HttpTool(context);
        return  httpTool;
    }

    private HttpTool(Activity context){
        mWeakReference = new WeakReference<Activity>(context);


    }

    public void clear(){
        mWeakReference.clear();
    }


}
