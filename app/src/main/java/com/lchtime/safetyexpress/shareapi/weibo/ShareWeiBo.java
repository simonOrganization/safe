package com.lchtime.safetyexpress.shareapi.weibo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.shareapi.ShareConstants;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;

import java.util.UUID;

import static com.lchtime.safetyexpress.utils.UIUtils.getResources;


/**
 * Created by Dreamer on 2017/6/16.
 */

public class ShareWeiBo {
//    private IWeiboShareAPI mWeiboShareAPI;
    private static ShareWeiBo mShareWeiBo = new ShareWeiBo();


    public static ShareWeiBo getShareWeiboInstants(){
        return mShareWeiBo;
    }


    public void shareToWeibo(Activity activity,String url,String title,String des,IWeiboShareAPI mWeiboShareAPI) {

        WebpageObject webpageObject = new WebpageObject(); //分享网页是这个
        Bitmap thumb = BitmapFactory.decodeResource(activity.getResources(), R.drawable.share_icon);
        webpageObject.setThumbImage(thumb); //注意，它会按照jpeg做85%的压缩，压缩后的大小不能超过32K
        webpageObject.title = title;//不能超过512
        webpageObject.actionUrl = url;// 不能超过512
        webpageObject.description = des;//不能超过1024
        webpageObject.identify = UUID.randomUUID().toString();//这个不知道做啥的
        webpageObject.defaultText = "Webpage 默认文案";//这个也不知道做啥的
//上面这些，一条都不能少，不然就会出现分享失败，主要是接口调用失败，而不会通过activity返回错误的intent

//下面这个，就是用户在分享网页的时候，自定义的微博内容
        TextObject textObject = new TextObject();
        textObject.text = title;

        WeiboMultiMessage msg = new WeiboMultiMessage();
        msg.mediaObject = webpageObject;
        msg.textObject = textObject;
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = msg;




//        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
//        // 用transaction唯一标识一个请求
//        WeiboMultiMessage multiMessage = new WeiboMultiMessage();
//        TextObject textobj = new TextObject();
//        textobj.text = url;
//        multiMessage.textObject = textobj;

//        request.transaction = String.valueOf(System.currentTimeMillis());
//        request.multiMessage = multiMessage;

        AuthInfo authInfo = new AuthInfo(activity, ShareConstants.WEIBO_APP_KEY, ShareConstants.REDIRECT_URL, ShareConstants.SCOPE);
        Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(MyApplication.getContext());
        String token = "";
        if (accessToken != null) {
            token = accessToken.getToken();
        }
        mWeiboShareAPI.sendRequest(activity, request, authInfo, token, new WeiboAuthListener() {

            @Override
            public void onWeiboException( WeiboException arg0 ) {
            }

            @Override
            public void onComplete( Bundle bundle ) {
                // TODO Auto-generated method stub
//                Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
//                AccessTokenKeeper.writeAccessToken(MyApplication.getContext(), newToken);
//                Toast.makeText(MyApplication.getContext(), "onAuthorizeComplete token = " + newToken.getToken(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
            }
        });

    }

}
