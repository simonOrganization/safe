package com.lchtime.safetyexpress.ui.search.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.HomeNewsRecommendAdapter;
import com.lchtime.safetyexpress.adapter.NewSearchStringAdapter;
import com.lchtime.safetyexpress.adapter.SearchHistoryAdapter;
import com.lchtime.safetyexpress.bean.WordSerchBean;
import com.lchtime.safetyexpress.ui.search.HomeNewsSearchUI;
import com.lchtime.safetyexpress.ui.search.protocal.SerchProtocal;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lchtime.safetyexpress.views.FlowLayout;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;


/**
 * 词汇界面
 * Created by user on 2017/5/17.
 */

public class HomeSearchContentFragment extends Fragment {

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
    @ViewInject(R.id.slv_news_search_hot)
    FlowLayout slv_news_search_hot;

    public static String LOCAL_CONTENT_KEY = "localSerchWord";
    //private NewSearchStringAdapter historyAdapter;
    //private NewSearchStringAdapter hotAdapter;
    private ArrayList<String> historyList;
    //private ArrayList<String> hotList;
    private SerchProtocal mProtocal;

    //private HomeNewsRecommendAdapter homeNewsRecommendAdapter;

    private SearchHistoryAdapter mHistoryAdapter;

    //private String content;
    private String localContent;
    private String mType;
    private HomeNewsSearchUI activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_news_search_ui, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewUtils.inject(this, view);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initBasic();
        initData();
    }


    private void initBasic() {
        //slv_news_search_history.setSpace(20,20);
        activity = (HomeNewsSearchUI) getActivity();
        mType = activity.mType;

        //初始化历史搜索记录
        localContent = SpTools.getString(activity , LOCAL_CONTENT_KEY + mType);
        historyList = getHistoryList(localContent.split(","));
        mHistoryAdapter = new SearchHistoryAdapter(activity , historyList);
        Log.e("fxp","----------"+mHistoryAdapter.getItemCount());
        slv_news_search_history.setLayoutManager(new LinearLayoutManager(activity));
        slv_news_search_history.setAdapter(mHistoryAdapter);
        mHistoryAdapter.setOnItemClickListener(new SearchHistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, String content) {
                //每一个text进行搜索
                activity.setSearchContent(content);
            }
        });


        /*for (String word : historyList) {
            if (TextUtils.isEmpty(word)){
                continue;
            }
            final TextView tv = new TextView(activity);
            tv.setText(word);
            tv.setPadding(20, 10, 20, 10);
            tv.setGravity(Gravity.CENTER);
            tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
            tv.setBackground(getResources().getDrawable(R.drawable.shape_bg_serch));
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //每一个text进行搜索
                    activity.setSearchContent(tv.getText().toString().trim());
                }
            });
            slv_news_search_history.addView(tv);
        }*/
    }

    private void initData() {
        if (mProtocal == null){
            mProtocal = new SerchProtocal();
        }
        //传0首页热搜词汇
        mProtocal.getHotSerchData(mType, new SerchProtocal.NormalListener() {
            @Override
            public void normalResponse(Object response) {
                WordSerchBean bean = (WordSerchBean) response;
                slv_news_search_hot.setSpace(20,20);
                for (String word:bean.hot) {
                    final TextView tv = new TextView(activity);
                    tv.setText(word);
                    tv.setPadding(20,10,20,10);
                    tv.setGravity(Gravity.CENTER);
                    tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                    tv.setBackground(getResources().getDrawable(R.drawable.shape_bg_serch));
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //每一个text进行搜索
                            activity.setSearchContent(tv.getText().toString().trim());
                        }
                    });
                    slv_news_search_hot.addView(tv);
                }
            }
        });
    }

    public void addLoacalSearchView(String key){
        //先保存
        //去重
        localContent = SpTools.getString(activity , LOCAL_CONTENT_KEY + mType);
        String[] arr = localContent.split(",");
        boolean equal = false;
        for (String s:arr){
            if (key.equals(s)){
                equal = true;
                break;
            }
        }
        //如果没有重复的再保存，并且及时添加到view中
        if (!equal) {
            SpTools.setString(activity, LOCAL_CONTENT_KEY + mType, localContent + "," + key);
            /*final TextView tv = new TextView(activity);
            tv.setText(key);
            tv.setPadding(20,10,20,10);
            tv.setGravity(Gravity.CENTER);
            tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
            tv.setBackground(getResources().getDrawable(R.drawable.shape_bg_serch));
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //每一个text进行搜索
                    activity.setSearchContent(tv.getText().toString().trim());
                }
            });
            slv_news_search_history.addView(tv);*/
            historyList.add(key);
            Log.i("fxp" , "刷新前的历史数据==" + historyList.toString());
            mHistoryAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 清空本地历史记录
     * @param view
     */
    @OnClick(R.id.ll_news_search_clear)
    private void getGiveUp(View view){
        SpTools.setString(activity, LOCAL_CONTENT_KEY + mType, "");
        //content = "";
        localContent = "";
        //slv_news_search_history.removeAllViews();
        historyList.clear();
        mHistoryAdapter.notifyDataSetChanged();
    }

    private ArrayList<String> getHistoryList(String[] arr){
        ArrayList<String> list = new ArrayList<>();
        for (String str : arr){
            if(!str.equals(""))list.add(str);
        }
        return list;
    }

}
