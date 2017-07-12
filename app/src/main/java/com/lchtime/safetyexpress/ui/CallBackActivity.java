package com.lchtime.safetyexpress.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lchtime.safetyexpress.H5DetailUI;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Result;
import com.lchtime.safetyexpress.shareapi.ShareConstants;
import com.lchtime.safetyexpress.shareapi.weibo.ShareWeiBo;
import com.lchtime.safetyexpress.ui.home.GetMoneyActivity;
import com.lchtime.safetyexpress.ui.home.protocal.ShareProtocal;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;

/**
 * Created by Dreamer on 2017/6/27.
 */

public class CallBackActivity extends Activity implements IWeiboHandler.Response {

    private IWeiboShareAPI mWeiboShareAPI;
    private String qc_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_call_back);

        qc_id = getIntent().getStringExtra("qc_id");
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, ShareConstants.WEIBO_APP_KEY);
        mWeiboShareAPI.registerApp();
        if (savedInstanceState != null) {
            mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
        }

        String shareUrl = getIntent().getStringExtra("shareUrl");
        String title = getIntent().getStringExtra("title");
        String des = getIntent().getStringExtra("des");
        ShareWeiBo.getShareWeiboInstants().shareToWeibo(CallBackActivity.this,shareUrl,title,des,mWeiboShareAPI);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调

        mWeiboShareAPI.handleWeiboResponse(intent, this);
    }

    ShareProtocal protocal = new ShareProtocal();
    Gson gson = new Gson();
    @Override
    public void onResponse(BaseResponse baseResp) {
        if (baseResp != null) {
            switch (baseResp.errCode) {
                case WBConstants.ErrorCode.ERR_OK:
                    Toast.makeText(this, "成功", Toast.LENGTH_LONG).show();
                    if (GetMoneyActivity.flag == 1) {
                        GetMoneyActivity.flag = 0;
                        protocal.postShare(new ShareProtocal.ShareInfo() {
                            @Override
                            public void shareResponse(String response) {
                                if (TextUtils.isEmpty(response)) {
                                    CommonUtils.toastMessage("请重新分享已确保获得奖励。");
                                    return;
                                }

                                Result bean = gson.fromJson(response, Result.class);
                                if ("10".equals(bean.result.code)) {
                                    CommonUtils.toastMessage(bean.result.info);
                                } else {
                                    CommonUtils.toastMessage(bean.result.info);
                                }
                            }
                        });
                    }


                    if (H5DetailUI.flag == 1&& !TextUtils.isEmpty(qc_id)){
                        H5DetailUI.flag = 0;
                        protocal.postCircleShare(qc_id,new ShareProtocal.ShareInfo() {
                            @Override
                            public void shareResponse(String response) {
                                if (TextUtils.isEmpty(response)){
                                    CommonUtils.toastMessage("请重新分享已确保获得奖励。");
                                    return;
                                }

                                Result bean = gson.fromJson(response,Result.class);
                                if ("10".equals(bean.result.code)){
                                    CommonUtils.toastMessage(bean.result.info);
                                }else {
                                    CommonUtils.toastMessage(bean.result.info);
                                }
                            }
                        });
                    }
                    finish();
                    break;
                case WBConstants.ErrorCode.ERR_CANCEL:
                    Toast.makeText(this, "取消", Toast.LENGTH_LONG).show();
                    finish();
                    break;
                case WBConstants.ErrorCode.ERR_FAIL:
                    Toast.makeText(this, "错误" + baseResp.errMsg,
                            Toast.LENGTH_LONG).show();
                    finish();
                    break;
            }
        }
    }
}
