package com.lchtime.safetyexpress;

import android.app.Application;
import android.text.TextUtils;

import com.lchtime.safetyexpress.utils.ImageLoaderUtils;

/**
 * Created by user on 2017/4/14.
 */

public class MyApplication extends Application {

    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        ImageLoaderUtils.initImageLoader(this);
    }

}
