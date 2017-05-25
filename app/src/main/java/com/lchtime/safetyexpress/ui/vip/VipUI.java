package com.lchtime.safetyexpress.ui.vip;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.CheckUpdataBean;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.InitInfo;
import com.lchtime.safetyexpress.bean.VipInfoBean;
import com.lchtime.safetyexpress.pop.VipInfoHintPop;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.TabUI;
import com.lchtime.safetyexpress.ui.login.LoginUI;
import com.lchtime.safetyexpress.ui.login.RegisterUI;
import com.lchtime.safetyexpress.utils.BitmapUtils;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.LoginInternetRequest;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lchtime.safetyexpress.utils.UpdataImageUtils;
import com.lchtime.safetyexpress.views.CircleImageView;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.File;

/**
 * 我的
 * Created by user on 2017/4/14.
 */
@ContentView(R.layout.vip)
public class VipUI extends BaseUI {

    //头像
    @ViewInject(R.id.civ_vip_icon)
    private CircleImageView civ_vip_icon;

    //昵称
    @ViewInject(R.id.tv_vip_nickname)
    private TextView tv_vip_nickname;

    //没有登陆的布局
    @ViewInject(R.id.ll_vip_logout)
    private LinearLayout logOut;

    //登陆后显示的布局
    @ViewInject(R.id.ll_vip_login)
    private LinearLayout logIn;

    //登陆后显示的布局
    @ViewInject(R.id.tv_vip_info)
    private LinearLayout addr_pro_post;


    private Gson gson;

    private Handler handler;

    @Override
    protected void back() {
        exit();
    }

    @Override
    protected void setControlBasis() {
        setTitle("我");
        backGone();
        handler = new Handler();
    }

    @Override
    protected void prepareData() {

    }

    @Override
    protected void onResume() {
        super.onResume();

        String ub_id = SpTools.getString(this, Constants.userId,"");
        if (TextUtils.isEmpty(ub_id)){
            logIn.setVisibility(View.GONE);
            logOut.setVisibility(View.VISIBLE);
        }else {
            //登录的情况下
            logIn.setVisibility(View.VISIBLE);
            logOut.setVisibility(View.GONE);
            if (InitInfo.vipInfoBean != null) {
                initVipInfo();
            }
        }
    }

    //设置个人相关信息
    private void initVipInfo() {
        File file = new File(MyApplication.getContext().getFilesDir(),Constants.photo_name);//将要保存图片的路径
        //如果没有加载过图片了
        if (!file.exists()){
            civ_vip_icon.setImageDrawable(getResources().getDrawable(R.drawable.circle_user_image));
            if (!TextUtils.isEmpty(InitInfo.vipInfoBean.user_detail.ud_photo_fileid)){
                UpdataImageUtils.getUrlBitmap(InitInfo.vipInfoBean.user_detail.ud_photo_fileid, new UpdataImageUtils.BitmapListener() {
                    @Override
                    public void giveBitmap(final Bitmap bitmap) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                UpdataImageUtils.saveBitmapFile(bitmap,Constants.photo_name);
                                civ_vip_icon.setImageBitmap(bitmap);
                            }
                        });

                    }
                });

            }

        }else {
            civ_vip_icon.setImageBitmap(BitmapUtils.getBitmap(file.getAbsolutePath()));
        }

        if (InitInfo.isLogin) {
            tv_vip_nickname.setText(TextUtils.isEmpty(InitInfo.vipInfoBean.user_detail.ud_nickname) ? "设置昵称" : InitInfo.vipInfoBean.user_detail.ud_nickname);
        }else {
            String name = SpTools.getString(this,Constants.nik_name,"");
            tv_vip_nickname.setText(TextUtils.isEmpty(name) ? "设置昵称" : name);
        }

        String[] arr = {InitInfo.vipInfoBean.user_detail.ud_post,InitInfo.vipInfoBean.user_detail.ud_profession,InitInfo.vipInfoBean.user_detail.ud_addr};
        if (!(TextUtils.isEmpty(arr[0])||
                TextUtils.isEmpty(arr[1])||
                TextUtils.isEmpty(arr[2]))){
            float desity = getResources().getDisplayMetrics().density;
            addr_pro_post.removeAllViews();
            for (int i = 0; i < arr.length;i++){
                TextView tv = new TextView(this);
                tv.setText(arr[i]);
                tv.setBackground(getResources().getDrawable(R.drawable.shape_white_border));
                tv.setHeight(R.dimen.dm050);
                tv.setGravity(Gravity.CENTER);
                tv.setSingleLine(true);
                tv.setTextColor(Color.WHITE);
                tv.setTextSize(26/desity + 0.5f);
                tv.setPadding((int)(30/desity + 0.5f),0,(int)(30/desity + 0.5f),0);
                if (i > 0){
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins((int)(10/desity + 0.5f),0,0,0);//4个参数按顺序分别是左上右下

                    tv.setLayoutParams(layoutParams);
                }
                addr_pro_post.addView(tv);
            }
        }else {
            float desity = getResources().getDisplayMetrics().density;
            addr_pro_post.removeAllViews();
            TextView tv = new TextView(this);
            tv.setText("去完善个人信息");
            tv.setBackground(getResources().getDrawable(R.drawable.shape_white_border));
            tv.setHeight(R.dimen.dm050);
            tv.setGravity(Gravity.CENTER);
            tv.setSingleLine(true);
            tv.setTextColor(Color.WHITE);
            tv.setTextSize(26/desity + 0.5f);
            tv.setPadding((int)(30/desity + 0.5f),0,(int)(30/desity + 0.5f),0);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins((int)(10/desity + 0.5f),0,0,0);//4个参数按顺序分别是左上右下
            tv.setLayoutParams(layoutParams);

            addr_pro_post.addView(tv);
        }
    }

    /**
     *手机号登陆
     *
     * @param view
     */
    @OnClick(R.id.tv_phonenum_login)
    private void getLoginActivity(View view) {
        Intent intent = new Intent(VipUI.this, LoginUI.class);
        startActivity(intent);

    }

    /**
     *手机号快速注册
     *
     * @param view
     */
    @OnClick(R.id.tv_phonenum_register)
    private void getPhoneRegister(View view) {
        Intent intent = new Intent(VipUI.this, RegisterUI.class);
        startActivity(intent);

    }


    /**
     * 个人信息
     *
     * @param view
     */
    @OnClick(R.id.civ_vip_icon)
    private void getInfo(View view) {
        //有网络的情况下
        if (CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
            //没有个人信息的情况下
            if (InitInfo.vipInfoBean == null){
                Toast.makeText(this,"网络异常",Toast.LENGTH_SHORT).show();
                return;
            }else if(InitInfo.vipInfoBean.user_detail == null){
                Toast.makeText(this,"网络异常",Toast.LENGTH_SHORT).show();
                return;
            }
            if (!InitInfo.isLogin) {
                getVipInfo();
            }
            Intent intent = new Intent(VipUI.this, VipInfoUI.class);
            startActivity(intent);
        }else {
            Toast.makeText(this,"请检查网络设置",Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 设置昵称
     *
     * @param view
     */
    @OnClick(R.id.tv_vip_nickname)
    private void getNickname(View view) {
        //有网络的情况下
        if (CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
            //没有个人信息的情况下
            if (InitInfo.vipInfoBean == null){
                Toast.makeText(this,"网络异常",Toast.LENGTH_SHORT).show();
                return;
            }else if(InitInfo.vipInfoBean.user_detail == null){
                Toast.makeText(this,"网络异常",Toast.LENGTH_SHORT).show();
                return;
            }
            if (!InitInfo.isLogin) {
                getVipInfo();
            }
            Intent intent = new Intent(VipUI.this, VipInfoUI.class);
            startActivity(intent);
        }else {
            Toast.makeText(this,"请检查网络设置",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 完善个人信息
     *
     * @param view
     */
    @OnClick(R.id.tv_vip_info)
    private void getWanShanInfo(View view) {
        //有网络的情况下
        if (CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
            //没有个人信息的情况下
            if (InitInfo.vipInfoBean == null){
                Toast.makeText(this,"网络异常",Toast.LENGTH_SHORT).show();
                return;
            }else if(InitInfo.vipInfoBean.user_detail == null){
                Toast.makeText(this,"网络异常",Toast.LENGTH_SHORT).show();
                return;
            }
            if (!InitInfo.isLogin) {
                getVipInfo();
            }
            Intent intent = new Intent(VipUI.this, VipInfoUI.class);
            startActivity(intent);
        }else {
            Toast.makeText(this,"请检查网络设置",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 我的动态
     *
     * @param view
     */
    @OnClick(R.id.ll_vip_mycircle_active)
    private void getMyActive(View view) {
        String ub_id = SpTools.getString(this, Constants.userId,"");
        if (TextUtils.isEmpty(ub_id)){
            Intent intent = new Intent(this, LoginUI.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(VipUI.this, MyCircleActiveActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 我的收藏
     *
     * @param view
     */
    @OnClick(R.id.tv_vip_collected)
    private void getMyConllected(View view) {
        String ub_id = SpTools.getString(this, Constants.userId,"");
        if (TextUtils.isEmpty(ub_id)){
            Intent intent = new Intent(this, LoginUI.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(VipUI.this, MyConllected.class);
            startActivity(intent);
        }
    }

    /**
     * 意见反馈
     *
     * @param view
     */
    @OnClick(R.id.ll_vip_opinion)
    private void getOpinion(View view) {
//        String ub_id = SpTools.getString(this, Constants.CONFIGFILE,"");
//        if (TextUtils.isEmpty(ub_id)){
//            Intent intent = new Intent(this, LoginUI.class);
//            startActivity(intent);
//        }else {
            Intent intent = new Intent(VipUI.this, OpinionActivity.class);
            startActivity(intent);
//        }
    }

    /**
     * 我的钱包
     *
     * @param view
     */
    @OnClick(R.id.ll_vip_mymoney)
    private void getMyMoney(View view) {
        String ub_id = SpTools.getString(this, Constants.userId,"");
        if (TextUtils.isEmpty(ub_id)){
            Intent intent = new Intent(this, LoginUI.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(VipUI.this, MyMoneyActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 设置
     *
     * @param view
     */
    @OnClick(R.id.ll_vip_setting)
    private void getSetting(View view) {
        String ub_id = SpTools.getString(this, Constants.userId,"");
        if (TextUtils.isEmpty(ub_id)){
            Intent intent = new Intent(this, LoginUI.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(VipUI.this, VipSettingUI.class);
            startActivity(intent);
        }
    }

    private void getVipInfo() {
        if (gson == null) {
            gson = new Gson();
        }
        String userId = SpTools.getString(this, Constants.userId, "");
        //登录操作
        LoginInternetRequest.getVipInfo(userId, new LoginInternetRequest.ForResultListener() {
            @Override
            public void onResponseMessage(String code) {
                if (!TextUtils.isEmpty(code)) {
                    VipInfoBean vipInfoBean = gson.fromJson(code, VipInfoBean.class);
                    if (vipInfoBean != null) {
                        InitInfo.phoneNumber = vipInfoBean.user_detail.ub_phone;
                        InitInfo.vipInfoBean = vipInfoBean;
                        InitInfo.isLogin = true;
                        SpTools.setString(VipUI.this, Constants.nik_name, vipInfoBean.user_detail.ud_nickname);
                        SpTools.setString(VipUI.this, Constants.nik_name, vipInfoBean.user_detail.ud_nickname);
                    }
                }
            }
        });
    }

}
