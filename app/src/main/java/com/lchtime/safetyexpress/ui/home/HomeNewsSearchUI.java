package com.lchtime.safetyexpress.ui.home;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.NewSearchStringAdapter;
import com.lchtime.safetyexpress.bean.res.SearchRes;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.Const;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.JsonUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;

import okhttp3.Call;

/**
 * 新闻 搜索
 * Created by user on 2017/4/17.
 */
@ContentView(R.layout.home_news_search_ui)
public class HomeNewsSearchUI extends BaseUI {

    //取消
    @ViewInject(R.id.tv_news_search_cancel)
    private TextView tv_news_search_cancel;
    //编辑框
    @ViewInject(R.id.et_news_search)
    private EditText et_news_search;
    //ScrollView
    @ViewInject(R.id.sv_news_search)
    private ScrollView sv_news_search;
    //历史记录
    @ViewInject(R.id.tv_news_search_tip)
    private TextView tv_home_search_tip;
    //历史记录展示
    @ViewInject(R.id.slv_news_search_history)
    private RecyclerView slv_news_search_history;
    //清除搜索历史
    @ViewInject(R.id.ll_news_search_clear)
    private LinearLayout ll_news_search_clear;
    //列表展示
    @ViewInject(R.id.lv_news_search)
    private ListView lv_news_search;
    @ViewInject(R.id.slv_news_search_hot)
    RecyclerView slv_news_search_hot;
    private NewSearchStringAdapter historyAdapter;
    private NewSearchStringAdapter hotAdapter;
    private ArrayList<String> historyList;
    private ArrayList<String> hotList;

    //搜索内容
    private String content;

    @Override
    protected void back() {
        finish();
    }

    /**
     * 取消
     * @param view
     */
    @OnClick(R.id.tv_news_search_cancel)
    private void getCancel(View view){
        finish();
    }

    @Override
    protected void setControlBasis() {
        setStatus2();
        slv_news_search_history.setLayoutManager(new LinearLayoutManager(HomeNewsSearchUI.this));
        slv_news_search_hot.setLayoutManager(new LinearLayoutManager(HomeNewsSearchUI.this));
    }

    @Override
    protected void prepareData() {
        getDefaultData();
    }
    private void getDefaultData(){
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            return;
        }
        OkHttpUtils
                .post()
                .url(Const.NEW_SEARCH)
                .addParams("search","")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(HomeNewsSearchUI.this, "网络不稳定，请检查后重试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        SearchRes searchRes = (SearchRes) JsonUtils.stringToObject(response,SearchRes.class);
                        if(searchRes.getResult().getCode().equals("10")){
                            if(historyAdapter == null){
                                historyList= searchRes.getHistory();
                                historyAdapter = new NewSearchStringAdapter(historyList,HomeNewsSearchUI.this);
                                slv_news_search_history.setAdapter(historyAdapter);
                                hotList = searchRes.getHot();
                                hotAdapter = new NewSearchStringAdapter(hotList,HomeNewsSearchUI.this);
                                slv_news_search_hot.setAdapter(hotAdapter);

                            }else{
                                if(historyList!=null){
                                    historyList.clear();
                                    historyList = null;
                                }
                                historyList = searchRes.getHistory();
                                historyAdapter.notifyDataSetChanged();

                            }


                        }else{
                            Toast.makeText(HomeNewsSearchUI.this, searchRes.getResult().getInfo(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
