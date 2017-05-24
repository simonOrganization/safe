package com.lchtime.safetyexpress.ui.vip;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by android-cp on 2017/4/21.
 */

@ContentView(R.layout.vip_bindzhifubao)
public class BindZhiFuBaoActivity extends BaseUI implements View.OnClickListener {
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
    @BindView(R.id.tv_login_login)
    TextView tvLoginLogin;
    @BindView(R.id.editText_name)
    EditText editTextName;
    @BindView(R.id.editText_num)
    EditText editTextNum;
    private String titleText;
    private VipProtocal protocal;
    private String userid;
    private boolean isChange = false;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        ButterKnife.bind(this);
        if (protocal == null){
            protocal = new VipProtocal();
        }
        userid = SpTools.getString(this, Constants.userId,"");
        titleText = getIntent().getStringExtra("title");
        if ("更换提现账号".equals(titleText)) {
            isChange = true;
            tvLoginLogin.setText("修改");
        } else {
            isChange = false;
            tvLoginLogin.setText("提交");
        }
        setTitle(titleText);
        tvLoginLogin.setOnClickListener(this);

    }

    @Override
    protected void prepareData() {

    }

    @Override
    public void onClick(View v) {

        if (v == tvLoginLogin) {
            //如果是确认修改按钮
            String name = editTextName.getText().toString().trim();
            String num = editTextNum.getText().toString().trim();
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(num)){
                CommonUtils.toastMessage("名字或支付宝账号不能为空");
            }
            //如果为true就为更新，传1
            String type = isChange ? "1" : "0";
            protocal.getAddAcount(userid, type, name, num, new CircleProtocal.NormalListener() {
                @Override
                public void normalResponse(Object response) {
                    MyAccountBean bean = (MyAccountBean)response;
                    if ("10".equals(bean.result.code)){
                        CommonUtils.toastMessage(bean.result.info);

                        finish();
                    }else {
                        CommonUtils.toastMessage("失败");
                    }
                }
            });
        }
    }
}
