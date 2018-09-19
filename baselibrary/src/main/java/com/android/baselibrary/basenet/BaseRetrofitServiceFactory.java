package com.android.baselibrary.basenet;

import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author: WangHao
 * Created On: 2018/9/19 0019 15:38
 * Description:
 */
public abstract class BaseRetrofitServiceFactory {

    private static final int DEFAULT_TIMEOUT = 30;

    protected Converter.Factory getNobodyConverterFactory(){
        return NobodyConverterFactory.create();
    }

    protected Converter.Factory getConverterFactory() {
        Gson gson = new GsonBuilder().serializeNulls().create();
        return GsonConverterFactory.create(gson);
    }


    public Retrofit.Builder getBuilder() {
        // Setup http log
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(getLogLevel());
        OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new CustomInterceptor())
            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .build();

        Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(getEndpoint())
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        Converter.Factory nobodyConverterFactory = getNobodyConverterFactory();
        if (nobodyConverterFactory!=null){
            builder.addConverterFactory(nobodyConverterFactory);
        }

        Converter.Factory converterFactory = getConverterFactory();
        if (converterFactory != null) {
            builder.addConverterFactory(converterFactory);
        }

        return builder;
    }

    protected abstract HttpLoggingInterceptor.Level getLogLevel();

    protected abstract String getEndpoint();
}
