package com.android.baselibrary.base;

import com.android.baselibrary.baseutil.JsonUtil;
import com.android.baselibrary.baseutil.LoggerUtil;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Author: WangHao
 * Created On: 2018/9/26 0026 14:04
 * Description:
 */
public class HttpLogger implements HttpLoggingInterceptor.Logger{

    private StringBuilder mMessage = new StringBuilder();

    @Override
    public void log(String message) {
        // 请求或者响应开始
        if (message.startsWith("--> POST")) {
            mMessage.setLength(0);
        }
        // 以{}或者[]形式的说明是响应结果的json数据，需要进行格式化
        if ((message.startsWith("{") && message.endsWith("}"))
            || (message.startsWith("[") && message.endsWith("]"))) {
            message = JsonUtil.formatJson(JsonUtil.decodeUnicode(message));
        }
        mMessage.append(message.concat("\n"));
        // 响应结束，打印整条日志
        if (message.startsWith("<-- END HTTP")) {
            LoggerUtil.d(mMessage.toString());
        }
    }
}
