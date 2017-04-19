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
 * 重置密码
 * Created by user on 2017/4/15.
 */
@ContentView(R.layout.set_password_ui)
public class SetPasswordUI extends BaseUI {

    //手机号
    @ViewInject(R.id.et_set_pwd_username)
    private EditText et_set_pwd_username;
    //获取验证码
    @ViewInject(R.id.tv_set_pwd_getcode)
    private TextView tv_set_pwd_getcode;
    //密码
    @ViewInject(R.id.et_set_pwd_passport)
    private EditText et_set_pwd_passport;

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
//        Intent intent = new Intent(SetPasswordUI.this, LoginUI.class);
//        startActivity(intent);
//        finish();
    }

}
