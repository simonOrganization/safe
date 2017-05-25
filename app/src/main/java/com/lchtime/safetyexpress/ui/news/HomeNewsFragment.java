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
import com.lchtime.safetyexpress.ui.vip.fragment.*;
import com.lchtime.safetyexpress.ui.vip.fragment.BaseFragment;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.JsonUtils;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lchtime.safetyexpress.utils.refresh.PullLoadMoreRecyclerView;
import com.lchtime.safetyexpress.views.EmptyRecyclerView;
import com.lchtime.safetyexpress.views.LoadingPager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by yxn on 2017/4/25.
 */

public class HomeNewsFragment extends BaseFragment {
    @BindView(R.id.home_new_fragment_rc)
    EmptyRecyclerView home_new_fragment_rc;

    @BindView(R.id.refreshLayout)
    PullLoadMoreRecyclerView refreshLayout;
    private Context context;
    private HomeNewAdapter homeNewAdapter;
    private String type_id;
    private ArrayList<NewsBean> commentList;
    private View view;
    private Handler handler = new Handler();
    //    private View view = null;
//    private boolean IS_LOADED = false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        view = View.inflate(getContext(), R.layout.home_new_fragment,null);
        ButterKnife.bind(this, view);
        commentList = new ArrayList<>();
        homeNewAdapter = new HomeNewAdapter(context,commentList);
        home_new_fragment_rc.setAdapter(homeNewAdapter);
    }

    int footPage = 0;
    int headPage = 0;
    @Override
    protected View initSuccessView() {
        homeNewAdapter.notifyDataSetChanged();
        return view;
    }

    LoadingPager.LoadedResult currentResult = LoadingPager.LoadedResult.ERRO;
    @Override
    public LoadingPager.LoadedResult initData() {
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
                footPage = 0;
                if (commentList == null) {
                    commentList = new ArrayList<NewsBean>();
                }
                commentList.clear();
                if (position == 0 || position == 1){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            initPosition1to2(position + 1 + "",Const.NEW_TYPE,footPage + "");
                        }
                    }).start();

                }else{
                    if(!TextUtils.isEmpty(type_id)){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                getNewsList(type_id, Const.NEW_LIST,footPage + "");
                            }
                        }).start();

                    }
                }
            }


            @Override
            public void onLoadMore() {

                //如果是推荐或者是热点
                footPage++;
                if (position == 0 || position == 1){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            initPosition1to2(position + 1 + "",Const.NEW_TYPE,footPage + "");
                        }
                    }).start();

                }else{
                    if(!TextUtils.isEmpty(type_id)){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                getNewsList(type_id, Const.NEW_LIST,footPage + "");
                            }
                        }).start();

                    }
                }

            }
        });

        return currentResult;
    }

    int totalPage = 1;
    private void initPosition1to2(final String type, String url,String index) {
        String ub_id = SpTools.getString(getContext(),Constants.userId,"");
        if (Integer.parseInt(index) + 1 > totalPage){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    refreshLayout.setPullLoadMoreCompleted();
                }
            });
            return;
        }
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    refreshLayout.setPullLoadMoreCompleted();
                    CommonUtils.toastMessage("您当前无网络，请联网再试");
                }
            });

            currentResult = LoadingPager.LoadedResult.ERRO;
            return;
        }

        Response response = null;
        try {

            PostFormBuilder builder = OkHttpUtils
                    .post()
                    .url(url)
                    .addParams("type",type)
                    .addParams("page",index);
            if (!TextUtils.isEmpty(ub_id)){
                builder.addParams("ub_id",ub_id);
            }
            response = builder .build().execute();
            String myResponse = response.body().string();

            final NewsRes newsRes = (NewsRes) JsonUtils.stringToObject(myResponse,NewsRes.class);
            totalPage = newsRes.totalpage;
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
                if (commentList.size() == 0){
                    currentResult = LoadingPager.LoadedResult.EMPTY;
                    return ;
                }

                homeNewAdapter.setNewItemInterface(new HomeNewAdapter.NewsItemInterface() {
                    @Override
                    public void setNewOnItem(final int position) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(context, HomeNewsDetailUI.class);
                                intent.putExtra("newsId",commentList.get(position).cc_id);
                                intent.putExtra("type","news");
                                startActivity(intent);
                            }
                        });

                    }

                    @Override
                    public void setVideoPlay(final String url) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(context, MediaActivity.class);
                                intent.putExtra("url",url);
                                startActivity(intent);
                            }
                        });

                    }
                });



            }else{
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context,newsRes.getResult().getInfo(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    homeNewAdapter.notifyDataSetChanged();
                    refreshLayout.setPullLoadMoreCompleted();
                }
            });
            currentResult = LoadingPager.LoadedResult.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    refreshLayout.setPullLoadMoreCompleted();
                    Toast.makeText(context,"网络不稳定，请检查后重试",Toast.LENGTH_SHORT).show();
                }
            });
            currentResult = LoadingPager.LoadedResult.ERRO;

        }



    }

    private void getNewsList(String cid,String url,String index){
        String ub_id = SpTools.getString(getContext(),Constants.userId,"");
        if (Integer.parseInt(index) + 1 > totalPage){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    refreshLayout.setPullLoadMoreCompleted();
                }
            });
            return;
        }
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    refreshLayout.setPullLoadMoreCompleted();
                    CommonUtils.toastMessage("您当前无网络，请联网再试");
                }
            });
            currentResult = LoadingPager.LoadedResult.ERRO;
            return;
        }

        Response response = null;
        try {


            PostFormBuilder builder = OkHttpUtils
                    .post()
                    .url(url)
                    .addParams("cd_id",cid)
                    .addParams("page",index);
            if (!TextUtils.isEmpty(ub_id)){
                builder.addParams("ub_id",ub_id);
            }
            response = builder .build().execute();
            String myResponse = response.body().string();
            Log.i("cui","getNewsList=="+response);
            final NewsListRes newsListRes = (NewsListRes) JsonUtils.stringToObject(myResponse,NewsListRes.class);
            totalPage = newsListRes.totalpage;
            if(newsListRes.getResult().getCode().equals("10")){
//                            if(commentList!=null){
//                                commentList.clear();
//                                commentList = null;
//                                commentList = new ArrayList<NewsBean>();
//                            }
                commentList.addAll(newsListRes.getCms_context());
                if (commentList.size() == 0){
                    currentResult = LoadingPager.LoadedResult.EMPTY;
                    return ;
                }
                homeNewAdapter.setNewItemInterface(new HomeNewAdapter.NewsItemInterface() {
                    @Override
                    public void setNewOnItem(final int position) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(context, HomeNewsDetailUI.class);
                                intent.putExtra("newsId",commentList.get(position).cc_id);
                                intent.putExtra("type","news");
                                startActivity(intent);
                            }
                        });

                    }

                    @Override
                    public void setVideoPlay(final String url) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(context, MediaActivity.class);
                                intent.putExtra("url",url);
                                startActivity(intent);
                            }
                        });

                    }
                });


            }else {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, newsListRes.getResult().getInfo(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    homeNewAdapter.notifyDataSetChanged();
                    refreshLayout.setPullLoadMoreCompleted();
                }
            });
            currentResult = LoadingPager.LoadedResult.SUCCESS;

        } catch (IOException e) {
            e.printStackTrace();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    refreshLayout.setPullLoadMoreCompleted();
                    Toast.makeText(context,"网络不稳定，请检查后重试",Toast.LENGTH_SHORT).show();
                }
            });
            currentResult = LoadingPager.LoadedResult.ERRO;

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        commentList = null;
        headPage = 0;
        footPage = 0;
    }

}
