package com.lchtime.safetyexpress.ui.circle.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.AddSubscribeAdapter;
import com.lchtime.safetyexpress.bean.AddSubscribBean;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.InitInfo;
import com.lchtime.safetyexpress.ui.circle.protocal.CircleProtocal;
import com.lchtime.safetyexpress.utils.SpTools;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yxn on 2017/4/23.
 */

public class SubscirbeCommendFragment extends Fragment {
    @BindView(R.id.subscribe_comm_rc)
    RecyclerView subscribe_comm_rc;
    private Context context;
    private String userid;
    private AddSubscribeAdapter addSubscribeAdapter;
    private CircleProtocal protocal;
    public List<AddSubscribBean.ItemBean> commendList = new ArrayList<>();


    //默认为个人信息里面的行业岗位地址来筛选
    private String request_hy = InitInfo.vipInfoBean.user_detail.ud_profession;
    private String request_gw =InitInfo.vipInfoBean.user_detail.ud_post;
    private String request_addr = InitInfo.vipInfoBean.user_detail.ud_addr;
    private String request_page = "0";
    private int totalPage;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
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
        subscribe_comm_rc.setLayoutManager(new LinearLayoutManager(context));
        initData();
    }

    private void initData() {
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
        String page = request_page;
        protocal.getAddDyData(ub_id, hy, gw, addr, action, page, new CircleProtocal.NormalListener() {
            @Override
            public void normalResponse(Object response) {
                AddSubscribBean bean = (AddSubscribBean) response;
                totalPage = bean.totalpage;
                commendList.clear();
                if (bean.tj != null) {
                    commendList.addAll(bean.tj);
                }
                addSubscribeAdapter = new AddSubscribeAdapter(context,commendList);
                subscribe_comm_rc.setAdapter(addSubscribeAdapter);
            }
        });
    }
}
