package com.lchtime.safetyexpress.ui.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bslee.threelogin.api.OauthListener;
import com.bslee.threelogin.api.OauthLoginListener;
import com.bslee.threelogin.api.ThirdQQLoginApi;
import com.bslee.threelogin.api.ThirdWeiBoLoginApi;
import com.bslee.threelogin.api.ThirdWeiXinLoginApi;
import com.bslee.threelogin.db.LoginPlatForm;
import com.bslee.threelogin.model.AuthToken;
import com.bslee.threelogin.model.AuthUser;
import com.bslee.threelogin.model.QQToken;
import com.bslee.threelogin.model.QQUserInfo;
import com.bslee.threelogin.model.WeiBoToken;
import com.bslee.threelogin.model.WeiBoUserInfo;
import com.bslee.threelogin.model.WeiXinUserInfo;
import com.google.gson.Gson;
import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.InitInfo;
import com.lchtime.safetyexpress.bean.VipInfoBean;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.TabUI;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.LoginInternetRequest;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 登录
 * Created by user on 2017/4/15.
 */
@ContentView(R.layout.login)
public class LoginUI extends BaseUI {

    //手机号
    @ViewInject(R.id.et_login_username)
    private EditText et_login_username;
    //密码
    @ViewInject(R.id.et_login_passport)
    private EditText et_login_passport;

    private Gson gson;
    private Handler handler = new Handler();
    private MyReceiver mReceiver;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        setTitle("登录");
        rightVisible("注册");
        mReceiver = new MyReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,
                new IntentFilter("ACTION_WX_LOGIN_SUCEESS"));


    }

    @Override
    protected void prepareData() {

    }

    /**
     * 登录
     * @param view
     */
    @OnClick(R.id.tv_login_login)
    private void getLogin(View view){


        final String phonenumber = et_login_username.getText().toString().trim();
        final String password = et_login_passport.getText().toString().trim();
        LoginInternetRequest.login(phonenumber, password, new LoginInternetRequest.ForResultListener() {
            @Override
                    public void onResponseMessage(String code) {
                        if (code.equals("成功")){
                            SpTools.setString(LoginUI.this, Constants.phoneNum,phonenumber);
                            SpTools.setString(LoginUI.this, Constants.password,password);
                            LoginInternetRequest.getVipInfo(SpTools.getString(LoginUI.this, Constants.userId, ""), new LoginInternetRequest.ForResultListener() {
                                @Override
                                public void onResponseMessage(String code) {
                                    if(!TextUtils.isEmpty(code)) {
                                        if (gson == null){
                                            gson = new Gson();
                                        }
                                        VipInfoBean vipInfoBean = gson.fromJson(code, VipInfoBean.class);
                                        if (vipInfoBean != null) {
                                            InitInfo.phoneNumber = vipInfoBean.user_detail.ub_phone;
                                            InitInfo.vipInfoBean = vipInfoBean;
                                            InitInfo.isLogin = true;
                                            SpTools.setString(LoginUI.this, Constants.nik_name,vipInfoBean.user_detail.ud_nickname);
                                            SpTools.setString(LoginUI.this, Constants.nik_name,vipInfoBean.user_detail.ud_nickname);
                                            finish();
                                        }
                                    }
                                }
                            });

                }
            }
        });
    }

    /**
     * 注册
     * @param view
     */
    @OnClick(R.id.ll_right)
    private void getRegister(View view){
        Intent intent = new Intent(LoginUI.this, RegisterUI.class);
        startActivity(intent);
    }

    /**
     * 忘记密码
     * @param view
     */
    @OnClick(R.id.tv_login_forpwd)
    private void getForPwd(View view){
        Intent intent = new Intent(LoginUI.this, SetPasswordUI.class);
        startActivity(intent);
    }

    /**
     * 微信
     * @param view
     */
    @OnClick(R.id.ll_login_weixin)
    private void getWeixin(View view){
        makeText("微信");
//        ThirdWeiXinLoginApi.getWXAPI(getApplicationContext());
        ThirdWeiXinLoginApi.login(getApplicationContext());
    }

    /**
     * QQ
     * @param view
     */
    @OnClick(R.id.ll_login_qq)
    private void getQQ(View view){
        makeText("QQ");
//        ThirdQQLoginApi.getTencent(getApplicationContext());
//        ThirdQQLoginApi.login(this, oauth, oauthlogin);
    }

    /**
     * 微博
     * @param view
     */
    @OnClick(R.id.ll_login_sina)
    private void getSina(View view){
        makeText("微博");
//        ThirdWeiBoLoginApi.getSsoHandler(this);
//        ThirdWeiBoLoginApi.login(this, oauth, oauthlogin);
    }


    /**
     * QQ，WeiBo，WeiXin登录成功回调
     */
    private OauthLoginListener oauthlogin = new OauthLoginListener() {

        @Override
        public void OauthLoginSuccess(final AuthToken token, final AuthUser user) {

            handler.post(new Runnable() {

                @Override
                public void run() {

                    String uuid = "";//三方用户唯一ID
                    String name = "";
                    int type = token.authtype;
                    switch (type) {
                        case LoginPlatForm.QQZONE_PLATPORM:
                            uuid = ((QQToken) token).getOpenid();
                            name = ((QQUserInfo) user).getNickname();
                            break;

                        case LoginPlatForm.WECHAT_PLATPORM:
                            uuid = ((WeiXinUserInfo) user).getUnionid();
                            name = ((WeiXinUserInfo) user).getNickname();
                            break;
                        case LoginPlatForm.WEIBO_PLATPORM:
                            uuid = ((WeiBoToken) token).getUid();
                            name = ((WeiBoUserInfo) user).getName();

                            break;
                    }
                    //mProressbar.setText("状态:登录成功\n" +"ID:" + uuid + "\n昵称:" + name);
                    CommonUtils.toastMessage("状态:登录成功\n" +"ID:" + uuid + "\n昵称:" + name);
                }
            });
        }

        @Override
        public void OauthLoginFail() {
            handler.post(new Runnable() {

                @Override
                public void run() {
                    //mProressbar.setText("登录失败");
                }
            });

        }
    };

    /**
     * QQ，微博授权回调
     */
    private OauthListener oauth = new OauthListener() {

        @Override
        public void OauthSuccess(Object obj) {
//            mProressbar.setText("正在为你登录");
//            mProressbar.setVisibility(View.VISIBLE);
        }

        @Override
        public void OauthFail(Object type) {
            Toast.makeText(getApplicationContext(), "授权失败", Toast.LENGTH_SHORT)
                    .show();
        }

        @Override
        public void OauthCancel(Object type) {
            Toast.makeText(getApplicationContext(), "取消授权", Toast.LENGTH_SHORT)
                    .show();
        }
    };


    /**
     * 微信授权广播回调
     *
     * @author user
     *
     */
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("ACTION_WX_LOGIN_SUCEESS".equals(intent.getAction())) {
                //拿着code获取个人信息
                final String code = intent.getStringExtra("code");
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        ThirdWeiXinLoginApi.getOauthAcces(code, oauthlogin);
                    }
                }).start();
            }
        }
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        mReceiver = null;
        super.onDestroy();
    }

}
