package com.lchtime.safetyexpress.ui.circle;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hyphenate.easeui.bean.ContactBean;
import com.hyphenate.easeui.bean.EaseInitBean;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.HeaderAndFooterWrapper;
import com.lchtime.safetyexpress.adapter.SingleInfoRCAdapter;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.HXInfo;
import com.lchtime.safetyexpress.bean.InitInfo;
import com.lchtime.safetyexpress.bean.MyCircleActiveBean;
import com.lchtime.safetyexpress.bean.SingleInfoBean;
import com.lchtime.safetyexpress.bean.res.CircleBean;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.chat.hx.activity.ApplyMessage;
import com.lchtime.safetyexpress.ui.circle.protocal.CircleProtocal;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lchtime.safetyexpress.views.CircleImageView;
import com.lidroid.xutils.view.annotation.ContentView;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by android-cp on 2017/5/18.个人主页
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
    @BindView(R.id.pb_progress)
    ProgressBar pb_progress;


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
    private TextView tvSubscribe;
    private TextView tvFriend;
    private String is_dy;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        ButterKnife.bind(this);
        setIsLoading(true);
        setTitle("个人主页");
        uid = getIntent().getStringExtra("uid");
        Log.i("qaz", "setControlBasis: " + uid);
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
    private boolean flagSingleInfo = false;
    private boolean flagCirlceInfo = false;
    @Override
    public void prepareData() {
        if (protocal == null) {
            protocal = new CircleProtocal();
        }
        userid = SpTools.getString(this, Constants.userId,"");

        protocal.getSingleInfoList(userid, uid, new CircleProtocal.NormalListener() {
            @Override
            public void normalResponse(Object response) {
                if (response == null){
                    flagSingleInfo = true;
                    if (flagSingleInfo && flagCirlceInfo){
                        setIsLoading(false);
                    }
                    CommonUtils.toastMessage("加载个人信息失败");
                    return;
                }
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
                }else if (arr.length == 2){
                    tvVipProfession.setText(arr[0]);
                    tvVipPost.setText(arr[1]);
                    tVipAddr.setText("");
                }else if (arr.length == 1){
                    tvVipProfession.setText(arr[0]);
                    tvVipPost.setText("");
                    tVipAddr.setText("");
                }else {
                    tvVipProfession.setText("");
                    tvVipPost.setText("");
                    tVipAddr.setText("");
                }
                singleInfoContent.setText(TextUtils.isEmpty(infoBean.user.ud_memo) ?  "未完善" : infoBean.user.ud_memo);
                tvSubscribeNum.setText(TextUtils.isEmpty(infoBean.user.dyNum) ? 0 + "" : infoBean.user.dyNum);
                tvFriendNum.setText(TextUtils.isEmpty(infoBean.user.friendNum) ? 0 + "" : infoBean.user.friendNum);
                llSubscribe.setOnClickListener(SingleInfoUI.this);
                llFriends.setOnClickListener(SingleInfoUI.this);

                is_dy = infoBean.user.is_dy;
                if ("0".equals(is_dy)){
                    tvSubscribe.setText("+ 订阅");
                }else {
                    tvSubscribe.setText("已订阅");
                }


                flagSingleInfo = true;
                if (flagSingleInfo && flagCirlceInfo){
                    setIsLoading(false);
                }
            }
        });

        protocal.getMyCircleList(userid,uid, new CircleProtocal.NormalListener() {
            @Override
            public void normalResponse(Object response) {
                if (response == null){
                    flagCirlceInfo = true;
                    if (flagSingleInfo && flagCirlceInfo){
                        setIsLoading(false);
                    }
                    CommonUtils.toastMessage("加载个人圈子信息失败");
                    return;
                }
                //我的圈子列表
                MyCircleActiveBean bean = (MyCircleActiveBean) response;
                myCircleList.clear();
                if (bean != null && bean.quanzi!= null) {
                    myCircleList.addAll(bean.quanzi);
                }
                if (myCircleList.size() == 0){
                    CommonUtils.toastMessage("他还没有发布圈子");
                }
                wrapperAdapter.notifyDataSetChanged();
                flagCirlceInfo = true;
                if (flagSingleInfo && flagCirlceInfo){
                    setIsLoading(false);
                }
            }
        });


        if (TextUtils.isEmpty(userid)){
            tvFriend.setText("+ 好友");
        }else {
            //初始化是否为好友关系，是否订阅

            if (EaseInitBean.contactBean != null && EaseInitBean.contactBean.friendlist != null && EaseInitBean.contactBean.friendlist.size() != 0) {

                for (ContactBean bean : EaseInitBean.contactBean.friendlist) {
                    if (uid.equals(bean.ud_ub_id) || userid.equals(uid)) {
                        tvFriend.setText("已添加");
                        break;
                    } else {
                        tvFriend.setText("+ 好友");
                    }
                }
            }else {
                tvFriend.setText("+ 好友");
            }
        }

        if (userid.equals(uid)){
            tvFriend.setVisibility(View.GONE);
            tvSubscribe.setVisibility(View.GONE);
        }


        if ("已添加".equals(tvFriend.getText())){
            tvFriend.setEnabled(false);
        }

        tvSubscribe.setOnClickListener(this);
        tvFriend.setOnClickListener(this);


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
        tvSubscribe = (TextView) view.findViewById(R.id.tv_subscribe);
        tvFriend = (TextView) view.findViewById(R.id.tv_friend);
    }

    private boolean dyClick = false;
    private Gson gson = new Gson();
    @Override
    public void onClick(View v) {

        if (v == llSubscribe){

            Intent intent = new Intent(this,OtherPersonSubscribeActivity.class);
            intent.putExtra("uid",uid);
            startActivity(intent);

        }else if(v == llFriends){

        }else if(v == tvSubscribe){

            if (dyClick){
                return;
            }
            dyClick = true;
            //订阅
            String userid = SpTools.getString(this, Constants.userId,"");
            if (TextUtils.isEmpty(userid)){
                CommonUtils.toastMessage("请登陆！！！");
                return;
            }else {
                String type = "0".equals(is_dy) ? "1" : "0";
                protocal.changeSubscribe(userid, uid, type , new CircleProtocal.CircleListener() {
                    @Override
                    public void circleResponse(CircleBean response) {
                        if (response == null){
                            CommonUtils.toastMessage("网络请求失败");
                            dyClick = false;
                            return;
                        }
                        if ("10".equals(response.result.code)){
                            if ("0".equals(is_dy)){
                                tvSubscribe.setText("已订阅");
                                is_dy = "1";
                            }else {
                                tvSubscribe.setText("+ 订阅");
                                is_dy = "0";
                            }

                            InitInfo.circleRefresh = true;
                            InitInfo.homeRefresh = true;
                        }else {
                            if ("0".equals(is_dy)){
                                tvSubscribe.setText("+ 订阅");
                            }else {
                                tvSubscribe.setText("已订阅");
                            }
                            CommonUtils.toastMessage("订阅失败！");
                        }
                        dyClick = false;
                    }
                });
            }
        } else if(v == tvFriend){

            if (TextUtils.isEmpty(userid)){
                CommonUtils.toastMessage("请登陆！！！");
                return;
            }

            if (!"已添加".equals(tvFriend.getText())) {
                String url = getResources().getString(R.string.service_host_address)
                        .concat(getResources().getString(R.string.hxLogin));

                OkHttpUtils.post().url(url)
                        .addParams("ub_id", uid)
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        //Log.d("0000---------------0000",e.getMessage());
                        CommonUtils.toastMessage("您网络信号不稳定，请稍后再试");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        HXInfo bean = gson.fromJson(response, HXInfo.class);
                        String code = bean.result.code;
                        String info = bean.result.info;
                        if (code.equals("10")) {
                            Intent intent = new Intent(SingleInfoUI.this, ApplyMessage.class);
                            intent.putExtra("type", "1");
                            intent.putExtra("groupid", "");
                            intent.putExtra("master", bean.hx_account);
                            startActivityForResult(intent, 102);

                        } else if (code.equals("20")) {
                            CommonUtils.toastMessage(info);

                        }
                    }
                });

            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 102&&resultCode == 102){
            if (data != null){
                String result = data.getStringExtra("finish");
                if ("1".equals(result)){
                    tvFriend.setText("已添加");
                    tvFriend.setEnabled(false);
                    CommonUtils.toastMessage("添加好友成功！");
                }
            }
        }
    }

    public void notifyDataSetChange(){
        wrapperAdapter.notifyDataSetChanged();
    }

    public void setIsLoading(boolean isLoading){
        if (isLoading){
            pb_progress.setVisibility(View.VISIBLE);
            backgroundAlpha(0.5f);
        }else {
            pb_progress.setVisibility(View.GONE);
            backgroundAlpha(1f);
        }
    }


    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }


}
