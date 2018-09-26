package com.android.baselibrary.baseutil;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Author: WangHao
 * Created On: 2018/9/26 0026 11:18
 * Description:
 */
public class NetUtils {

    /**
     * 判断网络是否连接
     * @param context
     * @return
     */
    public static boolean isConnected(Context context){
        ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(null!=connectivityManager){
            NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
            if (null!=networkInfo&&networkInfo.isConnected()){
                if (networkInfo.getState()==NetworkInfo.State.CONNECTED){
                    return true;
                }
            }
        }
        return false;
    }
}
