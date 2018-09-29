package com.android.library;

import com.android.baselibrary.basedata.EventBusData;
import com.android.baselibrary.baseui.BaseActivity;
import com.android.baselibrary.retrofitbasenet.MyObserver;
import com.android.library.dealnet.WeatherSubscribe;

public class FullActivity extends BaseActivity {

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_full;
    }

    @Override
    protected void initViewsAndEvents() {

        eventBusPost(EventBusData.Action.DELETE_ALL_MESSAGE_IN_SESSION.createEventBusData("YES"));

        WeatherSubscribe.getData(new MyObserver<WeatherData>(this) {

            @Override
            public void onNext(WeatherData weatherData) {
                showToast(weatherData.getWeatherinfo().getCity());
            }
        });

      /*  FriendService
            .Factory
            .getFriendService()
            .getWeather()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new MyObserver<WeatherData>() {
                @Override
                public void onNext(WeatherData weatherData) {
                    showToast(weatherData.getWeatherinfo().getCity());
                }
            });*/

    }
}
