package com.lchtime.safetyexpress.ui.vip;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.AccountDetailAdapter;
import com.lchtime.safetyexpress.bean.AcountDetailBean;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.circle.protocal.CircleProtocal;
import com.lchtime.safetyexpress.ui.vip.protocal.VipProtocal;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lchtime.safetyexpress.utils.refresh.PullLoadMoreRecyclerView;
import com.lchtime.safetyexpress.views.EmptyRecyclerView;
import com.lidroid.xutils.view.annotation.ContentView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by android-cp on 2017/5/18.账户明细界面
 */
@ContentView(R.layout.account_detail)
public class AccountDetailActivity extends BaseUI {
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
    @BindView(R.id.rc_detail)
    PullLoadMoreRecyclerView rcDetail;
    @BindView(R.id.home_new_fragment_rc)
    EmptyRecyclerView home_new_fragment_rc;
    private AccountDetailAdapter adapter;
    private VipProtocal protocal;
    private List<AcountDetailBean.ListBean> detailList ;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        ButterKnife.bind(this);
        setTitle("账户明细");
        initData();
    }

    private int footPage = 0;
    private int totalPage = 0;
    private void initData() {
        home_new_fragment_rc.setLayoutManager(new LinearLayoutManager(this));
        detailList = new ArrayList<>();
        adapter = new AccountDetailAdapter(this,detailList);
        rcDetail.setAdapter(adapter);
        getDetailFromInternet("0");
        rcDetail.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        footPage = 0;
                        detailList.clear();
                        getDetailFromInternet("0");

                    }
                },2000);
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //如果是推荐或者是热点
                        footPage++;
                        if (footPage > totalPage){
                            CommonUtils.toastMessage("已经到底了！");
                            footPage--;
                            rcDetail.setPullLoadMoreCompleted();
                            return;
                        }
                        getDetailFromInternet(footPage + "");

                    }
                },2000);
            }
        });
    }

    private String userid = "";
    private void getDetailFromInternet(String page) {
        if (protocal == null){
            protocal = new VipProtocal();
        }
        userid = SpTools.getString(this, Constants.userId,"");
        if (TextUtils.isEmpty(userid)){
            CommonUtils.toastMessage("您没有登陆，请登陆后再试");
            return;
        }
        protocal.getAcountDetail(userid, page, new CircleProtocal.NormalListener() {
            @Override
            public void normalResponse(Object response) {
                AcountDetailBean bean  = (AcountDetailBean) response;
                if (bean.list != null){
                    detailList.addAll(bean.list);
                }
                adapter.notifyDataSetChanged();
                rcDetail.setPullLoadMoreCompleted();
            }
        });
    }

    @Override
    protected void prepareData() {

    }

}
