package com.lchtime.safetyexpress.ui.home;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.home.fragment.VideosRecommendFragment;
import com.lchtime.safetyexpress.views.XHorizontalScrollView;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 视频中心
 * Created by user on 2017/4/18.
 */
@ContentView(R.layout.home_videos_ui)
public class HomeVideosUI extends BaseUI {

    //XHorizontalScrollView
    @ViewInject(R.id.xhsv_home_videos)
    private XHorizontalScrollView xhsv_home_videos;
    //ViewPager
    @ViewInject(R.id.vp_home_videos)
    private ViewPager vp_home_videos;

    private VideosPagerAdapter videosPagerAdapter;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        setTitle("新闻中心");
        rightVisible(R.drawable.news_search);

        videosPagerAdapter = new VideosPagerAdapter(getSupportFragmentManager());
        vp_home_videos.setAdapter(videosPagerAdapter);

        final int pageMargin = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                        .getDisplayMetrics());
        vp_home_videos.setPageMargin(pageMargin);

        xhsv_home_videos.setViewPager(vp_home_videos);
    }

    @Override
    protected void prepareData() {

    }

    /**
     * 搜索
     *
     * @param view
     */
    @OnClick(R.id.ll_right)
    private void getSearch(View view) {
        Intent intent = new Intent(HomeVideosUI.this, HomeNewsSearchUI.class);
        startActivity(intent);
    }

    class VideosPagerAdapter extends FragmentPagerAdapter {

        //测试
        private String[] titles = {"推荐", "热点", "安全动态", "安全事故",
                "法规标准", "安全知识"};

        public VideosPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            return new VideosRecommendFragment();
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

    }
}