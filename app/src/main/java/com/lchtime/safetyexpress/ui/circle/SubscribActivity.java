package com.lchtime.safetyexpress.ui.circle;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.CircleAdapter;
import com.lchtime.safetyexpress.adapter.CircleSubscribAdapter;
import com.lchtime.safetyexpress.adapter.HeaderAndFooterWrapper;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.MydyBean;
import com.lchtime.safetyexpress.bean.QzContextBean;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.circle.protocal.CircleProtocal;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by yxn on 2017/4/21.
 */
@ContentView(R.layout.activity_subscribe)
public class SubscribActivity extends BaseUI {

    RecyclerView circle_subscribe_rc;
    //订阅页面的圈子布局
    @ViewInject(R.id.erc_subscribe_circle)
    RecyclerView erc_subscribe_circle;
    @ViewInject(R.id.iv_right)
    ImageView iv_right;

    @ViewInject(R.id.loading)
    RelativeLayout loading;
    @ViewInject(R.id.empty)
    RelativeLayout empty;
    @ViewInject(R.id.error)
    RelativeLayout error;
    @ViewInject(R.id.success)
    LinearLayout success;


    private CircleSubscribAdapter adapter;

    private CircleAdapter circleAdapter;
    private HeaderAndFooterWrapper wrapper;

    private CircleProtocal protocal;
    private String userid;
    private List<MydyBean.DyBean> dyList;
    private List<QzContextBean> circleList;
    private View view;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        rightVisible(R.drawable.circle_subscribe);
        setTitle("我的订阅");
        view = View.inflate(this, R.layout.subscribe_horizotol,null);
        initView();
        dyList = new ArrayList<>();
        circleList = new ArrayList<>();
        //上边的横向adapter
        adapter = new CircleSubscribAdapter(SubscribActivity.this,dyList);
        circle_subscribe_rc.setAdapter(adapter);
        //下面的adapter
        circleAdapter = new CircleAdapter(SubscribActivity.this,circleList);
        wrapper = new HeaderAndFooterWrapper(circleAdapter);
        //不显示订阅按钮
        circleAdapter.setShowDy(false);
        //和circle用一样的adapter
        circleAdapter.setIsCircle(true);
        wrapper.addHeaderView(view);
        erc_subscribe_circle.setAdapter(wrapper);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        circle_subscribe_rc.setLayoutManager(linearLayoutManager);
        erc_subscribe_circle.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
//        circle_subscribe_rc.setLayoutManager(new LinearLayoutManager(this));



        initListener();
    }

    private void initView() {
        //订阅页面顶部横向
        circle_subscribe_rc = (RecyclerView) view.findViewById(R.id.circle_subscribe_rc);
    }

    @Override
    protected void prepareData() {
        if (protocal == null){
            protocal = new CircleProtocal();
        }
        userid = SpTools.getString(this, Constants.userId,"");
        protocal.getMySubscribe(userid, new CircleProtocal.NormalListener() {
            @Override
            public void normalResponse(Object response) {
                if (response == null){
                    setErrorVisiblity();
                    return;
                }
                MydyBean bean = (MydyBean) response;
                dyList.clear();
                if (bean.dy != null) {
                    dyList.addAll(bean.dy);
                }
                if (dyList.size() == 0){
                    setEmptyVisiblity();
                    return;
                }
                //上边的横向adapter
                adapter.notifyDataSetChanged();
                //下面圈子列表
                circleList.clear();
                if (bean.quanzi != null) {
                    circleList.addAll(bean.quanzi);
                }
                wrapper.notifyDataSetChanged();
                setSuccessVisiblity();
            }
        });
    }

    public void refreshData(){
        if (protocal == null){
            protocal = new CircleProtocal();
        }
        userid = SpTools.getString(this, Constants.userId,"");
        protocal.getMySubscribe(userid, new CircleProtocal.NormalListener() {
            @Override
            public void normalResponse(Object response) {
                MydyBean bean = (MydyBean) response;
                dyList.clear();
                if (bean.dy != null) {
                    dyList.addAll(bean.dy);
                }
                //上边的横向adapter

                adapter.notifyDataSetChanged();
                //下面圈子列表
                circleList.clear();
                if (bean.quanzi != null) {
                    circleList.addAll(bean.quanzi);
                }
                wrapper.notifyDataSetChanged();

            }
        });
    }

    private void initListener() {
        iv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SubscribActivity.this,AddSubscribeUI.class));
            }
        });
    }

    public void setLoadingVisiblity(){
        loading.setVisibility(View.VISIBLE);
        empty.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        success.setVisibility(View.GONE);
    }
    public void setEmptyVisiblity(){
        loading.setVisibility(View.GONE);
        empty.setVisibility(View.VISIBLE);
        error.setVisibility(View.GONE);
        success.setVisibility(View.GONE);
    }
    public void setErrorVisiblity(){
        loading.setVisibility(View.GONE);
        empty.setVisibility(View.GONE);
        error.setVisibility(View.VISIBLE);
        success.setVisibility(View.GONE);
    }
    public void setSuccessVisiblity(){
        loading.setVisibility(View.GONE);
        empty.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        success.setVisibility(View.VISIBLE);
    }

}
