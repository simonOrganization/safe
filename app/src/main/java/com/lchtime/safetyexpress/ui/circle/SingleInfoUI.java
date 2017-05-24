package com.lchtime.safetyexpress.ui.circle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.HeaderAndFooterWrapper;
import com.lchtime.safetyexpress.adapter.MyCircleActiveAdapter;
import com.lchtime.safetyexpress.adapter.QuetionDetailAdapter;
import com.lchtime.safetyexpress.adapter.SingleInfoRCAdapter;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.MyCircleActiveBean;
import com.lchtime.safetyexpress.bean.SingleInfoBean;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.circle.protocal.CircleProtocal;
import com.lchtime.safetyexpress.ui.home.HomeQuewstionDetail;
import com.lchtime.safetyexpress.ui.vip.MyCircleActiveActivity;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lchtime.safetyexpress.views.CircleImageView;
import com.lchtime.safetyexpress.views.MyGridView;
import com.lidroid.xutils.view.annotation.ContentView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by android-cp on 2017/5/18.
 */
@ContentView(R.layout.single_info)
public class SingleInfoUI extends BaseUI implements View.OnClickListener {
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
    @BindView(R.id.single_info_list)
    RecyclerView singleInfoList;

    private HeaderAndFooterWrapper wrapperAdapter;
    private SingleInfoRCAdapter listAdapter;
    private CircleImageView civVipIcon;
    private TextView tvVipNickname;
    private TextView tvVipProfession;
    private TextView tvVipPost;
    private TextView tVipAddr ;
    private TextView singleInfoContent;
    private TextView tvSubscribeNum ;
    private TextView tvFriendNum ;
    private LinearLayout llSubscribe;
    private LinearLayout llFriends ;
    private String uid = "";
    private List<MyCircleActiveBean.QuanziBean> myCircleList = new ArrayList<>();

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        ButterKnife.bind(this);
        uid = getIntent().getStringExtra("uid");
        View view = View.inflate(this,R.layout.single_info_header,null);
        initView(view);
        singleInfoList.setLayoutManager(new GridLayoutManager(this,1));
        listAdapter = new SingleInfoRCAdapter(SingleInfoUI.this, myCircleList);
        wrapperAdapter = new HeaderAndFooterWrapper(listAdapter);
        wrapperAdapter.addHeaderView(view);
        singleInfoList.setAdapter(wrapperAdapter);
    }


    private CircleProtocal protocal;
    private String userid;
    @Override
    protected void prepareData() {
        if (protocal == null) {
            protocal = new CircleProtocal();
        }
        userid = SpTools.getString(this, Constants.userId,"");

        protocal.getSingleInfoList(userid, uid, new CircleProtocal.NormalListener() {
            @Override
            public void normalResponse(Object response) {
                SingleInfoBean infoBean = (SingleInfoBean) response;
                String[] arr = infoBean.user.user.split("\\s+");
                if (infoBean != null && !TextUtils.isEmpty(infoBean.user.ud_photo_fileid)){
                    Picasso.with(SingleInfoUI.this).load(infoBean.user.ud_photo_fileid).fit().into(civVipIcon);
                }
                tvVipNickname.setText(infoBean.user.ud_nickname);
                if (arr.length == 3) {
                    tvVipProfession.setText(arr[0]);
                    tvVipPost.setText(arr[1]);
                    tVipAddr.setText(arr[2]);
                }else {
                    tvVipProfession.setText("11111");
                    tvVipPost.setText("22222");
                    tVipAddr.setText("33333");
                }
                singleInfoContent.setText(TextUtils.isEmpty(infoBean.user.ud_memo) ?  "去完善" : infoBean.user.ud_memo);
                tvSubscribeNum.setText(TextUtils.isEmpty(infoBean.user.dyNum) ? 0 + "" : infoBean.user.dyNum);
                tvFriendNum.setText(TextUtils.isEmpty(infoBean.user.friendNum) ? 0 + "" : infoBean.user.friendNum);
                llSubscribe.setOnClickListener(SingleInfoUI.this);
                llFriends.setOnClickListener(SingleInfoUI.this);
            }
        });

        protocal.getMyCircleList(userid,uid, new CircleProtocal.NormalListener() {
            @Override
            public void normalResponse(Object response) {
                //我的圈子列表
                MyCircleActiveBean bean = (MyCircleActiveBean) response;
                myCircleList.clear();
                if (bean.quanzi!= null) {
                    myCircleList.addAll(bean.quanzi);
                }
                if (myCircleList.size() == 0){
                    CommonUtils.toastMessage("他还没有发布圈子");
                }
                wrapperAdapter.notifyDataSetChanged();
            }
        });


    }

    private void initView(View view) {
        civVipIcon = (CircleImageView) view.findViewById(R.id.civ_vip_icon);
        tvVipNickname = (TextView) view.findViewById(R.id.tv_vip_nickname);
        tvVipProfession = (TextView) view.findViewById(R.id.tv_vip_profession);
        tvVipPost = (TextView) view.findViewById(R.id.tv_vip_post);
        tVipAddr = (TextView) view.findViewById(R.id.tv_vip_addr);
        singleInfoContent = (TextView) view.findViewById(R.id.single_info_content);
        tvSubscribeNum = (TextView) view.findViewById(R.id.tv_subscribe_num);
        tvFriendNum = (TextView) view.findViewById(R.id.tv_friend_num);
        llSubscribe = (LinearLayout) view.findViewById(R.id.ll_subscribe);
        llFriends = (LinearLayout) view.findViewById(R.id.ll_friends);
    }


    @Override
    public void onClick(View v) {
        if (v == llSubscribe){

            Intent intent = new Intent(this,OtherPersonSubscribeActivity.class);
            intent.putExtra("uid",uid);
            startActivity(intent);

        }else if(v == llFriends){

        }
    }
}
