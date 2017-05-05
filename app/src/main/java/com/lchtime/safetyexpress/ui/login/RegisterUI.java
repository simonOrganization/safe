package com.lchtime.safetyexpress.ui.login;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.InitInfo;
import com.lchtime.safetyexpress.bean.VipInfoBean;
import com.lchtime.safetyexpress.pop.VipInfoHintPop;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.TabUI;
import com.lchtime.safetyexpress.ui.vip.VipInfoUI;
import com.lchtime.safetyexpress.ui.vip.VipUI;
import com.lchtime.safetyexpress.utils.LoginInternetRequest;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 注册
 * Created by user on 2017/4/15.
 */
@ContentView(R.layout.register)
public class RegisterUI extends BaseUI {

    private String registerCode;

    private Gson gson;

    private String userId;

    private VipInfoHintPop vipInfoHintPop;

    //手机号
    @ViewInject(R.id.et_register_username)
    private EditText et_register_username;
    //获取验证码
    @ViewInject(R.id.tv_register_getcode)
    private TextView register_getcode;

    //验证码输入框
    @ViewInject(R.id.et_register_code)
    private EditText et_register_code;

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
        vipInfoHintPop = new VipInfoHintPop(et_register_username, RegisterUI.this, R.layout.pop_info_hint);
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
//        Intent intent = new Intent(RegisterUI.this, TabUI.class);
//        startActivity(intent);
//        finish();
        String phoneNum = et_register_username.getText().toString().trim();
        String pwd = et_register_passport.getText().toString().trim();
        String customRegisterNum = et_register_code.getText().toString();
        if (registerCode == null){
            return ;
        }
        LoginInternetRequest.register(phoneNum, registerCode, pwd, customRegisterNum, register_getcode, new LoginInternetRequest.ForResultListener() {
            @Override
            public void onResponseMessage(String code) {
                //存储用户的ub_id
                SpTools.setString(RegisterUI.this, Constants.userId, code);
                userId = code;
                getVipInfo();
                //显示不全信息对话框
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        vipInfoHintPop.showAtLocation();
                        vipInfoHintPop.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(RegisterUI.this, VipInfoUI.class);
                                startActivity(intent);
                                vipInfoHintPop.dismiss();
                                finish();
                            }
                        });
                    }
                }, 1000);
                InitInfo.isShowed = false;

            }
        });
    }

    private void getVipInfo() {
        if (gson == null){
            gson = new Gson();
        }
        if (TextUtils.isEmpty(userId)){
            userId = SpTools.getString(this,Constants.userId,"");
        }
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
                        SpTools.setString(RegisterUI.this, Constants.nik_name,vipInfoBean.user_detail.ud_nickname);
                        SpTools.setString(RegisterUI.this, Constants.nik_name,vipInfoBean.user_detail.ud_nickname);
                    }
                }
            }
        });
    }


    /**
     * 获取验证码
     * @param view
     */
    @OnClick(R.id.tv_register_getcode)
    private void getRegisterGetCode(View view){
        String phoneNum = et_register_username.getText().toString();
        LoginInternetRequest.verificationCode(phoneNum, register_getcode, new LoginInternetRequest.ForResultListener() {
            @Override
            public void onResponseMessage(String code) {
                registerCode = code;

                Toast.makeText(RegisterUI.this,registerCode,Toast.LENGTH_SHORT).show();
            }
        });
    }

}
