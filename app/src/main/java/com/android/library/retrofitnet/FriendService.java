package com.android.library.retrofitnet;

import com.android.baselibrary.base.AppConfigs;
import com.android.baselibrary.basenet.BaseRetrofitServiceFactory;
import com.android.library.BuildConfig;
import com.android.library.WeatherData;

import io.reactivex.Observable;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.http.GET;

/**
 * Author: WangHao
 * Created On: 2018/9/20 0020 14:21
 * Description:
 */
public interface FriendService {

    @GET("101010100.html")
    Observable<WeatherData> getWeather();

    class Factory extends BaseRetrofitServiceFactory {

        private static FriendService mFriendService;

        public synchronized static FriendService getFriendService() {
            if (mFriendService == null) {
                mFriendService = new Factory().getBuilder().build().create(FriendService.class);
            }
            return mFriendService;
        }

        @Override
        protected HttpLoggingInterceptor.Level getLogLevel() {
            return AppConfigs.HTTP_LOG_LEVEL;
        }

        @Override
        protected String getEndpoint() {
            return BuildConfig.BASE_URL;
        }
    }
}
