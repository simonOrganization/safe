package com.lchtime.safetyexpress.ui.vip;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.TextView;

import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.InitInfo;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lchtime.safetyexpress.views.CircleImageView;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.File;

/**
 * 设置
 * Created by user on 2017/4/17.
 */
@ContentView(R.layout.vip_setting_ui)
public class VipSettingUI extends BaseUI {

    @ViewInject(R.id.tv_setting_version)
    private TextView tv_version;



    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        setTitle("设置");
        setVersion();
    }

    @Override
    protected void prepareData() {

    }

    @OnClick(R.id.ll_setting_about)
    private void getAbout(View view){
        Intent intent = new Intent(VipSettingUI.this,VipSettingAboutUsUI.class);
        startActivity(intent);
    }

    /**
     *
     * 退出登录
     *
     * */
    @OnClick(R.id.tv_info_phone_valid)
    private void getOutLog(View view){
        SpTools.setString(this, Constants.userId, null);//存储用户的ub_id
        SpTools.setString(this, Constants.phoneNum, null);//存储用户的手机号码
        SpTools.setString(this, Constants.password, null);//存储用户的密码
        File file = new File(MyApplication.getContext().getFilesDir(),Constants.photo_name);//将要保存图片的路径
        file.delete();
        InitInfo.vipInfoBean = null;
        InitInfo.isLogin = false;
        InitInfo.phoneNumber = null;
        finish();
    }

    private void setVersion() {
        String version = "未设置";
        try {
            version = getPackageManager().getPackageInfo(getPackageName(),0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        tv_version.setText(version);
    }


}
