package com.android.library.basenet;

import com.android.baselibrary.base.BaseLibraryApplication;
import com.android.baselibrary.base.HttpLogger;
import com.android.baselibrary.baseutil.NetUtils;
import com.android.baselibrary.retrofitbasenet.CustomGsonConverterFactory;
import com.android.baselibrary.retrofitbasenet.CustomInterceptor;
import com.android.baselibrary.retrofitbasenet.NobodyConverterFactory;
import com.android.library.AppConfigs;
import com.android.library.BuildConfig;
import com.android.library.dealnet.HttpApi;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author: WangHao
 * Created On: 2018/9/28 0028 10:19
 * Description:
 */
public class HttpMethods {
    public static final String CACHE_NAME = "Android_Library";

    private static final int DEFAULT_CONNECT_TIMEOUT = 30;
    private static final int DEFAULT_WRITE_TIMEOUT = 30;
    private static final int DEFAULT_READ_TIMEOUT = 30;

    /**
     * 请求失败重连次数
     */
    private int RETRY_COUNT = 0;

    private OkHttpClient.Builder okHttpBuilder;
    private Retrofit retrofit;
    private HttpApi httpApi;

    private HttpMethods(){
        okHttpBuilder = new OkHttpClient.Builder();
        /**
         * 设置缓存
         */
        File
            cacheFile = new File(BaseLibraryApplication.appContext.getExternalCacheDir(), CACHE_NAME);
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!NetUtils.isNetworkConnected()) {
                    request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
                }
                Response response = chain.proceed(request);
                if (!NetUtils.isNetworkConnected()) {
                    int maxAge = 0;
                    // 有网络时 设置缓存超时时间0个小时
                    response.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .removeHeader(CACHE_NAME)// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                        .build();
                } else {
                    // 无网络时，设置超时为4周
                    int maxStale = 60 * 60 * 24 * 28;
                    response.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .removeHeader(CACHE_NAME)
                        .build();
                }
                return response;
            }
        };
        okHttpBuilder.cache(cache).addInterceptor(cacheInterceptor);

        /**
         * 设置头信息
         */
        okHttpBuilder.addInterceptor(new CustomInterceptor());
        //设置 Debug Log 模式
        okHttpBuilder .addInterceptor(new HttpLoggingInterceptor(new HttpLogger()).setLevel(AppConfigs.HTTP_LOG_LEVEL));
        /**
         * 设置超时和重新连接
         */
        okHttpBuilder.connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS);
        okHttpBuilder.readTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS);
        okHttpBuilder.writeTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS);
        //错误重连
        okHttpBuilder.retryOnConnectionFailure(true);

        retrofit = new Retrofit.Builder()
            .client(okHttpBuilder.build())
            .addConverterFactory(getNobodyConverterFactory())//json转换成JavaBean
            .addConverterFactory(getConverterFactory())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .build();

        httpApi = retrofit.create(HttpApi.class);
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final HttpMethods INSTANCE = new HttpMethods();

    }

    //获取单例
    public static HttpMethods getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 获取retrofit
     *
     * @return
     */
    public Retrofit getRetrofit() {
        return retrofit;
    }


    public void changeBaseUrl(String baseUrl) {
        retrofit = new Retrofit.Builder()
            .client(okHttpBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(baseUrl)
            .build();
        httpApi = retrofit.create(HttpApi.class);
    }

    /**
     * 获取httpService
     *
     * @return
     */
    public HttpApi getHttpApi() {
        return httpApi;
    }

    /**
     * 设置订阅 和 所在的线程环境
     */
    public <T> void toSubscribe(Observable<T> o, DisposableObserver<T> s) {

        o.subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry(RETRY_COUNT)//请求失败重连次数
            .subscribe(s);

    }

    private Converter.Factory getNobodyConverterFactory() {
        return NobodyConverterFactory.create();
    }

    private Converter.Factory getConverterFactory() {
        Gson gson = new GsonBuilder().serializeNulls().create();
        return CustomGsonConverterFactory.create(gson);

    }
}
