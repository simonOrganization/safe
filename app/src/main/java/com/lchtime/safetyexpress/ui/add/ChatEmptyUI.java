package com.lchtime.safetyexpress.ui.add;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.login.LoginUI;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**原添加页面  已改为聊天界面没登录的状态
 * Created by user on 2017/4/14.
 */
@ContentView(R.layout.add)
public class ChatEmptyUI extends BaseUI {

    private LinearLayout mTitleLeft;

    @ViewInject(R.id.error_btn_retry)
    Button error_btn_retry;
    @Override
    protected void back() {
        exit();
    }

    @Override
    protected void setControlBasis() {
        mTitleLeft = (LinearLayout) findViewById(R.id.ll_back);
        mTitleLeft.setVisibility(View.GONE);
        setTitle("聊天");

        error_btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatEmptyUI.this, LoginUI.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void prepareData() {

    }
}
