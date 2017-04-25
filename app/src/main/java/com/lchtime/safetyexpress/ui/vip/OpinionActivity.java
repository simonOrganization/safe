package com.lchtime.safetyexpress.ui.vip;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.astuetz.PagerSlidingTabStrip;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by android-cp on 2017/4/21.
 */
@ContentView(R.layout.vip_opinion)
public class OpinionActivity extends BaseUI{
    @ViewInject(R.id.rc_pic)
    private RecyclerView picView;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        setTitle("意见反馈");
        rightTextVisible("提交");

        initRecyclerView();
    }

    private void initRecyclerView() {
        GridLayoutManager manager = new GridLayoutManager(this,3);
        picView.setLayoutManager(manager);
    }

    @Override
    protected void prepareData() {

    }
}
