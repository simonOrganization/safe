package com.lchtime.safetyexpress.shareapi.wx;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.bslee.threelogin.util.UIUtils;
import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.shareapi.Util;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;

import static android.R.attr.bitmap;
import static com.lchtime.safetyexpress.shareapi.Util.bmpToByteArray;
import static com.lchtime.safetyexpress.utils.UIUtils.getResources;


/**
 * Created by Dreamer on 2017/6/17.
 */

public class ShareWX {

    private static final int THUMB_SIZE = 150;
    private static ShareWX instance = new ShareWX();
    public static IWXAPI api;
    public static ShareWX getShareWXInstance(IWXAPI api2){
        api = api2;
        return instance;
    }
    /**
     * @param isShareFriend true 分享到朋友，false分享到朋友圈
     */
    public void share2Wx(boolean isShareFriend,String title,String des,String url) {


        if (!isWXAPPInstalled(MyApplication.getContext())) {
            UIUtils.showToastMessage(com.bslee.R.string.weixin_not_install);
            return;
        }

//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//        WXImageObject imgObj = new WXImageObject(bitmap);
//
//
//        // 初始化一个WXTextObject对象
//        WXTextObject textObj = new WXTextObject();
//        textObj.text = "下载安全快车APP,注册完善信息并分享给好友，现金红包马上到账！";
//        // 用WXTextObject对象初始化一个WXMediaMessage对象
//        WXMediaMessage msg = new WXMediaMessage();
//        msg.mediaObject = textObj;   // 发送文本类型的消息时，title字段不起作用
//        // msg.title = "Will be ignored";
//        //给用户的提示
//        msg.description = "注册分享，得百元红包";
//        msg.mediaObject = imgObj;
//        // 构造一个Req
//        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求
//        req.message = msg;



        WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpageObject);
        msg.title = title;
        msg.description = des;
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.share_icon);
        msg.thumbData = bmpToByteArray(thumb, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;


        // 分享或收藏的目标场景，通过修改scene场景值实现。
        // 发送到聊天界面 —— WXSceneSession
        // 发送到朋友圈 —— WXSceneTimeline
        // 添加到微信收藏 —— WXSceneFavorite
        req.scene = isShareFriend ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        // 调用api接口发送数据到微信
        api.sendReq(req);



//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//        WXImageObject imgObj = new WXImageObject(bitmap);

//        WXMediaMessage msg = new WXMediaMessage();
//        msg.mediaObject = imgObj;
//        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true);//缩略图大小
//        bitmap.recycle();
//        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);  // 设置缩略图
//
//        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = buildTransaction("img");
//        req.message = msg;
//        req.scene = isShareFriend ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
//        api.sendReq(req);
    }


    public static boolean isWXAPPInstalled(Context ctx) {
        if (api == null) {
            return false;
        }
        boolean result = api.isWXAppInstalled();
        if (result) {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                try {
                    ApplicationInfo info = packageManager.getApplicationInfo(
                            "com.tencent.mm", PackageManager.GET_META_DATA);
                    if (info != null) {
                        result = true;
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    result = false;
                }
            }
        }
        return result;
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
