package com.lchtime.safetyexpress.ui.home;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

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
    @ViewInject(R.id.slv_news_search_record)
    private ListView slv_news_search_record;
    //清除搜索历史
    @ViewInject(R.id.ll_news_search_clear)
    private LinearLayout ll_news_search_clear;
    //列表展示
    @ViewInject(R.id.lv_news_search)
    private ListView lv_news_search;

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

    }

    @Override
    protected void prepareData() {

    }

}
