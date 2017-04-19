package com.lchtime.safetyexpress.ui.vip;

import android.view.View;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 *个人资料-昵称
 *
 * Created by user on 2017/4/17.
 */
@ContentView(R.layout.vip_info_nickname_ui)
public class VipInfoNicknameUI extends BaseUI {
    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        setTitle("昵称");
        rightVisible("保存");
    }

    @Override
    protected void prepareData() {

    }

    /**
     * 保存
     * @param view
     */
    @OnClick(R.id.ll_right)
    private void getSave(View view){

    }
}
