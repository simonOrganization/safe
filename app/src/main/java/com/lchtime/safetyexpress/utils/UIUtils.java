package com.lchtime.safetyexpress.utils;

import android.content.Context;
import android.content.res.Resources;

import com.cp.mygoogle.base.MyApplication;

/**
 * Created by Dreamer on 2017/2/27.
 */

public class UIUtils {

    public static Context getContext(){
        return MyApplication.getContext();
    }

    public static Resources getResources(){
        return getContext().getResources();
    }

    public static String getString(int resId){
        return getResources().getString(resId);
    }

    public static String[] getStrings(int resId){
        return getResources().getStringArray(resId);
    }

    public static int getColor(int resId){
        return getResources().getColor(resId);
    }
    public static String getPackageName(){
        return getContext().getPackageName();
    }

    public static int dip2Px(int dip) {
        float density = getResources().getDisplayMetrics().density;
        int px = (int) (dip * density + .5);
        return px;
    }

    public static int px2Dip(int px) {
        float density = getResources().getDisplayMetrics().density;
        int dip = (int) (px/density + .5);
        return dip;
    }
}
