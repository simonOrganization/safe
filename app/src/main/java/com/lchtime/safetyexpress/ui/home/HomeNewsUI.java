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
import com.lchtime.safetyexpress.ui.home.fragment.NewsRecommendFragment;
import com.lchtime.safetyexpress.views.XHorizontalScrollView;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 新闻中心
 * Created by user on 2017/4/17.
 */
@ContentView(R.layout.home_news_ui)
public class HomeNewsUI extends BaseUI {

    //XHorizontalScrollView
    @ViewInject(R.id.xhsv_home_news)
    private XHorizontalScrollView xhsv_home_news;
    //ViewPager
    @ViewInject(R.id.vp_home_news)
    private ViewPager vp_home_news;

    private NewsPagerAdapter newsPagerAdapter;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        setTitle("新闻中心");
        rightVisible(R.drawable.news_search);

        newsPagerAdapter = new NewsPagerAdapter(getSupportFragmentManager());
        vp_home_news.setAdapter(newsPagerAdapter);

        final int pageMargin = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                        .getDisplayMetrics());
        vp_home_news.setPageMargin(pageMargin);

        xhsv_home_news.setViewPager(vp_home_news);
    }

    @Override
    protected void prepareData() {

    }

    /**
     * 搜索
     * @param view
     */
    @OnClick(R.id.ll_right)
    private void getSearch(View view){
        Intent intent = new Intent(HomeNewsUI.this, HomeNewsSearchUI.class);
        startActivity(intent);
    }

    class NewsPagerAdapter extends FragmentPagerAdapter {

        //测试
        private String[] titles = { "推荐", "热点追踪", "事故案例", "安全文化",
                "公共安全", "工伤保险", "应急预案", "职业健康" };

        public NewsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            return new NewsRecommendFragment();
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
