package com.lchtime.safetyexpress.ui.circle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.lchtime.safetyexpress.H5DetailUI;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.CircleAdapter;
import com.lchtime.safetyexpress.adapter.CircleHeaderAndFooterWrapper;
import com.lchtime.safetyexpress.bean.CircleItemUpBean;
import com.lchtime.safetyexpress.bean.CircleRedPointBean;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.FirstPic;
import com.lchtime.safetyexpress.bean.HomeBannerBean;
import com.lchtime.safetyexpress.bean.InitInfo;
import com.lchtime.safetyexpress.bean.PostBean;
import com.lchtime.safetyexpress.bean.ProfessionBean;
import com.lchtime.safetyexpress.bean.QzContextBean;
import com.lchtime.safetyexpress.bean.res.CircleBean;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.TabUI;
import com.lchtime.safetyexpress.ui.circle.protocal.CircleProtocal;
import com.lchtime.safetyexpress.ui.home.GetMoneyActivity;
import com.lchtime.safetyexpress.ui.home.protocal.PictureAdvantage;
import com.lchtime.safetyexpress.ui.search.HomeNewsSearchUI;
import com.lchtime.safetyexpress.ui.vip.SelectCityActivity;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lchtime.safetyexpress.utils.cacheutils.ACache;
import com.lchtime.safetyexpress.utils.refresh.PullLoadMoreRecyclerView;
import com.lchtime.safetyexpress.views.CirclePopView;
import com.lchtime.safetyexpress.views.SpinerPopWindow;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.sivin.Banner;
import com.sivin.BannerAdapter;

import java.util.ArrayList;
import java.util.List;

/**圈子
 * Created by user on 2017/4/14.
 */
@ContentView(R.layout.circle)
public class CircleUI extends BaseUI implements View.OnClickListener {
    //banner图
    @ViewInject(R.id.circle_rc)
    private PullLoadMoreRecyclerView pullLoadMoreRecyclerView;
    private RecyclerView circle_rc;
    private Banner sb_home_banner;


    private LinearLayout circle_work;
    private LinearLayout circle_gangwei;
    private LinearLayout circle_address;
    private ImageView circle_more;
//    private View circle_layout_view;
    //行业
    TextView tv_hy_selected;
    //岗位
    TextView tv_gw_selected;
    //地区
    TextView tv_addr_selected;
    ImageView hy_indicator;
    ImageView gw_indicator;
    ImageView addr_indicator;
    TextView unread_msg_number;


    private LinearLayout circle_work1;
    private LinearLayout circle_gangwei1;
    private LinearLayout circle_address1;
    private ImageView circle_more1;
    //    private View circle_layout_view;
    //行业
    TextView tv_hy_selected1;
    //岗位
    TextView tv_gw_selected1;
    //地区
    TextView tv_addr_selected1;
    ImageView hy_indicator1;
    ImageView gw_indicator1;
    ImageView addr_indicator1;
    LinearLayout layout_circle_header;

    private ACache aCache;
    public final String circle_adv = "CIRCLE_ADV";
    public final String circle_list = "CIRCLE_LIST";

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
    private View headerView;
    private CircleHeaderAndFooterWrapper wapperAdapter;
    private View headerView2;
    private ArrayList<String> Data;
    private static final int UPDATE_TEXT = 1;
    private UiReceiver mReceiver;


    @Override
    protected void back() {
        exit();
    }

    private boolean isOnCreate = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRedPoint(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (InitInfo.circleRefresh){
            refreshData("1");
            InitInfo.circleRefresh = false;
        }

        getNotice();
    }
    private class UiReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if ("ACTION_PUSH_SUCEESS".equals(intent.getAction())) {
               final String code = intent.getStringExtra("code" );
                if (!TextUtils.isEmpty(code)) {
                    initData();
                }
            }
        }
    }

    private void getNotice() {
        if (TextUtils.isEmpty(userid)){
            userid = SpTools.getUserId(this);
        }
        protocal.getDyIsShowRedPoint(userid, new CircleProtocal.NormalListener() {
            @Override
            public void normalResponse(Object response) {
                if (response == null){
                    CommonUtils.toastMessage("请求网络数据失败，请刷新重试");
                    return;
                }

                CircleRedPointBean bean = (CircleRedPointBean) response;
                if ("0".equals(bean.newqz)){
                    setRedPoint(false);
                }else {
                    setRedPoint(true);
                }
            }
        });
    }

    @Override
    protected void setControlBasis() {
        initView();
        initListener();
        userid = SpTools.getUserId(this);

        //轮播图
        BannerAdapter adapter = new BannerAdapter<FirstPic.LunboBean>(lunbo) {
            @Override
            protected void bindTips(TextView tv, FirstPic.LunboBean homeBannerBean) {
                //tv.setText(homeBannerBean.url);
            }

            @Override
            public void bindImage(ImageView imageView, FirstPic.LunboBean homeBannerBean) {
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                Glide.with(CircleUI.this)
                        .load(homeBannerBean.img)
                        .placeholder(R.drawable.banner_default)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .error(R.drawable.banner_default)
                        .into(imageView);
            }
        };
        sb_home_banner.setBannerAdapter(adapter);
        sb_home_banner.setOnBannerItemClickListener(new Banner.OnBannerItemClickListener() {
            @Override
            public void onItemClick(int position) {
//                makeText("广告" + position);
                if (position == 0){
                    Intent intent = new Intent(CircleUI.this,GetMoneyActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(CircleUI.this,H5DetailUI.class);
                    intent.putExtra("type","url");
                    intent.putExtra("url",lunbo.get(position).url + "?timestamp=" + System.currentTimeMillis());
                    startActivity(intent);
                }
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


                hy_indicator1.setSelected(false);
                gw_indicator1.setSelected(false);
                addr_indicator1.setSelected(false);
                if (HANG_YE.equals(currentSelected)&&hySelected == false){
                    tv_hy_selected1.setSelected(false);
                    hy_indicator1.setImageDrawable(getResources().getDrawable(R.drawable.circle_indicator_selector));
                }else if(GANG_WEI.equals(currentSelected)&&gwSelected == false){
                    tv_gw_selected1.setSelected(false);
                    gw_indicator1.setImageDrawable(getResources().getDrawable(R.drawable.circle_indicator_selector));
                }
            }
        });
    }
    private Handler handler = new Handler();

    private void initListener() {
        pullLoadMoreRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                page = 1;
                refreshData("1");
                getNotice();
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
                    },100);
                    return;
                }
                isLoadMore = true;
                refreshData(page + "");
            }
        });
    }

    private void initView() {
        unread_msg_number = (TextView) findViewById(R.id.unread_msg_number);
        circle_rc = (RecyclerView) pullLoadMoreRecyclerView.findViewById(R.id.home_new_fragment_rc);

        headerView = View.inflate(this, R.layout.circle_header,null);

        circle_work = (LinearLayout) headerView.findViewById(R.id.circle_work);
        circle_gangwei = (LinearLayout) headerView.findViewById(R.id.circle_gangwei);
        circle_address = (LinearLayout) headerView.findViewById(R.id.circle_address);
        circle_more = (ImageView) headerView.findViewById(R.id.circle_more);
//        circle_layout_view = headerView.findViewById(R.id.circle_layout_view);
        tv_hy_selected = (TextView) headerView.findViewById(R.id.tv_hy_selected);
        tv_gw_selected = (TextView) headerView.findViewById(R.id.tv_gw_selected);
        tv_addr_selected = (TextView) headerView.findViewById(R.id.tv_addr_selected);
        hy_indicator = (ImageView) headerView.findViewById(R.id.iv_hy_indicator);
        gw_indicator = (ImageView) headerView.findViewById(R.id.iv_gw_indicator);
        addr_indicator = (ImageView) headerView.findViewById(R.id.iv_addr_indicator);

        circle_work1 = (LinearLayout)findViewById(R.id.circle_work);
        circle_gangwei1 = (LinearLayout) findViewById(R.id.circle_gangwei);
        circle_address1 = (LinearLayout) findViewById(R.id.circle_address);
        circle_more1 = (ImageView) findViewById(R.id.circle_more);
//        circle_layout_view = headerView.findViewById(R.id.circle_layout_view);
        tv_hy_selected1 = (TextView) findViewById(R.id.tv_hy_selected);
        tv_gw_selected1 = (TextView) findViewById(R.id.tv_gw_selected);
        tv_addr_selected1= (TextView) findViewById(R.id.tv_addr_selected);
        hy_indicator1 = (ImageView) findViewById(R.id.iv_hy_indicator);
        gw_indicator1 = (ImageView) findViewById(R.id.iv_gw_indicator);
        addr_indicator1 = (ImageView) findViewById(R.id.iv_addr_indicator);
        layout_circle_header = (LinearLayout) findViewById(R.id.layout_circle_header);



        headerView2 = View.inflate(this, R.layout.home_banner,null);
        //banner图
        sb_home_banner = (Banner) headerView2.findViewById(R.id.sb_home_banner);

        rcAdapter = new CircleAdapter(CircleUI.this,circleList);
        rcAdapter.setIsCircle(true);
        wapperAdapter = new CircleHeaderAndFooterWrapper(rcAdapter);
        wapperAdapter.addHeaderView(headerView2);
        wapperAdapter.addHeaderView(headerView);
        circle_rc.setAdapter(wapperAdapter);
        circle_rc.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));


//        circle_rc.setLayoutManager(new GridLayoutManager(CircleUI.this, 1) );

        circle_rc.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                //判断是当前layoutManager是否为LinearLayoutManager
                // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    //获取最后一个可见view的位置
                    int lastItemPosition = linearManager.findLastVisibleItemPosition();
                    //获取第一个可见view的位置
                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();
                    if(firstItemPosition > 0){
                        layout_circle_header.setVisibility(View.VISIBLE);
                    }else {
                        layout_circle_header.setVisibility(View.GONE);
                    }
                    //System.out.println(lastItemPosition + "   " + firstItemPosition);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }
        });

        circle_work.setOnClickListener(this);
        circle_gangwei.setOnClickListener(this);
        circle_address.setOnClickListener(this);
        circle_more.setOnClickListener(this);

        circle_work1.setOnClickListener(this);
        circle_gangwei1.setOnClickListener(this);
        circle_address1.setOnClickListener(this);
        circle_more1.setOnClickListener(this);

        mReceiver = new UiReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,
                new IntentFilter("ACTION_PUSH_SUCEESS"));
    }


    private void initData(){
        aCache = ACache.get(mContext);
        //String cache_list = SpTools.getString(mContext , circle_list , "");
        //CircleBean circleBean = gson.fromJson(cache_list , CircleBean.class);
        CircleBean circleBean = (CircleBean) aCache.getAsObject(circle_list);
        if(circleBean != null){
            totalPage = circleBean.totalpage;
            circleList.clear();
            circleList.addAll(circleBean.qz_context);
            notifyDataSetChanged();
        }

        if (protocal == null) {
            protocal = new CircleProtocal();
        }

        userid = SpTools.getUserId(this);
        protocal.getCircleList(userid, "1", "4", "0", new CircleProtocal.CircleListener() {
            @Override
            public void circleResponse(CircleBean response) {
                aCache.put(circle_list , response);
                //SpTools.setString(mContext , circle_list , gson.toJson(response));
                totalPage = response.totalpage;
                circleList.clear();
                circleList.addAll(response.qz_context);
                notifyDataSetChanged();
            }
        });

    }
    //订阅
    @OnClick(R.id.circle_subscribe_but)
    private void getSubmit(View view){
        userid = SpTools.getUserId(this);
        if (TextUtils.isEmpty(userid)){
            CommonUtils.toastMessage("没有登陆");
            return;
        }
        startActivity(new Intent(CircleUI.this,SubscribActivity.class));

    }

    @Override
    protected void prepareData() {

        getAdvData();
    }
    /**
     * 获取广告
     */

    private PictureAdvantage picProtocal = new PictureAdvantage();
    private List<FirstPic.LunboBean> lunbo = new ArrayList<>();
    private Gson gson = new Gson();
    private void getAdvData() {
        //先获取缓存数据
        FirstPic cacheBean = (FirstPic) aCache.getAsObject(circle_adv);
        //FirstPic cacheBean = gson.fromJson(SpTools.getString(mContext , circle_adv , ""),FirstPic.class);
        if(cacheBean != null){
            lunbo.clear();
            lunbo.addAll(cacheBean.lunbo);
            sb_home_banner.notifiDataHasChanged();
        }

        //测试
        String ub_id = SpTools.getUserId(this);
        picProtocal.getFirstPic(ub_id, new PictureAdvantage.HotNewsListener() {
            @Override
            public void hotNewsResponse(String respose) {
                if (respose == null){
                    CommonUtils.toastMessage("请检查网络，获取推荐图片失败！");
                    return;
                }
                //SpTools.setString(mContext , circle_adv , respose);
                FirstPic bean = gson.fromJson(respose,FirstPic.class);

                if ("10".equals(bean.result.code)){
                    lunbo.clear();
                    lunbo.addAll(bean.lunbo);
                    sb_home_banner.notifiDataHasChanged();
                    aCache.put(circle_adv , bean);
                }else {
                    CommonUtils.toastMessage("获取推荐图片失败，请刷新重试！");
                }
            }
        });

    }


    /**
     * 搜索
     * @param view
     */
    @OnClick(R.id.ll_home_search)
    private void getSearch(View view){
        Intent intent = new Intent(CircleUI.this, HomeNewsSearchUI.class);
        intent.putExtra("type","3");
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CITY_REQUEST_CODE){
            if (data != null) {
                String selectCity = data.getStringExtra("city");
                if(!"地理位置".equals(selectCity)){
                    request_addr = selectCity;
                    refreshData("1");
                    tv_addr_selected.setText(selectCity);
                    tv_addr_selected1.setText(selectCity);
                }else{
                    request_addr = "";
                    refreshData("1");
                    tv_addr_selected.setText("地理位置");
                    tv_addr_selected1.setText("地理位置");
                }

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


                tv_hy_selected1.setText("行业选择");
                tv_hy_selected1.setSelected(false);
                hy_indicator1.setImageDrawable(getResources().getDrawable(R.drawable.circle_indicator_selector));

            }else if (GANG_WEI.equals(currentSelected)){
                tv_gw_selected.setText("岗位选择");
                tv_gw_selected.setSelected(false);
                gw_indicator.setImageDrawable(getResources().getDrawable(R.drawable.circle_indicator_selector));
                gwSelected = false;
                request_gw = "";

                tv_gw_selected1.setText("岗位选择");
                tv_gw_selected1.setSelected(false);
                gw_indicator1.setImageDrawable(getResources().getDrawable(R.drawable.circle_indicator_selector));

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


                tv_hy_selected1.setSelected(true);
                hy_indicator1.setImageDrawable(getResources().getDrawable(R.drawable.circle_indicator_selector_red));
                tv_hy_selected1.setText(title);

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


                tv_gw_selected1.setSelected(true);
                gw_indicator1.setImageDrawable(getResources().getDrawable(R.drawable.circle_indicator_selector_red));
                tv_gw_selected1.setText(title);
            }
        }
        refreshData("1");

    }

    private boolean isLoadMore = false;
    private int page = 1;
    private int totalPage = 1;
    public void refreshData(String page) {
        //请求筛选过的数据
        String ub_id = userid;
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
                if (response == null){
                    CommonUtils.toastMessage("更新失败");
                    pullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                    return;
                }
                totalPage = response.totalpage;
                if (!isLoadMore) {
                    circleList.clear();
                }
                if(response.qz_context != null) {
                    circleList.addAll(response.qz_context);
                }
                pullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                notifyDataSetChanged();
                isLoadMore = false;
            }
        });
    }


    public void refreshItemData(final String qc_id) {
        if (protocal == null){
            protocal = new CircleProtocal();
        }
        userid = SpTools.getUserId(this);
        protocal.getItemInfo(userid, qc_id, new CircleProtocal.NormalListener() {
            @Override
            public void normalResponse(Object response) {
                if (response == null){
                    CommonUtils.toastMessage("更新数据失败");
                    return;
                }
                CircleItemUpBean circleItemUpBean = (CircleItemUpBean) response;
                QzContextBean qzContextBean = circleItemUpBean.qz_info.get(0);
                for (int i = 0; i < circleList.size() ; i ++){
                    QzContextBean bean = circleList.get(i);
                    if (qc_id.equals(bean.qc_id)){
                        circleList.set(i,qzContextBean);
                        break;
                    }
                }
                notifyDataSetChanged();

            }
        });


    }
    //行业
    public static final String HANG_YE = "hy";
    private boolean hySelected = false;
    //岗位选择
    public static final String GANG_WEI = "gw";
    private boolean gwSelected = false;
    //地理位置
    public static final String ADDRESS ="addr";
    @Override
    public void onClick(View v) {
        if (v == circle_work || v == circle_gangwei || v == circle_address) {
            RecyclerView.LayoutManager layoutManager = circle_rc.getLayoutManager();
            ((LinearLayoutManager) layoutManager).scrollToPositionWithOffset(1, 0);
        }
        if (v == circle_work || v == circle_work1){
            //改变ui
            hy_indicator.setSelected(true);
            hy_indicator.setImageDrawable(getResources().getDrawable(R.drawable.circle_indicator_selector_red));
            tv_hy_selected.setSelected(true);
            //更新数据
            currentSelected = HANG_YE;
            if (InitInfo.professionBean != null) {
                cp.setDataAdapter(InitInfo.professionBean.hy);
            }

            hy_indicator1.setSelected(true);
            hy_indicator1.setImageDrawable(getResources().getDrawable(R.drawable.circle_indicator_selector_red));
            tv_hy_selected1.setSelected(true);
            //显示在circle_work的下方
//            cp.showPopWindow(circle_work);
            cp.showPopWindow(v);

        }else if (v == circle_gangwei || v == circle_gangwei1){
            //改变ui
            gw_indicator.setSelected(true);
            gw_indicator.setImageDrawable(getResources().getDrawable(R.drawable.circle_indicator_selector_red));
            tv_gw_selected.setSelected(true);

            gw_indicator1.setSelected(true);
            gw_indicator1.setImageDrawable(getResources().getDrawable(R.drawable.circle_indicator_selector_red));
            tv_gw_selected1.setSelected(true);

            currentSelected = GANG_WEI;
            if (gwList.size() <= 0 && InitInfo.postBean != null && InitInfo.postBean.gw != null) {
                for (PostBean.PostItemBean bean : InitInfo.postBean.gw) {
                    ProfessionBean.ProfessionItemBean itemBean = new ProfessionBean.ProfessionItemBean();
                    itemBean.isSelect = bean.isSelect;
                    itemBean.hy_name = bean.gw_name;
                    itemBean.hy_id = bean.gw_id;
                    gwList.add(itemBean);
                }
            }
            cp.setDataAdapter(gwList);
//            cp.showPopWindow(circle_work);
            cp.showPopWindow(v);

        }else if (v == circle_address || v == circle_address1){
            currentSelected = ADDRESS;
            String city = tv_addr_selected.getText().toString();
            Intent intent = new Intent(this, SelectCityActivity.class);
            intent.putExtra("city" ,city);
            Log.i("qza", "onClick: " +  city);
            startActivityForResult(intent,CITY_REQUEST_CODE);
        }else if (v == circle_more || v == circle_more1){

            if (moreData == null){
                moreData = new ArrayList<String>();
                moreData.add("按最新排序");
                moreData.add("按热门排序");
                moreData.add("按订阅量排序");
            }
            spinerPopWindow = new SpinerPopWindow(CircleUI.this,moreData,request_order);
            spinerPopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
//          spinerPopWindow.showAsDropDown(circle_more);
            spinerPopWindow.showAsDropDown(v);

            spinerPopWindow.setSpinerInterface(new SpinerPopWindow.SpinerInterface() {
                @Override
                public void setSpinerInterface( int position) {
                    spinerPopWindow.dismiss();
                    request_order = position + "";

                    //请求筛选过的数据
                    refreshData("1");

                }
            });
        }
    }


    public void setRedPoint(boolean isShow){
        if (isShow){
            unread_msg_number.setVisibility(View.VISIBLE);
            TabUI.getTabUI().setCircleRedPoint(true);
        }else {
            unread_msg_number.setVisibility(View.GONE);
            TabUI.getTabUI().setCircleRedPoint(false);
        }
    }

    /**
     * 刷新Adapter
     */
    public void notifyDataSetChanged() {
        if(wapperAdapter != null){
            wapperAdapter.upDateLabel();
            wapperAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        mReceiver = null;
        super.onDestroy();
    }
}
