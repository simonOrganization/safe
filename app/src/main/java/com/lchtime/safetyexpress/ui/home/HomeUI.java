package com.lchtime.safetyexpress.ui.home;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.HomeHotCircleAdapter;
import com.lchtime.safetyexpress.adapter.HomeHotTrackAdapter;
import com.lchtime.safetyexpress.adapter.HomeHotVideoAdapter;
import com.lchtime.safetyexpress.adapter.HomeNewAdapter;
import com.lchtime.safetyexpress.bean.HomeBannerBean;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.circle.CircleUI;
import com.lchtime.safetyexpress.ui.news.HomeNewActivity;
import com.lchtime.safetyexpress.views.MyGridView;
import com.lchtime.safetyexpress.views.MyListView;
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

/**
 * 首页
 * Created by user on 2017/4/14.
 */
@ContentView(R.layout.home)
public class HomeUI extends BaseUI {

    //banner图
    @ViewInject(R.id.sb_home_banner)
    private Banner sb_home_banner;
    //热门圈子
    @ViewInject(R.id.mgv_home_hot_circle)
    private MyGridView mgv_home_hot_circle;
    //热点追踪
    @ViewInject(R.id.mlv_home_hot_track)
    private MyListView mlv_home_hot_track;
    //视频热点
    @ViewInject(R.id.mlv_home_hot_video)
    private MyListView mlv_home_hot_video;

    private HomeHotCircleAdapter homeHotCircleAdapter;
    private HomeHotTrackAdapter homeHotTrackAdapter;
    private HomeHotVideoAdapter homeHotVideoAdapter;
    private HomeToCircleInterface homeToCircleInterface;
    public static HomeUI homeUI_instance = null;

    private List<HomeBannerBean> mDatas = new ArrayList<>();

    @Override
    protected void back() {
        exit();
    }

    @Override
    protected void setControlBasis() {
        homeUI_instance = this;
        //轮播图
        BannerAdapter adapter = new BannerAdapter<HomeBannerBean>(mDatas) {
            @Override
            protected void bindTips(TextView tv, HomeBannerBean homeBannerBean) {
                tv.setText("");
            }

            @Override
            public void bindImage(ImageView imageView, HomeBannerBean homeBannerBean) {
                Glide.with(HomeUI.this)
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
        //热门圈子
        homeHotCircleAdapter = new HomeHotCircleAdapter(HomeUI.this);
        mgv_home_hot_circle.setAdapter(homeHotCircleAdapter);
        mgv_home_hot_circle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                makeText("圈子" + position);
            }
        });
        //热点追踪
        homeHotTrackAdapter = new HomeHotTrackAdapter(HomeUI.this);
        mlv_home_hot_track.setAdapter(homeHotTrackAdapter);
        mlv_home_hot_track.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HomeUI.this, HomeNewsDetailUI.class);
                startActivity(intent);
            }
        });
        //视频热点
        homeHotVideoAdapter = new HomeHotVideoAdapter(HomeUI.this);
        mlv_home_hot_video.setAdapter(homeHotVideoAdapter);
        mlv_home_hot_video.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HomeUI.this, HomeVideosDeatilUI.class);
                startActivity(intent);
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
        Intent intent = new Intent(HomeUI.this, HomeNewsSearchUI.class);
        startActivity(intent);
    }

    /**
     * 扫一扫
     * @param view
     */
    @OnClick(R.id.ll_home_scan)
    private void getScan(View view){
        makeText("扫一扫");
    }

    /**
     * 新闻中心
     * @param view
     */
    @OnClick(R.id.ll_home_news)
    private void getNews(View view){
//        Intent intent = new Intent(HomeUI.this, HomeNewsUI.class);
        Intent intent = new Intent(HomeUI.this, HomeNewActivity.class);
        startActivity(intent);
    }

    /**
     * 视频中心
     * @param view
     */
    @OnClick(R.id.ll_home_video)
    private void getVideo(View view){
        Intent intent = new Intent(HomeUI.this, HomeVideosUI.class);
        startActivity(intent);
    }

    /**
     * 安全圈子
     * @param view
     */
    @OnClick(R.id.ll_home_circle)
    private void getCircle(View view){
       homeToCircleInterface.toCircleActivity();
        makeText("安全圈子");
    }

    /**
     * 疑难问答
     * @param view
     */
    @OnClick(R.id.ll_home_question)
    private void getQuestion(View view){
        Intent intent = new Intent(HomeUI.this, HomeQuestionUI.class);
        startActivity(intent);
    }
    public interface HomeToCircleInterface{
        void toCircleActivity();
    }
    public void setHomeToCircleInterface(HomeToCircleInterface homeToCircleInterface){
        this.homeToCircleInterface = homeToCircleInterface;
    }

}
