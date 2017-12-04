package com.lchtime.safetyexpress.ui.vip;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Result;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.circle.protocal.CircleProtocal;
import com.lchtime.safetyexpress.ui.vip.protocal.VipProtocal;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.DialogUtil;
import com.lidroid.xutils.view.annotation.ContentView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${Hongcha36} on 2017/11/8.
 * 确认提现
 */
@ContentView(R.layout.activity_affirm)
public class AffirmPWActivity extends BaseUI {

    @BindView(R.id.tv_enter)
    TextView mEnterTv;
    @BindView(R.id.et_password)
    EditText mPasswordEt;

    private String zfbNumber; //支付宝账号
    private String mAllMoney;//总金额
    private String userid;
    private VipProtocal protocal;
    private DialogUtil mDialog;


    @Override
    protected void setControlBasis() {
        ButterKnife.bind(this);
        setTitle("确认提现");
        mDialog = new DialogUtil(mContext);
        zfbNumber = getIntent().getStringExtra("ud_zfb_account");
        mAllMoney = getIntent().getStringExtra("ud_amount");
        userid = getIntent().getStringExtra("user_id");
        mEnterTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = mPasswordEt.getText().toString().trim();
                if(TextUtils.isEmpty(password)){
                    makeText("请输入登录密码");
                }else{
                    affirmPW(password);
                }
            }
        });
    }

    @Override
    protected void prepareData() {

    }


    /**
     * 访问网络 验证密码 ，是否超过错误次数
     */
    private void affirmPW(String password){

        if (protocal == null){
            protocal = new VipProtocal();
        }

        protocal.getTiXian(userid, mAllMoney, zfbNumber, password , mDialog ,new CircleProtocal.NormalListener() {
            @Override
            public void normalResponse(Object response) {
                Result result = (Result) response;
                if ("10".equals(result.result.code)){
                    CommonUtils.toastMessage(result.result.info);
                    finish();
                }else {
                    CommonUtils.toastMessage(result.result.info);
                }

            }
        });
    }


    @Override
    protected void back() {
        finish();
    }
}
