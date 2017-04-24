package com.lchtime.safetyexpress.ui.circle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.CircleSubscribAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by yxn on 2017/4/21.
 */

public class SubscribActivity extends Activity {
    @BindView(R.id.circle_subscribe_rc)
    RecyclerView circle_subscribe_rc;
    @BindView(R.id.iv_right)
    ImageView iv_right;
    private CircleSubscribAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);
        ButterKnife.bind(this);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        circle_subscribe_rc.setLayoutManager(linearLayoutManager);
//        circle_subscribe_rc.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CircleSubscribAdapter(this);
        circle_subscribe_rc.setAdapter(adapter);
    }
    @OnClick(R.id.iv_right)
    void setOnclick(View view){
        startActivity(new Intent(SubscribActivity.this,AddSubscribeUI.class));
    }
}
