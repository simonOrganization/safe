package com.lchtime.safetyexpress.ui.vip.fragment;

import android.view.View;
import android.widget.ListView;

import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.MyConllectedVideoAdapter;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.NewsBean;
import com.lchtime.safetyexpress.bean.res.NewsListRes;
import com.lchtime.safetyexpress.ui.vip.MyConllected;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.JsonUtils;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lchtime.safetyexpress.views.LoadingPager;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import okhttp3.Response;

/**
 * Created by android-cp on 2017/4/21.
 */

public class VedioFragment extends BaseFragment {

    private MyConllectedVideoAdapter adapter;

    private LoadingPager.LoadedResult loadedResult;

    private ArrayList<NewsBean> videoList;

    @Override
    protected View initSuccessView() {
        ListView listView = new ListView(MyApplication.getContext());
        listView.setAdapter(adapter);
        return listView;
    }

    @Override
    public LoadingPager.LoadedResult initData() {

            getVideoList("9","");

        if (loadedResult == null) {
            loadedResult = checkResult(videoList);
        }
        return loadedResult;
    }

    //拿到视频列表

    private void getVideoList(String type,String del_ids){
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            loadedResult = LoadingPager.LoadedResult.ERRO;
            return;
        }

        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.attention));
        try {
            Response response = OkHttpUtils
                    .post()
                    .url(url)
                    .addParams("module", type)
                    .addParams("ub_id", SpTools.getUserId(getContext()))
                    .addParams("type", type)
                    .addParams("del_ids", del_ids)
                    .build()
                    .execute();

            String myResponse = response.body().string();
            NewsListRes videoListRes = (NewsListRes) JsonUtils.stringToObject(myResponse,NewsListRes.class);
            if(videoListRes.getResult().getCode().equals("10")){
                if(videoList!=null){
                    videoList.clear();
                    videoList = null;
                    videoList = new ArrayList<NewsBean>();
                }
                videoList = videoListRes.getCms_context();

                adapter = new MyConllectedVideoAdapter(getContext(),this, videoList);

//                if (recyclerView == null){
//                    recyclerView = new EmptyRecyclerView(MyApplication.getContext());
//                    recyclerView.setAdapter(homeNewAdapter);
//                }
                adapter.notifyDataSetChanged();
                // home_new_fragment_rc.setAdapter(homeNewAdapter);
            }else{
                loadedResult = LoadingPager.LoadedResult.ERRO;
            }
        } catch (Exception e) {
            e.printStackTrace();
            loadedResult = LoadingPager.LoadedResult.ERRO;
        }
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

    public List<NewsBean> getDeleteList(){
        return adapter.getUpdataList();
    }

    public void updataDeleteList(){
        List<NewsBean> deleteList = getDeleteList();
        for (NewsBean bean: deleteList){
            videoList.remove(bean);
        }
        deleteList.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }
}
