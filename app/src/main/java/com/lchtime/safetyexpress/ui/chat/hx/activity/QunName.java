package com.lchtime.safetyexpress.ui.chat.hx.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.utils.CommonUtils;

/**
 * Created by Dreamer on 2017/6/8.
 */

public class QunName extends Activity implements View.OnClickListener {
    private TextView mTitle;
    private TextView mTitleRight;
    private LinearLayout mTitleLeft;
    private LinearLayout mLlTitleRight;

    private EditText mEtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_qun_name);
        initTitle();
        initView();
        initData();

    }


    private void initTitle() {
        mTitle = (TextView) findViewById(R.id.title);
        mTitleRight = (TextView) findViewById(R.id.tv_delete);
        mTitleLeft = (LinearLayout) findViewById(R.id.ll_back);
        mLlTitleRight = (LinearLayout) findViewById(R.id.ll_right);
        mLlTitleRight.setVisibility(View.VISIBLE);
        mTitle.setText("群聊名称");
        mTitleRight.setText("修改");
        mTitleRight.setVisibility(View.VISIBLE);
        mLlTitleRight.setOnClickListener(this);
        mTitleLeft.setOnClickListener(this);
    }

    private void initView() {
        mEtName = (EditText) findViewById(R.id.tv_qun_name);
    }

    private void initData() {
        String name = getIntent().getStringExtra("name");
        if (TextUtils.isEmpty(name)){
            name = "";
        }
        mEtName.setText(name);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_back){
            finish();
        }else if (v.getId() == R.id.ll_right){
            String name = mEtName.getText().toString().trim();
            if (TextUtils.isEmpty(name)){
                CommonUtils.toastMessage("名字不能为空！");
                return;
            }
            Intent intent = new Intent();
            intent.putExtra("name",name);
            setResult(100,intent);
            finish();
        }
    }
}
