package com.lchtime.safetyexpress.ui.vip;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 手机验证/更换手机号
 * Created by user on 2017/4/17.
 */
@ContentView(R.layout.vip_info_phone_valid_ui)
public class VipInfoPhoneValidUI extends BaseUI {

    //手机号
    @ViewInject(R.id.et_info_phone_valid_phone)
    private EditText et_info_phone_valid_phone;
    //验证码
    @ViewInject(R.id.et_info_phone_valid_code)
    private EditText et_info_phone_valid_code;
    //验证/更换手机号
    @ViewInject(R.id.tv_info_phone_valid)
    private TextView tv_info_phone_valid;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        String type = getIntent().getStringExtra("type");
        if ("valid".equals(type)){
            setTitle("手机验证");
            et_info_phone_valid_phone.setText("18888888888");
            et_info_phone_valid_phone.setFocusable(false);
            tv_info_phone_valid.setText("验证");
        }else if("change".equals(type)){
            setTitle("更换手机号");
            tv_info_phone_valid.setText("更换手机号码");
        }

    }

    @Override
    protected void prepareData() {

    }

    /**
     * 更换手机号码
     * @param view
     */
    @OnClick(R.id.tv_info_phone_valid)
    private void getChange(View view){
        Intent intent = new Intent(VipInfoPhoneValidUI.this, VipInfoPhoneValidUI.class);
        intent.putExtra("type","change");
        startActivity(intent);
        finish();
    }

}
