package com.lchtime.safetyexpress;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Process;
import android.os.StrictMode;

import com.baidu.cloud.media.player.BDCloudMediaPlayer;
import com.bslee.threelogin.util.UIUtils;
import com.hyphenate.easeui.domain.EaseUser;
import com.igexin.sdk.PushManager;
import com.lchtime.safetyexpress.ui.chat.hx.DemoHelper;
import com.lchtime.safetyexpress.utils.ImageLoaderUtils;
import com.lchtime.safetyexpress.weight.glide_https.HTTPSUtils;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.zhy.http.okhttp.OkHttpUtils;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import service.DemoPushService;


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
    private static OkHttpClient mOkHttpClient;

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
        //MultiDex.install(this);
        super.onCreate();
        instance = this;
        applicationContext = this;

        /*HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                //.sslSocketFactory(createSSLSocketFactory())
                //.hostnameVerifier(new TrustAllHostnameVerifier())

                //其他配置
                .build();*/
        mOkHttpClient = new HTTPSUtils(instance).getInstance();
        OkHttpUtils.initClient(mOkHttpClient);
        //Glide.get(this).register(GlideUrl.class ,  InputStream.class, new OkHttpUrlLoader.Factory(RetrofitUtils.getOkHttpClient()));

        DemoHelper.getInstance().init(applicationContext);

        ImageLoaderUtils.initImageLoader(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }


        BDCloudMediaPlayer.setAK("e0a5a3a40d2148c7b18be89ffd32a8dd");
    }

    public static OkHttpClient getmOkHttpClient() {
        return mOkHttpClient;
    }

    /**
     * 默认信任所有的证书
     * TODO 最好加上证书认证，主流App都有自己的证书
     *
     * @return
     */
    @SuppressLint("TrulyRandom")
    private static SSLSocketFactory createSSLSocketFactory() {

        SSLSocketFactory sSLSocketFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllManager()},
                    new SecureRandom());
            sSLSocketFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return sSLSocketFactory;
    }

    private static class TrustAllManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)

                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
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
        //MultiDex.install(this);
    }

    public Map<String,EaseUser> getTopUserList(){
        return DemoHelper.getInstance().getTopUserList();
    }

    public void setTopUserList(Map<String,EaseUser> contactList){
        DemoHelper.getInstance().setTopUserList(contactList);
    }


}
