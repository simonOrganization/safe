package com.lchtime.safetyexpress.ui.news;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.NewsFragmentAdapter;
import com.lchtime.safetyexpress.bean.NewTypeBean;
import com.lchtime.safetyexpress.bean.res.NewsRes;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.Const;
import com.lchtime.safetyexpress.ui.home.HomeNewsSearchUI;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.JsonUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;

import okhttp3.Call;


/**
 * Created by yxn on 2017/4/25.
 */
@ContentView(R.layout.activity_home_news)
public class HomeNewActivity extends BaseUI {

    @ViewInject(R.id.activity_new_tablayout)
    TabLayout activity_new_tablayout;
    @ViewInject(R.id.activity_new_vp)
    ViewPager activity_new_vp;
    private NewsFragmentAdapter fragmentAdapter;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        //布局设置
        setTitle("新闻中心");
        rightVisible(R.drawable.news_search);
//        setContentView(R.layout.activity_home_news);
//        ButterKnife.bind(this);
        getTabData();

    }
    /**
     * 搜索
     * @param view
     */
    @OnClick(R.id.ll_right)
    private void getSearch(View view){
        Intent intent = new Intent(HomeNewActivity.this, HomeNewsSearchUI.class);
        startActivity(intent);
    }

    @Override
    protected void prepareData() {
        //准备数据


    }
    private void getTabData(){
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            return;
        }
        OkHttpUtils
                .post()
                .url(Const.NEW_TYPE)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        NewsRes newsRes = (NewsRes) JsonUtils.stringToObject(response, NewsRes.class);
                        if(newsRes.getResult().getCode().equals("10")){
                            ArrayList<NewTypeBean> list = new ArrayList<NewTypeBean>();
                            list.add(new NewTypeBean("推荐"));
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
                            fragmentAdapter.setmCommentDatas(newsRes.getTj());
                            activity_new_vp.setAdapter(fragmentAdapter);
                            activity_new_tablayout.setupWithViewPager(activity_new_vp);
                            activity_new_tablayout.setTabsFromPagerAdapter(fragmentAdapter);
                            activity_new_vp.setCurrentItem(0);

                        }else{
                            Toast.makeText(HomeNewActivity.this,newsRes.getResult().getInfo(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
