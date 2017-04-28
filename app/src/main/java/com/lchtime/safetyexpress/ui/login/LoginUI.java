package com.lchtime.safetyexpress.ui.login;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.InitInfo;
import com.lchtime.safetyexpress.bean.VipInfoBean;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.TabUI;
import com.lchtime.safetyexpress.utils.LoginInternetRequest;
import com.lchtime.safetyexpress.utils.SpTools;
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

    private Gson gson;


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


        final String phonenumber = et_login_username.getText().toString().trim();
        final String password = et_login_passport.getText().toString().trim();
        LoginInternetRequest.login(phonenumber, password, new LoginInternetRequest.ForResultListener() {
            @Override
                    public void onResponseMessage(String code) {
                        if (code.equals("成功")){
                            SpTools.setString(LoginUI.this, Constants.phoneNum,phonenumber);
                            SpTools.setString(LoginUI.this, Constants.password,password);
                            LoginInternetRequest.getVipInfo(SpTools.getString(LoginUI.this, Constants.userId, ""), new LoginInternetRequest.ForResultListener() {
                                @Override
                                public void onResponseMessage(String code) {
                                    if(!TextUtils.isEmpty(code)) {
                                        if (gson == null){
                                            gson = new Gson();
                                        }
                                        VipInfoBean vipInfoBean = gson.fromJson(code, VipInfoBean.class);
                                        if (vipInfoBean != null) {
                                            InitInfo.phoneNumber = vipInfoBean.user_base;
                                            InitInfo.vipInfoBean = vipInfoBean;
                                            InitInfo.isLogin = true;
                                            SpTools.setString(LoginUI.this, Constants.nik_name,vipInfoBean.user_detail.ud_nickname);
                                            SpTools.setString(LoginUI.this, Constants.nik_name,vipInfoBean.user_detail.ud_nickname);
                                            finish();
                                        }
                                    }
                                }
                            });

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
