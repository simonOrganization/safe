package com.lchtime.safetyexpress.ui.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.vip.MyConllected;
import com.lchtime.safetyexpress.ui.vip.fragment.BaseFragment;
import com.lchtime.safetyexpress.ui.vip.fragment.FragmentFactory;
import com.lchtime.safetyexpress.ui.vip.fragment.FragmentFactory2WenDa;
import com.lchtime.safetyexpress.ui.vip.fragment.GuanZhuFragment;
import com.lchtime.safetyexpress.ui.vip.fragment.TiWenFragment;
import com.lchtime.safetyexpress.ui.vip.fragment.WenDaFragment;
import com.lchtime.safetyexpress.views.LoadingPager;
import com.lidroid.xutils.view.annotation.ContentView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by android-cp on 2017/5/23. 我的问答界面
 */
@ContentView(R.layout.question_myquestion)
public class MyQuestion extends BaseUI {


    @BindView(R.id.ll_home)
    LinearLayout llHome;
    @BindView(R.id.v_title)
    TextView vTitle;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.ll_right)
    LinearLayout llRight;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.main_tabs)
    PagerSlidingTabStrip mainTabs;
    @BindView(R.id.tv_titleshow)
    TextView tvTitleshow;
    @BindView(R.id.ll_titleshow)
    LinearLayout llTitleshow;
    @BindView(R.id.vp_viewpager)
    ViewPager vpViewpager;

    private MyPagerAdapter myPagerAdapter;

    private String[] mMainTitle = {"关注","回答","提问"};
    private MyOnpageChangeListener myOnpageChangeListener;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        setTitle("我的问答");
        ButterKnife.bind(this);

        bindViewPager();

    }


    //将viewpager和tab绑定
    private void bindViewPager() {
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        vpViewpager.setAdapter(myPagerAdapter);
        mainTabs.setViewPager(vpViewpager);
        vpViewpager.setOffscreenPageLimit(0);
    }

    @Override
    protected void prepareData() {
        initListener();
    }


    private void initListener() {
        if (myOnpageChangeListener == null) {
            myOnpageChangeListener = new MyOnpageChangeListener();
        }
        mainTabs.setOnPageChangeListener(myOnpageChangeListener);

        vpViewpager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                myOnpageChangeListener.onPageSelected(0);
                vpViewpager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }



    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = FragmentFactory2WenDa.createFragment(position);
            if (fragment != null){
                return fragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return mMainTitle.length;
//            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mMainTitle[position];
        }
    }

    private BaseFragment currentFragment;
    class MyOnpageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currentFragment = FragmentFactory2WenDa.createFragment(position);
            LoadingPager loadingPager = currentFragment.getLoadingPager();
            loadingPager.triggerLoadData2();
            switch (position){


                case 0://返回 关注 对应的fragment
                    tvTitleshow.setText("关注问题" );
                    break;
                case 1://返回 回答 对应的fragment
                    tvTitleshow.setText("我的回答");
                    break;
                case 2://返回 提问 对应的fragment
                    tvTitleshow.setText("我的提问");
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    public void setText(String text){
        tvTitleshow.setText(text);
    }

}
