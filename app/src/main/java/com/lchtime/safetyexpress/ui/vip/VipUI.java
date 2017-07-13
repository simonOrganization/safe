package com.lchtime.safetyexpress.ui.vip;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.lchtime.safetyexpress.bean.MyAccountBean;
import com.lchtime.safetyexpress.bean.Third1Bean;
import com.lchtime.safetyexpress.bean.Third2Bean;
import com.lchtime.safetyexpress.bean.VipInfoBean;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.TabUI;
import com.lchtime.safetyexpress.ui.circle.protocal.CircleProtocal;
import com.lchtime.safetyexpress.ui.login.LoginUI;
import com.lchtime.safetyexpress.ui.login.RegisterUI;
import com.lchtime.safetyexpress.ui.login.protocal.MutiLoginProtocal;
import com.lchtime.safetyexpress.ui.vip.protocal.VipProtocal;
import com.lchtime.safetyexpress.utils.BitmapUtils;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.LoginInternetRequest;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lchtime.safetyexpress.utils.UpdataImageUtils;
import com.lchtime.safetyexpress.views.CircleImageView;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.tauth.Tencent;

import java.io.File;


/**
 * 我的
 * Created by user on 2017/4/14.
 */
@ContentView(R.layout.vip)
public class VipUI extends BaseUI implements View.OnClickListener {

    //头像
    @ViewInject(R.id.civ_vip_icon)
    private CircleImageView civ_vip_icon;

    //昵称
    @ViewInject(R.id.tv_vip_nickname)
    private TextView tv_vip_nickname;

    //没有登陆的布局
    @ViewInject(R.id.ll_vip_logout)
    private LinearLayout logOut;

    //登陆后显示的布局
    @ViewInject(R.id.ll_vip_login)
    private LinearLayout logIn;

    //登陆后显示的布局
    @ViewInject(R.id.tv_vip_info)
    private LinearLayout addr_pro_post;


    @ViewInject(R.id.pb_progress)
    private ProgressBar pb_progress;

    //微信登录
    @ViewInject(R.id.ll_vip_weixin)
    private LinearLayout ll_vip_weixin;

    //QQ登录
    @ViewInject(R.id.ll_login_qq)
    private LinearLayout ll_login_qq;

    //微博登录
    @ViewInject(R.id.ll_login_sina)
    private LinearLayout ll_login_sina;


    private Gson gson = new Gson();

    private Handler handler;

    private MyReceiver mReceiver;
    private TextView tv_money_num;
    private String userid;

    @Override
    protected void back() {
        exit();
    }

    @Override
    protected void setControlBasis() {
        setTitle("我");
        backGone();
        tv_money_num = (TextView) findViewById(R.id.tv_money_num);
        handler = new Handler();
        mReceiver = new MyReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,
                new IntentFilter("ACTION_WX_LOGIN_SUCEESS"));
    }

    @Override
    protected void prepareData() {
        ll_vip_weixin.setOnClickListener(this);
        ll_login_qq.setOnClickListener(this);
        ll_login_sina.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshVip();
    }

    private void refreshVip() {
        String ub_id = SpTools.getString(this, Constants.userId,"");
        if (TextUtils.isEmpty(ub_id)){
            logIn.setVisibility(View.GONE);
            logOut.setVisibility(View.VISIBLE);
            tv_money_num.setVisibility(View.INVISIBLE);
        }else {
            //登录的情况下
            logIn.setVisibility(View.VISIBLE);
            logOut.setVisibility(View.GONE);
            if (InitInfo.vipInfoBean != null) {
                initVipInfo();
            }
        }
    }

    //设置个人相关信息
    private void initVipInfo() {
        File file = new File(MyApplication.getContext().getFilesDir(),Constants.photo_name);//将要保存图片的路径
        //如果没有加载过图片了
        if (!file.exists()){
            civ_vip_icon.setImageDrawable(getResources().getDrawable(R.drawable.circle_user_image));
            if (!TextUtils.isEmpty(InitInfo.vipInfoBean.user_detail.ud_photo_fileid)){
                UpdataImageUtils.getUrlBitmap(InitInfo.vipInfoBean.user_detail.ud_photo_fileid, new UpdataImageUtils.BitmapListener() {
                    @Override
                    public void giveBitmap(final Bitmap bitmap) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                UpdataImageUtils.saveBitmapFile(bitmap,Constants.photo_name);
                                civ_vip_icon.setImageBitmap(bitmap);
                            }
                        });

                    }
                });

            }

        }else {
            civ_vip_icon.setImageBitmap(BitmapUtils.getBitmap(file.getAbsolutePath()));
        }

        if (isLogin) {
            tv_vip_nickname.setText(TextUtils.isEmpty(InitInfo.vipInfoBean.user_detail.ud_nickname) ? "设置昵称" : InitInfo.vipInfoBean.user_detail.ud_nickname);
        }else {
            String name = SpTools.getString(this,Constants.nik_name,"");
            tv_vip_nickname.setText(TextUtils.isEmpty(name) ? "设置昵称" : name);
        }

        String[] arr = {InitInfo.vipInfoBean.user_detail.ud_post,InitInfo.vipInfoBean.user_detail.ud_profession,InitInfo.vipInfoBean.user_detail.ud_addr};
        if (!(TextUtils.isEmpty(arr[0])||
                TextUtils.isEmpty(arr[1])||
                TextUtils.isEmpty(arr[2]))){
            float desity = getResources().getDisplayMetrics().density;
            addr_pro_post.removeAllViews();
            for (int i = 0; i < arr.length;i++){
                TextView tv = new TextView(this);
                tv.setText(arr[i]);
                tv.setBackground(getResources().getDrawable(R.drawable.shape_white_border));
                tv.setHeight(R.dimen.dm050);
                tv.setGravity(Gravity.CENTER);
                tv.setSingleLine(true);
                tv.setTextColor(Color.WHITE);
                tv.setTextSize(26/desity + 0.5f);
                tv.setPadding((int)(30/desity + 0.5f),0,(int)(30/desity + 0.5f),0);
                if (i > 0){
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins((int)(10/desity + 0.5f),0,0,0);//4个参数按顺序分别是左上右下

                    tv.setLayoutParams(layoutParams);
                }
                addr_pro_post.addView(tv);
            }
        }else {
            float desity = getResources().getDisplayMetrics().density;
            addr_pro_post.removeAllViews();
            TextView tv = new TextView(this);
            tv.setText("去完善个人信息");
            tv.setBackground(getResources().getDrawable(R.drawable.shape_white_border));
            tv.setHeight(R.dimen.dm050);
            tv.setGravity(Gravity.CENTER);
            tv.setSingleLine(true);
            tv.setTextColor(Color.WHITE);
            tv.setTextSize(26/desity + 0.5f);
            tv.setPadding((int)(30/desity + 0.5f),0,(int)(30/desity + 0.5f),0);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins((int)(10/desity + 0.5f),0,0,0);//4个参数按顺序分别是左上右下
            tv.setLayoutParams(layoutParams);

            addr_pro_post.addView(tv);
        }


        initMoneyNum();
    }

    private VipProtocal mVipProtocal = new VipProtocal();
    //获取余额
    private void initMoneyNum() {

        userid = SpTools.getString(this, Constants.userId, "");


        if (TextUtils.isEmpty(userid)){
            tv_money_num.setVisibility(View.GONE);
            return;
        }
        mVipProtocal.getMyAcountInfo(userid, new CircleProtocal.NormalListener() {
            @Override
            public void normalResponse(Object response) {
                if (response == null){
                    CommonUtils.toastMessage("获取账户余额失败！");
                    return;
                }
                MyAccountBean bean = (MyAccountBean) response;
                tv_money_num.setText("¥" + bean.ud_amount);
                tv_money_num.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     *手机号登陆
     *
     * @param view
     */
    @OnClick(R.id.tv_phonenum_login)
    private void getLoginActivity(View view) {
        Intent intent = new Intent(VipUI.this, LoginUI.class);
        startActivity(intent);

    }

    /**
     *手机号快速注册
     *
     * @param view
     */
    @OnClick(R.id.tv_phonenum_register)
    private void getPhoneRegister(View view) {
        Intent intent = new Intent(VipUI.this, RegisterUI.class);
        startActivity(intent);

    }


    /**
     * 个人信息
     *
     * @param view
     */
    @OnClick(R.id.civ_vip_icon)
    private void getInfo(View view) {
        //有网络的情况下
        if (CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
            //没有个人信息的情况下
            if (InitInfo.vipInfoBean == null){
                Toast.makeText(this,"网络异常",Toast.LENGTH_SHORT).show();
                return;
            }else if(InitInfo.vipInfoBean.user_detail == null){
                Toast.makeText(this,"网络异常",Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isLogin) {
                getVipInfo();
            }
            Intent intent = new Intent(VipUI.this, VipInfoUI.class);
            startActivity(intent);
        }else {
            Toast.makeText(this,"请检查网络设置",Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 设置昵称
     *
     * @param view
     */
    @OnClick(R.id.tv_vip_nickname)
    private void getNickname(View view) {
        //有网络的情况下
        if (CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
            //没有个人信息的情况下
            if (InitInfo.vipInfoBean == null){
                Toast.makeText(this,"网络异常",Toast.LENGTH_SHORT).show();
                return;
            }else if(InitInfo.vipInfoBean.user_detail == null){
                Toast.makeText(this,"网络异常",Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isLogin) {
                getVipInfo();
            }
            Intent intent = new Intent(VipUI.this, VipInfoUI.class);
            startActivity(intent);
        }else {
            Toast.makeText(this,"请检查网络设置",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 完善个人信息
     *
     * @param view
     */
    @OnClick(R.id.tv_vip_info)
    private void getWanShanInfo(View view) {
        //有网络的情况下
        if (CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
            //没有个人信息的情况下
            if (InitInfo.vipInfoBean == null){
                Toast.makeText(this,"网络异常",Toast.LENGTH_SHORT).show();
                return;
            }else if(InitInfo.vipInfoBean.user_detail == null){
                Toast.makeText(this,"网络异常",Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isLogin) {
                getVipInfo();
            }
            Intent intent = new Intent(VipUI.this, VipInfoUI.class);
            startActivity(intent);
        }else {
            Toast.makeText(this,"请检查网络设置",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 我的动态
     *
     * @param view
     */
    @OnClick(R.id.ll_vip_mycircle_active)
    private void getMyActive(View view) {
        String ub_id = SpTools.getString(this, Constants.userId,"");
        if (TextUtils.isEmpty(ub_id)){
            Intent intent = new Intent(this, LoginUI.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(VipUI.this, MyCircleActiveActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 我的收藏
     *
     * @param view
     */
    @OnClick(R.id.tv_vip_collected)
    private void getMyConllected(View view) {
        String ub_id = SpTools.getString(this, Constants.userId,"");
        if (TextUtils.isEmpty(ub_id)){
            Intent intent = new Intent(this, LoginUI.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(VipUI.this, MyConllected.class);
            startActivity(intent);
        }
    }

    /**
     * 意见反馈
     *
     * @param view
     */
    @OnClick(R.id.ll_vip_opinion)
    private void getOpinion(View view) {
//        String ub_id = SpTools.getString(this, Constants.CONFIGFILE,"");
//        if (TextUtils.isEmpty(ub_id)){
//            Intent intent = new Intent(this, LoginUI.class);
//            startActivity(intent);
//        }else {
            Intent intent = new Intent(VipUI.this, OpinionActivity.class);
            startActivity(intent);
//        }
    }

    /**
     * 我的钱包
     *
     * @param view
     */
    @OnClick(R.id.ll_vip_mymoney)
    private void getMyMoney(View view) {
        String ub_id = SpTools.getString(this, Constants.userId,"");
        if (TextUtils.isEmpty(ub_id)){
            Intent intent = new Intent(this, LoginUI.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(VipUI.this, MyMoneyActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 设置
     *
     * @param view
     */
    @OnClick(R.id.ll_vip_setting)
    private void getSetting(View view) {
        String ub_id = SpTools.getString(this, Constants.userId,"");
        if (TextUtils.isEmpty(ub_id)){
            Intent intent = new Intent(this, LoginUI.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(VipUI.this, VipSettingUI.class);
            startActivity(intent);
        }
    }

    private void getVipInfo() {
        if (gson == null) {
            gson = new Gson();
        }
        String userId = SpTools.getString(this, Constants.userId, "");
        //登录操作
        LoginInternetRequest.getVipInfo(userId, new LoginInternetRequest.ForResultListener() {
            @Override
            public void onResponseMessage(String code) {
                if (!TextUtils.isEmpty(code)) {
                    VipInfoBean vipInfoBean = gson.fromJson(code, VipInfoBean.class);
                    if (vipInfoBean != null) {
                        InitInfo.phoneNumber = vipInfoBean.user_detail.ub_phone;
                        InitInfo.vipInfoBean = vipInfoBean;
                        isLogin = true;
                        SpTools.setString(VipUI.this, Constants.nik_name, vipInfoBean.user_detail.ud_nickname);
                    }
                }
            }
        });
    }



    boolean isLogin = false;
    private SsoHandler mSsoHandler;
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_vip_weixin){

            if (isLogin){
                return;
            }
            isLogin = true;
            //显示progressbar
            pb_progress.setVisibility(View.VISIBLE);
            backgroundAlpha(0.5f);
            ThirdWeiXinLoginApi.getWXAPI(getApplicationContext());
            ThirdWeiXinLoginApi.login(getApplicationContext());

        }else if (v.getId() == R.id.ll_login_qq){

            if (isLogin){
                return;
            }
            isLogin = true;
            //显示progressbar
            pb_progress.setVisibility(View.VISIBLE);
            backgroundAlpha(0.5f);
            ThirdQQLoginApi.getTencent(getApplicationContext());
            ThirdQQLoginApi.login(this, oauth, oauthlogin);

        }else if (v.getId() == R.id.ll_login_sina){

            if (isLogin){
                return;
            }
            isLogin = true;
            //显示progressbar
            pb_progress.setVisibility(View.VISIBLE);
            backgroundAlpha(0.5f);
            mSsoHandler = ThirdWeiBoLoginApi.getSsoHandler(this);
            ThirdWeiBoLoginApi.login(this, oauth, oauthlogin);

        }
    }

    public void backgroundAlpha(float bgAlpha)
    {
        if (bgAlpha == 1f){
            TabUI.setProgress(false);
        }else {
            TabUI.setProgress(true);
        }
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


    private MutiLoginProtocal protocal = new MutiLoginProtocal();
    //获取到三方登录的相关信息，连接到自己的服务器传送资料
    private void MuTiLogin(final String uuid, final String name, final String header, final String gender, final String loginType) {

        protocal.postMutiLogin(uuid, name, header, gender, loginType, new MutiLoginProtocal.MutiLoginListener() {
            @Override
            public void normalResponse(Object response) {
                if (response == null){
                    isLogin = false;
                    backgroundAlpha(1f);
                    pb_progress.setVisibility(View.GONE);
                    CommonUtils.toastMessage("登录失败，请稍后再试！");
                    return;
                }


                try{
                    Third1Bean bean = gson.fromJson((String) response,Third1Bean.class);
                    if ("10".equals(bean.result.code)){
                        //没有申请过
                        if ("0".equals(bean.userid.ub_id)){

                            registerNewThird(uuid, name, header, gender, loginType);


                        }else {
                            //申请过了 ，直接登录
                            isLogin = false;
                            backgroundAlpha(1f);
                            pb_progress.setVisibility(View.GONE);
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
        SpTools.setString(this, Constants.userId, ub_id);//存储用户的ub_id
        TabUI.getTabUI().init();
        TabUI.getTabUI().setCurrentTabByTag("tab1");
    }


    /**
     * QQ，微博授权回调
     */
    private OauthListener oauth = new OauthListener() {

        @Override
        public void OauthSuccess(Object obj) {
//            mProressbar.setText("正在为你登录");
//            mProressbar.setVisibility(View.VISIBLE);
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
                        String resid = errorCode == BaseResp.ErrCode.ERR_USER_CANCEL ? "授权取消" : "授";
                        Toast.makeText(VipUI.this, resid, Toast.LENGTH_SHORT).show();
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


    //一定要！！！！
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }

        if (requestCode == 333 && resultCode == 333&& data != null){
            //三方登录注册回来的
            String phone = data.getStringExtra("phone");
            String pwd = data.getStringExtra("pwd");

            protocal.postMutiNewLogin(tp_openid, tp_username, tp_head, tp_gender, type, phone, pwd, new MutiLoginProtocal.MutiLoginListener() {
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
                            thirdLogin(bean.userid.ub_id);
                            refreshVip();
                            TabUI.getTabUI().setCurrentTabByTag("tab1");
                        }else {
                            isLogin = false;
                            backgroundAlpha(1f);
                            pb_progress.setVisibility(View.GONE);
                            CommonUtils.toastMessage("登录失败，请重新尝试");
                        }

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
