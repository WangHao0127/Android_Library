package com.android.library.home.presenter;

import android.content.Context;

import com.android.baselibrary.baseui.BasePresenter;
import com.android.library.entity.WeatherData;
import com.android.library.home.contract.WeatherContract;
import com.android.library.home.model.WeatherModelImpl;

/**
 * Author: WangHao
 * Created On: 2019/2/26 0026 15:36
 * Description:
 */
public class WeatherPresenter extends BasePresenter<WeatherContract.WeatherView> {

    private WeatherModelImpl mModel;

    public WeatherPresenter() {
        mModel = new WeatherModelImpl();
    }

    public void getWeatherInfo(Context mContext) {
        mModel.Weather(mContext, new WeatherContract.WeatherCallBack() {
            @Override
            public void onSuccess(WeatherData login) {
                getView().onGetSuccess(login);
            }

            @Override
            public void onFail(String errorInfo) {
                getView().onGetFail(errorInfo);
            }
        });
    }
}
