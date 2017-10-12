package com.hyphenate.easeui.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.R.attr.value;

/**
 * Created by Administrator on 2017/7/25.
 */

public class SPUtils {

    private static String LOGIN = "login";
    public static final String CONFIGFILE = "user_data";
    /**
     * 保存
     * @param context
     * @param key
     * @param value
     */
    public static void setString(Context context , String key , String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(CONFIGFILE , Context.MODE_APPEND);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key , value);
        editor.commit();

    }

    public static String getString(Context context , String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(CONFIGFILE , Context.MODE_APPEND);
        return sharedPreferences.getString(key , "");
    }

    /**
     * 获取用户头像
     * @param context
     * @return
     */
    public static String getUserHead(Context context){
        SharedPreferences sp = context.getSharedPreferences(CONFIGFILE , Context.MODE_APPEND);
        return sp.getString("ud_photo_fileid" , "");
    }

}
