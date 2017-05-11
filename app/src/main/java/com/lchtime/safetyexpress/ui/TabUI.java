package com.lchtime.safetyexpress.ui;

import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.InitInfo;
import com.lchtime.safetyexpress.bean.PostBean;
import com.lchtime.safetyexpress.bean.ProfessionBean;
import com.lchtime.safetyexpress.bean.VipInfoBean;
import com.lchtime.safetyexpress.ui.add.AddUI;
import com.lchtime.safetyexpress.ui.chat.ChatUI;
import com.lchtime.safetyexpress.ui.circle.CircleUI;
import com.lchtime.safetyexpress.ui.home.HomeUI;
import com.lchtime.safetyexpress.ui.login.LoginUI;
import com.lchtime.safetyexpress.ui.vip.VipUI;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.LoginInternetRequest;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lchtime.safetyexpress.views.CirclePublicPop;


/**
 * TabUI
 * Created by user on 2017/3/13.
 */

public class TabUI extends TabActivity implements OnClickListener {

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



    private String userId;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tab);
        application = (MyApplication) getApplication();
        tabUI = this;
        Drawable drawable;
        int right = CommonUtils.getDimen(this, R.dimen.dm035);
        int bottom = CommonUtils.getDimen(this, R.dimen.dm035);
        rg_tab = (RadioGroup) findViewById(R.id.rg_tab);
        tab_check = (CheckBox) findViewById(R.id.tab_check);
        tab_check.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                CirclePublicPop circlePublicPop = new CirclePublicPop(TabUI.this);
                circlePublicPop.showPopData(rg_tab);
            }
        });
        rb_tab_1 = (RadioButton) findViewById(R.id.rb_tab_1);
        rb_tab_1.setOnClickListener(this);
        rb_tab_1.setText("首页");
        drawable = getResources().getDrawable(R.drawable.tab_1);
        drawable.setBounds(0, 0, right, bottom);
        rb_tab_1.setCompoundDrawables(null, drawable, null, null);
        rb_tab_2 = (RadioButton) findViewById(R.id.rb_tab_2);
        rb_tab_2.setOnClickListener(this);
        rb_tab_2.setText("圈子");
        drawable = getResources().getDrawable(R.drawable.tab_2);
        drawable.setBounds(0, 0, right, bottom);
        rb_tab_2.setCompoundDrawables(null, drawable, null, null);
        rb_tab_3 = (RadioButton) findViewById(R.id.rb_tab_3);
        rb_tab_3.setOnClickListener(this);
        rb_tab_3.setText("");
        rb_tab_4 = (RadioButton) findViewById(R.id.rb_tab_4);
        rb_tab_4.setOnClickListener(this);
        rb_tab_4.setText("聊天");
        drawable = getResources().getDrawable(R.drawable.tab_4);
        drawable.setBounds(0, 0, right, bottom);
        rb_tab_4.setCompoundDrawables(null, drawable, null, null);
        rb_tab_5 = (RadioButton) findViewById(R.id.rb_tab_5);
        rb_tab_5.setOnClickListener(this);
        rb_tab_5.setText("我的");
        drawable = getResources().getDrawable(R.drawable.tab_5);
        drawable.setBounds(0, 0, right, bottom);
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
        spec = tabHost.newTabSpec("tab3").setIndicator("tab3").setContent(intent);
        tabHost.addTab(spec);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_tab_1:
                setCurrentTabByTag("tab1");
                break;
            case R.id.rb_tab_2:
                setCurrentTabByTag("tab2");
                break;
            case R.id.rb_tab_3:
                setCurrentTabByTag("tab3");

                break;
            case R.id.rb_tab_4:
                setCurrentTabByTag("tab4");
                break;
            case R.id.rb_tab_5:
                setCurrentTabByTag("tab5");
                break;
        }
    }

    private void setCurrentTabByTag(String tag) {
        rb_tab_1.setChecked("tab1".equals(tag));
        rb_tab_2.setChecked("tab2".equals(tag));
        rb_tab_3.setChecked("tab3".equals(tag));
        rb_tab_4.setChecked("tab4".equals(tag));
        rb_tab_5.setChecked("tab5".equals(tag));
        tabHost.setCurrentTabByTag(tag);
    }

}
