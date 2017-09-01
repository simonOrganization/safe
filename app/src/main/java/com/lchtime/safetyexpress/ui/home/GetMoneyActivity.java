package com.lchtime.safetyexpress.ui.home;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.Result;
import com.lchtime.safetyexpress.bean.VipInfoBean;
import com.lchtime.safetyexpress.pop.SharePop;
import com.lchtime.safetyexpress.shareapi.ShareConstants;
import com.lchtime.safetyexpress.shareapi.Util;
import com.lchtime.safetyexpress.shareapi.qq.ShareQQ;
import com.lchtime.safetyexpress.shareapi.wx.ShareWX;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.CallBackActivity;
import com.lchtime.safetyexpress.ui.home.protocal.ShareProtocal;
import com.lchtime.safetyexpress.ui.login.LoginUI;
import com.lchtime.safetyexpress.ui.vip.VipInfoUI;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.LoginInternetRequest;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lchtime.safetyexpress.weight.LoginDialog;
import com.lidroid.xutils.view.annotation.ContentView;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hyphenate.easeui.bean.EaseConstants.userId;


/**
 * Created by Dreamer on 2017/6/14. 轮播图分享得奖界面
 */
@ContentView(R.layout.activity_get_money)
public class GetMoneyActivity extends BaseUI implements View.OnClickListener {


    @BindView(R.id.ll_home)
    LinearLayout mLlHome;
    @BindView(R.id.v_title)
    TextView mVTitle;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.ll_back)
    LinearLayout mLlBack;
    @BindView(R.id.tv_right)
    TextView mTvRight;
    @BindView(R.id.iv_right)
    ImageView mIvRight;
    @BindView(R.id.tv_delete)
    TextView mTvDelete;
    @BindView(R.id.ll_right)
    LinearLayout mLlRight;
    @BindView(R.id.rl_title)
    RelativeLayout mRlTitle;
    @BindView(R.id.iv_first)
    ImageView mIvFirst;
    @BindView(R.id.first)
    LinearLayout mFirst;
    @BindView(R.id.iv_second)
    ImageView mIvSecond;
    @BindView(R.id.second)
    LinearLayout mSecond;
    @BindView(R.id.iv_third)
    ImageView mIvThird;
    @BindView(R.id.third)
    LinearLayout mThird;
    @BindView(R.id.forth)
    LinearLayout mForth;
    @BindView(R.id.money_progress_bar)
    ImageView mMoneyProgressBar;
    @BindView(R.id.cb_give_money)
    CheckBox mCbGiveMoney;
    @BindView(R.id.tv_small_title)
    TextView mSmallTitle;
    private List<LinearLayout> list = new ArrayList<>();
    private List<ImageView> listImg = new ArrayList<>();
    private String ub_id;
    private int x = 0;


    public static IWXAPI api;
    private SharePop sharePop;

    public static int flag = 0;
    public boolean isGetMoney = false; //是否已经领取奖励

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        ButterKnife.bind(this);
        setTitle("");
        sharePop = new SharePop(mIvThird, GetMoneyActivity.this, R.layout.pop_share);
        initWX();


        list.add(mFirst);
        list.add(mSecond);
        list.add(mThird);
        list.add(mForth);

        listImg.add(mIvFirst);
        listImg.add(mIvSecond);
        listImg.add(mIvThird);



    }

    @Override
    protected void onResume() {
        super.onResume();

        ub_id = SpTools.getUserId(this);

        if (TextUtils.isEmpty(ub_id)) {
            setSelected(0);
        } else {
            getInfo();
        }
//        setOnclickCheckBox(3);
//         setSelected(3);

        mCbGiveMoney.setOnClickListener(this);
    }

    private void initWX() {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, ShareConstants.WX_APP_ID, false);
        // 将该app注册到微信
        api.registerApp(ShareConstants.WX_APP_ID);
    }

    private Gson gson = new Gson();

    private void getInfo() {
        x = 1;
        LoginInternetRequest.getVipInfo(ub_id, new LoginInternetRequest.ForResultListener() {
            @Override
            public void onResponseMessage(String code) {
                if (!TextUtils.isEmpty(code)) {
                    VipInfoBean vipInfoBean = gson.fromJson(code, VipInfoBean.class);
                    if (vipInfoBean != null) {
//                        InitInfo.phoneNumber = vipInfoBean.user_detail.ub_phone;
//                        InitInfo.vipInfoBean = vipInfoBean;
//                        InitInfo.isLogin = true;
//                        SpTools.setString(GetMoneyActivity.this, Constants.nik_name, vipInfoBean.user_detail.ud_nickname);
                        if ((TextUtils.isEmpty(vipInfoBean.user_detail.ud_profession)) ||
                                (TextUtils.isEmpty(vipInfoBean.user_detail.ud_post)) ||
                                (TextUtils.isEmpty(vipInfoBean.user_detail.ud_addr))) {
                            setSelected(1);
                            mSmallTitle.setText("您还未完善信息");
                        } else {
                            //判断是否分享  //是否分享  未分享  已分享,未领奖  已领奖
                            if ("未分享".equals(vipInfoBean.user_detail.share)){
                                setSelected(2);
                                mSmallTitle.setText("您还未分享");
                            }else if ("已分享,未领奖".equals(vipInfoBean.user_detail.share)){
                                setSelected(3);
                                mSmallTitle.setText("您还未领奖");
                            }else if ("已领奖".equals(vipInfoBean.user_detail.share)){
                                setSelected(4);
                                mSmallTitle.setText("您已完成任务！");
                                isGetMoney = true; //表示已经领取过奖励
                            }

                        }
                    } else {
                        setSelected(1);
                    }
                } else {
                    CommonUtils.toastMessage("网络错误，请重新载入界面查看最新动态");
                    setSelected(1);
                }
            }
        });

    }


    @Override
    protected void prepareData() {

    }


    public void setSelected(final int num) {

        //设置bar
        if (num == 0) {
            mMoneyProgressBar.setImageResource(R.drawable.money_bar1);
        } else if (num == 1) {
            mMoneyProgressBar.setImageResource(R.drawable.money_bar2);
        } else if (num == 2) {
            mMoneyProgressBar.setImageResource(R.drawable.money_bar3);
        } else if (num == 3 || num == 4) {
            mMoneyProgressBar.setImageResource(R.drawable.money_bar4);
        }

        if (num == 3){
            mCbGiveMoney.setChecked(false);
        }else {
            mCbGiveMoney.setChecked(true);
        }
        for (int i = 0;i<list.size(); i++){
            if (i == num){
                list.get(i).setVisibility(View.VISIBLE);
                if (num != 3 && num != 4) {
                    listImg.get(i).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            clickTask(num);
                        }
                    });
                }
            }else {
                list.get(i).setVisibility(View.GONE);
            }
        }

        if(num == 4){
            list.get(3).setVisibility(View.VISIBLE);
        }


    }



    private void clickTask(int num) {

        if (num == 0) {
            LoginDialog dialog = new LoginDialog(mContext, new LoginDialog.onClickLogin() {
                @Override
                public void OnClickLogin() {
                    Intent intent = new Intent(mContext,LoginUI.class);
                    startActivity(intent);
                }
            });
            dialog.show();
            //finish();
        } else if (num == 1) {
            Intent intent = new Intent(this, VipInfoUI.class);
            startActivity(intent);
            //finish();
        } else if (num == 2) {
            //分享到朋友圈 分享网址http://47.94.44.84/download/
//            标题：注册分享，得百元红包
//            描述：下载安全快车APP,注册完善信息并分享给好友，现金红包马上到账！
            sharePop.showAtLocation();
            sharePop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flag = 1;
                    switch (v.getId()) {

                        case R.id.ll_share_weixin:
                            sharePop.dismiss();
                            ShareWX.getShareWXInstance(api).share2Wx(true,Constants.SHARE_TITLE,Constants.SHARE_DES,Constants.SHARE);
                            break;
                        case R.id.ll_share_friend:
                            sharePop.dismiss();
                            ShareWX.getShareWXInstance(api).share2Wx(false,Constants.SHARE_TITLE,Constants.SHARE_DES,Constants.SHARE);
                            break;
                        case R.id.ll_share_sina:
                            sharePop.dismiss();
//                        ShareWeiBo.getShareWeiboInstants().shareToWeibo(H5DetailUI.this,"微博分享测试" + baseUrl,mWeiboShareAPI);
                            Intent intent = new Intent(GetMoneyActivity.this,CallBackActivity.class);
                            intent.putExtra("shareUrl",Constants.SHARE);
                            intent.putExtra("more","more");
                            intent.putExtra("title",Constants.SHARE_TITLE);
                            intent.putExtra("des",Constants.SHARE_DES);
                            startActivity(intent);
                            break;
                        case R.id.ll_share_qq:
                            sharePop.dismiss();
                            ShareQQ.getShareQQInstants().shareToQQ(GetMoneyActivity.this,qqShareListener, Constants.SHARE_TITLE,Constants.SHARE_DES,Constants.SHARE);
                            break;
                    }
                }
            });


        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cb_give_money){
            mCbGiveMoney.setChecked(true);
            if(isGetMoney){
                makeText("您已领取奖励，请在我的钱包查看");
                return;
            }

            if (!mCbGiveMoney.isChecked()){
                //mCbGiveMoney.setChecked(true);
            }else {
                //执行去领奖操作
                //mCbGiveMoney.setChecked(true);

                protocal.postGetMoney(new ShareProtocal.ShareInfo() {
                    @Override
                    public void shareResponse(String response) {
                        if (TextUtils.isEmpty(response)){
                            CommonUtils.toastMessage("获取奖励失败，请重新再试！");
                            mCbGiveMoney.setChecked(false);
                            return;
                        }

                        Result bean = gson.fromJson(response,Result.class);
                        if ("10".equals(bean.result.code)){
                            CommonUtils.toastMessage(bean.result.info);
                            mCbGiveMoney.setChecked(true);

                        }else {
                            CommonUtils.toastMessage(bean.result.info);
                            mCbGiveMoney.setChecked(false);
                        }
                    }
                });

            }
        }
    }


    ShareProtocal protocal = new ShareProtocal();
    IUiListener qqShareListener = new IUiListener() {
        @Override
        public void onCancel() {
            Util.toastMessage(GetMoneyActivity.this, "onCancel: ");
        }

        @Override
        public void onComplete(Object response) {
            flag = 0;
            // TODO Auto-generated method stub
            protocal.postShare(new ShareProtocal.ShareInfo() {
                @Override
                public void shareResponse(String response) {

                    if (TextUtils.isEmpty(response)){
                        CommonUtils.toastMessage("请重新分享已确保获得奖励。");
                        return;
                    }

                    Result bean = gson.fromJson(response,Result.class);
                    if ("10".equals(bean.result.code)){
                        CommonUtils.toastMessage(bean.result.info);
                    }else {
                        CommonUtils.toastMessage(bean.result.info);
                    }
                }
            });

//            Util.toastMessage(GetMoneyActivity.this, "onComplete: " + response.toString());

        }

        @Override
        public void onError(UiError e) {
            // TODO Auto-generated method stub
            Util.toastMessage(GetMoneyActivity.this, "onError: " + e.errorMessage, "e");
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, qqShareListener);
    }



}
