package com.lchtime.safetyexpress.ui.search.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.HomeVideosRecommendAdapter;
import com.lchtime.safetyexpress.bean.QJSearchBean;
import com.lchtime.safetyexpress.ui.search.HomeNewsSearchUI;
import com.lchtime.safetyexpress.ui.search.protocal.SerchProtocal;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by Dreamer on 2017/5/20.
 */

public class VideosSearchResultFragment extends Fragment {

    //搜索新闻
    @ViewInject(R.id.slv_news_search)
    private RecyclerView slv_news_search;
    @ViewInject(R.id.slv_video_search)
    private RecyclerView slv_video_search;
    @ViewInject(R.id.llnews_title)
    private LinearLayout llnews_title;
    @ViewInject(R.id.llvideo_title)
    private LinearLayout llvideo_title;

    private HomeNewsSearchUI activity;
    private String mType;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_search_result, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewUtils.inject(this, view);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (HomeNewsSearchUI) getActivity();
        mType = activity.mType;
        slv_news_search.setVisibility(View.GONE);
        llnews_title.setVisibility(View.GONE);
        initBasic();
        initData();
    }



    private void initBasic() {
        slv_video_search.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initData() {
        //初始化搜索
        getSearch(activity.getKey());
    }

    private SerchProtocal mProtocal;
    private HomeVideosRecommendAdapter homeVideosRecommendAdapter;
    public void getSearch(String key){
        if (mProtocal == null){
            mProtocal = new SerchProtocal();
        }
        mProtocal.getSearchResult(key, Integer.parseInt(mType), mType, new SerchProtocal.NormalListener() {
            @Override
            public void normalResponse(Object response) {
                if (response == null){
                    CommonUtils.toastMessage("服务器数据类型异常");
                    return;
                }
                switch (Integer.parseInt(mType)){
//                    case 0:
//                        //全局搜索
//                        QJSearchBean bean0 = (QJSearchBean) response;
//                        break;
//                    case 1:
                    case 2:
                        //新闻视频
                        QJSearchBean bean = (QJSearchBean) response;
                        homeVideosRecommendAdapter = new HomeVideosRecommendAdapter(getActivity(),bean.sp_info);
                        slv_video_search.setAdapter(homeVideosRecommendAdapter);
                        break;
//                    case 3:
//                        //圈子搜索
//                        QJSearchBean bean2 = (QJSearchBean) response;
//                        break;
                }
            }
        });
    }

}
