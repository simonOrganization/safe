package com.lchtime.safetyexpress.ui.chat.hx.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.InitInfo;
import com.lchtime.safetyexpress.bean.Result;
import com.lchtime.safetyexpress.bean.VipInfoBean;
import com.lchtime.safetyexpress.ui.chat.hx.activity.protocal.GetInfoProtocal;
import com.lchtime.safetyexpress.ui.chat.hx.fragment.protocal.AddCommandProtocal;
import com.lchtime.safetyexpress.ui.chat.hx.utils.WindowUtils;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.SpTools;

/**
 * Created by Dreamer on 2017/6/8.
 */

public class ApplyMessage extends Activity implements View.OnClickListener {
    private TextView mTitle;
    private TextView mTitleRight;
    private LinearLayout mTitleLeft;
    private LinearLayout mLlTitleRight;

    private EditText mEtName;
    private String mType;
    private String mUserid;
    private String groupId;
    private String mMaster;
    private ProgressBar mBar;
    private VipInfoBean mVipInfoBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_qun_name);
        mVipInfoBean = SpTools.getUser(this);
        initTitle();
        initView();
        initData();

    }


    private void initTitle() {
        mBar = (ProgressBar) findViewById(R.id.pb_progress);
        mTitle = (TextView) findViewById(R.id.title);
        mTitleRight = (TextView) findViewById(R.id.tv_delete);
        mTitleLeft = (LinearLayout) findViewById(R.id.ll_back);
        mLlTitleRight = (LinearLayout) findViewById(R.id.ll_right);
        mLlTitleRight.setVisibility(View.VISIBLE);
        mTitle.setText("朋友验证");
        mTitleRight.setText("发送");
        mTitleRight.setVisibility(View.VISIBLE);
        mLlTitleRight.setOnClickListener(this);
        mTitleLeft.setOnClickListener(this);
    }

    private void initView() {

        mEtName = (EditText) findViewById(R.id.tv_qun_name);
        mEtName.setHint("请输入验证消息");
        mEtName.setText("我是" + mVipInfoBean.user_detail.ud_nickname + ",请加我为好友吧！");
        mEtName.setSelection(mEtName.getText().toString().length());
    }

    private void initData() {

        //0为群，1位好友
        mType = getIntent().getStringExtra("type");
        groupId = getIntent().getStringExtra("groupid");
        mMaster = getIntent().getStringExtra("master");
        mUserid = SpTools.getUserId(this);
    }

    private GetInfoProtocal mProtocal;
    private Gson gson = new Gson();
    @Override
    public void onClick(View v) {
        if (mProtocal == null){
            mProtocal = new GetInfoProtocal();
        }
        if (v.getId() == R.id.ll_back){
            finish();
        }else if (v.getId() == R.id.ll_right){
//            if ("0".equals(mType)){
                setLoadding(true);
                String message = mEtName.getText().toString().trim();
                if (TextUtils.isEmpty(message)){
                    message = "我是" + mVipInfoBean.user_detail.ud_nickname + ",请加我为好友吧！";
                }
                //发送验证消息
                if (TextUtils.isEmpty(mUserid)){
                    CommonUtils.toastMessage("申请添加失败，请稍后再试！");
                    setLoadding(false);
                    return;
                }

                if (TextUtils.isEmpty(mMaster)){
                    CommonUtils.toastMessage("服务器繁忙，请稍后重试!");
                    setLoadding(false);
                    return;
                }
                if (groupId == null){
                    groupId = "";
                }


                mProtocal.getApply(mVipInfoBean.user_detail.ub_phone, groupId,mType,message, mMaster, new AddCommandProtocal.NormalListener() {
                    @Override
                    public void normalResponse(Object response) {
                        if (response == null){
                            CommonUtils.toastMessage("申请添加失败，请稍后再试");
                            setLoadding(false);
                            return;
                        }
                        Result result = gson.fromJson((String) response, Result.class);
                        if ("10".equals(result.result.code)){
                            CommonUtils.toastMessage(result.result.info);
                            Intent intent = new Intent();
                            intent.putExtra("finish","1");
                            setResult(102,intent);
                            setLoadding(false);
                            finish();
                        }else {
                            CommonUtils.toastMessage(result.result.info);
                            setLoadding(false);
                        }
                    }
                });
//            }

        }
    }


    public void setLoadding(boolean isLoadding){
        if (isLoadding){
            WindowUtils.backgroundAlpha(this,0.5f);
            mBar.setVisibility(View.VISIBLE);
        }else {
            WindowUtils.backgroundAlpha(this,1f);
            mBar.setVisibility(View.GONE);
        }
    }
}
