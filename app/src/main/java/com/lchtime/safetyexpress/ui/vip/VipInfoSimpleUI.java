package com.lchtime.safetyexpress.ui.vip;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 *个人资料简介
 *
 * Created by user on 2017/4/17.
 */
@ContentView(R.layout.vip_info_simple_ui)
public class VipInfoSimpleUI extends BaseUI {
    String name;
    @ViewInject(R.id.et_info_simple)
    private EditText et_simple;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        setTitle("个人简介");
        rightVisible("保存");
        name = getIntent().getStringExtra("data");
        if (!TextUtils.isEmpty(name)){
            et_simple.setText(name);
        }
        et_simple.setSelection(et_simple.getText().toString().length());

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
        String simple = et_simple.getText().toString().trim();
        if (!TextUtils.isEmpty(simple)){
            Intent intent = new Intent();
            intent.putExtra("simple",simple);
            setResult(3,intent);
            finish();
        }else {
            Toast.makeText(this,"您没有输入任何内容",Toast.LENGTH_SHORT).show();
        }
    }
}
