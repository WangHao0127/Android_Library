package com.android.library.home.model;

import android.content.Context;

import com.android.baselibrary.retrofitbasenet.MyObserver;
import com.android.library.dealnet.WeatherSubscribe;
import com.android.library.entity.WeatherData;
import com.android.library.home.contract.WeatherContract;

/**
 * Author: WangHao
 * Created On: 2019/2/26 0026 15:32
 * Description:
 */
public class WeatherModelImpl implements WeatherContract.WeatherModel {

    @Override
    public void Weather(Context mContext, WeatherContract.WeatherCallBack callBack) {
        WeatherSubscribe.getData(new MyObserver<WeatherData>(mContext) {

            @Override
            public void onNext(WeatherData weatherData) {
                callBack.onSuccess(weatherData);
            }

            @Override
            public void onError(Throwable e) {
                callBack.onFail(e.toString());
            }
        });
    }
}
