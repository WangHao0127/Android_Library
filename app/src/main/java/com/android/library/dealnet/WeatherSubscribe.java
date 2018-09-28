package com.android.library.dealnet;

import com.android.library.WeatherData;
import com.android.library.basenet.HttpMethods;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;

/**
 * Author: WangHao
 * Created On: 2018/9/28 0028 10:53
 * Description: 把功能模块来分别存放不同的请求方法，比如登录注册类LoginSubscribe、电影类MovieSubscribe
 */
public class WeatherSubscribe {

    /**
     * 获取天气数据数据
     */
    public static void getData(DisposableObserver<WeatherData> subscriber) {
        Observable<WeatherData> observable =  HttpMethods.getInstance().getHttpApi().getWeather();
        HttpMethods.getInstance().toSubscribe(observable, subscriber);
    }




}
