package com.hyphenate.easeui.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.R.attr.value;

/**
 * Created by Administrator on 2017/7/25.
 */

public class SPUtils {

    private static String LOGIN = "login";

    /**
     * 保存
     * @param context
     * @param key
     * @param value
     */
    public static void setString(Context context , String key , String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN , Context.MODE_APPEND);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key , value);
        editor.commit();

    }

    public static String getString(Context context , String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN , Context.MODE_APPEND);
        return sharedPreferences.getString(key , "");
    }



}
