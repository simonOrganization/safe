package com.lchtime.safetyexpress.ui.login;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.TabUI;
import com.lchtime.safetyexpress.utils.LoginInternetRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 登录
 * Created by user on 2017/4/15.
 */
@ContentView(R.layout.login)
public class LoginUI extends BaseUI {

    //手机号
    @ViewInject(R.id.et_login_username)
    private EditText et_login_username;
    //密码
    @ViewInject(R.id.et_login_passport)
    private EditText et_login_passport;


    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        setTitle("登录");
        rightVisible("注册");
    }

    @Override
    protected void prepareData() {

    }

    /**
     * 登录
     * @param view
     */
    @OnClick(R.id.tv_login_login)
    private void getLogin(View view){


        String phonenumber = et_login_username.getText().toString().trim();
        String password = et_login_passport.getText().toString().trim();
        LoginInternetRequest.login(phonenumber, password, new LoginInternetRequest.ForResultListener() {
            @Override
                    public void onResponseMessage(String code) {
                        if (code.equals("成功")){
                            finish();
                }
            }
        });
    }

    /**
     * 注册
     * @param view
     */
    @OnClick(R.id.ll_right)
    private void getRegister(View view){
        Intent intent = new Intent(LoginUI.this, RegisterUI.class);
        startActivity(intent);
    }

    /**
     * 忘记密码
     * @param view
     */
    @OnClick(R.id.tv_login_forpwd)
    private void getForPwd(View view){
        Intent intent = new Intent(LoginUI.this, SetPasswordUI.class);
        startActivity(intent);
    }

    /**
     * 微信
     * @param view
     */
    @OnClick(R.id.ll_login_weixin)
    private void getWeixin(View view){
        makeText("微信");
    }

    /**
     * QQ
     * @param view
     */
    @OnClick(R.id.ll_login_qq)
    private void getQQ(View view){
        makeText("QQ");
    }

    /**
     * 微博
     * @param view
     */
    @OnClick(R.id.ll_login_sina)
    private void getSina(View view){
        makeText("微博");
    }


}
