package com.lchtime.safetyexpress.ui.home.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.HomeVideosRecommendAdapter;
import com.lchtime.safetyexpress.bean.NewsBean;
import com.lchtime.safetyexpress.bean.res.VideoRes;
import com.lchtime.safetyexpress.ui.home.protocal.VideoProtocal;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.DialogUtil;
import com.lchtime.safetyexpress.utils.refresh.PullLoadMoreRecyclerView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * 视频- 推荐
 * Created by user on 2017/4/18.
 */

public class VideosRecommendFragment extends BaseHintFragment {

    //列表展示
    @ViewInject(R.id.refreshLayout)
    private PullLoadMoreRecyclerView refreshLayout;
    /*@ViewInject(R.id.home_new_fragment_rc)
    private EmptyRecyclerView home_new_fragment_rc;*/

    private HomeVideosRecommendAdapter homeVideosRecommendAdapter;

    private VideoProtocal protocal = new VideoProtocal();
    public ArrayList<NewsBean> videoList = new ArrayList();;
    private DialogUtil mDialog;
    private boolean isLoaded = false;
    private boolean isFirst = true;
    int footPage = 1;
    int totalpage = 1;
    private String cd_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_videos_recommend_fragment, container, false);
        mDialog = new DialogUtil(getActivity());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewUtils.inject(this, view);

        //home_new_fragment_rc.setLayoutManager(new LinearLayoutManager(getActivity()));
        refreshLayout.setLinearLayout();

        Bundle bundle = getArguments();
        cd_id = bundle.getString("cd_id");

        homeVideosRecommendAdapter = new HomeVideosRecommendAdapter(getActivity(),videoList);
        refreshLayout.setAdapter(homeVideosRecommendAdapter);
        refreshLayout.setFooterViewText("加载中...");
        refreshLayout.setFooterViewBackgroundColor(R.color.white);
        refreshLayout.setPushRefreshEnable(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isFirst = false;
        if(isVisible){
            lazyLoad();
        }
        refreshLayout.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        footPage = 1;
                        videoList.clear();

                        if(!TextUtils.isEmpty(cd_id)){

                            protocal.getVideoList("1", cd_id,new VideoProtocal.VideoListListener() {
                                @Override
                                public void videoListResponse(VideoRes videoRes) {
                                    totalpage = videoRes.totalpage;
                                    videoList.clear();
                                    videoList.addAll(videoRes.cms_context);
                                    homeVideosRecommendAdapter.notifyDataSetChanged();
                                    refreshLayout.setPullLoadMoreCompleted();
                                    footPage++;
                                }

                                @Override
                                public void onError() {
                                    refreshLayout.setPullLoadMoreCompleted();
                                }
                            });
                        }


                    }
                },0);
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(footPage > totalpage){
                            CommonUtils.toastMessage("没有更多了");
                            refreshLayout.setPullLoadMoreCompleted();
                        }else if(!TextUtils.isEmpty(cd_id)){
                                protocal.getVideoList(footPage + "", cd_id,new VideoProtocal.VideoListListener() {
                                    @Override
                                    public void videoListResponse(VideoRes videoRes) {
                                        totalpage = videoRes.totalpage;
                                        videoList.addAll(videoRes.cms_context);
                                        homeVideosRecommendAdapter.notifyDataSetChanged();
                                        refreshLayout.setPullLoadMoreCompleted();
                                        footPage++;
                                    }

                                    @Override
                                    public void onError() {
                                        refreshLayout.setPullLoadMoreCompleted();
                                    }
                                });
                            }
                        //refreshLayout.setPullLoadMoreCompleted();
                    }
                },0);
            }
        });


    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        videoList = null;
    }

    @Override
    protected void lazyLoad() {
        if(!isLoaded && !isFirst){
            isLoaded = true;
            mDialog.show();
            protocal.getVideoList("1" , cd_id , new VideoProtocal.VideoListListener() {
                @Override
                public void videoListResponse(VideoRes videoRes) {
                    totalpage = videoRes.totalpage;
                    footPage++;
                    mDialog.dissmiss();
                    if(videoList != null){
                        videoList.clear();
                    }else{
                        videoList = new ArrayList();
                    }
                    videoList.addAll(videoRes.cms_context);
                    //if(getActivity() != null){
                    homeVideosRecommendAdapter.notifyDataSetChanged();
                    refreshLayout.setPullLoadMoreCompleted();
                    //}
                }

                @Override
                public void onError() {
                    mDialog.dissmiss();
                    refreshLayout.setPullLoadMoreCompleted();
                }
            });
        }
    }

    @Override
    protected void onHide() {
        JCVideoPlayer.releaseAllVideos();
    }
}