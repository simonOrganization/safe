package com.lchtime.safetyexpress.ui.login;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.TabUI;
import com.lchtime.safetyexpress.utils.LoginInternetRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 重置密码
 * Created by user on 2017/4/15.
 */
@ContentView(R.layout.set_password_ui)
public class SetPasswordUI extends BaseUI {

    private String registerCode;
    //手机号
    @ViewInject(R.id.et_set_pwd_username)
    private EditText et_set_pwd_username;
    //获取验证码
    @ViewInject(R.id.tv_set_pwd_getcode)
    private TextView set_pwd_getcode;
    //密码
    @ViewInject(R.id.et_set_pwd_passport)
    private EditText set_pwd_passport;

    //验证码输入框
    @ViewInject(R.id.et_set_pwd_code)
    private EditText et_set_pwd_code;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        setTitle("重置密码");
    }

    @Override
    protected void prepareData() {

    }

    /**
     * 完成
     * @param view
     */
    @OnClick(R.id.tv_set_pwd_perfect)
    private void getPerfect(View view){

        String phoneNum = et_set_pwd_username.getText().toString();
        String customCode = et_set_pwd_code.getText().toString();
        String pwd = set_pwd_passport.getText().toString();
        LoginInternetRequest.forgetPassword(phoneNum, registerCode, customCode, pwd, pwd, set_pwd_getcode, set_pwd_passport, set_pwd_passport,
                new LoginInternetRequest.ForResultListener() {
                    @Override
                    public void onResponseMessage(String code) {
                        Intent intent = new Intent(SetPasswordUI.this, LoginUI.class);
                        startActivity(intent);
                        finish();
                    }
                });

    }


    /**
     * 获取验证码
     * @param view
     */
    @OnClick(R.id.tv_set_pwd_getcode)
    private void getRegisterGetCode(View view){
        String phoneNum = et_set_pwd_username.getText().toString();
        LoginInternetRequest.verificationCode(phoneNum, set_pwd_getcode, new LoginInternetRequest.ForResultListener() {
            @Override
            public void onResponseMessage(String code) {
                registerCode = code;
            }
        });
    }

}
