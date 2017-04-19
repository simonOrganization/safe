package com.lchtime.safetyexpress.ui.login;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.TabUI;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 注册
 * Created by user on 2017/4/15.
 */
@ContentView(R.layout.register)
public class RegisterUI extends BaseUI {

    //手机号
    @ViewInject(R.id.et_register_username)
    private EditText et_register_username;
    //获取验证码
    @ViewInject(R.id.tv_register_getcode)
    private TextView tv_register_getcode;
    //密码
    @ViewInject(R.id.et_register_passport)
    private EditText et_register_passport;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        setTitle("注册");
        rightVisible("登录");
    }

    @Override
    protected void prepareData() {

    }

    /**
     * 登录
     * @param view
     */
    @OnClick(R.id.ll_right)
    private void getLogin(View view){
        finish();
    }

    /**
     * 注册
     * @param view
     */
    @OnClick(R.id.tv_register_reg)
    private void getRegister(View view){
        Intent intent = new Intent(RegisterUI.this, TabUI.class);
        startActivity(intent);
        finish();
    }

}
