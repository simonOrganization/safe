package com.lchtime.safetyexpress.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yxn on 2017/3/31.
 */

public class JsonUtils {
    private static Gson gson = new Gson();

    /****
     * 将json字符串转化成实体对象
     * @param json
     * @param classOfT
     * @return
     */
    public static Object stringToObject(String json,Class classOfT){
        return gson.fromJson(json,classOfT);
    }

    /***
     * 将对象转换成json字符串 或 把list转化成json
     * @param object
     * @return
     */
    public static String objcetToString(Object object){
        return gson.toJson(object);
    }
    /**
     * 把json 字符串转化成list
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> stringToList(String json , Class<T> cls  ){
        Gson gson = new Gson();
        List<T> list = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for(final JsonElement elem : array){
            list.add(gson.fromJson(elem, cls));
        }
        return list ;
    }

}
