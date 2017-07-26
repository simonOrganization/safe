package com.lchtime.safetyexpress.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.bslee.threelogin.api.ThirdWeiXinLoginApi;
import com.google.gson.Gson;
import com.lchtime.safetyexpress.H5DetailUI;
import com.lchtime.safetyexpress.bean.Result;
import com.lchtime.safetyexpress.shareapi.wx.ShareWX;
import com.lchtime.safetyexpress.ui.home.GetMoneyActivity;
import com.lchtime.safetyexpress.ui.home.protocal.ShareProtocal;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {


	private IWXAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ThirdWeiXinLoginApi.getWXAPI(getApplicationContext()).handleIntent(
				getIntent(), this);
	}

	// 微信发送请求到第三方应用时，会回调到该方法
	@Override
	public void onReq(com.tencent.mm.sdk.modelbase.BaseReq arg0) {

	}

	ShareProtocal protocal = new ShareProtocal();
	Gson gson = new Gson();
	//授权成功失败回调
	@Override
	public void onResp(BaseResp resp) {
		Log.i("qaz","   onResp 1   :"+resp.errCode);
		//授权
		if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
			String code = ((SendAuth.Resp) resp).code;
			//Log.i("qaz","   code    :"+code);
			if (code != null) {
				Intent action = new Intent();
				action.setAction("ACTION_WX_LOGIN_SUCEESS");
				action.putExtra("code", code);
				LocalBroadcastManager.getInstance(this).sendBroadcast(action);
				finish();
				return;

			}else {
				Intent action = new Intent();
				action.setAction("ACTION_WX_LOGIN_SUCEESS");
				action.putExtra("code", code);
				action.putExtra("erro",resp.errCode);
				LocalBroadcastManager.getInstance(this).sendBroadcast(action);
				finish();
				return;
			}

//			int errorCode = resp.errCode;
//			if( errorCode != ErrCode.ERR_OK ){
//				String resid = errorCode == ErrCode.ERR_USER_CANCEL ? "授权取消" : "授权失败";
//				Toast.makeText(this, resid, Toast.LENGTH_SHORT).show();
//			}
		}

		//分享
		String result = "";

		switch (resp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				result = "发送成功";

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

				if (H5DetailUI.flag == 1 && !TextUtils.isEmpty(H5DetailUI.qc_id)){

					H5DetailUI.flag = 0;
					protocal.postCircleShare(H5DetailUI.qc_id,new ShareProtocal.ShareInfo() {
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


				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				result = "发送取消";
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				result = "发送被拒绝";
				break;
			default:
				result = "发送返回";
				break;
		}

		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
		
		finish();
		
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		//如果分享的时候，该已经开启，那么微信开始这个activity时，会调用onNewIntent，所以这里要处理微信的返回结果
		setIntent(intent);
		ShareWX.api.handleIntent(intent, this);
	}

}
