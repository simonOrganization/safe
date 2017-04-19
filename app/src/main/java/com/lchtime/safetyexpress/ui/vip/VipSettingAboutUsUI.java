package com.lchtime.safetyexpress.ui.vip;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lidroid.xutils.view.annotation.ContentView;

/**
 * 关于我们
 * Created by user on 2017/4/17.
 */
@ContentView(R.layout.vip_setting_about_us_ui)
public class VipSettingAboutUsUI extends BaseUI {

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        setTitle("关于我们");
    }

    @Override
    protected void prepareData() {

    }
}
