package com.android.baselibrary.basenet;




import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

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

        Request.Builder builder = chain.request().newBuilder();
        if (headers != null && headers.size() > 0) {
            Set<String> keys = headers.keySet();
            for (String headerKey : keys) {
                builder.addHeader(headerKey, headers.get(headerKey)).build();
            }
        }

        //token失效，重新登录
        Response response = chain.proceed(builder.build());
        if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {//HTTP Status Code(http状态码) 401
        }
        return response;

    }
}
