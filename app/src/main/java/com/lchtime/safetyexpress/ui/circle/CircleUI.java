package com.lchtime.safetyexpress.ui.circle;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.CircleAdapter;
import com.lchtime.safetyexpress.bean.CircleSelectBean;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.HomeBannerBean;
import com.lchtime.safetyexpress.bean.InitInfo;
import com.lchtime.safetyexpress.bean.PostBean;
import com.lchtime.safetyexpress.bean.ProfessionBean;
import com.lchtime.safetyexpress.bean.QzContextBean;
import com.lchtime.safetyexpress.bean.res.CircleBean;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.circle.protocal.CircleProtocal;
import com.lchtime.safetyexpress.ui.home.HomeNewsSearchUI;
import com.lchtime.safetyexpress.ui.vip.SelectCityActivity;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lchtime.safetyexpress.views.CirclePopView;
import com.lchtime.safetyexpress.views.EmptyRecyclerView;
import com.lchtime.safetyexpress.views.SpinerPopWindow;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.mzhy.http.okhttp.OkHttpUtils;
import com.mzhy.http.okhttp.callback.StringCallback;
import com.sivin.Banner;
import com.sivin.BannerAdapter;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**圈子
 * Created by user on 2017/4/14.
 */
@ContentView(R.layout.circle)
public class CircleUI extends BaseUI {
    //banner图
    @ViewInject(R.id.sb_home_banner)
    private Banner sb_home_banner;
    @ViewInject(R.id.circle_rc)
    private EmptyRecyclerView circle_rc;
    @ViewInject(R.id.circle_work)
    private LinearLayout circle_work;
    @ViewInject(R.id.circle_gangwei)
    private LinearLayout circle_gangwei;
    @ViewInject(R.id.circle_address)
    private LinearLayout circle_address;
    @ViewInject(R.id.circle_layout_view)
    private View circle_layout_view;
    @ViewInject(R.id.circle_pop_view)
    View circle_pop_view;
    //行业
    @ViewInject(R.id.tv_hy_selected)
    TextView tv_hy_selected;
    //岗位
    @ViewInject(R.id.tv_gw_selected)
    TextView tv_gw_selected;
    //地区
    @ViewInject(R.id.tv_addr_selected)
    TextView tv_addr_selected;
    @ViewInject(R.id.iv_hy_indicator)
    ImageView hy_indicator;
    @ViewInject(R.id.iv_gw_indicator)
    ImageView gw_indicator;
    @ViewInject(R.id.iv_addr_indicator)
    ImageView addr_indicator;

    public static final int CITY_REQUEST_CODE = 0;

    private CirclePopView cp;
    private CircleAdapter rcAdapter;
    private List<HomeBannerBean> mDatas = new ArrayList<>();
    private List<QzContextBean> circleList = new ArrayList<>();
    private List<ProfessionBean.ProfessionItemBean> gwList = new ArrayList<>();
    private List<ProfessionBean.ProfessionItemBean> selected = new ArrayList<>();
    private SpinerPopWindow spinerPopWindow;

    //当前的选项
    public String currentSelected;
    public String request_hy = "";
    public String request_gw = "";
    public String request_addr = "";
    public String request_order = "0";

    private CircleProtocal protocal;
    private ArrayList<String> moreData;
    private String userid;

    @Override
    protected void back() {
        exit();
    }

    @Override
    protected void setControlBasis() {
        userid = SpTools.getString(this,Constants.userId,"");

        //轮播图
        BannerAdapter adapter = new BannerAdapter<HomeBannerBean>(mDatas) {
            @Override
            protected void bindTips(TextView tv, HomeBannerBean homeBannerBean) {
                tv.setText("");
            }

            @Override
            public void bindImage(ImageView imageView, HomeBannerBean homeBannerBean) {
                Glide.with(CircleUI.this)
                        .load(homeBannerBean.getImgurl())
                        .placeholder(R.drawable.home_banner)
                        .error(R.drawable.home_banner)
                        .into(imageView);
            }
        };
        sb_home_banner.setBannerAdapter(adapter);
        sb_home_banner.setOnBannerItemClickListener(new Banner.OnBannerItemClickListener() {
            @Override
            public void onItemClick(int position) {
                makeText("广告" + position);
            }
        });
        cp = new CirclePopView(CircleUI.this);
        initData();
        cp.setCirclePopInterface(new CirclePopView.CirclePopInterface() {
            @Override
            public void getPopIds(String positions) {
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
                    tv_hy_selected.setSelected(false);
                    hy_indicator.setImageDrawable(getResources().getDrawable(R.drawable.circle_indicator_selector));
                }else if(GANG_WEI.equals(currentSelected)&&gwSelected == false){
                    tv_gw_selected.setSelected(false);
                    gw_indicator.setImageDrawable(getResources().getDrawable(R.drawable.circle_indicator_selector));
                }
            }
        });
    }



    private void initData(){

        if (protocal == null) {
            protocal = new CircleProtocal();
        }

        userid = SpTools.getString(this, Constants.userId,"");
        protocal.getCircleList(userid, "0", "4", "0", new CircleProtocal.CircleListener() {
            @Override
            public void circleResponse(CircleBean response) {
                circle_rc.setLayoutManager(new GridLayoutManager(CircleUI.this, 1) {
                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }
                });
                circleList.clear();
                circleList.addAll(response.qz_context);
                rcAdapter = new CircleAdapter(CircleUI.this,circleList);
                circle_rc.setAdapter(rcAdapter);
            }
        });

    }
    //订阅
    @OnClick(R.id.circle_subscribe_but)
    private void getSubmit(View view){
        userid = SpTools.getString(this,Constants.userId,"");
        if (TextUtils.isEmpty(userid)){
            CommonUtils.toastMessage("没有登陆");
            return;
        }
        startActivity(new Intent(CircleUI.this,SubscribActivity.class));

    }


    //行业选择
    public static final String HANG_YE = "hy";
    private boolean hySelected = false;
    @OnClick(R.id.circle_work)
    private void getWork(View view){
        //改变ui
        hy_indicator.setSelected(true);
        hy_indicator.setImageDrawable(getResources().getDrawable(R.drawable.circle_indicator_selector_red));
        tv_hy_selected.setSelected(true);
        //更新数据
        currentSelected = HANG_YE;
        cp.setDataAdapter(InitInfo.professionBean.hy);
        //显示在circle_work的下方
        cp.showPopWindow(circle_work);


    }
    //岗位选择
    public static final String GANG_WEI = "gw";
    private boolean gwSelected = false;
    @OnClick(R.id.circle_gangwei)
    private void getGangwei(View view){
        //改变ui
        gw_indicator.setSelected(true);
        gw_indicator.setImageDrawable(getResources().getDrawable(R.drawable.circle_indicator_selector_red));
        tv_gw_selected.setSelected(true);
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
        cp.showPopWindow(circle_work);

    }
    //地理位置
    public static final String ADDRESS ="addr";
    @OnClick(R.id.circle_address)
    private void getAddress(View view){
        currentSelected = ADDRESS;
        Intent intent = new Intent(this, SelectCityActivity.class);
        startActivityForResult(intent,CITY_REQUEST_CODE);

    }
    //按照什么来排序
    @OnClick(R.id.circle_more)
    private void getMord(View view){
        if (moreData == null){
            moreData = new ArrayList<String>();
            moreData.add("按最新排序");
            moreData.add("按热门排序");
            moreData.add("按订阅量排序");
        }
        spinerPopWindow = new SpinerPopWindow(CircleUI.this,moreData);
        spinerPopWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        spinerPopWindow.showAsDropDown(circle_layout_view);
        spinerPopWindow.setSpinerInterface(new SpinerPopWindow.SpinerInterface() {
            @Override
            public void setSpinerInterface(int position) {
                spinerPopWindow.dismiss();
                request_order = position + "";
                //请求筛选过的数据
                refreshData();

            }
        });
    }

    @Override
    protected void prepareData() {
        getAdvData();
    }
    /**
     * 获取广告
     */
    private void getAdvData() {
        //测试
//        showProgressDialog();
        OkHttpUtils.post().url("http://www.baidu.com")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
//                dismissProgressDialog();
                setTextData();
            }

            @Override
            public void onResponse(String s, int i) {
                setTextData();
//                dismissProgressDialog();
            }
        });
    }

    private void setTextData() {
        mDatas.clear();
        HomeBannerBean homeBannerBean = null;
        homeBannerBean = new HomeBannerBean();
        homeBannerBean.setImgurl("");
        mDatas.add(homeBannerBean);
        homeBannerBean = new HomeBannerBean();
        homeBannerBean.setImgurl("");
        mDatas.add(homeBannerBean);
        homeBannerBean = new HomeBannerBean();
        homeBannerBean.setImgurl("");
        mDatas.add(homeBannerBean);
        homeBannerBean = new HomeBannerBean();
        homeBannerBean.setImgurl("");
        mDatas.add(homeBannerBean);
        sb_home_banner.notifiDataHasChanged();
    }
    /**
     * 搜索
     * @param view
     */
    @OnClick(R.id.ll_home_search)
    private void getSearch(View view){
        Intent intent = new Intent(CircleUI.this, HomeNewsSearchUI.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CITY_REQUEST_CODE){
            if (data != null) {
                String selectCity = data.getStringExtra("city");
                request_addr = selectCity;
                refreshData();
                tv_addr_selected.setText(selectCity);
            }
        }
    }

    //更新popubwindow标题的ui
    private void updataPop(String positions) {
        if (TextUtils.isEmpty(positions)){
            if (HANG_YE.equals(currentSelected)){
                tv_hy_selected.setText("行业选择");
                tv_hy_selected.setSelected(false);
                hy_indicator.setImageDrawable(getResources().getDrawable(R.drawable.circle_indicator_selector));
                hySelected = false;
                request_hy = "";
            }else if (GANG_WEI.equals(currentSelected)){
                tv_gw_selected.setText("岗位选择");
                tv_gw_selected.setSelected(false);
                gw_indicator.setImageDrawable(getResources().getDrawable(R.drawable.circle_indicator_selector));
                gwSelected = false;
                request_gw = "";
            }
        }else {
            String[] arr = positions.split(",");
            String title = "";
            if (HANG_YE.equals(currentSelected)) {
                tv_hy_selected.setSelected(true);
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
                tv_hy_selected.setText(title);
            } else if (GANG_WEI.equals(currentSelected)) {
                tv_gw_selected.setSelected(true);
                gw_indicator.setImageDrawable(getResources().getDrawable(R.drawable.circle_indicator_selector_red));
                gwSelected = true;
                request_gw = "";
                for (int i = 0; i < arr.length; i++) {
                    if (i == 0) {
                        title = title + gwList.get(Integer.parseInt(arr[i])).hy_name;
                        request_gw = request_gw + gwList.get(Integer.parseInt(arr[i])).hy_name;
                    } else {
                        title = title + "," + gwList.get(Integer.parseInt(arr[i])).hy_name;
                        request_gw = request_gw + "-" + gwList.get(Integer.parseInt(arr[i])).hy_name;
                    }
                }
                tv_gw_selected.setText(title);
            }
        }
        refreshData();

    }

    public void refreshData() {
        //请求筛选过的数据
        String ub_id = userid;
        String page = "0";
        //全部圈子
        String type = "4";
        String ud_profession = request_hy;
        String ud_post = request_gw;
        String ud_addr = request_addr;
        //排序类型
        String order = request_order;
        protocal.getCircleSelectedList(ub_id, page, type, ud_profession, ud_post, ud_addr, order, new CircleProtocal.CircleListener() {
            @Override
            public void circleResponse(CircleBean response) {
                circleList.clear();
                if(response.qz_context != null) {
                    circleList.addAll(response.qz_context);
                }
                rcAdapter.notifyDataSetChanged();
            }
        });
    }
}
