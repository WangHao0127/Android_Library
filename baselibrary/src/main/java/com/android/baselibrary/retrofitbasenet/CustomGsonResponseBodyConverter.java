package com.android.baselibrary.retrofitbasenet;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Author: WangHao
 * Created On: 2018/9/28 0028 14:08
 * Description:
 */
final class CustomGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    CustomGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        JsonReader jsonReader = gson.newJsonReader(value.charStream());
        jsonReader.setLenient(true);
        try {
            return adapter.read(jsonReader);
        } finally {
            value.close();
        }
    }
}