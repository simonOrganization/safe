package com.lchtime.safetyexpress.ui.home;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lidroid.xutils.view.annotation.ContentView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by android-cp on 2017/5/26.
 */
@ContentView(R.layout.activity_h5_answer)
public class H5AnswerActivity extends BaseUI {
    @BindView(R.id.ll_home)
    LinearLayout llHome;
    @BindView(R.id.v_title)
    TextView vTitle;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.ll_right)
    LinearLayout llRight;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.recycler)
    RecyclerView recycler;

    @Override
    protected void back() {
        finish();
        setTitle("撰写回答");
        rightTextVisible("发送");
    }

    @Override
    protected void setControlBasis() {
        ButterKnife.bind(this);
    }

    @Override
    protected void prepareData() {

    }

    @Override
    protected void clickEvent() {
        //发送按钮点击事件

    }
}
