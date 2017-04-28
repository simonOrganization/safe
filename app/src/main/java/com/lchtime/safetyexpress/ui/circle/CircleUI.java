package com.lchtime.safetyexpress.ui.circle;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.CircleAdapter;
import com.lchtime.safetyexpress.adapter.SpinerAdapter;
import com.lchtime.safetyexpress.bean.CircleOneBean;
import com.lchtime.safetyexpress.bean.CircleSelectBean;
import com.lchtime.safetyexpress.bean.CircleTwoBean;
import com.lchtime.safetyexpress.bean.HomeBannerBean;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.home.HomeNewsSearchUI;
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
    @ViewInject(R.id.circle_empty)
    private TextView circle_empty;
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

    private CirclePopView cp;
    private CircleAdapter rcAdapter;
    private List<HomeBannerBean> mDatas = new ArrayList<>();
    private ArrayList<CircleOneBean> oneList = new ArrayList<CircleOneBean>();
    private ArrayList<CircleSelectBean> arrayList = new ArrayList<CircleSelectBean>();
    private SpinerPopWindow spinerPopWindow;
    @Override
    protected void back() {
        exit();
    }

    @Override
    protected void setControlBasis() {

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
                Toast.makeText(CircleUI.this,positions,Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initData(){
        arrayList.add(new CircleSelectBean(false));
        arrayList.add(new CircleSelectBean(false));
        arrayList.add(new CircleSelectBean(false));
        arrayList.add(new CircleSelectBean(false));
        arrayList.add(new CircleSelectBean(false));
        arrayList.add(new CircleSelectBean(false));
        arrayList.add(new CircleSelectBean(false));
        ArrayList<String> oneLists = new ArrayList<String>();
        oneLists.add("");
        ArrayList<String> twoList = new ArrayList<String>();
        twoList.add("");
        twoList.add("");
        twoList.add("");
        twoList.add("");
        ArrayList<String> twoLists = new ArrayList<String>();
        twoLists.add("");
        twoLists.add("");
        twoLists.add("");
        twoLists.add("");
        twoLists.add("");
        twoLists.add("");
        twoLists.add("");
        oneList.add(new CircleOneBean(oneLists));
        oneList.add(new CircleOneBean(twoLists));
        oneList.add(new CircleOneBean(twoList));
        oneList.add(new CircleOneBean(twoLists));
        oneList.add(new CircleOneBean(twoList));
        circle_rc.setLayoutManager(new GridLayoutManager(this, 1) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        rcAdapter = new CircleAdapter(this,oneList);
        circle_rc.setAdapter(rcAdapter);

    }
    @OnClick(R.id.circle_subscribe_but)
    private void getSubmit(View view){
        startActivity(new Intent(CircleUI.this,SubscribActivity.class));

    }
    @OnClick(R.id.circle_work)
    private void getWork(View view){
        cp.setDataAdapter(arrayList);
        cp.showPopWindow(circle_work);

    }
    @OnClick(R.id.circle_gangwei)
    private void getGangwei(View view){
        cp.setDataAdapter(arrayList);
        cp.showPopWindow(circle_work);
        circle_pop_view.setVisibility(View.VISIBLE);
        ;

    }
    @OnClick(R.id.circle_address)
    private void getAddress(View view){
        cp.setDataAdapter(arrayList);
        cp.showPopWindow(circle_work);

    }
    @OnClick(R.id.circle_more)
    private void getMord(View view){
        Log.i("yang","getMord");
        final ArrayList<String> moreData = new ArrayList<String>();
        moreData.add("按最新排序");
        moreData.add("按热门排序");
        moreData.add("按订阅量排序");
        spinerPopWindow = new SpinerPopWindow(CircleUI.this,moreData);
        spinerPopWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        spinerPopWindow.showAsDropDown(circle_layout_view);
        spinerPopWindow.setSpinerInterface(new SpinerPopWindow.SpinerInterface() {
            @Override
            public void setSpinerInterface(int position) {
                spinerPopWindow.dismiss();
                Toast.makeText(CircleUI.this,"pops=="+position,Toast.LENGTH_SHORT).show();
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


}
