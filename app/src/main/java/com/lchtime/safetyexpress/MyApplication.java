package com.lchtime.safetyexpress;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Process;
import android.os.StrictMode;
import android.support.multidex.MultiDex;

import com.bslee.threelogin.util.UIUtils;
import com.hyphenate.easeui.domain.EaseUser;
import com.lchtime.safetyexpress.ui.chat.hx.DemoHelper;
import com.lchtime.safetyexpress.utils.ImageLoaderUtils;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2017/4/14.
 */

public class MyApplication extends Application {

    private static MyApplication instance;
    public static Context applicationContext;
    public static String currentUserNick = "";


    private static Context mContext;
    private static Handler mMainThreadHandler;
    private static int mMainThreadId;

    public Map<String, String> getMemProtocolCacheMap() {
        return MemProtocolCacheMap;
    }

    private Map<String,String> MemProtocolCacheMap = new HashMap<>();

    private IWeiboShareAPI mWeiboShareAPI;
    @Override
    public void onCreate() {
        UIUtils.initContext(this);

        mContext = getApplicationContext();

        mMainThreadHandler = new Handler();

        mMainThreadId = Process.myTid();
        MultiDex.install(this);
        super.onCreate();
        instance = this;
        applicationContext = this;

        DemoHelper.getInstance().init(applicationContext);

        ImageLoaderUtils.initImageLoader(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

//        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, ShareConstants.WEIBO_APP_KEY);
//        mWeiboShareAPI.registerApp();

//        // com.getui.demo.DemoPushService 为第三方自定义推送服务
//        PushManager.getInstance().initialize(this.getApplicationContext(), DemoPushService.class);
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

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public Map<String,EaseUser> getTopUserList(){
        return DemoHelper.getInstance().getTopUserList();
    }

    public void setTopUserList(Map<String,EaseUser> contactList){
        DemoHelper.getInstance().setTopUserList(contactList);
    }

}
