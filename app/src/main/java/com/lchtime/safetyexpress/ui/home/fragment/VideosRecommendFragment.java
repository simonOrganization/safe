package com.lchtime.safetyexpress.ui.home.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.HomeVideosRecommendAdapter;
import com.lchtime.safetyexpress.ui.home.HomeVideosDeatilUI;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.mzhy.http.okhttp.OkHttpUtils;
import com.mzhy.http.okhttp.callback.StringCallback;

/**
 * 视频- 推荐
 * Created by user on 2017/4/18.
 */

public class VideosRecommendFragment extends Fragment {

    //列表展示
    @ViewInject(R.id.lv_videos_recommend)
    private ListView lv_videos_recommend;

    private HomeVideosRecommendAdapter homeVideosRecommendAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_videos_recommend_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewUtils.inject(this, view);

        homeVideosRecommendAdapter = new HomeVideosRecommendAdapter(getActivity());
        lv_videos_recommend.setAdapter(homeVideosRecommendAdapter);
        lv_videos_recommend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ((position - 1) < homeVideosRecommendAdapter.getCount()) {
                    Intent intent = new Intent(getActivity(), HomeVideosDeatilUI.class);
                    startActivity(intent);
                }
            }
        });
    }

}