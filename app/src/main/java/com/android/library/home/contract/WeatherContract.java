package com.android.library.home.contract;

import android.content.Context;

import com.android.library.entity.WeatherData;

/**
 * Author: WangHao
 * Created On: 2019/2/26 0026 15:28
 * Description:契约类
 */
public class WeatherContract {

    public interface WeatherView {
        void onGetSuccess(WeatherData weatherData);

        void onGetFail(String errorInfo);
    }

    public interface WeatherModel {
        void Weather(Context mContext,WeatherCallBack callBack);
    }

    public interface WeatherCallBack {
        void onSuccess(WeatherData login);

        void onFail(String errorInfo);
    }
}
