package com.lchtime.safetyexpress.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lchtime.safetyexpress.bean.NewTypeBean;
import com.lchtime.safetyexpress.bean.NewsBean;

import java.util.ArrayList;

/**
 * Created by yxn on 2017/4/25.
 */

public class NewsFragmentAdapter extends FragmentPagerAdapter {
    private ArrayList<NewTypeBean> mDatas;
    private ArrayList<Fragment> fragments;
    private ArrayList<NewsBean> newsList;
    public NewsFragmentAdapter(FragmentManager fm,ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }
    public void setmDatas(ArrayList<NewTypeBean> mDatas){
        this.mDatas = mDatas;
    }
    public void setmCommentDatas(ArrayList<NewsBean> newsList){
        this.newsList = newsList;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = fragments.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        if(position != 0 && position != 1){

            bundle.putString("typeId",mDatas.get(position).getCd_id());

        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return mDatas.get(position).getCd_name();
    }
}
