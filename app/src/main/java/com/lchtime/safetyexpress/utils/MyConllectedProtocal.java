package com.lchtime.safetyexpress.utils;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.HomeNewAdapter;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.NewsBean;
import com.lchtime.safetyexpress.bean.res.NewsListRes;
import com.lchtime.safetyexpress.ui.home.HomeNewsDetailUI;
import com.lchtime.safetyexpress.ui.news.MediaActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;

import okhttp3.Call;

/**
 * Created by android-cp on 2017/5/4.
 */

public class MyConllectedProtocal {

    public static void requestDelete(String type,String del_ids){
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            CommonUtils.toastMessage("请检查网络");
            return;
        }

        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.attention));
        OkHttpUtils
                .post()
                .url(url)
                .addParams("module", 0 + "")
                .addParams("ub_id", SpTools.getString(MyApplication.getContext(), Constants.userId, ""))
                .addParams("type", type)
                .addParams("del_ids", del_ids)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        NewsListRes newsListRes = (NewsListRes) JsonUtils.stringToObject(myResponse,NewsListRes.class);
                        if(newsListRes.getResult().getCode().equals("10")){
                            if(commentList!=null){
                                commentList.clear();
                                commentList = null;
                                commentList = new ArrayList<NewsBean>();
                            }
                            Log.i("yang","onResponse===="+newsListRes.getCms_context().size());
                            commentList = newsListRes.getCms_context();
                            Log.i("yang","onResponse===="+commentList.size());
                            if (homeNewAdapter == null) {
                                homeNewAdapter = new HomeNewAdapter(getContext(), commentList);
                            }
                            homeNewAdapter.setNewItemInterface(new HomeNewAdapter.NewsItemInterface() {
                                @Override
                                public void setNewOnItem(int position) {
                                    Intent intent = new Intent(getContext(), HomeNewsDetailUI.class);
                                    intent.putExtra("newsId","");
                                    startActivity(intent);
                                }

                                @Override
                                public void setVideoPlay(String url) {
                                    Intent intent = new Intent(getContext(), MediaActivity.class);
                                    intent.putExtra("url",url);
                                    startActivity(intent);
                                }
                            });

//                if (recyclerView == null){
//                    recyclerView = new EmptyRecyclerView(MyApplication.getContext());
//                    recyclerView.setAdapter(homeNewAdapter);
//                }
                            homeNewAdapter.notifyDataSetChanged();
                            // home_new_fragment_rc.setAdapter(homeNewAdapter);
                        }else{
                            Toast.makeText(getContext(),newsListRes.getResult().getInfo(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
