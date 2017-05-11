package com.lchtime.safetyexpress.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.NewTypeBean;
import com.lchtime.safetyexpress.bean.res.VideoRes;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.home.fragment.VideosRecommendFragment;
import com.lchtime.safetyexpress.ui.home.protocal.VideoProtocal;
import com.lchtime.safetyexpress.views.XHorizontalScrollView;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

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

    private VideoProtocal protocal;
    private List<NewTypeBean> titleList;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        setTitle("视频中心");
        rightVisible(R.drawable.news_search);

    }

    @Override
    protected void prepareData() {

        if (protocal == null){
            protocal = new VideoProtocal();
        }
        protocal.getVideoDir(new VideoProtocal.VideoDirListener() {
            @Override
            public void videoDirResponse(VideoRes response) {
                titleList = response.cms_dir;
                videosPagerAdapter = new VideosPagerAdapter(getSupportFragmentManager());
                vp_home_videos.setAdapter(videosPagerAdapter);

                final int pageMargin = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                                .getDisplayMetrics());
                vp_home_videos.setPageMargin(pageMargin);

                xhsv_home_videos.setViewPager(vp_home_videos);
                videosPagerAdapter.notifyDataSetChanged();
            }
        });

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


        public VideosPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
                Fragment fragment = new VideosRecommendFragment();
                Bundle bundle = new Bundle();
                bundle.putString("cd_id",titleList.get(arg0).cd_id);
                fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return titleList == null ? 0 :titleList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList == null ? "" : titleList.get(position).cd_name;
        }

    }
}