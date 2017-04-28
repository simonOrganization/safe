package com.lchtime.safetyexpress.ui.vip;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.InitInfo;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 手机号
 * <p>
 * Created by user on 2017/4/17.
 */
@ContentView(R.layout.vip_info_phone_ui)
public class VipInfoPhoneUI extends BaseUI {

    //手机号
    @ViewInject(R.id.tv_info_phone_phone)
    private TextView tv_info_phone_phone;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        setTitle("手机号");
        String phoneNumber = InitInfo.phoneNumber;
        String head=phoneNumber.substring(0,2);
        String tail=phoneNumber.substring(6);
        String s=head+"****"+tail;

        tv_info_phone_phone.setText(s);
    }

    @Override
    protected void prepareData() {

    }

    /**
     * 更换手机号
     * @param view
     */
    @OnClick(R.id.tv_info_phone_change)
    private void getChange(View view){
        Intent intent = new Intent(VipInfoPhoneUI.this, VipInfoPhoneValidUI.class);
        intent.putExtra("type","valid");
        startActivity(intent);
        finish();
    }

}
