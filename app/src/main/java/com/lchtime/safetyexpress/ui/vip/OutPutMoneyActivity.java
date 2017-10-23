package com.lchtime.safetyexpress.ui.vip;

import android.graphics.Bitmap;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.InitInfo;
import com.lchtime.safetyexpress.bean.Result;
import com.lchtime.safetyexpress.bean.VipInfoBean;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.circle.protocal.CircleProtocal;
import com.lchtime.safetyexpress.ui.vip.protocal.VipProtocal;
import com.lchtime.safetyexpress.utils.BitmapUtils;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.DialogUtil;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lchtime.safetyexpress.utils.UpdataImageUtils;
import com.lchtime.safetyexpress.views.CircleImageView;
import com.lidroid.xutils.view.annotation.ContentView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by android-cp on 2017/5/18.提现界面
 */
@ContentView(R.layout.vip_inputzhifubao)
public class OutPutMoneyActivity extends BaseUI implements View.OnClickListener {
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
    @BindView(R.id.civ_vip_icon)
    CircleImageView civVipIcon;
    @BindView(R.id.tv_zhifubao_num)
    TextView tvZhifubaoNum;
    @BindView(R.id.et_money_num)
    EditText etMoneyNum;
    @BindView(R.id.all_output)
    TextView allOutput;
    @BindView(R.id.tv_output)
    TextView tvOutput;
    private Handler handler = new Handler();
    private String accountNum;
    private VipProtocal protocal;

    private DialogUtil mDialog;
    private String num;
    private float mAllmoney;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        ButterKnife.bind(this);
        setTitle("提现");
        mDialog = new DialogUtil(mContext);
        accountNum = getIntent().getStringExtra("ud_zfb_account");
        num = getIntent().getStringExtra("ud_amount");
        if(num != null){
            mAllmoney = Float.valueOf(num);
        }
        //初始化头像
        initPhoto();
        //初始化支付宝账号
        initPhoneNum();
        initListener();

    }

    private void initListener() {
        allOutput.setOnClickListener(this);
        tvOutput.setOnClickListener(this);
    }

    private void initPhoneNum() {

        tvZhifubaoNum.setText(accountNum);
    }

    //头像初始化
    private void initPhoto() {
        VipInfoBean bean = SpTools.getUser(this);
        File file = new File(MyApplication.getContext().getFilesDir(), Constants.photo_name);//将要保存图片的路径
        //如果没有加载过图片了
        if (!file.exists()){
            civVipIcon.setImageDrawable(getResources().getDrawable(R.drawable.circle_user_image2));
            if (!TextUtils.isEmpty(bean.user_detail.ud_photo_fileid)){
                UpdataImageUtils.getUrlBitmap(bean.user_detail.ud_photo_fileid, new UpdataImageUtils.BitmapListener() {
                    @Override
                    public void giveBitmap(final Bitmap bitmap) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                UpdataImageUtils.saveBitmapFile(bitmap,Constants.photo_name);
                                civVipIcon.setImageBitmap(bitmap);
                            }
                        });

                    }
                });

            }

        }else {
            civVipIcon.setImageBitmap(BitmapUtils.getBitmap(file.getAbsolutePath()));
        }
    }

    @Override
    protected void prepareData() {

    }

    @Override
    public void onClick(View v) {

        String money = etMoneyNum.getText().toString().trim();
        if (v == allOutput){
            //String num = getIntent().getStringExtra("ud_amount");
            if(mAllmoney < 20.00){
                CommonUtils.toastMessage("提现最低20元");
                return;
            }
            if(mAllmoney > 5000.00){
                CommonUtils.toastMessage("提现最高5000元");
                return;
            }
            if (TextUtils.isEmpty(num)){
                CommonUtils.toastMessage("全部提现出现问题");
            }else {
                //如果的到activity传过来的值
                tiXianInternet(num);

            }
        }else if (v == tvOutput){
            if(TextUtils.isEmpty(money)){
                CommonUtils.toastMessage("请输入金额");
                return;
            }
            float m = Float.valueOf(money);
            if(m < 20.00){
                CommonUtils.toastMessage("提现最低20元");
                return;
            }
            if(mAllmoney > 5000.00){
                CommonUtils.toastMessage("提现最高5000元");
                return;
            }
            tiXianInternet(money);
        }
    }

    private String userid;
    private void tiXianInternet(String num) {
        if (userid == null){
            userid = SpTools.getUserId(this);
        }
        if (protocal == null){
            protocal = new VipProtocal();
        }
        if (TextUtils.isEmpty(userid)){
            CommonUtils.toastMessage("请登录后再提现");
            return;
        }
        mDialog.show();
        protocal.getTiXian(userid, num, accountNum, mDialog ,new CircleProtocal.NormalListener() {
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
}
