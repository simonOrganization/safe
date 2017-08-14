package com.lchtime.safetyexpress.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.bean.ContactListBean;
import com.hyphenate.easeui.bean.EaseInitBean;
import com.hyphenate.easeui.domain.EaseUser;
import com.igexin.sdk.PushManager;
import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.GetUpBean;
import com.lchtime.safetyexpress.bean.InitInfo;
import com.lchtime.safetyexpress.bean.PostBean;
import com.lchtime.safetyexpress.bean.ProfessionBean;
import com.lchtime.safetyexpress.bean.VipInfoBean;
import com.lchtime.safetyexpress.ui.chat.hx.Constant;
import com.lchtime.safetyexpress.ui.chat.hx.DemoHelper;
import com.lchtime.safetyexpress.ui.chat.hx.activity.protocal.GetInfoProtocal;
import com.lchtime.safetyexpress.ui.chat.hx.db.DemoDBManager;
import com.lchtime.safetyexpress.ui.chat.hx.db.TopUserDao;
import com.lchtime.safetyexpress.ui.chat.hx.fragment.protocal.AddCommandProtocal;
import com.lchtime.safetyexpress.ui.home.protocal.HomeQuestionProtocal;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.LoginInternetRequest;
import com.lchtime.safetyexpress.utils.SpTools;

import java.util.Map;

import service.DemoIntentService;
import service.DemoPushService;

/**
 * Created by Dreamer on 2017/6/22.
 */

public class Splash extends Activity {

    private static final int REQUEST_PERMISSION = 0;
    private MyApplication application;
    private String userId;
    private Map<String, EaseUser> topMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initData();
    }


    private void initData() {
        /*PackageManager pkgManager = getPackageManager();

        // 读写 sd card 权限非常重要, android6.0默认禁止的, 建议初始化之前就弹窗让用户赋予该权限
        boolean sdCardWritePermission =
                pkgManager.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, getPackageName()) == PackageManager.PERMISSION_GRANTED;

        // read phone state用于获取 imei 设备信息
        boolean phoneSatePermission =
                pkgManager.checkPermission(Manifest.permission.READ_PHONE_STATE, getPackageName()) == PackageManager.PERMISSION_GRANTED;

        if (Build.VERSION.SDK_INT >= 23 && !sdCardWritePermission || !phoneSatePermission) {
            requestPermission();
        }else {
            // com.getui.demo.DemoPushService 为第三方自定义推送服务
            PushManager.getInstance().initialize(this.getApplicationContext(), DemoPushService.class);
        }*/
        // com.getui.demo.DemoIntentService 为第三方自定义的推送服务事件接收类
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), DemoIntentService.class);

        application = (MyApplication) getApplication();

        getVipInfo();
        topMap = MyApplication.getInstance().getTopUserList();
        initUpData();
        initHXFriends();



    }

    private boolean info = false;
    private void getVipInfo() {
        if (gson == null){
            gson = new Gson();
        }
        userId = SpTools.getUserId(this);
        if (!TextUtils.isEmpty(userId)){
            PushManager.getInstance().bindAlias(this,userId);
            PushManager.getInstance().turnOnPush(this);
            //登录操作
            LoginInternetRequest.getVipInfo(userId, new LoginInternetRequest.ForResultListener() {
                @Override
                public void onResponseMessage(String code) {
                    if(!TextUtils.isEmpty(code)) {
                        VipInfoBean vipInfoBean = gson.fromJson(code, VipInfoBean.class);
                        if (vipInfoBean != null) {
                            SpTools.saveUser(Splash.this , vipInfoBean);
                            loginHX(vipInfoBean.user_detail.getHXId(), Constant.HX_PWD);
                        }else {
                            loginHx = true;
                            isShowTab();
                        }
                    }else {
                        loginHx = true;
                        isShowTab();
                    }

                    initHy();
                }
            });
        }else {
            loginHx = true;
            isShowTab();
            initHy();
        }




    }


    private void initHy() {
        //获取行业、岗位
        LoginInternetRequest.getProfession("", new LoginInternetRequest.ForResultListener() {
            @Override
            public void onResponseMessage(String code) {
                if (!TextUtils.isEmpty(code)) {
                    InitInfo.professionBean = gson.fromJson(code, ProfessionBean.class);
                }else {
                    //CommonUtils.toastMessage("初始化行业失败！");
                }
                initGw();
            }
        });
    }

    private void initGw() {
        LoginInternetRequest.getPost("", new LoginInternetRequest.ForResultListener() {
            @Override
            public void onResponseMessage(String code) {
                if (!TextUtils.isEmpty(code)) {
                    InitInfo.postBean = gson.fromJson(code, PostBean.class);
                }else {
                    //CommonUtils.toastMessage("初始化岗位失败");
                }
                info = true;
                isShowTab();
            }
        });
    }



    private boolean upData = false;
    private GetInfoProtocal mProtocal = new GetInfoProtocal();
    private void initUpData() {
        //String ub_id = SpTools.getString(this, Constants.userId,"");
        if (TextUtils.isEmpty(userId)){
            //CommonUtils.toastMessage("聊天置顶信息获取失败，请登录后重试！");
            upData = true;
            isShowTab();
            return;
        }
        //
        mProtocal.getUp(userId, "0", "", new AddCommandProtocal.NormalListener() {
            @Override
            public void normalResponse(Object response) {
                if (response == null){
                    CommonUtils.toastMessage("聊天置顶信息获取失败，请稍后重试!");
                    upData = true;
                    isShowTab();
                    return;
                }

                GetUpBean bean = gson.fromJson((String) response,GetUpBean.class);
                if ("10".equals(bean.result.code)){
                    if (bean.hx_account == null || bean.hx_account.size() == 0){
                        upData = true;
                        isShowTab();
                        return;
                    }

                    InitInfo.up_accounts.addAll(bean.hx_account);
                    TopUserDao dao = new TopUserDao(Splash.this);
                    for (int i = 0;i < InitInfo.up_accounts.size();i++){
                        String account = InitInfo.up_accounts.get(i);
                        if (topMap.containsKey(account)){
                            continue;
                        }
                        EaseUser user = new EaseUser(account);
                        topMap.put(account,user);
                        dao.saveContact(user);
                    }

                    upData = true;
                    isShowTab();

                }else {
                    CommonUtils.toastMessage(bean.result.info);
                    upData = true;
                    isShowTab();
                }

            }
        });
    }

    private boolean hx = false;
    private HomeQuestionProtocal protocal = new HomeQuestionProtocal();
    private Gson gson = new Gson();
    private void initHXFriends() {
        if(!TextUtils.isEmpty(userId)){
            protocal.getMyFriends(new HomeQuestionProtocal.QuestionListener() {
                @Override
                public void questionResponse(Object response) {
                    if (response == null){
                        //CommonUtils.toastMessage("请求好友数据失败，请稍后再试！");
                        hx = true;
                        isShowTab();
                        return;
                    }
                    try {
                        ContactListBean bean = gson.fromJson((String) response, ContactListBean.class);
                        if ("10".equals(bean.result.code)){
                            if (bean.friendlist == null || bean.friendlist.size() == 0){
                                hx = true;
                                isShowTab();
                                return;
                            }
                            EaseInitBean.contactBean= bean;
                            hx = true;
                            isShowTab();
                        }else {
                            //CommonUtils.toastMessage("请求好友数据失败，请稍后再试！");
                            hx = true;
                            isShowTab();
                        }
                    }catch (Exception exception){
                        //CommonUtils.toastMessage("请求好友数据失败，请稍后再试！");
                        hx = true;
                        isShowTab();
                    }

                }
            });
        }else{
            hx = true;
            isShowTab();
        }


    }

    private boolean loginHx = false;
    private void loginHX(String currentUsername,String currentPassword) {
        DemoDBManager.getInstance().closeDB();

        // reset current user name before login
        DemoHelper.getInstance().setCurrentUserName(currentUsername);

        final long start = System.currentTimeMillis();
        // call login method
//        Log.d(TAG, "EMClient.getInstance().login");
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
           // CommonUtils.toastMessage("您当前无网络，请联网再试");
            loginHx = true;
            isShowTab();
            return;
        }

        EMClient.getInstance().login(currentUsername, currentPassword, new EMCallBack() {

            @Override
            public void onSuccess() {


                // ** manually load all local groups and conversation
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();

                // update current user's display name for APNs
                boolean updatenick = EMClient.getInstance().updateCurrentUserNick(
                        MyApplication.currentUserNick.trim());
                if (!updatenick) {
                    Log.e("LoginActivity", "update current user nick fail");
                }

                if (!Splash.this.isFinishing() ) {
//                    setProgress(true);
//                    progress.setVisibility(View.VISIBLE);
                }
                // get user's info (this should be get from App's server or 3rd party service)
                DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();

//                Intent intent = new Intent(this,
//                        MainActivity.class);
//                startActivity(intent);

                loginHx = true;
                isShowTab();

            }

            @Override
            public void onProgress(int progress, String status) {
                Log.d("", "login: onProgress");
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
//                setProgress(false);
//                progress.setVisibility(View.GONE);

                loginHx = true;
                isShowTab();
            }
        });
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE},
                REQUEST_PERMISSION);
    }


    private void isShowTab() {
        synchronized (this) {
            if (info && upData && hx && loginHx) {
                //是否走引导页
                boolean isYD = SpTools.getBoolean(this,"first",true);
                if (isYD){
                    //走引导页
                    Intent intent = new Intent(this,YDActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(Splash.this, TabUI.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }




}
