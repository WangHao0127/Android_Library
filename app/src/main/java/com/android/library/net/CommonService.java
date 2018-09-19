package com.android.library.net;

import com.android.baselibrary.netbase.BaseRetrofitServiceFactory;
import com.android.library.BuildConfig;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import static com.android.baselibrary.base.AppConfigs.HTTP_LOG_LEVEL;

/**
 * Author: WangHao
 * Created On: 2018/9/19 0019 11:22
 * Description:
 */
public interface CommonService {

    @POST("")
    @FormUrlEncoded
    Observable<String> getWeather(@FieldMap Map<String, Object> params);

    class Factory extends BaseRetrofitServiceFactory {

        private static CommonService service = null;

        public static synchronized CommonService getService() {
            if (service == null) {
                service = new Factory().getBuilder().build().create(CommonService.class);
            }
            return service;
        }

        @Override
        protected HttpLoggingInterceptor.Level getLogLevel() {
            return HTTP_LOG_LEVEL;
        }

        @Override
        protected String getEndpoint() {
            return BuildConfig.BASE_URL;
        }
    }
}
