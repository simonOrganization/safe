package com.lchtime.safetyexpress.ui.vip;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.Calendar;

/**
 * 个人资料
 * Created by user on 2017/4/15.
 */
@ContentView(R.layout.vip_info_ui)
public class VipInfoUI extends BaseUI {

    //生日
    @ViewInject(R.id.tv_info_birthday)
    private TextView tv_info_birthday;

    private String updateDate;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        setTitle("个人资料");
    }

    @Override
    protected void prepareData() {

    }

    /**
     * 昵称
     * @param view
     */
    @OnClick(R.id.ll_info_nickname)
    private void getNickname(View view){
        Intent intent = new Intent(VipInfoUI.this, VipInfoNicknameUI.class);
        startActivity(intent);
    }

    /**
     * 手机号
     * @param view
     */
    @OnClick(R.id.ll_info_phone)
    private void getPhone(View view){
        Intent intent = new Intent(VipInfoUI.this, VipInfoPhoneUI.class);
        startActivity(intent);
    }

    /**
     * 生日
     * @param view
     */
    @OnClick(R.id.ll_info_birthday)
    private void getBirthday(View view){
    }

    /**
     * 密码
     * @param view
     */
    @OnClick(R.id.ll_info_password)
    private void getPassword(View view){
        Intent intent = new Intent(VipInfoUI.this, VipInfoPasswordUI.class);
        startActivity(intent);
    }

}
