package com.lchtime.safetyexpress.ui.home.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.HomeVideosRecommendAdapter;
import com.lchtime.safetyexpress.bean.NewsBean;
import com.lchtime.safetyexpress.bean.res.VideoRes;
import com.lchtime.safetyexpress.ui.home.protocal.VideoProtocal;
import com.lchtime.safetyexpress.utils.refresh.PullLoadMoreRecyclerView;
import com.lchtime.safetyexpress.views.EmptyRecyclerView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 视频- 推荐
 * Created by user on 2017/4/18.
 */

public class VideosRecommendFragment extends Fragment {

    //列表展示
    @ViewInject(R.id.refreshLayout)
    private PullLoadMoreRecyclerView refreshLayout;
    @ViewInject(R.id.home_new_fragment_rc)
    private EmptyRecyclerView home_new_fragment_rc;

    private HomeVideosRecommendAdapter homeVideosRecommendAdapter;

    private VideoProtocal protocal;
    public ArrayList<NewsBean> videoList;
    private int pageIndex = 0;

    int footPage = 0;
    int headPage = 0;
    private String cd_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_videos_recommend_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewUtils.inject(this, view);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        home_new_fragment_rc.setLayoutManager(new LinearLayoutManager(getContext()));
        videoList = new ArrayList<NewsBean>();
        Bundle bundle = getArguments();
        cd_id = bundle.getString("cd_id");
        if (protocal == null){
            protocal = new VideoProtocal();
        }
        protocal.getVideoList("0", cd_id,new VideoProtocal.VideoListListener() {
            @Override
            public void videoListResponse(VideoRes videoRes) {
                videoList = videoRes.cms_context;
                homeVideosRecommendAdapter = new HomeVideosRecommendAdapter(getActivity(),videoList);
                home_new_fragment_rc.setLayoutManager(new LinearLayoutManager(getContext()));
                refreshLayout.setAdapter(homeVideosRecommendAdapter);
            }
        });

        refreshLayout.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        footPage = 0;
                        videoList.clear();

                        if(!TextUtils.isEmpty(cd_id)){

                            protocal.getVideoList("0", cd_id,new VideoProtocal.VideoListListener() {
                                @Override
                                public void videoListResponse(VideoRes videoRes) {
                                    videoList.clear();
                                    videoList.addAll(videoRes.cms_context);
                                    homeVideosRecommendAdapter .notifyDataSetChanged();
                                    refreshLayout.setPullLoadMoreCompleted();
                                }
                            });
                        }


                    }
                },2000);
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //如果是推荐或者是热点
                        footPage++;

                        if(!TextUtils.isEmpty(cd_id)){
                            footPage++;
                            protocal.getVideoList(footPage + "", cd_id,new VideoProtocal.VideoListListener() {
                                @Override
                                public void videoListResponse(VideoRes videoRes) {
                                    videoList.addAll(videoRes.cms_context);
                                    homeVideosRecommendAdapter .notifyDataSetChanged();
                                }
                            });
                        }
                        refreshLayout.setPullLoadMoreCompleted();
                    }
                },2000);
            }
        });


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        videoList = null;
        headPage = 0;
        footPage = 0;
    }

}