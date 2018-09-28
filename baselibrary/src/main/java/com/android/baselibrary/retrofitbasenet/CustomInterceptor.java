package com.android.baselibrary.retrofitbasenet;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Mark on 2017/4/6.
 */
public class CustomInterceptor implements Interceptor {

    private Map<String, String> headers;

    public CustomInterceptor(Map<String, String> headers) {
        this.headers = headers;
    }

    public CustomInterceptor() {

    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        MediaType mediaType = response.body().contentType();
        String content= response.body().string();
        return response.newBuilder()
            .body(ResponseBody.create(mediaType, content))
            .build();


       /* Request originalRequest = chain.request();
        Request.Builder requestBuilder = originalRequest.newBuilder()
            .addHeader("Accept-Encoding", "gzip")
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .method(originalRequest.method(), originalRequest.body());
        //                requestBuilder.addHeader("Authorization", "Bearer " + BaseConstant
        // .TOKEN);//添加请求头信息，服务器进行token有效性验证
        Request request = requestBuilder.build();
        return chain.proceed(request);*/

        /*Request.Builder builder = chain.request().newBuilder();
        if (headers != null && headers.size() > 0) {
            Set<String> keys = headers.keySet();
            for (String headerKey : keys) {
                builder.addHeader(headerKey, headers.get(headerKey)).build();
            }
        }
        //token失效，重新登录
//        Response response = chain.proceed(builder.build());
//        if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {//HTTP Status Code
(http状态码) 401
//        }
        return chain.proceed(builder.build());*/

    }
}
