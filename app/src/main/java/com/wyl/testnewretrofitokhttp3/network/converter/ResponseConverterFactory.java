package com.wyl.testnewretrofitokhttp3.network.converter;


import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.wyl.testnewretrofitokhttp3.network.util.GsonUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by Zaifeng on 2018/3/1.
 * 提供ResponseBody的转换和RequestBody的转换
 */
public final class ResponseConverterFactory extends Converter.Factory {


    public static ResponseConverterFactory create() {
        return create(new Gson());
    }


    public static ResponseConverterFactory create(Gson gson) {
        return new ResponseConverterFactory(gson);
    }

    private final Gson gson;

    private ResponseConverterFactory(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.gson = GsonUtil.gson;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        //返回我们自定义的Gson响应体变换器
        return new GsonResponseBodyConverter<>(gson, type);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = GsonUtil.gson.getAdapter(TypeToken.get(type));
        return new GsonRequestBodyConverter<>(gson, adapter);
    }
}
