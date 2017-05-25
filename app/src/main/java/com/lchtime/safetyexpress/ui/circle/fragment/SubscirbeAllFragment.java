package com.lchtime.safetyexpress.ui.circle.fragment;

import android.content.Context;
import android.content.Intent;
import android.media.DrmInitData;
import android.os.Bundle;
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
import android.widget.Toast;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.AddSubscribeAdapter;
import com.lchtime.safetyexpress.bean.AddSubscribBean;
import com.lchtime.safetyexpress.bean.CircleSelectBean;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.InitInfo;
import com.lchtime.safetyexpress.bean.PostBean;
import com.lchtime.safetyexpress.bean.ProfessionBean;
import com.lchtime.safetyexpress.bean.res.CircleBean;
import com.lchtime.safetyexpress.ui.circle.CircleUI;
import com.lchtime.safetyexpress.ui.circle.protocal.CircleProtocal;
import com.lchtime.safetyexpress.ui.vip.SelectCityActivity;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lchtime.safetyexpress.views.CirclePopView;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yxn on 2017/4/23.
 */

public class SubscirbeAllFragment extends Fragment {
    @BindView(R.id.subscribe_all_rc)
    RecyclerView subscribe_all_rc;
    private CirclePopView cp;
    @BindView(R.id.subscribe_all_ll)
    LinearLayout subscribe_all_ll;
    @BindView(R.id.tv_adddy_hy)
    TextView tv_adddy_hy;
    @BindView(R.id.tv_adddy_gw)
    TextView tv_adddy_gw;
    @BindView(R.id.tv_adddy_addr)
    TextView tv_adddy_addr;

    @BindView(R.id.iv_hy_indicator)
    ImageView hy_indicator;
    @BindView(R.id.iv_gw_indicator)
    ImageView gw_indicator;
    @BindView(R.id.iv_addr_indicator)
    ImageView addr_indicator;
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
        initPopwindow();
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
        String action = "1";
        String page = request_page;
        protocal.getAddDyData(ub_id,hy, gw, addr, action, page, new CircleProtocal.NormalListener() {
            @Override
            public void normalResponse(Object response) {
                AddSubscribBean bean = (AddSubscribBean) response;
                totalPage = bean.totalpage;
                allList.clear();
                if (bean.all != null) {
                    allList.addAll(bean.all);
                }
                addSubscribeAdapter = new AddSubscribeAdapter(context,allList,SubscirbeAllFragment.this);
                subscribe_all_rc.setAdapter(addSubscribeAdapter);
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
    @OnClick({R.id.subscribe_all_work,R.id.subscribe_all_gangwei,R.id.subscribe_all_address})
    void setOnclick(View view){
        switch (view.getId()){
            case R.id.subscribe_all_work:
                //改变ui
                hy_indicator.setSelected(true);
                hy_indicator.setImageDrawable(getResources().getDrawable(R.drawable.circle_indicator_selector_red));
                tv_adddy_hy.setSelected(true);
                currentSelected = HANG_YE;
                cp.setDataAdapter(InitInfo.professionBean.hy);
                cp.showPopWindow(subscribe_all_ll);
            break;
            case R.id.subscribe_all_gangwei:
                //改变ui
                gw_indicator.setSelected(true);
                gw_indicator.setImageDrawable(getResources().getDrawable(R.drawable.circle_indicator_selector_red));
                tv_adddy_gw.setSelected(true);
                currentSelected = GANG_WEI;
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
                break;
            case R.id.subscribe_all_address:
                currentSelected = ADDRESS;
                Intent intent = new Intent(getActivity(), SelectCityActivity.class);
                startActivityForResult(intent,CITY_REQUEST_CODE);
                break;
        }

    }
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
        refreshData();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CITY_REQUEST_CODE){
            if (data != null) {
                String selectCity = data.getStringExtra("city");
                request_addr = selectCity;
                refreshData();
                tv_adddy_addr.setText(selectCity);
            }
        }
    }

    public void refreshData() {
        String ub_id = userid;
        String hy = request_hy;
        String gw = request_gw;
        String addr = request_addr;
        //0为推荐1为全部
        String action = "1";
        String page = request_page;
        protocal.getAddDyData(ub_id, hy, gw, addr, action, page, new CircleProtocal.NormalListener() {
            @Override
            public void normalResponse(Object response) {
                AddSubscribBean bean = (AddSubscribBean) response;
                allList.clear();
                if(bean.all != null) {
                    allList.addAll(bean.all);
                }
                addSubscribeAdapter.notifyDataSetChanged();
            }
        });
    }
}
