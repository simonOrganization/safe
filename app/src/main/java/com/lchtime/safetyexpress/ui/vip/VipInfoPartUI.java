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
 *个人资料-昵称
 *
 * Created by user on 2017/4/17.
 */
@ContentView(R.layout.vip_info_nickname_ui)
public class VipInfoPartUI extends BaseUI {

    @ViewInject(R.id.et_edit_nikname)
    private EditText et_part;
    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        et_part.setHint("请输入您所在的部门");
        setTitle("部门");
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
        String editPart = et_part.getText().toString().trim();
        if (!TextUtils.isEmpty(editPart)){
            Intent intent = new Intent();
            intent.putExtra("editPart",editPart);
            setResult(1,intent);
            finish();
        }
        else {
            Toast.makeText(this,"您没有输入任何内容",Toast.LENGTH_SHORT).show();
        }
    }
}
