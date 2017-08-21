package com.lchtime.safetyexpress.ui.news;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.NewsFragmentAdapter;
import com.lchtime.safetyexpress.bean.NewTypeBean;
import com.lchtime.safetyexpress.bean.res.NewsRes;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.Const;
import com.lchtime.safetyexpress.ui.search.HomeNewsSearchUI;
import com.lchtime.safetyexpress.ui.vip.fragment.BaseFragment;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.JsonUtils;
import com.lchtime.safetyexpress.views.LoadingPager;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;

import okhttp3.Call;


/**
 * Created by yxn on 2017/4/25.主页安全头条界面
 */
@ContentView(R.layout.activity_home_news)
public class HomeNewActivity extends BaseUI {

    @ViewInject(R.id.activity_new_tablayout)
    TabLayout activity_new_tablayout;
    @ViewInject(R.id.activity_new_vp)
    ViewPager activity_new_vp;
    @ViewInject(R.id.loading)
    RelativeLayout loading;
    @ViewInject(R.id.empty)
    RelativeLayout empty;
    @ViewInject(R.id.error)
    RelativeLayout error;
    @ViewInject(R.id.success)
    LinearLayout success;
    @ViewInject(R.id.error_btn_retry)
    Button error_btn_retry;

    private NewsFragmentAdapter fragmentAdapter;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private MyOnpageChangeListener listener;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        //布局设置
        setTitle("安全头条");
        rightVisible(R.drawable.news_search);
//        setContentView(R.layout.activity_home_news);
//        ButterKnife.bind(this);
        getTabData();

        error_btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLoadingVisiblity();
                getTabData();
            }
        });

    }


    /**
     * 搜索
     * @param view
     */
    @OnClick(R.id.ll_right)
    private void getSearch(View view){
        Intent intent = new Intent(HomeNewActivity.this, HomeNewsSearchUI.class);
        intent.putExtra("type","1");
        startActivity(intent);
    }

    @Override
    protected void prepareData() {
        //准备数据


    }
    private void getTabData(){
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            setErrorVisiblity();
            //CommonUtils.toastMessage("您当前无网络，请联网再试");
            return;
        }
        OkHttpUtils
                .post()
                .url(Const.NEW_TYPE)
                .addParams("type","0")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        setErrorVisiblity();
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        NewsRes newsRes = (NewsRes) JsonUtils.stringToObject(response, NewsRes.class);
                        if (newsRes.getCms_dir().size() == 0){
                            //如果没有数据
                            setEmptyVisiblity();
                            return;
                        }
                        if(newsRes.getResult().getCode().equals("10")){
                            ArrayList<NewTypeBean> list = new ArrayList<NewTypeBean>();
                            list.add(new NewTypeBean("推荐"));
                            //新增加的tab
                            list.add(new NewTypeBean("热点追踪"));
                            list.addAll(newsRes.getCms_dir());
                            activity_new_tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);//MODE_FIXED
                            for (int i = 0; i < list.size(); i++) {
                                activity_new_tablayout
                                        .addTab(activity_new_tablayout.newTab()
                                                .setText(list.get(i).getCd_name()));
                                HomeNewsFragment fragment = new HomeNewsFragment();
                                fragments.add(fragment);
                            }
                            fragmentAdapter = new NewsFragmentAdapter(getSupportFragmentManager(),fragments);
                            fragmentAdapter.setmDatas(list);
                            activity_new_vp.setAdapter(fragmentAdapter);
                            activity_new_tablayout.setupWithViewPager(activity_new_vp);
                            activity_new_tablayout.setTabsFromPagerAdapter(fragmentAdapter);
                            initListener();
                            setSuccessVisiblity();
                        }else{
                            setErrorVisiblity();
                            Toast.makeText(HomeNewActivity.this,newsRes.getResult().getInfo(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void initListener() {
        listener = new MyOnpageChangeListener();

        activity_new_vp.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                listener.onPageSelected(0);
                activity_new_vp.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        activity_new_vp.addOnPageChangeListener(listener);

    }

    class MyOnpageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            BaseFragment currentFragment = (BaseFragment) fragments.get(position);
            LoadingPager loadingPager = currentFragment.getLoadingPager();
            loadingPager.triggerLoadData();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    public void setLoadingVisiblity(){
        loading.setVisibility(View.VISIBLE);
        empty.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        success.setVisibility(View.GONE);
    }
    public void setEmptyVisiblity(){
        loading.setVisibility(View.GONE);
        empty.setVisibility(View.VISIBLE);
        error.setVisibility(View.GONE);
        success.setVisibility(View.GONE);
    }
    public void setErrorVisiblity(){
        loading.setVisibility(View.GONE);
        empty.setVisibility(View.GONE);
        error.setVisibility(View.VISIBLE);
        success.setVisibility(View.GONE);
    }
    public void setSuccessVisiblity(){
        loading.setVisibility(View.GONE);
        empty.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        success.setVisibility(View.VISIBLE);
    }

}
