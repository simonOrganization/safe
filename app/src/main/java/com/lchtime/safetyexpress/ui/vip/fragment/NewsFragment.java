package com.lchtime.safetyexpress.ui.vip.fragment;

import android.view.View;
import android.widget.ListView;

import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.adapter.HomeNewsRecommendAdapter;
import com.lchtime.safetyexpress.views.LoadingPager;

/**
 * Created by android-cp on 2017/4/21.
 */

public class NewsFragment extends BaseFragment {
    private HomeNewsRecommendAdapter homeNewsRecommendAdapter;
    @Override
    protected View initSuccessView() {
        ListView listView = new ListView(MyApplication.getContext());
        homeNewsRecommendAdapter = new HomeNewsRecommendAdapter(getActivity());
        listView.setAdapter(homeNewsRecommendAdapter);
        return listView;
    }

    @Override
    public LoadingPager.LoadedResult initData() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return LoadingPager.LoadedResult.SUCCESS;
    }
}
