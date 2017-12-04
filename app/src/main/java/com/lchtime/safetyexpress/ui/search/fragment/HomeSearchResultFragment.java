package com.lchtime.safetyexpress.ui.search.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.Text;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.HomeNewAdapter;
import com.lchtime.safetyexpress.adapter.HomeVideosRecommendAdapter;
import com.lchtime.safetyexpress.bean.QJSearchBean;
import com.lchtime.safetyexpress.H5DetailUI;
import com.lchtime.safetyexpress.ui.news.MediaActivity;
import com.lchtime.safetyexpress.ui.search.HomeNewsSearchUI;
import com.lchtime.safetyexpress.ui.search.protocal.SerchProtocal;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * Created by Dreamer on 2017/5/20.
 */

public class HomeSearchResultFragment extends Fragment {

    //搜索新闻
    @ViewInject(R.id.slv_news_search)
    private RecyclerView slv_news_search;
    //视频
    @ViewInject(R.id.slv_video_search)
    private RecyclerView slv_video_search;
    @ViewInject(R.id.llnews_title)
    private LinearLayout llnews_title;
    @ViewInject(R.id.llvideo_title)
    private LinearLayout llvideo_title;

    @ViewInject(R.id.empty)
    private View mEmptyView;
    @ViewInject(R.id.tv_error)
    private TextView mErrorView;

    private HomeNewsSearchUI activity;
    private String mType;
    private String mId;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_search_result, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewUtils.inject(this, view);
        mErrorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (HomeNewsSearchUI) getActivity();
        mType = activity.mType;
        initBasic();
        initData();
    }



    private void initBasic() {
        slv_news_search.setLayoutManager(new LinearLayoutManager(getContext()));
        slv_video_search.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initData() {
        mId = SpTools.getUserId(getActivity());
        //初始化搜索
        getSearch(activity.getKey());

    }

    private SerchProtocal mProtocal;
    private HomeNewAdapter homeNewAdapter;
    private HomeVideosRecommendAdapter homeVideosRecommendAdapter;
    public void getSearch(String key){
        if (mProtocal == null){
            mProtocal = new SerchProtocal();
        }
        mProtocal.getSearchResult(key,mId, Integer.parseInt(mType), mType, new SerchProtocal.NormalListener() {

            @Override
            public void normalResponse(Object response) {
                if (response == null){
                    CommonUtils.toastMessage("服务器数据类型异常");
                    return;
                }
                switch (Integer.parseInt(mType)){
                    case 0:
                        //全局搜索
                        final QJSearchBean bean = (QJSearchBean) response;
                        homeNewAdapter = new HomeNewAdapter(getContext(),bean.news);
                        homeNewAdapter.setNewItemInterface(new HomeNewAdapter.NewsItemInterface() {
                            @Override
                            public void setNewOnItem(int position) {
                                Intent intent = new Intent(getContext(), H5DetailUI.class);
                                intent.putExtra("newsId",bean.news.get(position).cc_id);
                                intent.putExtra("type","news");
                                startActivity(intent);
                            }
                            @Override
                            public void setVideoPlay(String url) {
                                Intent intent = new Intent(getContext(), MediaActivity.class);
                                intent.putExtra("url",url);
                                startActivity(intent);
                            }
                        });
                        slv_news_search.setAdapter(homeNewAdapter);

                        homeVideosRecommendAdapter = new HomeVideosRecommendAdapter(getActivity(),bean.sp_info);
                        slv_video_search.setAdapter(homeVideosRecommendAdapter);
                        if(bean.news == null || bean.news.size() == 0){
                            llnews_title.setVisibility(View.GONE);
                        }else {
                            llnews_title.setVisibility(View.VISIBLE);
                        }
                        if(bean.sp_info == null || bean.sp_info.size() == 0){
                            llvideo_title.setVisibility(View.GONE);
                        }else {
                            llvideo_title.setVisibility(View.VISIBLE);
                        }
                        if(bean.news.size() == 0 &&bean.sp_info.size() == 0){
                            mEmptyView.setVisibility(View.VISIBLE);
                        }else {
                            mEmptyView.setVisibility(View.GONE);
                        }

                        break;

                }
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        JCVideoPlayer.releaseAllVideos();
    }
}
