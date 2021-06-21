package com.wyl.testnewretrofitokhttp3.network.util;

import com.example.myapplication.network.converter.GsonDateConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

public class GsonUtil {

    public static Gson gson = new GsonBuilder().serializeNulls().registerTypeAdapter(Date.class, new GsonDateConverter()).create();
    public static Gson gsonIgnoreNull = new GsonBuilder().registerTypeAdapter(Date.class, new GsonDateConverter()).create();
}
