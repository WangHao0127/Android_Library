package com.android.library.dealnet;

import com.android.library.WeatherData;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;

/**
 * Author: WangHao
 * Created On: 2018/9/28 0028 10:53
 * Description:
 */
public interface HttpApi {

    //请填写自己的接口名
    @GET("101010100.html")
    Observable<WeatherData> getWeather();
}
