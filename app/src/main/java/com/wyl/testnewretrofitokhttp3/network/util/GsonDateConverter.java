package com.wyl.testnewretrofitokhttp3.network.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class GsonDateConverter implements JsonSerializer<Date>, JsonDeserializer<Date> {
    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);

    @Override
    public JsonElement serialize(Date arg0, Type arg1, JsonSerializationContext arg2) {
        return new JsonPrimitive(sdf.format(arg0));
    }

    @Override
    public Date deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
        Date date = null;
        try {
            date = sdf.parse(arg0.getAsJsonPrimitive().getAsString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;

    }
}