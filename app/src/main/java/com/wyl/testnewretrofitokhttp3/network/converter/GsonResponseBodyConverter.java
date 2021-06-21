package com.wyl.testnewretrofitokhttp3.network.converter;

import com.google.gson.Gson;
import com.wyl.testnewretrofitokhttp3.network.util.GsonUtil;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * 自定义GsonResponseBodyConverter
 */

public class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final Type type;


    public GsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = GsonUtil.gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {

        String response = value.string();
//        //先将返回的json数据解析到Response中，如果code==Constant.SUCCESS，则解析到我们的实体基类中，否则抛异常
//        Response httpResult = GsonUtil.gson.fromJson(response, Response.class);
//        if (httpResult.getCode() == Constant.SUCCESS) {
            //Constant.SUCCESS的时候就直接解析，不可能出现解析异常。因为我们实体基类中传入的泛型，就是数据成功时候的格式
            return GsonUtil.gson.fromJson(response, type);
//        } else {
//            ErrorResponse errorResponse = GsonUtil.gson.fromJson(response, ErrorResponse.class);
//            //抛一个自定义ResultException
//            throw new ResultException(errorResponse.getCode(), errorResponse.getMsg());
//        }
    }
}
