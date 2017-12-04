package com.lchtime.safetyexpress.ui.vip;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.MyCircleActiveAdapter;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.MyCircleActiveBean;
import com.lchtime.safetyexpress.bean.QzContextBean;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.circle.protocal.CircleProtocal;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lidroid.xutils.view.annotation.ContentView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.lchtime.safetyexpress.ui.circle.CircleUI.CITY_DETAIL_CODE;


/**
 * Created by android-cp on 2017/5/17.我的圈子界面
 */
@ContentView(R.layout.activity_mycircle_active)
public class MyCircleActiveActivity extends BaseUI {
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
    @BindView(R.id.rc_mycircle_active)
    RecyclerView rcMycircleActive;
    @BindView(R.id.pb_progress)
    ProgressBar pb_progress;
    @BindView(R.id.empty)
    View mEmptyView; //错误界面

    private CircleProtocal protocal;
    private String userid = "";
    private List<QzContextBean> myCircleList = new ArrayList<>();
    private MyCircleActiveAdapter adapter;
    private MyCircleActiveAdapter.OnDeleteListener mListener;
    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        ButterKnife.bind(this);
        setTitle("我的圈子");
        findViewById(R.id.tv_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }

    @Override
    protected void prepareData() {
        setIsLoading(true);
        initData();
    }

    private void initData() {
        if (protocal == null) {
            protocal = new CircleProtocal();
        }
        userid = SpTools.getUserId(this);
        mListener = new MyCircleActiveAdapter.OnDeleteListener() {
            @Override
            public void onDeleteItem() {
                if (myCircleList.size() == 0){
                    mEmptyView.setVisibility(View.VISIBLE);
                }else{
                    mEmptyView.setVisibility(View.GONE);
                }
            }
        };
        protocal.getMyCircleList(userid,"", new CircleProtocal.NormalListener() {
            @Override
            public void normalResponse(Object response) {
                if (response == null){
                    CommonUtils.toastMessage("加载我的圈子失败，请稍后再试！");
                    setIsLoading(false);
                    mEmptyView.setVisibility(View.VISIBLE);
                    return;
                }

                rcMycircleActive.setLayoutManager(new LinearLayoutManager(MyCircleActiveActivity.this));
                //我的圈子列表
                MyCircleActiveBean bean = (MyCircleActiveBean) response;
                myCircleList.clear();
                if (bean.quanzi!= null) {
                    myCircleList.addAll(bean.quanzi);
                }
                if (adapter ==null) {
                    adapter = new MyCircleActiveAdapter(MyCircleActiveActivity.this , myCircleList , mListener);
                    rcMycircleActive.setAdapter(adapter);
                }else {
                    adapter.notifyDataSetChanged();
                }
                if (myCircleList.size() == 0){
                    mEmptyView.setVisibility(View.VISIBLE);
                }else{
                    mEmptyView.setVisibility(View.GONE);
                }
                setIsLoading(false);
            }
        });
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
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
    public void refreshItemData(final String qc_id){
        initData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CITY_DETAIL_CODE && resultCode == RESULT_OK){
            initData();
        }
    }
}
