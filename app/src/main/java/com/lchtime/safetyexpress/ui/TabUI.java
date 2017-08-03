package com.lchtime.safetyexpress.ui;

import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.dalong.recordlib.RecordVideoActivity;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.bean.ContactListBean;
import com.hyphenate.easeui.bean.EaseInitBean;
import com.hyphenate.easeui.domain.EaseUser;
import com.igexin.sdk.PushManager;
import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.CircleRedPointBean;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.GetUpBean;
import com.lchtime.safetyexpress.bean.InitInfo;
import com.lchtime.safetyexpress.bean.PostBean;
import com.lchtime.safetyexpress.bean.ProfessionBean;
import com.lchtime.safetyexpress.bean.VipInfoBean;
import com.lchtime.safetyexpress.pop.GuiZePop;
import com.lchtime.safetyexpress.ui.add.ChatEmptyUI;
import com.lchtime.safetyexpress.ui.chat.hx.Constant;
import com.lchtime.safetyexpress.ui.chat.hx.DemoHelper;
import com.lchtime.safetyexpress.ui.chat.hx.activity.HXMainActivity;
import com.lchtime.safetyexpress.ui.chat.hx.activity.protocal.GetInfoProtocal;
import com.lchtime.safetyexpress.ui.chat.hx.db.DemoDBManager;
import com.lchtime.safetyexpress.ui.chat.hx.db.TopUserDao;
import com.lchtime.safetyexpress.ui.chat.hx.fragment.protocal.AddCommandProtocal;
import com.lchtime.safetyexpress.ui.circle.CirclePublishActivity;
import com.lchtime.safetyexpress.ui.circle.CircleUI;
import com.lchtime.safetyexpress.ui.circle.PublishCircleUI;
import com.lchtime.safetyexpress.ui.circle.protocal.CircleProtocal;
import com.lchtime.safetyexpress.ui.home.AskQuestionActivity;
import com.lchtime.safetyexpress.ui.home.HomeUI;
import com.lchtime.safetyexpress.ui.home.protocal.HomeQuestionProtocal;
import com.lchtime.safetyexpress.ui.vip.VipUI;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.LoginInternetRequest;
import com.lchtime.safetyexpress.utils.SpTools;

import java.io.File;
import java.util.List;
import java.util.Map;

import service.DemoPushService;


/**
 * TabUI  主界面  里面放有五个界面
 * Created by user on 2017/3/13.
 */

public class TabUI extends TabActivity implements OnClickListener {

    public static final int TAKE_DATA = 200;

    private static TabUI tabUI;
//    private MyApplication application;

    private RadioButton rb_tab_1;
    private RadioButton rb_tab_2;
    private RadioButton rb_tab_3;
    private RadioButton rb_tab_4;
    private RadioButton rb_tab_5;
    private RadioGroup rg_tab;
    private CheckBox tab_check;
    private TabHost tabHost;
    private LinearLayout unclickble_pic;
    private View unclickble_view;
    private RelativeLayout showWindow;
    private LinearLayout publictv;
    private LinearLayout publiccamera;
    private LinearLayout publicquestion;
    private View none;
    private View fire_event;
    private String videoPath;
    private LinearLayout jiangli_guize;
    private static final int REQUEST_PERMISSION = 0;


    private String userId;
    private GuiZePop guiZePop;
    private static RelativeLayout pb_progress;
    private static ProgressBar progress;
    private TextView unread_msg_number;
    private TextView unread_msg_number_circle;

    private Map<String, EaseUser> topMap;


    private static final String TAG = "TabUI";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tab);

        mTabUI = this;


        tabUI = this;
        Drawable drawable;
        int right = CommonUtils.getDimen(this, R.dimen.dm044);
        int bottom = CommonUtils.getDimen(this, R.dimen.dm036);
        rg_tab = (RadioGroup) findViewById(R.id.rg_tab);
        tab_check = (CheckBox) findViewById(R.id.tab_check);
        showWindow = (RelativeLayout) findViewById(R.id.show_window);
        unclickble_pic = (LinearLayout) findViewById(R.id.ll_unclickble_pic);
        unclickble_view =  findViewById(R.id.ll_unclickble_view);
        none = findViewById(R.id.view_none);
        fire_event = findViewById(R.id.fire_event);
        publictv = (LinearLayout) findViewById(R.id.circle_public_tv);
        publiccamera = (LinearLayout) findViewById(R.id.circle_public_camera);
        publicquestion = (LinearLayout) findViewById(R.id.circle_public_question);
        jiangli_guize = (LinearLayout) findViewById(R.id.jiangli_guize);
        pb_progress = (RelativeLayout) findViewById(R.id.pb_progress);
        progress = (ProgressBar) findViewById(R.id.progress);
        unread_msg_number = (TextView) findViewById(R.id.unread_msg_number);
        unread_msg_number_circle = (TextView) findViewById(R.id.unread_msg_number_circle);
        unread_msg_number.setVisibility(View.GONE);
        unread_msg_number_circle.setVisibility(View.GONE);
        publictv.setOnClickListener(this);
        publiccamera.setOnClickListener(this);
        publicquestion.setOnClickListener(this);
        none.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showWindow.setVisibility(View.GONE);
                tab_check.setChecked(false);
            }
        });
        fire_event.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //为了消费事件,什么都不用写
            }
        });


        unclickble_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


        //弹框，弹出奖励规则
        jiangli_guize.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showWindow.setVisibility(View.GONE);
                tab_check.setChecked(false);
                guiZePop = new GuiZePop(jiangli_guize, TabUI.this, R.layout.pop_guize);
                guiZePop.showAtLocation();
                guiZePop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        guiZePop.dismiss();
                    }
                });
            }
        });

        tab_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    showWindow.setVisibility(View.VISIBLE);
                }else {
                    showWindow.setVisibility(View.GONE);
                }

            }
        });
        initVideoPath();

        //initShowWindow();
        rb_tab_1 = (RadioButton) findViewById(R.id.rb_tab_1);
        rb_tab_1.setOnClickListener(this);
        rb_tab_1.setText("首页");
        drawable = getResources().getDrawable(R.drawable.tab_1);
        drawable.setBounds(0, 0, CommonUtils.getDimen(this, R.dimen.dm044), CommonUtils.getDimen(this, R.dimen.dm036));
        //drawable.setBounds(0, 0, right, bottom);
        rb_tab_1.setCompoundDrawables(null, drawable, null, null);

        rb_tab_2 = (RadioButton) findViewById(R.id.rb_tab_2);
        rb_tab_2.setOnClickListener(this);
        rb_tab_2.setText("圈子");
        drawable = getResources().getDrawable(R.drawable.tab_2);
        drawable.setBounds(0, 0,CommonUtils.getDimen(this, R.dimen.dm036), CommonUtils.getDimen(this, R.dimen.dm038));
        rb_tab_2.setCompoundDrawables(null, drawable, null, null);
//        rb_tab_3 = (RadioButton) findViewById(R.id.rb_tab_3);
//        rb_tab_3.setOnClickListener(this);
//        rb_tab_3.setText("");
        rb_tab_4 = (RadioButton) findViewById(R.id.rb_tab_4);
        rb_tab_4.setOnClickListener(this);
        rb_tab_4.setText("聊天");
        drawable = getResources().getDrawable(R.drawable.tab_4);
        drawable.setBounds(0, 0, CommonUtils.getDimen(this, R.dimen.dm040), CommonUtils.getDimen(this, R.dimen.dm036));
        rb_tab_4.setCompoundDrawables(null, drawable, null, null);
        rb_tab_5 = (RadioButton) findViewById(R.id.rb_tab_5);
        rb_tab_5.setOnClickListener(this);
        rb_tab_5.setText("我的");
        drawable = getResources().getDrawable(R.drawable.tab_5);
        drawable.setBounds(0, 0,CommonUtils.getDimen(this, R.dimen.dm037), CommonUtils.getDimen(this, R.dimen.dm036));
        rb_tab_5.setCompoundDrawables(null, drawable, null, null);
        TabHost.TabSpec spec;
        Intent intent = null;
        tabHost = getTabHost();
        //首页
        intent = new Intent().setClass(this, HomeUI.class);
        spec = tabHost.newTabSpec("tab1").setIndicator("tab1").setContent(intent);
        tabHost.addTab(spec);
        //圈子
        intent = new Intent().setClass(this, CircleUI.class);
        spec = tabHost.newTabSpec("tab2").setIndicator("tab2").setContent(intent);
        tabHost.addTab(spec);

        //聊天没有登录显示的界面
        intent = new Intent().setClass(this, ChatEmptyUI.class);
        spec = tabHost.newTabSpec("tab3").setIndicator("tab3").setContent(intent);
        tabHost.addTab(spec);
        //聊天登录后显示的界面
        intent = new Intent().setClass(this, HXMainActivity.class);
        spec = tabHost.newTabSpec("tab4").setIndicator("tab4").setContent(intent);
        tabHost.addTab(spec);
        //我的界面
        intent = new Intent().setClass(this, VipUI.class);
        spec = tabHost.newTabSpec("tab5").setIndicator("tab5").setContent(intent);
        tabHost.addTab(spec);
        HomeUI.homeUI_instance.setHomeToCircleInterface(new HomeUI.HomeToCircleInterface() {
            @Override
            public void toCircleActivity() {
                setCurrentTabByTag("tab2");
            }
        });
        registerBroadcastReceiver();
        updateUnreadLabel();
        init(true);

       /* mReceiver = new UiReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,
                new IntentFilter("ACTION_LOGIN_SUCEESS"));*/

    }
   // private UiReceiver mReceiver;
    private ProgressDialog mDialog;
    //只是splash 使用
    public void init(boolean flag) {
        initRedPoint();
    }

    public void init() {
        getVipInfo();
        topMap = MyApplication.getInstance().getTopUserList();
        initUpData();
        initHXFriends();

        initRedPoint();
    }

    private String ub_id;
    private CircleProtocal mCircleProtocal = new CircleProtocal();
    private void initRedPoint() {
        if (TextUtils.isEmpty(ub_id)){
            ub_id = SpTools.getUserId(this);
        }
        mCircleProtocal.getDyIsShowRedPoint(ub_id, new CircleProtocal.NormalListener() {
            @Override
            public void normalResponse(Object response) {
                if (response == null){
                    CommonUtils.toastMessage("请求网络数据失败，请刷新重试");
                    return;
                }

                CircleRedPointBean bean = (CircleRedPointBean) response;
                if ("0".equals(bean.newqz)){
                    setCircleRedPoint(false);
                }else {
                    setCircleRedPoint(true);
                }
            }
        });
    }


    private HomeQuestionProtocal protocal = new HomeQuestionProtocal();
    private Gson gson = new Gson();
    private void initHXFriends() {
        protocal.getMyFriends(new HomeQuestionProtocal.QuestionListener() {
            @Override
            public void questionResponse(Object response) {
                if (response == null){
                    CommonUtils.toastMessage("请求好友数据失败，请稍后再试！");
                    return;
                }
                try {
                    ContactListBean bean = gson.fromJson((String) response, ContactListBean.class);
                    if ("10".equals(bean.result.code)){
                        if (bean.friendlist == null || bean.friendlist.size() == 0){
                            return;
                        }
                        EaseInitBean.contactBean= bean;
                    }else {
                        CommonUtils.toastMessage("请求好友数据失败，请稍后再试！");
                    }
                }catch (Exception exception){
                 //   CommonUtils.toastMessage("请求好友数据失败，请稍后再试！");
                }

            }
        });


    }
 /*   private class UiReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if ("ACTION_LOGIN_SUCEESS".equals(intent.getAction())) {
                final String code = intent.getStringExtra("code" );
                if (!TextUtils.isEmpty(code)) {
                    Log.i("qaz", "onReceive: " + "1----------------");


                }
            }
        }
    }*/


    private void initVideoPath() {
        File path=new File(Environment.getExternalStorageDirectory(),
                "kuaichevideo");
        if (!path.exists()) {
            path.mkdirs();
        }
        videoPath=path.getAbsolutePath()+File.separator+System.currentTimeMillis()+".mp4";
    }
    //拍摄功能
    /*private void initShowWindow() {
        publiccamera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!hasPermission(new String[]{"android.permission.READ_EXTERNAL_STORAGE","android.permission.CAMERA","android.permission.RECORD_AUDIO"})) {
                    requestPermission(1, new String[]{"android.permission.READ_EXTERNAL_STORAGE","android.permission.CAMERA","android.permission.RECORD_AUDIO"});
                } else {
                    Intent intent=new Intent(TabUI.this, RecordVideoActivity.class);
                    intent.putExtra(RecordVideoActivity.RECORD_VIDEO_PATH,videoPath);
                    startActivityForResult(intent,TAKE_DATA);
                }

            }
        });
    }*/

    private void getVipInfo() {
        if (gson == null){
            gson = new Gson();
        }
        userId = SpTools.getString(this, Constants.userId,"");
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
                            InitInfo.phoneNumber = vipInfoBean.user_detail.ub_phone;
                            InitInfo.vipInfoBean = vipInfoBean;
                           // vipInfoBean = vipInfoBean;
                            SpTools.setString(TabUI.this, Constants.nik_name,vipInfoBean.user_detail.ud_nickname);
                            SpTools.setString(TabUI.this , Constants.ud_profession,vipInfoBean.user_detail.ud_profession);
                            SpTools.setString(TabUI.this , Constants.ud_post,vipInfoBean.user_detail.ud_post);
                            SpTools.setString(TabUI.this , Constants.ud_addr,vipInfoBean.user_detail.ud_addr);
                           /* Log.i("qaz", "onResponseMessage: " + vipInfoBean.user_detail.ud_profession +
                                    vipInfoBean.user_detail.ud_post + vipInfoBean.user_detail.ud_addr);*/
                            loginHX(vipInfoBean.user_detail.ub_phone, Constant.HX_PWD);

                        }
                    }
                }
            });
        }

        //获取行业、岗位
        LoginInternetRequest.getProfession("", new LoginInternetRequest.ForResultListener() {
            @Override
            public void onResponseMessage(String code) {
                InitInfo.professionBean = gson.fromJson(code, ProfessionBean.class);

            }
        });

        LoginInternetRequest.getPost("", new LoginInternetRequest.ForResultListener() {
            @Override
            public void onResponseMessage(String code) {
                InitInfo.postBean = gson.fromJson(code, PostBean.class);

            }
        });
    }

    /**
     * 保存用户信息
     * @param vipInfoBean
     */
    /*private void saveVipInfoBean(VipInfoBean vipInfoBean) {
        if (vipInfoBean != null) {
            InitInfo.phoneNumber = vipInfoBean.user_detail.ub_phone;
            InitInfo.vipInfoBean = vipInfoBean;
            InitInfo.isLogin = true;
            SpTools.setString(mContext , Constants.userId , vipInfoBean.user_detail.ub_id);
            SpTools.setString(mContext , Constants.nik_name,vipInfoBean.user_detail.ud_nickname);
            SpTools.setString(mContext , Constants.ud_profession,vipInfoBean.user_detail.ud_profession);
            SpTools.setString(mContext , Constants.ud_post,vipInfoBean.user_detail.ud_post);
            SpTools.setString(mContext , Constants.ud_addr,vipInfoBean.user_detail.ud_addr);
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
    }*/

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


                // ** manually load all local groups and conversation
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();

                // update current user's display name for APNs
                boolean updatenick = EMClient.getInstance().updateCurrentUserNick(
                        MyApplication.currentUserNick.trim());
                if (!updatenick) {
                    Log.e("LoginActivity", "update current user nick fail");
                }

                if (!TabUI.this.isFinishing() ) {
//                    setProgress(true);
//                    progress.setVisibility(View.VISIBLE);
                }
                // get user's info (this should be get from App's server or 3rd party service)
                DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();

//                Intent intent = new Intent(this,
//                        MainActivity.class);
//                startActivity(intent);

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
//                setProgress(false);
//                progress.setVisibility(View.GONE);
            }
        });
    }


    public String currentTag = "tab1";
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_tab_1:
                currentTag = "tab1";
                setCurrentTabByTag("tab1");
                break;
            case R.id.rb_tab_2:
                currentTag = "tab2";
                setCurrentTabByTag("tab2");


                //如果是第一次那么就显示奖励规则
                if (InitInfo.isShowJLGZ){
                    InitInfo.isShowJLGZ = false;
                    guiZePop = new GuiZePop(jiangli_guize, TabUI.this, R.layout.pop_guize);
                    guiZePop.showAtLocation();
                    guiZePop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            guiZePop.dismiss();
                        }
                    });
                }
                break;
            case R.id.rb_tab_3:

//                setCurrentTabByTag("tab3");
                break;
            case R.id.rb_tab_4:
                currentTag = "tab4";
                if (TextUtils.isEmpty(ub_id)){
                    ub_id = SpTools.getUserId(this);
                }

                if (TextUtils.isEmpty(ub_id)){

                    setCurrentTabByTag("tab3");

                    return;
                }
                setCurrentTabByTag("tab4");
                break;
            case R.id.rb_tab_5:
                currentTag = "tab5";
                setCurrentTabByTag("tab5");
                break;
            case R.id.circle_public_tv:  //发布文字
                if(isFullPersionDate()){
                    startActivity(new Intent(this, PublishCircleUI.class));
                }else{
                    CommonUtils.toastMessage("请完善个人信息");
                }
                break;
            case R.id.circle_public_camera: //拍摄
                String ub_id = SpTools.getString(this, Constants.userId,"");

                if (TextUtils.isEmpty(ub_id)){
                    CommonUtils.toastMessage("登录后才能发布圈子！");
                    return;
                }
                if(!hasPermission(new String[]{"android.permission.READ_EXTERNAL_STORAGE","android.permission.CAMERA","android.permission.RECORD_AUDIO"})) {
                    requestPermission(1, new String[]{"android.permission.READ_EXTERNAL_STORAGE","android.permission.CAMERA","android.permission.RECORD_AUDIO"});
                } else {
                    if(isFullPersionDate()){
                        Intent intent=new Intent(TabUI.this, RecordVideoActivity.class);
                        intent.putExtra(RecordVideoActivity.RECORD_VIDEO_PATH,videoPath);
                        startActivityForResult(intent,TAKE_DATA);
                    }else{
                        CommonUtils.toastMessage("请完善个人信息");
                    }
                }
                break;
            case R.id.circle_public_question:  //提问
                if(isFullPersionDate()){
                    Intent intent = new Intent(this,AskQuestionActivity.class);
                    startActivity(intent);
                }else{
                    CommonUtils.toastMessage("请完善个人信息");
                }
                break;
        }
        tab_check.setChecked(false);
    }

    /**
     * 检查个人资料是否完善，检查行业，岗位，地理位置
     */
    private boolean isFullPersionDate(){
        String  profession = SpTools.getString(this, Constants.ud_profession , "") ;
        String post  = SpTools.getString(this, Constants.ud_post , "");
        String addr = SpTools.getString(this, Constants.ud_addr , "");
        /*Log.i("qaz", "isFullPersionDate: "  + profession +post
                + addr);*/
        return (!SpTools.getString(TabUI.this, Constants.ud_profession , "").equals("")&&
        !SpTools.getString(TabUI.this, Constants.ud_post , "").equals("")&&
        !SpTools.getString(TabUI.this, Constants.ud_addr , "").equals(""));


    }

    public void setCurrentTabByTag(String tag) {
        rb_tab_1.setChecked("tab1".equals(tag));
        rb_tab_2.setChecked("tab2".equals(tag));
//        rb_tab_3.setChecked();
        rb_tab_4.setChecked("tab4".equals(tag)||"tab3".equals(tag));
        rb_tab_5.setChecked("tab5".equals(tag));
        tabHost.setCurrentTabByTag(tag);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case TAKE_DATA:
                if(resultCode==RecordVideoActivity.TAKE_VIDEO_CODE){
                    String videoPath=data.getStringExtra(RecordVideoActivity.TAKE_VIDEO_PATH);
                    Intent publishIntent = new Intent(TabUI.this, CirclePublishActivity.class);
                    publishIntent.putExtra("path",videoPath);
                    startActivity(publishIntent);

                    //Toast.makeText(this, "视频路径："+videoPath, Toast.LENGTH_SHORT).show();
                }
//                }else if(resultCode==RecordVideoActivity.TAKE_PHOTO_CODE){
//                    String photoPath=data.getStringExtra(RecordVideoActivity.TAKE_PHOTO_PATH);
//                    Toast.makeText(this, "图片路径："+photoPath, Toast.LENGTH_SHORT).show();
//                }
                break;
        }
    }


    /*
     *
     *以下为  为了小视频申请动态权限
     */
    protected boolean hasPermission(String... permissions) {
        String[] arr = permissions;
        int length = permissions.length;

        for(int i = 0; i < length; ++i) {
            String permission = arr[i];
            if(ContextCompat.checkSelfPermission(this, permission) != 0) {
                return false;
            }
        }

        return true;
    }


    protected void requestPermission(int code, String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, code);
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case 1:
                if(grantResults.length >= 2
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED) {

                    Intent intent=new Intent(TabUI.this, RecordVideoActivity.class);
                    intent.putExtra(RecordVideoActivity.RECORD_VIDEO_PATH,videoPath);
                    startActivityForResult(intent,TAKE_DATA);

                } else {
                    CommonUtils.toastMessage("读取内存卡/相机权限/录音权限已被拒绝");
                }
                break;
        }

        if (requestCode == REQUEST_PERMISSION) {
            if ((grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                PushManager.getInstance().initialize(this.getApplicationContext(), DemoPushService.class);
            } else {
                Log.e(TAG, "We highly recommend that you need to grant the special permissions before initializing the SDK, otherwise some "
                        + "functions will not work");
                PushManager.getInstance().initialize(this.getApplicationContext(), DemoPushService.class);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    private GetInfoProtocal mProtocal = new GetInfoProtocal();

    private void initUpData() {
        String ub_id = SpTools.getString(this, Constants.userId,"");
        if (TextUtils.isEmpty(ub_id)){
            CommonUtils.toastMessage("聊天置顶信息获取失败，请登录后重试！");
            return;
        }
        //
        mProtocal.getUp(ub_id, "0", "", new AddCommandProtocal.NormalListener() {
            @Override
            public void normalResponse(Object response) {
                if (response == null){
                    CommonUtils.toastMessage("聊天置顶信息获取失败，请稍后重试!");
                    return;
                }

                GetUpBean bean = gson.fromJson((String) response,GetUpBean.class);
                if ("10".equals(bean.result.code)){
                    if (bean.hx_account == null || bean.hx_account.size() == 0){
                        return;
                    }

                    InitInfo.up_accounts.addAll(bean.hx_account);
                    TopUserDao dao = new TopUserDao(TabUI.this);
                    for (int i = 0;i < InitInfo.up_accounts.size();i++){
                        String account = InitInfo.up_accounts.get(i);
                        if (topMap.containsKey(account)){
                            continue;
                        }
                        EaseUser user = new EaseUser(account);
                        topMap.put(account,user);
                        dao.saveContact(user);
                    }

                }else {
                    CommonUtils.toastMessage(bean.result.info);
                }




            }
        });
    }

    public static void setProgress(boolean show){
        if (pb_progress != null) {
            if (show) {
                pb_progress.setVisibility(View.VISIBLE);
            } else {
                pb_progress.setVisibility(View.GONE);
            }
        }
    }



    public void setCircleRedPoint(boolean isShow){
        if (isShow) {
            unread_msg_number_circle.setVisibility(View.VISIBLE);
        }else {
            unread_msg_number_circle.setVisibility(View.GONE);
        }
    }

    private static TabUI mTabUI;
    public static TabUI getTabUI (){
        return mTabUI;
    }

    public void updateUnreadLabel(){
        int count = getUnreadMsgCountTotal();
        if (count > 0) {
            unread_msg_number.setVisibility(View.VISIBLE);
            unread_msg_number.setText(count + "");
        }else if (count > 99) {
            unread_msg_number.setVisibility(View.VISIBLE);
            unread_msg_number.setText("99+");
        }else {
            unread_msg_number.setVisibility(View.GONE);
        }
    }
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        int chatroomUnreadMsgCount = 0;
        unreadMsgCountTotal = EMClient.getInstance().chatManager().getUnreadMsgsCount();
        for(EMConversation conversation:EMClient.getInstance().chatManager().getAllConversations().values()){
            if(conversation.getType() == EMConversation.EMConversationType.ChatRoom)
                chatroomUnreadMsgCount=chatroomUnreadMsgCount+conversation.getUnreadMsgCount();
        }
        return unreadMsgCountTotal-chatroomUnreadMsgCount;
    }

    private LocalBroadcastManager broadcastManager;
    private BroadcastReceiver broadcastReceiver;
    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(Constant.ACTION_GROUP_CHANAGED);
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                updateUnreadLabel();

                String action = intent.getAction();
                if(action.equals(Constant.ACTION_CONTACT_CHANAGED)){
//                    if (EaseCommonUtils.getTopActivity(TabUI.this).equals(GroupsActivity.class.getName())) {
//                        GroupsActivity.instance.onResume();
//                    }
                }

//                if(action.equals(Constant.ACTION_GROUP_CHANAGED)){
//                    if (EaseCommonUtils.getTopActivity(MainActivity.this).equals(GroupsActivity.class.getName())) {
//                        GroupsActivity.instance.onResume();
//                    }
//                }

            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    private void unregisterBroadcastReceiver(){
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterBroadcastReceiver();

//        try {
//            unregisterReceiver(internalDebugReceiver);
//        } catch (Exception e) {
//        }

    }


    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            // notify new message
            for (EMMessage message : messages) {
                DemoHelper.getInstance().getNotifier().onNewMsg(message);
            }
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //red packet code : 处理红包回执透传消息
            for (EMMessage message : messages) {
                EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
                final String action = cmdMsgBody.action();//获取自定义action
            }
            //end of red packet code
            refreshUIWithMessage();
        }

        @Override
        public void onMessageRead(List<EMMessage> list) {

        }

        @Override
        public void onMessageDelivered(List<EMMessage> list) {

        }


        @Override
        public void onMessageChanged(EMMessage message, Object change) {}
    };


    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
                // refresh unread count
                updateUnreadLabel();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ub_id = SpTools.getUserId(this);
        if ("tab4".equals(currentTag)){


            if (TextUtils.isEmpty(ub_id)){
                currentTag = "tab4";
                setCurrentTabByTag("tab3");

                return;
            }

            currentTag = "tab4";
            setCurrentTabByTag("tab4");
        }


        if (TextUtils.isEmpty(ub_id)){
            ub_id = SpTools.getUserId(this);
        }

        if (!TextUtils.isEmpty(ub_id)){
            EMClient.getInstance().chatManager().addMessageListener(messageListener);
            updateUnreadLabel();
            initRedPoint();
        }
    }

    @Override
    protected void onStop() {
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.popActivity(this);

        super.onStop();
    }
}
