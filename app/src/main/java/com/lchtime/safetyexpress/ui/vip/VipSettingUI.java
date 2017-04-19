package com.lchtime.safetyexpress.ui.vip;

import android.content.Intent;
import android.view.View;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 设置
 * Created by user on 2017/4/17.
 */
@ContentView(R.layout.vip_setting_ui)
public class VipSettingUI extends BaseUI {

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        setTitle("设置");
    }

    @Override
    protected void prepareData() {

    }

    @OnClick(R.id.ll_setting_about)
    private void getAbout(View view){
        Intent intent = new Intent(VipSettingUI.this,VipSettingAboutUsUI.class);
        startActivity(intent);
    }
}
