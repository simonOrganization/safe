package com.lchtime.safetyexpress;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Process;
import android.text.TextUtils;

import com.lchtime.safetyexpress.utils.ImageLoaderUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2017/4/14.
 */

public class MyApplication extends Application {

    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }


    private static Context mContext;
    private static Handler mMainThreadHandler;
    private static int mMainThreadId;

    public Map<String, String> getMemProtocolCacheMap() {
        return MemProtocolCacheMap;
    }

    private Map<String,String> MemProtocolCacheMap = new HashMap<>();

    @Override
    public void onCreate() {
        mContext = getApplicationContext();

        mMainThreadHandler = new Handler();

        mMainThreadId = Process.myTid();

        super.onCreate();
        instance = this;
        ImageLoaderUtils.initImageLoader(this);
    }

    public static Context getContext() {
        return mContext;
    }

    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    public static int getMainThreadId() {
        return mMainThreadId;
    }

}
