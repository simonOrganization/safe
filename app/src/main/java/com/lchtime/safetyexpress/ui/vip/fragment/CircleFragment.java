package com.lchtime.safetyexpress.ui.vip.fragment;

import android.view.View;

import com.lchtime.safetyexpress.views.LoadingPager;

/**
 * Created by android-cp on 2017/4/21.
 */

public class CircleFragment extends BaseFragment {
    @Override
    protected View initSuccessView() {
        return null;
    }

    @Override
    public LoadingPager.LoadedResult initData() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return LoadingPager.LoadedResult.ERRO;
    }
}
