package com.lchtime.safetyexpress.ui.vip;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lidroid.xutils.view.annotation.ContentView;

/**
 * Created by android-cp on 2017/4/21.
 */

@ContentView(R.layout.vip_bindzhifubao)
public class BindZhiFuBaoActivity extends BaseUI{
    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {

        setTitle("提现账号绑定");
    }

    @Override
    protected void prepareData() {

    }
}
