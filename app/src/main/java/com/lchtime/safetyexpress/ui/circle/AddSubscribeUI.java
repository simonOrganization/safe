package com.lchtime.safetyexpress.ui.circle;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.ui.circle.fragment.SubscirbeAllFragment;
import com.lchtime.safetyexpress.ui.circle.fragment.SubscirbeCommendFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yxn on 2017/4/23.
 */

public class AddSubscribeUI extends FragmentActivity {
    @BindView(R.id.add_subscirbe_rg)
    RadioGroup add_subscirbe_rg;
    private Fragment[] fragments;
    private int mIndex = 0;
    @BindViews({R.id.add_subscirbe_line_left,R.id.add_subscirbe_line_right})
    List<View> views;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_subscribe_layout);
        ButterKnife.bind(this);
        //初始化titile
        TextView tv_title = (TextView) findViewById(R.id.title);
        if (tv_title != null) {
            tv_title.setText("添加订阅");
        }
        initFragment();
        add_subscirbe_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.add_subscirbe_line_rb_all:
                        setIndexSelected(0);
                        break;
                    case R.id.add_subscirbe_line_rb_comm:
                        setIndexSelected(1);
                        break;
                }
            }
        });
    }

    private void initFragment() {
        SubscirbeAllFragment saf = new SubscirbeAllFragment();
        SubscirbeCommendFragment scf = new SubscirbeCommendFragment();
        fragments = new Fragment[]{saf,scf};
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.add_subscribe_frame,fragments[0]).commit();
//        ft.add(R.id.add_subscribe_frame,fragments[1]).commit();
        setIndexSelected(0);
    }
    private void setIndexSelected(int index) {
        if (mIndex == index) {
            return;
        }
        if(index == 0){
            views.get(0).setBackgroundColor(Color.parseColor("#ea553f"));
            views.get(1).setBackgroundColor(Color.parseColor("#f5f5f5"));
        }else if(index == 1){
            views.get(1).setBackgroundColor(Color.parseColor("#ea553f"));
            views.get(0).setBackgroundColor(Color.parseColor("#f5f5f5"));
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        //隐藏
        ft.hide(fragments[mIndex]);
        //判断是否添加
        if (!fragments[index].isAdded()) {
            ft.add(R.id.add_subscribe_frame, fragments[index]).show(fragments[index]);
        } else {
            ft.show(fragments[index]);
        }
        ft.commit();
        //再次赋值
        mIndex = index;
    }

    @OnClick(R.id.ll_back)
    void setOnclick(View view){
        AddSubscribeUI.this.finish();
    }

}
