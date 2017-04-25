package com.lchtime.safetyexpress.ui.vip.fragment;

import android.view.View;
import android.widget.ListView;

import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.adapter.MyConllectedVideoAdapter;
import com.lchtime.safetyexpress.ui.vip.MyConllected;
import com.lchtime.safetyexpress.views.LoadingPager;

/**
 * Created by android-cp on 2017/4/21.
 */

public class VedioFragment extends BaseFragment {

    private MyConllectedVideoAdapter adapter;

    @Override
    protected View initSuccessView() {
        ListView listView = new ListView(MyApplication.getContext());
        adapter = new MyConllectedVideoAdapter(MyApplication.getContext(),this);
        listView.setAdapter(adapter);
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

    public void updataListView(boolean isDelete){
        if(adapter != null){
            adapter.setDelete(isDelete);
            adapter.notifyDataSetChanged();
        }
    }

    public void updataDeleteNum(int plusOrDelete){
        ((MyConllected)getActivity()).setDeleteNum(plusOrDelete);
    }
}
