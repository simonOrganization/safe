package com.lchtime.safetyexpress.ui.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.HXInfo;
import com.lchtime.safetyexpress.bean.InitInfo;
import com.lchtime.safetyexpress.bean.Third1Bean;
import com.lchtime.safetyexpress.bean.Third2Bean;
import com.lchtime.safetyexpress.bean.VipInfoBean;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.TabUI;
import com.lchtime.safetyexpress.ui.chat.hx.Constant;
import com.lchtime.safetyexpress.ui.chat.hx.DemoHelper;
import com.lchtime.safetyexpress.ui.chat.hx.db.DemoDBManager;
import com.lchtime.safetyexpress.ui.login.protocal.MutiLoginProtocal;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.LoginInternetRequest;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.tauth.Tencent;


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

    @ViewInject(R.id.pb_progress)
    private ProgressBar pb_progress;

    private Gson gson = new Gson();
    private Handler handler = new Handler();
    private MyReceiver mReceiver;

    private MutiLoginProtocal protocal = new MutiLoginProtocal();
    private String Clientid;

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
    boolean isLogin = false;
    @OnClick(R.id.tv_login_login)
    private void getLogin(View view){
        if (isLogin == true){
            return;
        }
        isLogin = true;
        //显示progressbar
        pb_progress.setVisibility(View.VISIBLE);
        backgroundAlpha(0.5f);
        final String phonenumber = et_login_username.getText().toString().trim();
        final String password = et_login_passport.getText().toString().trim();
        LoginInternetRequest.login(phonenumber, password, new LoginInternetRequest.ForResultListener() {
            @Override
                    public void onResponseMessage(String code) {
                        if (code.equals("成功")){
                            SpTools.setPassword(LoginUI.this, phonenumber);
                            SpTools.setPhone(LoginUI.this,password);
                            LoginInternetRequest.getVipInfo(SpTools.getUserId(LoginUI.this), new LoginInternetRequest.ForResultListener() {
                                @Override
                                public void onResponseMessage(String code) {
                                    if(!TextUtils.isEmpty(code)) {
                                        if (gson == null){
                                            gson = new Gson();
                                        }
                                        VipInfoBean vipInfoBean = gson.fromJson(code, VipInfoBean.class);
                                        saveVipInfoBean(vipInfoBean);
                                    }
                                }
                            });

                         }else {
                            isLogin = false;
                            backgroundAlpha(1f);
                            pb_progress.setVisibility(View.GONE);
                        }
            }
        });
    }

    /**
     * 保存用户信息
     * @param vipInfoBean
     */
    private void saveVipInfoBean(VipInfoBean vipInfoBean) {
        if (vipInfoBean != null) {
            SpTools.saveUser(mContext , vipInfoBean);
            //获取环信账号
            LoginInternetRequest.getHXinfo(new LoginInternetRequest.ForResultListener() {
                @Override
                public void onResponseMessage(String code) {
                    if (TextUtils.isEmpty(code)){
                        CommonUtils.toastMessage("获取聊天信息失败");
                        isLogin = false;
                        backgroundAlpha(1f);
                        pb_progress.setVisibility(View.GONE);
                        return;
                    }
                    HXInfo info = gson.fromJson(code, HXInfo.class);
                    //登录环信
                    loginHX(info.hx_account, Constant.HX_PWD);
                    finish();

                }
            });

        }
    }

    /**
     * 登录环信
     * @param currentUsername
     * @param currentPassword
     */
    private void loginHX(String currentUsername,String currentPassword) {
        DemoDBManager.getInstance().closeDB();

        // reset current user name before login
        DemoHelper.getInstance().setCurrentUserName(currentUsername);

        final long start = System.currentTimeMillis();
        // call login method
//        Log.d(TAG, "EMClient.getInstance().login");
        EMClient.getInstance().login(currentUsername, currentPassword, new EMCallBack() {

            @Override
            public void onSuccess() {
//                Log.d(TAG, "login: onSuccess");
                // ** manually load all local groups and conversation
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();

                // update current user's display name for APNs
                boolean updatenick = EMClient.getInstance().updateCurrentUserNick(
                        MyApplication.currentUserNick.trim());
                if (!updatenick) {
                    Log.e("LoginActivity", "update current user nick fail");
                }

                if (!LoginUI.this.isFinishing() ) {
                    isLogin = false;
                    backgroundAlpha(1f);
                    pb_progress.setVisibility(View.GONE);
                }
                // get user's info (this should be get from App's server or 3rd party service)
                DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();

//                Intent intent = new Intent(this,
//                        MainActivity.class);
//                startActivity(intent);

                finish();
            }

            @Override
            public void onProgress(int progress, String status) {
//                Log.d(TAG, "login: onProgress");
            }

            @Override
            public void onError(final int code, final String message) {
//                Log.d(TAG, "login: onError: " + code);
//                if (!progressShow) {
//                    return;
//                }
                runOnUiThread(new Runnable() {
                    public void run() {
//                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message,
                                Toast.LENGTH_SHORT).show();
                    }
                });
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
        finish();
    }

    /**
     * 忘记密码
     * @param view
     */
    @OnClick(R.id.tv_login_forpwd)
    private void getForPwd(View view){
        Intent intent = new Intent(LoginUI.this, SetPasswordUI.class);
        startActivity(intent);
        finish();
    }

    /**
     * 微信
     * @param view
     */
    @OnClick(R.id.ll_login_weixin)
    private void getWeixin(View view){
        if (isLogin == true){
            return;
        }
        isLogin = true;
        //显示progressbar
        pb_progress.setVisibility(View.VISIBLE);
        backgroundAlpha(0.5f);
        ThirdWeiXinLoginApi.getWXAPI(getApplicationContext());
        ThirdWeiXinLoginApi.login(getApplicationContext());
    }

    /**
     * QQ
     * @param view
     */
    @OnClick(R.id.ll_login_qq)
    private void getQQ(View view){
        if (isLogin == true){
            return;
        }
        isLogin = true;
        //显示progressbar
        pb_progress.setVisibility(View.VISIBLE);
        backgroundAlpha(0.5f);
        ThirdQQLoginApi.getTencent(getApplicationContext());
        ThirdQQLoginApi.login(this, oauth, oauthlogin);
    }

    /**
     * 微博
     * @param view
     */

    private SsoHandler mSsoHandler;
    @OnClick(R.id.ll_login_sina)
    private void getSina(View view){
        if (isLogin == true){
            return;
        }
        isLogin = true;
        //显示progressbar
        pb_progress.setVisibility(View.VISIBLE);
        backgroundAlpha(0.5f);
        mSsoHandler = ThirdWeiBoLoginApi.getSsoHandler(this);
        ThirdWeiBoLoginApi.login(this, oauth, oauthlogin);
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
                    String header = "";
                    String gender = "";
                    String loginType = "";
                    int type = token.authtype;
                    switch (type) {
                        case LoginPlatForm.QQZONE_PLATPORM:
                            uuid = ((QQToken) token).getOpenid();
                            name = ((QQUserInfo) user).getNickname();
                            header = ((QQUserInfo) user).getFigureurl_qq_1();
                            gender = ((QQUserInfo) user).getGender();
                            loginType = "2";
                            break;

                        case LoginPlatForm.WECHAT_PLATPORM:
                            uuid = ((WeiXinUserInfo) user).getOpenid();
                            name = ((WeiXinUserInfo) user).getNickname();
                            header = ((WeiXinUserInfo) user).getHeadimgurl();
                            gender = ((WeiXinUserInfo) user).getSex();
                            if ("1".equals(gender)){
                                gender = "男";
                            }else {
                                gender = "女";
                            }
                            loginType = "1";
                            break;
                        case LoginPlatForm.WEIBO_PLATPORM:
                            uuid = ((WeiBoToken) token).getUid();
                            name = ((WeiBoUserInfo) user).getName();
                            header = ((WeiBoUserInfo) user).getProfile_image_url();
                            gender = ((WeiBoUserInfo) user).getGender();
                            loginType = "3";
                            break;
                    }
//                    CommonUtils.toastMessage(uuid + name);
                    MuTiLogin(uuid,name,header,gender,loginType);
                }
            });
        }

        @Override
        public void OauthLoginFail() {
            handler.post(new Runnable() {

                @Override
                public void run() {
                    isLogin = false;
                    backgroundAlpha(1f);
                    pb_progress.setVisibility(View.GONE);
                    CommonUtils.toastMessage("授权失败，请重试！");
                }
            });

        }
    };

    //获取到三方登录的相关信息，连接到自己的服务器传送资料
    private void MuTiLogin(final String uuid, final String name, final String header, final String gender, final String loginType) {

        protocal.postMutiLogin(uuid, name, header, gender, loginType, Clientid, new MutiLoginProtocal.MutiLoginListener() {
            @Override
            public void normalResponse(Object response) {
                if (response == null){
                    isLogin = false;
                    backgroundAlpha(1f);
                    pb_progress.setVisibility(View.GONE);
                    CommonUtils.toastMessage("登录失败，请稍后再试！");
                    return;
                }

//                Third1Bean bean = (Third1Bean) response;
                try{
                    Third1Bean bean = gson.fromJson((String) response,Third1Bean.class);
                    if ("10".equals(bean.result.code)){
                        //没有申请过
                        if ("0".equals(bean.userid.ub_id)){

                            registerNewThird(uuid, name, header, gender, loginType);


                        }else {
                            /*VipInfoBean vipInfoBean = new VipInfoBean();
                            vipInfoBean.user_detail.ud_addr = bean.user.ud_addr;
                            vipInfoBean.user_detail.ud_nickname = bean.user.ud_nickname;
                            vipInfoBean.user_detail.ud_photo_fileid = bean.user.ud_photo_fileid;
                            vipInfoBean.user_detail.ud_post = bean.user.ud_post;
                            vipInfoBean.user_detail.ud_profession = bean.user.ud_profession;
                            vipInfoBean.user_detail.ub_phone = bean.userid.phone;
                            vipInfoBean.user_detail.ud_sex = bean.userid.tp_gender;
                            vipInfoBean.user_detail.ub_id = bean.user.ud_ub_id;
                            //申请过了 ，直接登录
                            saveVipInfoBean(vipInfoBean);*/
                            thirdLogin(bean.userid.ub_id);
                        }
                    }else {
                        isLogin = false;
                        backgroundAlpha(1f);
                        pb_progress.setVisibility(View.GONE);
                        CommonUtils.toastMessage("请您稍后重试！");
                    }
                }catch (Exception exception){
                    isLogin = false;
                    backgroundAlpha(1f);
                    pb_progress.setVisibility(View.GONE);
                    CommonUtils.toastMessage("请您稍后重试！");
                }

            }
        });
    }

    String tp_openid;
    String tp_username;
    String tp_head;
    String tp_gender;
    String type;
    private void registerNewThird(String tp_openid,String tp_username,String tp_head,String tp_gender,String type) {

        this.tp_openid = tp_openid;
        this.tp_username = tp_username;
        this.tp_head = tp_head;
        this.tp_gender = tp_gender;
        this.type = type;

        Intent intent = new Intent(this, RegisterUI.class);
        intent.putExtra("third","third");
        startActivityForResult(intent,333);



    }

    //第三方登录成功
    private void thirdLogin(String ub_id) {
        SpTools.setUserId(LoginUI.this , ub_id);//存储用户的ub_id
        TabUI.getTabUI().init();
        finish();
    }

    /**
     * QQ，微博授权回调
     */
    private OauthListener oauth = new OauthListener() {

        @Override
        public void OauthSuccess(Object obj) {
//            mProressbar.setText("正在为你登录");
//            mProressbar.setVisibility(View.VISIBLE);
//            isLogin = false;
//            backgroundAlpha(1f);
//            pb_progress.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "授权成功,请稍后...", Toast.LENGTH_SHORT)
                    .show();
        }

        @Override
        public void OauthFail(Object type) {
            isLogin = false;
            backgroundAlpha(1f);
            pb_progress.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "授权失败,请重试！", Toast.LENGTH_SHORT)
                    .show();
        }

        @Override
        public void OauthCancel(Object type) {
            isLogin = false;
            backgroundAlpha(1f);
            pb_progress.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "您取消了授权，请重试!", Toast.LENGTH_SHORT)
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
                if (!TextUtils.isEmpty(code)) {
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            ThirdWeiXinLoginApi.getOauthAcces(code, oauthlogin);
                        }
                    }).start();
                }else {
                    int errorCode = intent.getIntExtra("erro",0);
                    if( errorCode != BaseResp.ErrCode.ERR_OK ){
                        isLogin = false;
                        backgroundAlpha(1f);
                        pb_progress.setVisibility(View.GONE);
                        //微信
                        String resid = errorCode == BaseResp.ErrCode.ERR_USER_CANCEL ? "授权取消" : "授权失败";
                        Toast.makeText(LoginUI.this, resid, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        mReceiver = null;
        super.onDestroy();
    }


    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }


    //一定要！！！！
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
        if(!TextUtils.isEmpty(SpTools.getUserId(mContext))){
             Clientid   =  SpTools.getUserId(mContext);

        }
        if (requestCode == 333 && resultCode == 333&& data != null){
            //三方登录注册回来的
            String phone = data.getStringExtra("phone");
            String pwd = data.getStringExtra("pwd");

            protocal.postMutiNewLogin(tp_openid, tp_username, tp_head, tp_gender, type, phone, pwd,Clientid , new MutiLoginProtocal.MutiLoginListener() {
                @Override
                public void normalResponse(Object response) {
                    if (response == null){
                        isLogin = false;
                        backgroundAlpha(1f);
                        pb_progress.setVisibility(View.GONE);
                        CommonUtils.toastMessage("登录失败，请重新尝试");
                        return;
                    }
                    try {
                        Third2Bean bean = gson.fromJson((String) response,Third2Bean.class);
                        if ("10".equals(bean.result.code)) {
                            isLogin = false;
                            backgroundAlpha(1f);
                            pb_progress.setVisibility(View.GONE);

                            /*VipInfoBean vipInfoBean = new VipInfoBean();
                            vipInfoBean.user_detail.ud_addr = bean.user.ud_addr;
                            vipInfoBean.user_detail.ud_nickname = bean.user.ud_nickname;
                            vipInfoBean.user_detail.ud_photo_fileid = bean.user.ud_photo_fileid;
                            vipInfoBean.user_detail.ud_post = bean.user.ud_post;
                            vipInfoBean.user_detail.ud_profession = bean.user.ud_profession;
                            vipInfoBean.user_detail.ub_phone = bean.userid.phone;
                            vipInfoBean.user_detail.ud_sex = bean.userid.tp_gender;

                            saveVipInfoBean(vipInfoBean);*/
                            thirdLogin(bean.userid.ub_id);
                        }else {
                            isLogin = false;
                            backgroundAlpha(1f);
                            pb_progress.setVisibility(View.GONE);
                            CommonUtils.toastMessage("登录失败，请重新尝试");
                        }
                        finish();
                    }catch (Exception exception) {

                        isLogin = false;
                        backgroundAlpha(1f);
                        pb_progress.setVisibility(View.GONE);
                        CommonUtils.toastMessage("登录失败，请重新尝试");
                    }

                }
            });

            return;
        }


        Tencent.onActivityResultData(requestCode, resultCode, data, ThirdQQLoginApi.getIUiListener(this,oauth, oauthlogin));

        if(requestCode == com.tencent.connect.common.Constants.REQUEST_API) {
            if(resultCode == com.tencent.connect.common.Constants.REQUEST_LOGIN) {
                Tencent.handleResultData(data, ThirdQQLoginApi.getIUiListener(this,oauth, oauthlogin));

            }
        }
    }


}
