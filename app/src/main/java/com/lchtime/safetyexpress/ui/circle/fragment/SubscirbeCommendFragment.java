package com.lchtime.safetyexpress.ui.circle.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.AddSubscribeAdapter;
import com.lchtime.safetyexpress.bean.AddSubscribBean;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.InitInfo;
import com.lchtime.safetyexpress.ui.circle.protocal.CircleProtocal;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lchtime.safetyexpress.utils.refresh.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yxn on 2017/4/23.
 */

public class SubscirbeCommendFragment extends Fragment {
    @BindView(R.id.subscribe_comm_rc)
    PullLoadMoreRecyclerView pullLoadMoreRecyclerView;
    RecyclerView subscribe_comm_rc;
    private Context context;
    private String userid;
    private AddSubscribeAdapter addSubscribeAdapter;
    private CircleProtocal protocal;
    public List<AddSubscribBean.ItemBean> commendList = new ArrayList<>();


    //默认为个人信息里面的行业岗位地址来筛选
    private String request_hy ;
    private String request_gw ;
    private String request_addr ;
    private String request_page = "0";
    private int totalPage = 1;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        initInfo();
    }

    private void initInfo() {
        if (InitInfo.vipInfoBean != null ) {
            if (InitInfo.vipInfoBean.user_detail != null) {
                request_hy = InitInfo.vipInfoBean.user_detail.ud_profession;
                request_gw =InitInfo.vipInfoBean.user_detail.ud_post;
                request_addr = InitInfo.vipInfoBean.user_detail.ud_addr;
            }
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.subscribe_comm_fragment,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        subscribe_comm_rc = (RecyclerView) pullLoadMoreRecyclerView.findViewById(R.id.home_new_fragment_rc);
        subscribe_comm_rc.setLayoutManager(new LinearLayoutManager(context));
        initData("1");
        initListener();
    }

    private Handler handler = new Handler();
    private void initListener() {
        pullLoadMoreRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                page = 1;
                initData("1");
            }

            @Override
            public void onLoadMore() {
                page++;
                if (page > totalPage){
                    CommonUtils.toastMessage("没有更多了");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                        }
                    },300);
                    return;
                }
                isLoadMore = true;
                initData(page+"");
            }
        });
    }
    private boolean isLoadMore = false;
    private int page = 1;
    public void initData(String page) {
        userid = SpTools.getString(getContext(), Constants.userId,"");
        if (protocal == null){
            protocal = new CircleProtocal();
        }
        String ub_id = userid;
        String hy = request_hy;
        String gw = request_gw;
        String addr = request_addr;
        //0为推荐1为全部
        String action = "0";

        if ((!TextUtils.isEmpty(userid))&&(!TextUtils.isEmpty(hy))&&(!TextUtils.isEmpty(gw))&&(!TextUtils.isEmpty(addr))) {
            protocal.getAddDyData(ub_id, hy, gw, addr, action, page, new CircleProtocal.NormalListener() {
                @Override
                public void normalResponse(Object response) {
                    if (response == null) {
                        CommonUtils.toastMessage("加载数据失败");
                        pullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                        return;
                    }
                    AddSubscribBean bean = (AddSubscribBean) response;
                    totalPage = bean.totalpage;
                    if (!isLoadMore) {
                        commendList.clear();
                    }
                    if (bean.tj != null) {
                        commendList.addAll(bean.tj);
                    }
                    if (addSubscribeAdapter == null) {

                        addSubscribeAdapter = new AddSubscribeAdapter(context, commendList, SubscirbeCommendFragment.this);
                        subscribe_comm_rc.setAdapter(addSubscribeAdapter);
                    }
                    addSubscribeAdapter.notifyDataSetChanged();
                    isLoadMore = false;
                    pullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                }
            });
        }
    }
}
