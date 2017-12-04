package com.lchtime.safetyexpress.ui.vip;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.InitInfo;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.chat.hx.Constant;
import com.lchtime.safetyexpress.utils.LoginInternetRequest;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 手机验证/更换手机号
 * Created by user on 2017/4/17.
 */
@ContentView(R.layout.vip_info_phone_valid_ui)
public class VipInfoPhoneValidUI extends BaseUI {

    public static final String TYPE_VALID = "valid";
    public static final String TYPE_CHANGE = "change";

    public boolean isReset = true;

    public static String currentType ;
    public static String currentCode ;

    private String phoneNumber;
    //手机号
    @ViewInject(R.id.et_info_valid_phone)
    private EditText et_info_phone_valid_phone;
    //验证码
    @ViewInject(R.id.et_info_phone_valid_code)
    private EditText et_info_phone_valid_code;

    //获取验证码
    @ViewInject(R.id.et_info_phone_valid_getcode)
    private TextView tv_info_phone_valid_code;

    //验证/更换手机号
    @ViewInject(R.id.tv_info_phone_valid)
    private TextView tv_info_phone_valid;
    private String phonenum;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        currentType = getIntent().getStringExtra("type");
        phoneNumber = SpTools.getString(mContext , Constants.phoneNum);
        if (TYPE_VALID.equals(currentType)){
            setTitle("手机验证");
            et_info_phone_valid_phone.setText(phoneNumber);
            et_info_phone_valid_phone.setFocusable(false);
            tv_info_phone_valid.setText("验证");
        }else if(TYPE_CHANGE.equals(currentType)){
            setTitle("更换手机号");
            tv_info_phone_valid.setText("更换手机号码");
        }

    }

    @Override
    protected void prepareData() {

    }

    /**
     * 手机验证/更换手机号码
     * @param view
     */
    @OnClick(R.id.tv_info_phone_valid)
    private void getChange(View view){

        String code = et_info_phone_valid_code.getText().toString().trim();
        if (TYPE_CHANGE.equals(currentType)){
            phonenum = et_info_phone_valid_phone.getText().toString().trim();
        }else {
            phonenum = phoneNumber;
        }
        if (TextUtils.isEmpty(code)){
            Toast.makeText(VipInfoPhoneValidUI.this,"验证码不能为空",Toast.LENGTH_SHORT).show();
            return ;
        }
        if (currentCode!= null){
            if (code.equals(currentCode)){
                //如果是验证按钮
                if (TYPE_VALID.equals(currentType)){
                    Toast.makeText(VipInfoPhoneValidUI.this,"验证成功！",Toast.LENGTH_SHORT).show();
                    LoginInternetRequest.reset();
                Intent intent = new Intent(VipInfoPhoneValidUI.this, VipInfoPhoneValidUI.class);
                intent.putExtra("type",TYPE_CHANGE);
                startActivity(intent);
                finish();
                //如果是更换手机号码按钮
                }else if(TYPE_CHANGE.equals(currentType)) {
                    if (!TextUtils.isEmpty(phonenum)) {

                        LoginInternetRequest.ChangePhone(code, SpTools.getUserId(this), phonenum, new LoginInternetRequest.ForResultListener() {
                            @Override
                            public void onResponseMessage(String code) {
                                Toast.makeText(VipInfoPhoneValidUI.this,"修改成功",Toast.LENGTH_SHORT).show();
                                SpTools.setString(mContext , Constants.phoneNum , phonenum);
                                finish();
                            }
                        });

                    }else {
                        Toast.makeText(VipInfoPhoneValidUI.this,"手机号码不能为空",Toast.LENGTH_SHORT).show();
                    }
                }

            }else {
                Toast.makeText(VipInfoPhoneValidUI.this,"验证码错误",Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(VipInfoPhoneValidUI.this,"请先获取验证码！",Toast.LENGTH_SHORT).show();
        }







    }

    @OnClick(R.id.et_info_phone_valid_getcode)
    private void getCode(View view){
        if (TYPE_CHANGE.equals(currentType)){
            phonenum = et_info_phone_valid_phone.getText().toString().trim();
        }else {
            phonenum = phoneNumber;
        }
        if (!TextUtils.isEmpty(phonenum)) {
            LoginInternetRequest.verificationCode(phonenum, tv_info_phone_valid_code, new LoginInternetRequest.ForResultListener() {
                @Override
                public void onResponseMessage(String code) {
                    currentCode = code;
                    Toast.makeText(VipInfoPhoneValidUI.this,  "验证码发送成功", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(VipInfoPhoneValidUI.this, "手机号码不能为空！", Toast.LENGTH_SHORT).show();
        }


    }

}
