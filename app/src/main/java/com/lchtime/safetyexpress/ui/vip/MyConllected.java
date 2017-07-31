package com.lchtime.safetyexpress.ui.vip;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.NewsBean;
import com.lchtime.safetyexpress.bean.QzContextBean;
import com.lchtime.safetyexpress.bean.res.NewsListRes;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.vip.fragment.BaseFragment;
import com.lchtime.safetyexpress.ui.vip.fragment.CircleFragment;
import com.lchtime.safetyexpress.ui.vip.fragment.FragmentFactory;
import com.lchtime.safetyexpress.ui.vip.fragment.NewsFragment;
import com.lchtime.safetyexpress.ui.vip.fragment.VedioFragment;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.MyConllectedProtocal;
import com.lchtime.safetyexpress.views.LoadingPager;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by android-cp on 2017/4/20.我的收藏界面 其中有三个fragment
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

    @ViewInject(R.id.rv_delete_all)
    private RelativeLayout rv_delete;

    private MyPagerAdapter myPagerAdapter;

    private String[] mMainTitle = {"新闻","视频","圈子"};

    //设置初始化显示，默认不显示勾选项
    private boolean flag = false;

    //设置删除键角标
    private int num = 0;
    private BaseFragment currentFragment;

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
                if (currentFragment != null){
                    if (currentFragment instanceof NewsFragment){
                        deleteNews();
                    }else if (currentFragment instanceof VedioFragment){
                        deleteVideos();
                    }else if (currentFragment instanceof CircleFragment){
                        deleteCircles();
                    }
                }


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
            currentFragment = FragmentFactory.createFragment(position);
            LoadingPager loadingPager = currentFragment.getLoadingPager();
            loadingPager.triggerLoadData();
            if(flag){
                clickEvent();
            }
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
            rv_delete.setVisibility(View.GONE);
        }else {
            flag = true;
            //显示删除小圆点
            rightTextVisible("取消");
            rv_delete.setVisibility(View.VISIBLE);
        }

        ((VedioFragment)FragmentFactory.createFragment(1)).updataListView(flag);
        ((NewsFragment)FragmentFactory.createFragment(0)).updataListView(flag);
        ((CircleFragment)FragmentFactory.createFragment(2)).updataListView(flag);

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

    //删除新闻
    private void deleteNews() {
        //执行删除动作
        List<NewsBean> list = ((NewsFragment) FragmentFactory.createFragment(0)).getDeleteList();

        if (list == null){
            CommonUtils.toastMessage("您没有选择要删除的选项");
            return ;
        }
        String del_ids = "";
        for (int i = 0; i < list.size(); i ++){
            NewsBean bean = list.get(i);
            if (i == 0){
                del_ids = del_ids + bean.getCc_id();
            }else {
                del_ids = del_ids + ";" + bean.getCc_id();
            }
        }
        if (!TextUtils.isEmpty(del_ids)) {
            MyConllectedProtocal.requestDelete("1", del_ids, "0",new MyConllectedProtocal.DeleteResponse() {
                @Override
                public void onDeleteResponse(NewsListRes newsListRes) {
                    if(newsListRes.getResult().getCode().equals("10")){
                        //成功
                        Toast.makeText(MyApplication.getContext(),newsListRes.getResult().getInfo(),Toast.LENGTH_SHORT).show();

                        ((NewsFragment) FragmentFactory.createFragment(0)).updataDeleteList();
                        clickEvent();
                        num = 0;
                        deleteAll.setText("删除");

                    }else{
                        Toast.makeText(MyApplication.getContext(),"删除收藏失败！",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            CommonUtils.toastMessage("您没有选择要删除的选项");
        }
    }


    //删除视频
    private void deleteVideos() {
        List<NewsBean> list = ((VedioFragment) FragmentFactory.createFragment(1)).getDeleteList();


        if (list == null){
            CommonUtils.toastMessage("您没有选择要删除的选项");
            return ;
        }
        String del_ids = "";
        for (int i = 0; i < list.size(); i ++){
            NewsBean bean = list.get(i);
            if (i == 0){
                del_ids = del_ids + bean.getCc_id();
            }else {
                del_ids = del_ids + ";" + bean.getCc_id();
            }
        }
        if (!TextUtils.isEmpty(del_ids)) {
            MyConllectedProtocal.requestDelete("1", del_ids, "9",new MyConllectedProtocal.DeleteResponse() {
                @Override
                public void onDeleteResponse(NewsListRes newsListRes) {
                    if(newsListRes.getResult().getCode().equals("10")){
                        //成功
                        Toast.makeText(MyApplication.getContext(),newsListRes.getResult().getInfo(),Toast.LENGTH_SHORT).show();

                        ((VedioFragment) FragmentFactory.createFragment(1)).updataDeleteList();
                        clickEvent();
                        num = 0;
                        deleteAll.setText("删除");

                    }else{
                        Toast.makeText(MyApplication.getContext(),"删除收藏失败！",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            CommonUtils.toastMessage("您没有选择要删除的选项");
        }

    }


    //删除视频
    private void deleteCircles() {
        List<QzContextBean> list = ((CircleFragment) FragmentFactory.createFragment(2)).getDeleteList();


        if (list == null){
            CommonUtils.toastMessage("您没有选择要删除的选项");
            return ;
        }
        String del_ids = "";
        for (int i = 0; i < list.size(); i ++){
            QzContextBean bean = list.get(i);
            if (i == 0){
                del_ids = del_ids + bean.qc_id;
            }else {
                del_ids = del_ids + ";" + bean.qc_id;
            }
        }
        if (!TextUtils.isEmpty(del_ids)) {
            MyConllectedProtocal.requestDelete("1", del_ids, "5",new MyConllectedProtocal.DeleteResponse() {
                @Override
                public void onDeleteResponse(NewsListRes newsListRes) {
                    if(newsListRes.getResult().getCode().equals("10")){
                        //成功
                        Toast.makeText(MyApplication.getContext(),newsListRes.getResult().getInfo(),Toast.LENGTH_SHORT).show();

                        ((CircleFragment) FragmentFactory.createFragment(2)).updataDeleteList();
                        clickEvent();
                        num = 0;
                        deleteAll.setText("删除");

                    }else{
                        Toast.makeText(MyApplication.getContext(),"删除收藏失败！",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            CommonUtils.toastMessage("您没有选择要删除的选项");
        }

    }

}
