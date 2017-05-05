package com.lchtime.safetyexpress.ui.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.HomeNewAdapter;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.NewsBean;
import com.lchtime.safetyexpress.bean.res.NewsListRes;
import com.lchtime.safetyexpress.bean.res.NewsRes;
import com.lchtime.safetyexpress.ui.Const;
import com.lchtime.safetyexpress.ui.home.HomeNewsDetailUI;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.JsonUtils;
import com.lchtime.safetyexpress.utils.refresh.PullLoadMoreRecyclerView;
import com.lchtime.safetyexpress.views.EmptyRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by yxn on 2017/4/25.
 */

public class HomeNewsFragment extends Fragment {
    @BindView(R.id.home_new_fragment_rc)
    EmptyRecyclerView home_new_fragment_rc;

    @BindView(R.id.refreshLayout)
    PullLoadMoreRecyclerView refreshLayout;
    private Context context;
    private HomeNewAdapter homeNewAdapter;
    private String type_id;
    private ArrayList<NewsBean> commentList;
//    private View view = null;
//    private boolean IS_LOADED = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    int footPage = 0;
    int headPage = 0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_new_fragment,container,false);
        ButterKnife.bind(this,view);
        commentList = new ArrayList<>();
        homeNewAdapter = new HomeNewAdapter(context,commentList);
        home_new_fragment_rc.setAdapter(homeNewAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        home_new_fragment_rc.setLayoutManager(new LinearLayoutManager(context));

        Bundle bundle = getArguments();
        final int position = bundle.getInt("position");
        if(position == 0){
            Log.i("yang","position == 0");
//            commentList = bundle.getParcelableArrayList("comments");
            //推荐
            initPosition1to2("1", Const.NEW_TYPE,0 + "");
        }else if(position == 1){
            //热点追踪
            initPosition1to2("2",Const.NEW_TYPE,0 + "");
        }else{
            Log.i("yang","else   ===");
            type_id = bundle.getString("typeId");
            if(!TextUtils.isEmpty(type_id)){
                getNewsList(type_id, Const.NEW_LIST,0 + "");
                Log.i("yang","isEmpty===="+type_id);
            }

        }

        refreshLayout.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        footPage = 0;
                        commentList = new ArrayList<NewsBean>();
                        if (position == 0 || position == 1){
                            initPosition1to2(position + 1 + "",Const.NEW_TYPE,footPage + "");
                        }else{
                            if(!TextUtils.isEmpty(type_id)){
                                getNewsList(type_id, Const.NEW_LIST,footPage + "");
                            }
                        }
                        refreshLayout.setPullLoadMoreCompleted();
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
                        if (position == 0 || position == 1){
                            initPosition1to2(position + 1 + "",Const.NEW_TYPE,footPage + "");
                        }else{
                            if(!TextUtils.isEmpty(type_id)){
                                getNewsList(type_id, Const.NEW_LIST,footPage + "");
                            }
                        }
                        refreshLayout.setPullLoadMoreCompleted();
                    }
                },2000);
            }
        });


    }

    private void initPosition1to2(final String type, String url,String index) {
//        commentList = (ArrayList<NewsBean>) bundle.getSerializable(type);
//        homeNewAdapter = new HomeNewAdapter(context,commentList);
//        homeNewAdapter.setNewItemInterface(new HomeNewAdapter.NewsItemInterface() {
//            @Override
//            public void setNewOnItem(int position) {
//                Intent intent = new Intent(context, HomeNewsDetailUI.class);
//                intent.putExtra("newsId","");
//                startActivity(intent);
//            }
//
//            @Override
//            public void setVideoPlay(String url) {
//                Intent intent = new Intent(context, MediaActivity.class);
//                intent.putExtra("url",url);
//                startActivity(intent);
//            }
//        });
//        home_new_fragment_rc.setAdapter(homeNewAdapter);

        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            return;
        }
        OkHttpUtils
                .post()
                .url(url)
                .addParams("type",type)
                .addParams("page",index)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(context,"网络不稳定，请检查后重试",Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        NewsRes newsRes = (NewsRes) JsonUtils.stringToObject(response,NewsRes.class);
                        if(newsRes.getResult().getCode().equals("10")){
//                            if(commentList!=null){
//                                commentList.clear();
//                                commentList = null;
//                                commentList = new ArrayList<NewsBean>();
//                            }
                            if ("1".equals(type)) {
                                commentList.addAll(newsRes.tj);
                            }else {
                                commentList.addAll(newsRes.hot);
                            }


                            homeNewAdapter.setNewItemInterface(new HomeNewAdapter.NewsItemInterface() {
                                @Override
                                public void setNewOnItem(int position) {
                                    Intent intent = new Intent(context, HomeNewsDetailUI.class);
                                    intent.putExtra("newsId","");
                                    startActivity(intent);
                                }

                                @Override
                                public void setVideoPlay(String url) {
                                    Intent intent = new Intent(context, MediaActivity.class);
                                    intent.putExtra("url",url);
                                    startActivity(intent);
                                }
                            });

                            homeNewAdapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(context,newsRes.getResult().getInfo(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }

    private void getNewsList(String cid,String url,String index){
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            return;
        }
        OkHttpUtils
                .post()
                .url(url)
                .addParams("cd_id",cid)
                .addParams("page",index)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(context,"网络不稳定，请检查后重试",Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        Log.i("cui","getNewsList=="+response);
                        NewsListRes newsListRes = (NewsListRes) JsonUtils.stringToObject(response,NewsListRes.class);
                        if(newsListRes.getResult().getCode().equals("10")){
//                            if(commentList!=null){
//                                commentList.clear();
//                                commentList = null;
//                                commentList = new ArrayList<NewsBean>();
//                            }
                            commentList.addAll(newsListRes.getCms_context());
                            homeNewAdapter.setNewItemInterface(new HomeNewAdapter.NewsItemInterface() {
                                @Override
                                public void setNewOnItem(int position) {
                                    Intent intent = new Intent(context, HomeNewsDetailUI.class);
                                    intent.putExtra("newsId","");
                                    startActivity(intent);
                                }

                                @Override
                                public void setVideoPlay(String url) {
                                    Intent intent = new Intent(context, MediaActivity.class);
                                    intent.putExtra("url",url);
                                    startActivity(intent);
                                }
                            });
                            homeNewAdapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(context,newsListRes.getResult().getInfo(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        commentList = null;
        headPage = 0;
        footPage = 0;
    }
}
