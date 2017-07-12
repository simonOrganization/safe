package com.lchtime.safetyexpress.ui.vip;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 *个人资料-公司名称
 *
 * Created by user on 2017/4/17.
 */
@ContentView(R.layout.vip_info_nickname_ui)
public class VipInfoCompanyUI extends BaseUI {
    String name;
    @ViewInject(R.id.et_edit_nikname)
    private EditText et_company;
    @ViewInject(R.id.del)
    private ImageView del;
    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        et_company.setHint("请输入公司名称");
        setTitle("公司名称");
        rightVisible("保存");
        name = getIntent().getStringExtra("data");
        if (!TextUtils.isEmpty(name)){
            et_company.setText(name);
        }

        et_company.setSelection(et_company.getText().toString().length());
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_company.setText("");
            }
        });
        et_company.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0){
                    del.setVisibility(View.VISIBLE);
                }else {
                    del.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
        String companyName = et_company.getText().toString().trim();
        if (!TextUtils.isEmpty(companyName)){
            Intent intent = new Intent();
            intent.putExtra("companyName",companyName);
            setResult(2,intent);
            finish();
        }else {
            Toast.makeText(this,"您没有输入任何内容",Toast.LENGTH_SHORT).show();
        }
    }
}
