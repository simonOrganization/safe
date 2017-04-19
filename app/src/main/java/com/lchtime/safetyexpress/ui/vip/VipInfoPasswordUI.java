package com.lchtime.safetyexpress.ui.vip;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lidroid.xutils.view.annotation.ContentView;

/**
 * 修改密码
 * Created by user on 2017/4/17.
 */
@ContentView(R.layout.vip_info_password_ui)
public class VipInfoPasswordUI extends BaseUI {

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        setTitle("修改密码");
    }

    @Override
    protected void prepareData() {

    }
}
