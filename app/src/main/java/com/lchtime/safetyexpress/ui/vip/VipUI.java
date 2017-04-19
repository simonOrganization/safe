package com.lchtime.safetyexpress.ui.vip;

import android.content.Intent;
import android.os.Handler;
import android.view.View;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.pop.VipInfoHintPop;
import com.lchtime.safetyexpress.ui.BaseUI;
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
        Intent intent = new Intent(VipUI.this, VipInfoNicknameUI.class);
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
     * 设置
     *
     * @param view
     */
    @OnClick(R.id.ll_vip_setting)
    private void getSetting(View view) {
        Intent intent = new Intent(VipUI.this, VipSettingUI.class);
        startActivity(intent);
    }

}
