package com.lchtime.safetyexpress.ui.vip;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.pop.VipInfoHintPop;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.login.LoginUI;
import com.lchtime.safetyexpress.ui.login.RegisterUI;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lchtime.safetyexpress.views.CircleImageView;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 我的
 * Created by user on 2017/4/14.
 */
@ContentView(R.layout.vip)
public class VipUI extends BaseUI {

    //头像
    @ViewInject(R.id.civ_vip_icon)
    private CircleImageView civ_vip_icon;

    //没有登陆的布局
    @ViewInject(R.id.ll_vip_logout)
    private LinearLayout logOut;

    //登陆后显示的布局
    @ViewInject(R.id.ll_vip_login)
    private LinearLayout logIn;

    private VipInfoHintPop vipInfoHintPop;

    @Override
    protected void back() {
        exit();
    }

    @Override
    protected void setControlBasis() {
        setTitle("我");
        backGone();
        vipInfoHintPop = new VipInfoHintPop(civ_vip_icon, VipUI.this, R.layout.pop_info_hint);
    }

    @Override
    protected void prepareData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                vipInfoHintPop.showAtLocation();
                vipInfoHintPop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(VipUI.this, VipInfoUI.class);
                        startActivity(intent);
                        vipInfoHintPop.dismiss();
                    }
                });
            }
        }, 1000);

        String ub_id = SpTools.getString(this, Constants.userId,"");
        if (TextUtils.isEmpty(ub_id)){
            logIn.setVisibility(View.GONE);
            logOut.setVisibility(View.VISIBLE);
        }else {
            logIn.setVisibility(View.VISIBLE);
            logOut.setVisibility(View.GONE);
            initVipInfo();
        }
    }

    //设置个人相关信息
    private void initVipInfo() {

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
            Intent intent = new Intent(VipUI.this, VipInfoUI.class);
            startActivity(intent);

    }

    /**
     * 设置昵称
     *
     * @param view
     */
    @OnClick(R.id.tv_vip_nickname)
    private void getNickname(View view) {
        Intent intent = new Intent(VipUI.this, VipInfoUI.class);
        startActivity(intent);
    }

    /**
     * 完善个人信息
     *
     * @param view
     */
    @OnClick(R.id.tv_vip_info)
    private void getWanShanInfo(View view) {
        Intent intent = new Intent(VipUI.this, VipInfoUI.class);
        startActivity(intent);
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

}
