package com.lchtime.safetyexpress.ui.circle.protocal;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CirclePhone extends BaseUI {

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
    @BindView(R.id.iv_circle_photo)
    ImageView ivCirclePhoto;
    private String url;
    private ImageView iv_circle_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_circle_phone);
        ButterKnife.bind(this);
        setTitle("图片预览");
        url = getIntent().getStringExtra("url");
        iv_circle_photo = (ImageView) findViewById(R.id.iv_circle_photo);
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (!TextUtils.isEmpty(url)) {
            Picasso.with(this).load(url).into(iv_circle_photo);
        } else {
            Picasso.with(this).load(R.drawable.banner_default).into(iv_circle_photo);
        }

    }

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {


    }

    @Override
    protected void prepareData() {

    }
}
