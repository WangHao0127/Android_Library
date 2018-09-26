package com.android.baselibrary.basenet;

import com.android.baselibrary.base.HttpLogger;

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

    private static final int DEFAULT_TIMEOUT = 10;
    private static final int RW_TIMEOUT = 20;

    protected Converter.Factory getNobodyConverterFactory(){
        return NobodyConverterFactory.create();
    }

    protected Converter.Factory getConverterFactory() {
        Gson gson = new GsonBuilder().serializeNulls().create();
        return GsonConverterFactory.create(gson);

    }

    public Retrofit.Builder getBuilder() {
        // Setup http log
        OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new CustomInterceptor())
            .addInterceptor(new HttpLoggingInterceptor(new HttpLogger()).setLevel(getLogLevel()))
            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(RW_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(RW_TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
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
