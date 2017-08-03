package com.lchtime.safetyexpress.ui.vip;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.MyAccountBean;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.circle.protocal.CircleProtocal;
import com.lchtime.safetyexpress.ui.vip.protocal.VipProtocal;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.event.OnClick;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by android-cp on 2017/4/21.我的钱包界面
 */
@ContentView(R.layout.vip_mymoney)
public class MyMoneyActivity extends BaseUI implements View.OnClickListener {
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
    @BindView(R.id.tv_account_balance)
    TextView tvAccountBalance;
    @BindView(R.id.iv_zhifubao_img)
    ImageView ivZhifubaoImg;
    @BindView(R.id.rl_money_zhifubao)
    RelativeLayout rlMoneyZhifubao;
    @BindView(R.id.tv_change_account)
    TextView tvChangeAccount;
    @BindView(R.id.tv_bind_zhifubao)
    TextView tvBindZhifubao;
    @BindView(R.id.item_authorization_zhifubao)
    RelativeLayout item_authorization_zhifubao;
    private VipProtocal protocal;
    private boolean isBind = false;
    private MyAccountBean bean;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        ButterKnife.bind(this);
        setTitle("我的账户");
        rightTextVisible("账户明细");

    }

    private String userid;

    @Override
    protected void prepareData() {
        if (protocal == null) {
            protocal = new VipProtocal();
        }
        if (userid == null) {
            userid = SpTools.getUserId(this);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        protocal.getMyAcountInfo(userid, new CircleProtocal.NormalListener() {
            @Override
            public void normalResponse(Object response) {
                if (response == null){
                    CommonUtils.toastMessage("获取账户相关信息失败，请重新获取！");
                    return;
                }
                bean = (MyAccountBean) response;
                tvAccountBalance.setText("¥" + bean.ud_amount);
                if ("1".equals(bean.tparty.ud_zfb_account)) {
                    //没有绑定支付宝
                    isBind = false;
                    tvBindZhifubao.setText("未绑定");
                    tvBindZhifubao.setTextColor(getResources().getColor(R.color.commen_reg));
                    tvChangeAccount.setVisibility(View.GONE);
                } else {
                    //绑定了支付宝
                    isBind = true;
                    tvBindZhifubao.setText("已绑定");
                    tvBindZhifubao.setTextColor(getResources().getColor(R.color.title_999));
                    tvChangeAccount.setVisibility(View.VISIBLE);
                    tvChangeAccount.setOnClickListener(MyMoneyActivity.this);
                }
            }
        });
    }

    @OnClick(R.id.item_authorization_zhifubao)
    private void getBindZhiFuBao(View view) {
        //如果已绑定
        if (isBind){
            //如果绑定，进入提现界面
            if (bean != null) {
                Intent intent = new Intent(MyMoneyActivity.this, OutPutMoneyActivity.class);
                intent.putExtra("ud_zfb_account", bean.tparty.ud_zfb_account);
                intent.putExtra("ud_amount", bean.ud_amount);
                startActivity(intent);
            }else {
                CommonUtils.toastMessage("请检查网络");
            }
        }else {
            //如果没有绑定，进入绑定账号界面
            Intent intent = new Intent(this, BindZhiFuBaoActivity.class);
            intent.putExtra("title","绑定账号");
            startActivity(intent);
        }

    }

    @Override
    public void onClick(View v) {
        //如果点击了更换提现账号
        if (v == tvChangeAccount){
            Intent intent = new Intent(this, BindZhiFuBaoActivity.class);
            intent.putExtra("title","更换提现账号");
            startActivity(intent);
        }
    }

    @Override
    protected void clickEvent() {
        //右上侧账户明细点击事件
        Intent intent = new Intent(this,AccountDetailActivity.class);
        startActivity(intent);
    }
}
