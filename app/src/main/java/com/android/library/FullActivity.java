package com.android.library;

import com.android.baselibrary.basedata.EventBusData;
import com.android.baselibrary.baseui.BaseActivity;
import com.android.baselibrary.baseutil.LoggerUtil;
import com.android.library.retrofitnet.FriendService;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class FullActivity extends BaseActivity {

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_full;
    }

    @Override
    protected void initViewsAndEvents() {

        eventBusPost(EventBusData.Action.DELETE_ALL_MESSAGE_IN_SESSION.createEventBusData("宝宝快点好"));

        FriendService.Factory.getFriendService().getWeather().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new DisposableObserver<WeatherData>() {
                @Override
                public void onNext(WeatherData weatherData) {
                    showToast(weatherData.getWeatherinfo().getCity());
                }

                @Override
                public void onError(Throwable e) {
                    LoggerUtil.d(e.toString());
                }

                @Override
                public void onComplete() {

                }
            });
    }

}
