package com.lovera.diego.restock.common;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class Serializer {

    public static <T> String Serialize(T obj){
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    public static <T> T Deserialize(String json, Type type){
        Gson gson = new Gson();
        return (T)gson.fromJson(json, type);
    }
}
