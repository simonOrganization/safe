package com.lchtime.safetyexpress;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Process;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.bslee.threelogin.util.UIUtils;
import com.lchtime.safetyexpress.ui.chat.hx.HuanXinHelper;
import com.lchtime.safetyexpress.utils.ImageLoaderUtils;
import com.squareup.picasso.Transformation;

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


    public static Context applicationContext;

    public Map<String, String> getMemProtocolCacheMap() {
        return MemProtocolCacheMap;
    }

    private Map<String,String> MemProtocolCacheMap = new HashMap<>();

    @Override
    public void onCreate() {
        //MultiDex.install(this);
        super.onCreate();
        UIUtils.initContext(this);

        mContext = getApplicationContext();

        mMainThreadHandler = new Handler();

        mMainThreadId = Process.myTid();

        applicationContext = this;
        instance = this;
        HuanXinHelper.getInstance().init(applicationContext);
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


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
       // MultiDex.install(this);
    }


}
