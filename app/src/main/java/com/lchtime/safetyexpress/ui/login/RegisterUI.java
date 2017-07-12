package com.lchtime.safetyexpress.ui.login;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.InitInfo;
import com.lchtime.safetyexpress.bean.VipInfoBean;
import com.lchtime.safetyexpress.pop.VipInfoHintPop;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.vip.VipInfoUI;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.LoginInternetRequest;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import static com.lchtime.safetyexpress.bean.InitInfo.phoneNumber;

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


    @ViewInject(R.id.pb_progress)
    private ProgressBar pb_progress;
    private String mType;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        setTitle("注册");
        mType = getIntent().getStringExtra("third");
        vipInfoHintPop = new VipInfoHintPop(et_register_username, RegisterUI.this, R.layout.pop_info_hint);
    }

    @Override
    protected void prepareData() {

    }

    /**
     *
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

    boolean isLogin = false;
    @OnClick(R.id.tv_register_reg)
    private void getRegister(View view){
        if (isLogin == true){
            return;
        }
        isLogin = true;
        //显示progressbar
        pb_progress.setVisibility(View.VISIBLE);
        backgroundAlpha(0.5f);

        String phoneNum = et_register_username.getText().toString().trim();
        String pwd = et_register_passport.getText().toString().trim();
        String customRegisterNum = et_register_code.getText().toString();
        if (registerCode == null){
            Toast.makeText(RegisterUI.this,"您没有获取验证码",Toast.LENGTH_SHORT).show();
            isLogin = false;
            backgroundAlpha(1f);
            pb_progress.setVisibility(View.GONE);
            return ;
        }
        if (TextUtils.isEmpty(mType)) {
            //注册
            LoginInternetRequest.register(phoneNum, registerCode, pwd, customRegisterNum, register_getcode, new LoginInternetRequest.ForResultListener() {
                @Override
                public void onResponseMessage(String code) {
                    if (!TextUtils.isEmpty(code)) {
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
                        isLogin = false;
                        backgroundAlpha(1f);
                        pb_progress.setVisibility(View.GONE);
                    } else {
                        isLogin = false;
                        backgroundAlpha(1f);
                        pb_progress.setVisibility(View.GONE);
                    }
                }
            });
        }else {
            //三方登录过来的
            if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
                CommonUtils.toastMessage("您当前无网络，请联网再试");
                isLogin = false;
                backgroundAlpha(1f);
                pb_progress.setVisibility(View.GONE);
                return;
            }
            if(TextUtils.isEmpty(phoneNum)){
                CommonUtils.toastMessage("您输入的手机号为空");
                isLogin = false;
                backgroundAlpha(1f);
                pb_progress.setVisibility(View.GONE);
                return;
            }else if(TextUtils.isEmpty(customRegisterNum)){
                CommonUtils.toastMessage("您输入的验证码为空");
                isLogin = false;
                backgroundAlpha(1f);
                pb_progress.setVisibility(View.GONE);
                return;
            }else if(TextUtils.isEmpty(pwd)){
                CommonUtils.toastMessage("您输入的密码为空");
                isLogin = false;
                backgroundAlpha(1f);
                pb_progress.setVisibility(View.GONE);
                return;
            }
            if(!TextUtils.isEmpty(phoneNum)){
                if(!CommonUtils.isMobilePhone(phoneNum)){
                    CommonUtils.toastMessage("您输入的手机号有误");
                    isLogin = false;
                    backgroundAlpha(1f);
                    pb_progress.setVisibility(View.GONE);
                    return ;
                }
            }
            if(!customRegisterNum.equals(registerCode)){
                CommonUtils.toastMessage("您输入的验证码错误");
                isLogin = false;
                backgroundAlpha(1f);
                pb_progress.setVisibility(View.GONE);
                return;
            }
            Intent intent = new Intent();
            intent.putExtra("phone",phoneNum);
            intent.putExtra("pwd",pwd);
            setResult(333,intent);
            finish();

        }
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
                        phoneNumber = vipInfoBean.user_detail.ub_phone;
                        InitInfo.vipInfoBean = vipInfoBean;
                        InitInfo.isLogin = true;
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
                if ("net".equals(registerCode)){
                    Toast.makeText(RegisterUI.this,"请查看网络",Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(RegisterUI.this,"验证码发送成功",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

}
