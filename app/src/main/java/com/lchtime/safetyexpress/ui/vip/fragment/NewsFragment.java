package com.lchtime.safetyexpress.ui.vip.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.lchtime.safetyexpress.H5DetailUI;
import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.HomeNewAdapter;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.NewsBean;
import com.lchtime.safetyexpress.bean.res.NewsListRes;
import com.lchtime.safetyexpress.ui.news.MediaActivity;
import com.lchtime.safetyexpress.ui.vip.MyConllected;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.JsonUtils;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lchtime.safetyexpress.views.EmptyRecyclerView;
import com.lchtime.safetyexpress.views.LoadingPager;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * Created by android-cp on 2017/4/21.
 */

public class NewsFragment extends BaseFragment {
    public HomeNewAdapter homeNewAdapter;

    private ArrayList<NewsBean> commentList;

    private LoadingPager.LoadedResult loadedResult;
    private EmptyRecyclerView recyclerView;
    private boolean isEdit = false;

    @Override
    protected View initSuccessView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_news_conllect , null);
        recyclerView = (EmptyRecyclerView) view.findViewById(R.id.recyclerView);
        //recyclerView = new EmptyRecyclerView(MyApplication.getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(homeNewAdapter);

        return view;
    }

    @Override
    public LoadingPager.LoadedResult initData() {


        getNewsList("","");
        if (loadedResult == null) {
            loadedResult = checkResult(commentList);
        }


        return loadedResult;
    }


    //拿到新闻列表

    private void getNewsList(String type,String del_ids){
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
                    .addParams("module", 0 + "")
                    .addParams("ub_id", SpTools.getUserId(getContext()))
                    .addParams("type", type)
                    .addParams("del_ids", del_ids)
                    .build()
                    .execute();


            String myResponse = response.body().string();
            NewsListRes newsListRes = (NewsListRes) JsonUtils.stringToObject(myResponse,NewsListRes.class);
            if(newsListRes.getResult().getCode().equals("10")){
                if(commentList == null){
                    commentList = new ArrayList<NewsBean>();
                }
                commentList.clear();
                commentList.addAll(newsListRes.getCms_context());

                homeNewAdapter = new HomeNewAdapter(getContext(), commentList);

                homeNewAdapter.setNewItemInterface(new HomeNewAdapter.NewsItemInterface() {
                    @Override
                    public void setNewOnItem(int position) {
                        if(isEdit){ //如果是正在编辑状态

                        }else{
                            Intent intent = new Intent(getContext(), H5DetailUI.class);
                            intent.putExtra("newsId",commentList.get(position).cc_id);
                            intent.putExtra("type","news");
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void setVideoPlay(String url) {
                        Intent intent = new Intent(getContext(), MediaActivity.class);
                        intent.putExtra("url",url);
                        startActivity(intent);
                       /* //视频
                        //Intent intent = new Intent(HomeUI.this, VideoH5Activity.class);
                        intent.putExtra("newsId", vedioNewsList.get(position).cc_id);
                        intent.putExtra("type","video");
                        intent.putExtra("videoUrl", vedioNewsList.get(position).media.get(1));
                        startActivity(intent);*/
                    }
                });

//                if (recyclerView == null){
//                    recyclerView = new EmptyRecyclerView(MyApplication.getContext());
//                    recyclerView.setAdapter(homeNewAdapter);
//                }
                homeNewAdapter.notifyDataSetChanged();
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
        isEdit = isDelete;
        if(homeNewAdapter != null){
            homeNewAdapter.setCheckBoxShow(this,isDelete);
            homeNewAdapter.notifyDataSetChanged();
        }
    }

    public void updataDeleteNum(int num){
        ((MyConllected)getActivity()).setDeleteNum(num);
    }


    public List<NewsBean> getDeleteList(){
        return homeNewAdapter.getUpdataList();
    }

    public void updataDeleteList(){
        List<NewsBean> deleteList = getDeleteList();
        for (NewsBean bean: deleteList){
            commentList.remove(bean);
        }
        deleteList.clear();
        homeNewAdapter.notifyDataSetChanged();
    }
}
