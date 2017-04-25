package com.lchtime.safetyexpress.ui.vip.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lchtime.safetyexpress.utils.UIUtils;
import com.lchtime.safetyexpress.views.LoadingPager;

import java.util.List;
import java.util.Map;

/**
 * Created by Dreamer on 2017/3/1.
 */

public abstract class BaseFragment extends Fragment {


    public LoadingPager getLoadingPager() {
        return mLoadingPager;
    }

    private LoadingPager mLoadingPager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mLoadingPager = new LoadingPager(UIUtils.getContext()) {
            @Override
            public LoadedResult initData() {
                return BaseFragment.this.initData();
            }

            @Override
            public View initSuccessView() {
                return BaseFragment.this.initSuccessView();
            }
        };
        return mLoadingPager;
    }

    public LoadingPager.LoadedResult checkResult(Object resObj){

        if (resObj == null){

            return LoadingPager.LoadedResult.EMPTY;
        }

        if (resObj instanceof List){
            if (((List) resObj).size() == 0){
                return LoadingPager.LoadedResult.EMPTY;
            }
        }

        if (resObj instanceof Map){
            if (((Map) resObj).size() == 0){
                return LoadingPager.LoadedResult.EMPTY;
            }
        }

        return LoadingPager.LoadedResult.SUCCESS;

    }

    protected abstract View initSuccessView();


    public abstract LoadingPager.LoadedResult initData();
}
