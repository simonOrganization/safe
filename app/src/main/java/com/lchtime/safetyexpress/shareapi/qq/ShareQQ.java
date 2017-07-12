package com.lchtime.safetyexpress.shareapi.qq;

import android.app.Activity;
import android.os.Bundle;

import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.shareapi.ShareConstants;
import com.tencent.connect.share.QQShare;
import com.tencent.open.utils.ThreadManager;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

/**
 * Created by Dreamer on 2017/6/16.
 */

public class ShareQQ {

    private static ShareQQ mShareQQ = new ShareQQ();
    public static Tencent mTencent;
    private String title = "";
    private String describe = "";
    private String url = "";
    public Activity activity;
    private IUiListener qqShareListener ;

    public static ShareQQ getShareQQInstants(){
        return mShareQQ;
    }

    public void shareToQQ(Activity activity,IUiListener qqShareListener,String title,String describe,String url){
        this.activity = activity;
        this.qqShareListener = qqShareListener;
        this.title = title;
        this.describe = describe;
        this.url = url;
        if (mTencent == null) {
            mTencent = Tencent.createInstance(ShareConstants.QQ_APP_KEY, MyApplication.getContext());
        }

        shareOnlyImageOnQQ(activity);
    }


    private void shareOnlyImageOnQQ(Activity activity) {
        final Bundle params = new Bundle();
//        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "测试应用");
//        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
//        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN); //打开这句话，可以实现分享纯图到QQ空间

        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);// 标题
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, describe);// 摘要
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,url);// 内容地址
        doShareToQQ(params,activity);
    }


    private void doShareToQQ(final Bundle params, final Activity activity) {
        // QQ分享要在主线程做
        ThreadManager.getMainHandler().post(new Runnable() {

            @Override
            public void run() {
                if (null != mTencent) {
                    mTencent.shareToQQ(activity, params, qqShareListener);
                }
            }
        });
    }




}
