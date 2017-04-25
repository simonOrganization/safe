package com.lchtime.safetyexpress.ui.vip;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.vip.fragment.BaseFragment;
import com.lchtime.safetyexpress.ui.vip.fragment.FragmentFactory;
import com.lchtime.safetyexpress.ui.vip.fragment.VedioFragment;
import com.lchtime.safetyexpress.views.LoadingPager;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by android-cp on 2017/4/20.
 */
@ContentView(R.layout.vip_myconllected)
public class MyConllected extends BaseUI {
    //顶部tab
    @ViewInject(R.id.main_tabs)
    private PagerSlidingTabStrip myConllectedTab;
    //ViewPager
    @ViewInject(R.id.main_viewpager)
    private ViewPager myTabViewPager;

    @ViewInject(R.id.tv_delete_all)
    private TextView deleteAll;

    private MyPagerAdapter myPagerAdapter;

    private String[] mMainTitle = {"新闻","视频","圈子"};

    //设置初始化显示，默认不显示勾选项
    private boolean flag = false;

    //设置删除键角标
    private int num = 0;
    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        setTitle("我的收藏");
        rightTextVisible("编辑");
        bindViewPager();

    }
//将viewpager和tab绑定
    private void bindViewPager() {
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        myTabViewPager.setAdapter(myPagerAdapter);
        myConllectedTab.setViewPager(myTabViewPager);
    }

    @Override
    protected void prepareData() {
        // TODO: 2017/4/21 加载数据
        initListener();
    }

    private void initListener() {
        final MyOnpageChangeListener myOnpageChangeListener = new MyOnpageChangeListener();
        myConllectedTab.setOnPageChangeListener(myOnpageChangeListener);

        myTabViewPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                myOnpageChangeListener.onPageSelected(0);
                myTabViewPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        //删除按键
        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //执行删除动作
            }
        });
    }

    class MyPagerAdapter extends FragmentPagerAdapter{

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = FragmentFactory.createFragment(position);
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


    class MyOnpageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            BaseFragment baseFragment = FragmentFactory.createFragment(position);
            LoadingPager loadingPager = baseFragment.getLoadingPager();
            loadingPager.triggerLoadData();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    protected void clickEvent() {
        if (flag == true){
            flag = false;
            //不显示删除小圆点
            rightTextVisible("编辑");

            deleteAll.setVisibility(View.GONE);
        }else {
            flag = true;
            //显示删除小圆点

            rightTextVisible("取消");

            deleteAll.setVisibility(View.VISIBLE);
        }

        ((VedioFragment)FragmentFactory.createFragment(1)).updataListView(flag);

    }

    public void setDeleteNum(int plusOrDelete){
        if (plusOrDelete > 0){
            num++;
        }else {
            num--;
        }

        String text = "删除";
        if (num < 10 && num > 0) {
            text = "删除0" + num;
        }else if(num >= 10){
            text = "删除" + num;
        }
        deleteAll.setText(text);
    }
}
