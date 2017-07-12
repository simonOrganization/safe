package com.lchtime.safetyexpress.ui.circle.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.AddSubscribeAdapter;
import com.lchtime.safetyexpress.adapter.HeaderAndFooterWrapper;
import com.lchtime.safetyexpress.bean.AddSubscribBean;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.InitInfo;
import com.lchtime.safetyexpress.bean.PostBean;
import com.lchtime.safetyexpress.bean.ProfessionBean;
import com.lchtime.safetyexpress.ui.circle.protocal.CircleProtocal;
import com.lchtime.safetyexpress.ui.vip.SelectCityActivity;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lchtime.safetyexpress.utils.refresh.PullLoadMoreRecyclerView;
import com.lchtime.safetyexpress.views.CirclePopView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yxn on 2017/4/23.
 */

public class SubscirbeAllFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.subscribe_all_rc)
    PullLoadMoreRecyclerView pullLoadMoreRecyclerView;
    RecyclerView subscribe_all_rc;
    private CirclePopView cp;

    private List<ProfessionBean.ProfessionItemBean> gwList = new ArrayList<>();
    private Context context;
    private String userid;
    public String currentSelected;
    public static final int CITY_REQUEST_CODE = 0;
    private AddSubscribeAdapter addSubscribeAdapter;
    private CircleProtocal protocal;
    public List<AddSubscribBean.ItemBean> allList = new ArrayList<>();

    private String request_hy = "";
    private String request_gw = "";
    private String request_addr = "";
    private String request_page = "0";
    private int totalPage;

    private HeaderAndFooterWrapper wrapper;
    private LinearLayout subscribe_all_ll;
    private TextView tv_adddy_hy;
    private TextView tv_adddy_gw;
    private TextView tv_adddy_addr;
    private ImageView hy_indicator;
    private ImageView gw_indicator;
    private ImageView addr_indicator;
    private LinearLayout subscribe_all_work;
    private LinearLayout subscribe_all_gangwei;
    private LinearLayout subscribe_all_address;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.subscribe_all_fragment,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initListener();
        initPopwindow();
        initData();

    }

    private void initView() {
        subscribe_all_rc = (RecyclerView) pullLoadMoreRecyclerView.findViewById(R.id.home_new_fragment_rc);
        View view = View.inflate(getContext(),R.layout.subscribe_all_header,null);
        subscribe_all_ll = (LinearLayout) view.findViewById(R.id.subscribe_all_ll);
        tv_adddy_hy = (TextView) view.findViewById(R.id.tv_adddy_hy);
        tv_adddy_gw = (TextView) view.findViewById(R.id.tv_adddy_gw);
        tv_adddy_addr = (TextView) view.findViewById(R.id.tv_adddy_addr);
        hy_indicator = (ImageView) view.findViewById(R.id.iv_hy_indicator);
        gw_indicator = (ImageView) view.findViewById(R.id.iv_gw_indicator);
        addr_indicator = (ImageView) view.findViewById(R.id.iv_addr_indicator);
        subscribe_all_work = (LinearLayout) view.findViewById(R.id.subscribe_all_work);
        subscribe_all_gangwei = (LinearLayout) view.findViewById(R.id.subscribe_all_gangwei);
        subscribe_all_address = (LinearLayout) view.findViewById(R.id.subscribe_all_address);

        addSubscribeAdapter = new AddSubscribeAdapter(context,allList,SubscirbeAllFragment.this);
        wrapper = new HeaderAndFooterWrapper(addSubscribeAdapter);
        subscribe_all_rc.setAdapter(wrapper);
        wrapper.addHeaderView(view);
    }

    private Handler handler = new Handler();
    private void initListener() {
        pullLoadMoreRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                page = 1;
                refreshData("1");
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
                refreshData(page + "");
            }
        });
        subscribe_all_work.setOnClickListener(this);
        subscribe_all_gangwei.setOnClickListener(this);
        subscribe_all_address.setOnClickListener(this);


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
        String action = "1";
        String page = request_page;
        protocal.getAddDyData(ub_id,hy, gw, addr, action, page, new CircleProtocal.NormalListener() {
            @Override
            public void normalResponse(Object response) {
                if (response == null){
                    CommonUtils.toastMessage("获取数据失败，请稍后再试");
                    return;
                }
                AddSubscribBean bean = (AddSubscribBean) response;
                totalPage = bean.totalpage;
                allList.clear();
                if (bean.all != null) {
                    allList.addAll(bean.all);
                }
                wrapper.notifyDataSetChanged();
            }
        });
    }

    //行业选择
    public static final String HANG_YE = "hy";
    //岗位选择
    public static final String GANG_WEI = "gw";
    //地理位置
    public static final String ADDRESS ="addr";
    private boolean hySelected = false;
    private boolean gwSelected = false;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.subscribe_all_work:
                //改变ui
                hy_indicator.setSelected(true);
                hy_indicator.setImageDrawable(getResources().getDrawable(R.drawable.circle_indicator_selector_red));
                tv_adddy_hy.setSelected(true);
                currentSelected = HANG_YE;
                if (InitInfo.professionBean != null) {
                    cp.setDataAdapter(InitInfo.professionBean.hy);

                    cp.showPopWindow(subscribe_all_ll);
                }
                break;
            case R.id.subscribe_all_gangwei:
                //改变ui
                gw_indicator.setSelected(true);
                gw_indicator.setImageDrawable(getResources().getDrawable(R.drawable.circle_indicator_selector_red));
                tv_adddy_gw.setSelected(true);
                currentSelected = GANG_WEI;
                if (InitInfo.postBean != null) {
                    if (gwList.size() <= 0) {
                        for (PostBean.PostItemBean bean : InitInfo.postBean.gw) {
                            ProfessionBean.ProfessionItemBean itemBean = new ProfessionBean.ProfessionItemBean();
                            itemBean.isSelect = bean.isSelect;
                            itemBean.hy_name = bean.gw_name;
                            itemBean.hy_id = bean.gw_id;
                            gwList.add(itemBean);
                        }
                    }
                    cp.setDataAdapter(gwList);
                    cp.showPopWindow(subscribe_all_ll);
                }
                break;
            case R.id.subscribe_all_address:
                currentSelected = ADDRESS;
                Intent intent = new Intent(getActivity(), SelectCityActivity.class);
                startActivityForResult(intent,CITY_REQUEST_CODE);
                break;
        }

    }
//    @OnClick({R.id.subscribe_all_work,R.id.subscribe_all_gangwei,R.id.subscribe_all_address})
//    void setOnclick(View view){
//        switch (view.getId()){
//            case R.id.subscribe_all_work:
//                //改变ui
//                hy_indicator.setSelected(true);
//                hy_indicator.setImageDrawable(getResources().getDrawable(R.drawable.circle_indicator_selector_red));
//                tv_adddy_hy.setSelected(true);
//                currentSelected = HANG_YE;
//                cp.setDataAdapter(InitInfo.professionBean.hy);
//                cp.showPopWindow(subscribe_all_ll);
//            break;
//            case R.id.subscribe_all_gangwei:
//                //改变ui
//                gw_indicator.setSelected(true);
//                gw_indicator.setImageDrawable(getResources().getDrawable(R.drawable.circle_indicator_selector_red));
//                tv_adddy_gw.setSelected(true);
//                currentSelected = GANG_WEI;
//                if (gwList.size() <= 0) {
//                    for (PostBean.PostItemBean bean : InitInfo.postBean.gw) {
//                        ProfessionBean.ProfessionItemBean itemBean = new ProfessionBean.ProfessionItemBean();
//                        itemBean.isSelect = bean.isSelect;
//                        itemBean.hy_name = bean.gw_name;
//                        itemBean.hy_id = bean.gw_id;
//                        gwList.add(itemBean);
//                    }
//                }
//                cp.setDataAdapter(gwList);
//                cp.showPopWindow(subscribe_all_ll);
//                break;
//            case R.id.subscribe_all_address:
//                currentSelected = ADDRESS;
//                Intent intent = new Intent(getActivity(), SelectCityActivity.class);
//                startActivityForResult(intent,CITY_REQUEST_CODE);
//                break;
//        }
//
//    }
    private void initPopwindow() {
        subscribe_all_rc.setLayoutManager(new LinearLayoutManager(context));
        cp = new CirclePopView(getActivity());
        cp.setCirclePopInterface(new CirclePopView.CirclePopInterface() {
            @Override
            public void getPopIds(String positions) {
                //筛选条件
                //Toast.makeText(context,"position:===="+positions,Toast.LENGTH_SHORT).show();
                updataPop(positions);
            }
        });

        cp.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                hy_indicator.setSelected(false);
                gw_indicator.setSelected(false);
                addr_indicator.setSelected(false);
                if (HANG_YE.equals(currentSelected)&&hySelected == false){
                    tv_adddy_hy.setSelected(false);
                    hy_indicator.setImageDrawable(getResources().getDrawable(R.drawable.circle_indicator_selector));
                }else if(GANG_WEI.equals(currentSelected)&&gwSelected == false){
                    tv_adddy_gw.setSelected(false);
                    gw_indicator.setImageDrawable(getResources().getDrawable(R.drawable.circle_indicator_selector));
                }
            }
        });
    }

    //更新popubwindow标题的ui
    private void updataPop(String positions) {
        if (TextUtils.isEmpty(positions)){
            if (HANG_YE.equals(currentSelected)){
                tv_adddy_hy.setText("行业选择");
                tv_adddy_hy.setSelected(false);
                hy_indicator.setImageDrawable(getResources().getDrawable(R.drawable.circle_indicator_selector));
                hySelected = false;
                request_hy = "";
            }else if (GANG_WEI.equals(currentSelected)){
                tv_adddy_gw.setText("岗位选择");
                tv_adddy_gw.setSelected(false);
                gw_indicator.setImageDrawable(getResources().getDrawable(R.drawable.circle_indicator_selector));
                gwSelected = false;
                request_gw = "";
            }
        }else {
            String[] arr = positions.split(",");
            String title = "";
            if (HANG_YE.equals(currentSelected)) {
                tv_adddy_hy.setSelected(true);
                hy_indicator.setImageDrawable(getResources().getDrawable(R.drawable.circle_indicator_selector_red));
                hySelected = true;
                request_hy = "";
                for (int i = 0; i < arr.length; i++) {
                    if (i == 0) {
                        title = title + InitInfo.professionBean.hy.get(Integer.parseInt(arr[i])).hy_name;
                        request_hy = request_hy + InitInfo.professionBean.hy.get(Integer.parseInt(arr[i])).hy_name;
                    } else {
                        title = title + "," + InitInfo.professionBean.hy.get(Integer.parseInt(arr[i])).hy_name;
                        request_hy = request_hy + "-" + InitInfo.professionBean.hy.get(Integer.parseInt(arr[i])).hy_name;
                    }
                }
                tv_adddy_hy.setText(title);
            } else if (GANG_WEI.equals(currentSelected)) {
                request_gw = "";
                tv_adddy_gw.setSelected(true);
                gw_indicator.setImageDrawable(getResources().getDrawable(R.drawable.circle_indicator_selector_red));
                gwSelected = true;
                for (int i = 0; i < arr.length; i++) {
                    if (i == 0) {
                        title = title + gwList.get(Integer.parseInt(arr[i])).hy_name;
                        request_gw = request_gw + gwList.get(Integer.parseInt(arr[i])).hy_name;
                    } else {
                        title = title + "," + gwList.get(Integer.parseInt(arr[i])).hy_name;
                        request_gw = request_gw + "-" + gwList.get(Integer.parseInt(arr[i])).hy_name;
                    }
                }
                tv_adddy_gw.setText(title);
            }
        }
        refreshData("1");

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CITY_REQUEST_CODE){
            if (data != null) {
                String selectCity = data.getStringExtra("city");
                request_addr = selectCity;
                refreshData("1");
                tv_adddy_addr.setText(selectCity);
            }
        }
    }
    private int page= 1;
    private boolean isLoadMore = false;
    public void refreshData(String page) {
        String ub_id = userid;
        String hy = request_hy;
        String gw = request_gw;
        String addr = request_addr;
        //0为推荐1为全部
        String action = "1";
        protocal.getAddDyData(ub_id, hy, gw, addr, action, page, new CircleProtocal.NormalListener() {
            @Override
            public void normalResponse(Object response) {
                if (response == null){
                    CommonUtils.toastMessage("请检查网络");
                    pullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                    return;
                }
                AddSubscribBean bean = (AddSubscribBean) response;
                totalPage = bean.totalpage;
                if (!isLoadMore) {
                    allList.clear();
                }
                if(bean.all != null) {
                    allList.addAll(bean.all);
                }
                wrapper.notifyDataSetChanged();
                pullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                isLoadMore = false;
            }
        });
    }


}
