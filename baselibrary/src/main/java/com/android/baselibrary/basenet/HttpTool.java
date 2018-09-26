package com.android.baselibrary.basenet;

import android.app.Activity;

import java.lang.ref.WeakReference;

/**
 * Author: WangHao
 * Created On: 2018/9/19 0019 11:22
 * Description:
 */
public class HttpTool {

    private WeakReference<Activity> mWeakReference;

    public static HttpTool newInstance(Activity context){
        HttpTool httpTool = new HttpTool(context);
        return  httpTool;
    }

    private HttpTool(Activity context){
        mWeakReference = new WeakReference<>(context);

    }

    public void clear(){
        mWeakReference.clear();
    }

}
