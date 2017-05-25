package com.lchtime.safetyexpress.ui;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TabHost;

import com.dalong.recordlib.RecordVideoActivity;
import com.google.gson.Gson;
import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.InitInfo;
import com.lchtime.safetyexpress.bean.PostBean;
import com.lchtime.safetyexpress.bean.ProfessionBean;
import com.lchtime.safetyexpress.bean.VipInfoBean;
import com.lchtime.safetyexpress.pop.GuiZePop;
import com.lchtime.safetyexpress.pop.VipInfoHintPop;
import com.lchtime.safetyexpress.ui.add.AddUI;
import com.lchtime.safetyexpress.ui.chat.ChatUI;
import com.lchtime.safetyexpress.ui.circle.CirclePublishActivity;
import com.lchtime.safetyexpress.ui.circle.CircleUI;
import com.lchtime.safetyexpress.ui.circle.PublishCircleUI;
import com.lchtime.safetyexpress.ui.home.AskQuestionActivity;
import com.lchtime.safetyexpress.ui.home.HomeUI;
import com.lchtime.safetyexpress.ui.login.RegisterUI;
import com.lchtime.safetyexpress.ui.vip.VipInfoUI;
import com.lchtime.safetyexpress.ui.vip.VipUI;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.LoginInternetRequest;
import com.lchtime.safetyexpress.utils.SpTools;

import java.io.File;


/**
 * TabUI
 * Created by user on 2017/3/13.
 */

public class TabUI extends TabActivity implements OnClickListener {

    public static final int TAKE_DATA = 200;

    private static TabUI tabUI;
    private MyApplication application;

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



    private String userId;
    private Gson gson;
    private GuiZePop guiZePop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tab);
        application = (MyApplication) getApplication();
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

        initShowWindow();
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

        intent = new Intent().setClass(this, HomeUI.class);
        spec = tabHost.newTabSpec("tab1").setIndicator("tab1").setContent(intent);
        tabHost.addTab(spec);
        intent = new Intent().setClass(this, CircleUI.class);
        spec = tabHost.newTabSpec("tab2").setIndicator("tab2").setContent(intent);
        tabHost.addTab(spec);
        intent = new Intent().setClass(this, AddUI.class);
//        spec = tabHost.newTabSpec("tab3").setIndicator("tab3").setContent(intent);
//        tabHost.addTab(spec);
        intent = new Intent().setClass(this, ChatUI.class);
        spec = tabHost.newTabSpec("tab4").setIndicator("tab4").setContent(intent);
        tabHost.addTab(spec);
        intent = new Intent().setClass(this, VipUI.class);
        spec = tabHost.newTabSpec("tab5").setIndicator("tab5").setContent(intent);
        tabHost.addTab(spec);
        HomeUI.homeUI_instance.setHomeToCircleInterface(new HomeUI.HomeToCircleInterface() {
            @Override
            public void toCircleActivity() {
                setCurrentTabByTag("tab2");
            }
        });

       getVipInfo();


    }

    private void initVideoPath() {
        File path=new File(Environment.getExternalStorageDirectory(),
                "kuaichevideo");
        if (!path.exists()) {
            path.mkdirs();
        }
        videoPath=path.getAbsolutePath()+File.separator+System.currentTimeMillis()+".mp4";
    }

    private void initShowWindow() {
        publiccamera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TabUI.this, RecordVideoActivity.class);
                intent.putExtra(RecordVideoActivity.RECORD_VIDEO_PATH,videoPath);
                startActivityForResult(intent,TAKE_DATA);
            }
        });
    }

    private void getVipInfo() {
        if (gson == null){
            gson = new Gson();
        }
        userId = SpTools.getString(this, Constants.userId,"");
        //登录操作
        LoginInternetRequest.getVipInfo(userId, new LoginInternetRequest.ForResultListener() {
            @Override
            public void onResponseMessage(String code) {
                if(!TextUtils.isEmpty(code)) {
                    VipInfoBean vipInfoBean = gson.fromJson(code, VipInfoBean.class);
                    if (vipInfoBean != null) {
                        InitInfo.phoneNumber = vipInfoBean.user_detail.ub_phone;
                        InitInfo.vipInfoBean = vipInfoBean;
                        InitInfo.isLogin = true;
                        SpTools.setString(TabUI.this, Constants.nik_name,vipInfoBean.user_detail.ud_nickname);
                        SpTools.setString(TabUI.this, Constants.nik_name,vipInfoBean.user_detail.ud_nickname);
                    }
                }
            }
        });
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
                break;
            case R.id.rb_tab_3:
                setCurrentTabByTag("tab3");
                break;
            case R.id.rb_tab_4:
                currentTag = "tab4";
                setCurrentTabByTag("tab4");
                break;
            case R.id.rb_tab_5:
                currentTag = "tab5";
                setCurrentTabByTag("tab5");
                break;
            case R.id.circle_public_tv:
                startActivity(new Intent(this, PublishCircleUI.class));
                break;
            case R.id.circle_public_camera:
                String ub_id = SpTools.getString(this, Constants.userId,"");
                if (TextUtils.isEmpty(ub_id)){
                    CommonUtils.toastMessage("登录后才能发布圈子！");
                    return;
                }
                break;
            case R.id.circle_public_question:
                Intent intent = new Intent(this,AskQuestionActivity.class);
                startActivity(intent);
                break;
        }
        tab_check.setChecked(false);
    }


    private void setCurrentTabByTag(String tag) {
        rb_tab_1.setChecked("tab1".equals(tag));
        rb_tab_2.setChecked("tab2".equals(tag));
        //rb_tab_3.setChecked("tab3".equals(tag));
        rb_tab_4.setChecked("tab4".equals(tag));
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

}
