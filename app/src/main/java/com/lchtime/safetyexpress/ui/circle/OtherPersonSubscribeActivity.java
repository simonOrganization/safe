package com.lchtime.safetyexpress.ui.circle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.AddSubscribeAdapter;
import com.lchtime.safetyexpress.bean.AddSubscribBean;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.circle.protocal.CircleProtocal;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lidroid.xutils.view.annotation.ContentView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by android-cp on 2017/5/18. 个人主页中 点击订阅进入的  他的订阅列表界面
 */

@ContentView(R.layout.other_subscribe_layout)
public class OtherPersonSubscribeActivity extends BaseUI {
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
    @BindView(R.id.rc_other_person_subscribe)
    RecyclerView rcOtherPersonSubscribe;
    @BindView(R.id.pb_progress)
    ProgressBar pb_progress;
    private String uid = "";
    private CircleProtocal protocal ;
    private AddSubscribeAdapter addSubscribeAdapter;
    public List<AddSubscribBean.ItemBean> allList = new ArrayList<>();
    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        ButterKnife.bind(this);
        uid = getIntent().getStringExtra("uid");
        if (TextUtils.isEmpty(uid)){
            uid = "";
        }
        setTitle("他的订阅");

        rcOtherPersonSubscribe.setLayoutManager(new LinearLayoutManager(this));
    }

    private String userid;
    @Override
    public void prepareData() {
        setIsLoading(true);
        if (protocal == null){
            protocal = new CircleProtocal();
        }
        if (userid == null) {
            userid = SpTools.getUserId(this);
        }
        protocal.getOtherSubscribeList(userid, uid, new CircleProtocal.NormalListener() {
            @Override
            public void normalResponse(Object response) {
                if (response == null){
                    setIsLoading(false);
                    CommonUtils.toastMessage("加载数据失败，请重新加载");
                    return;
                }
                AddSubscribBean bean = (AddSubscribBean) response;
                allList.clear();
                if (bean.dy != null) {
                    allList.addAll(bean.dy);
                }
                addSubscribeAdapter = new AddSubscribeAdapter(OtherPersonSubscribeActivity.this,allList,OtherPersonSubscribeActivity.this);
                rcOtherPersonSubscribe.setAdapter(addSubscribeAdapter);
                setIsLoading(false);
            }
        });
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
