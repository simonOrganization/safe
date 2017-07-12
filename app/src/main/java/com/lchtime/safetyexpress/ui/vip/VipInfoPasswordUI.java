package com.lchtime.safetyexpress.ui.vip;

import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.InitInfo;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.utils.LoginInternetRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 修改密码
 * Created by user on 2017/4/17.
 */
@ContentView(R.layout.vip_info_password_ui)
public class VipInfoPasswordUI extends BaseUI implements View.OnClickListener {

    @ViewInject(R.id.tv_password_phone_num)
    private TextView phoneNum;
    @ViewInject(R.id.tv_info_phone_valid_getcode)
    private TextView tv_getCode;
    //用户输入的验证码
    @ViewInject(R.id.et_info_phone_valid_phone)
    private TextView et_getCode;
    //新密码
    @ViewInject(R.id.et_info_phone_valid_code)
    private EditText et_Code;

    @ViewInject(R.id.tv_info_phone_valid)
    private TextView tv_valid;

    @ViewInject(R.id.ll_yan)
    private LinearLayout ll_yan;

    private String code;
    private boolean isShow = false;
    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        setTitle("修改密码");
        String phoneNumber = InitInfo.phoneNumber;
        String head=phoneNumber.substring(0,3);
        String tail=phoneNumber.substring(7);
        String s=head+"****"+tail;
        phoneNum.setText(s);

    }

    @Override
    protected void prepareData() {
        tv_getCode.setOnClickListener(this);
        tv_valid.setOnClickListener(this);
        ll_yan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //获取验证码
        if (v == tv_getCode){
            LoginInternetRequest.verificationCode(InitInfo.phoneNumber, tv_getCode, new LoginInternetRequest.ForResultListener() {
                @Override
                public void onResponseMessage(String code) {
                    VipInfoPasswordUI.this.code = code;
                    Toast.makeText(VipInfoPasswordUI.this,"验证码发送成功",Toast.LENGTH_SHORT).show();
                }
            });
            //修改
        }else if(v == tv_valid){
            //用户输入的验证码
            String getCode = et_getCode.getText().toString().trim();
            //新密码
            String pwd = et_Code.getText().toString().trim();
            if (TextUtils.isEmpty(getCode) || TextUtils.isEmpty(pwd)){
                Toast.makeText(VipInfoPasswordUI.this,"验证码或密码不能为空",Toast.LENGTH_SHORT).show();
                return ;
            }else if (pwd.length() < 6){
                Toast.makeText(VipInfoPasswordUI.this,"密码不能少于6位",Toast.LENGTH_SHORT).show();
                return ;
            }
            if (!TextUtils.isEmpty(code)){
                //如果验证码正确
                if (code.equals(getCode)){
                    LoginInternetRequest.forgetPassword(InitInfo.phoneNumber, code, getCode, pwd, pwd, tv_getCode, et_Code, et_Code, new LoginInternetRequest.ForResultListener() {
                        @Override
                        public void onResponseMessage(String code) {
                            if("成功".equals(code)){
                                Toast.makeText(VipInfoPasswordUI.this,"修改密码成功",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });

                }else {
                    //如果验证码不正确
                    Toast.makeText(this,"验证码不正确",Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this,"验证码不能为空",Toast.LENGTH_SHORT).show();
            }
        }else if(v == ll_yan){
            //是否显示密码
            if (!isShow){
                et_Code.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                isShow = true;
            }else {
                et_Code.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_CLASS_TEXT);
                isShow = false;
            }
        }
    }
}
