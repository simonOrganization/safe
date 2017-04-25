package com.lchtime.safetyexpress.ui.vip;

import android.content.Intent;
import android.view.View;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by android-cp on 2017/4/21.
 */
@ContentView(R.layout.vip_mymoney)
public class MyMoneyActivity extends BaseUI{
    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        setTitle("我的账户");
        rightTextVisible("账户明细");

    }

    @Override
    protected void prepareData() {

    }

    @OnClick(R.id.item_authorization_zhifubao)
    private void getBindZhiFuBao(View view){
        Intent intent = new Intent(MyMoneyActivity.this,BindZhiFuBaoActivity.class);
        startActivity(intent);
    }
}
