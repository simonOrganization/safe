package com.lchtime.safetyexpress.ui.news;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.ui.BaseUI;

/**
 * Created by yxn on 2017/4/25.
 */

public class HomeNewActivity extends BaseUI {
    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        //布局设置
        setTitle("新闻中心");
        rightVisible(R.drawable.news_search);
        setContentView(R.layout.activity_home_news);


    }

    @Override
    protected void prepareData() {
        //准备数据

    }
}
