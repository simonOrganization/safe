package com.lchtime.safetyexpress.ui.home;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.search.HomeNewsSearchUI;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 视频详情界面
 * Created by user on 2017/4/18.
 */
@ContentView(R.layout.home_videos_detail_ui)
public class HomeVideosDeatilUI extends BaseUI {

    //评论数
    @ViewInject(R.id.tv_videos_detail_comment)
    private TextView tv_videos_detail_comment;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        setTitle("视频");
        rightVisible(R.drawable.news_search);
    }

    @Override
    protected void prepareData() {

    }

    /**
     * 搜索
     *
     * @param view
     */
    @OnClick(R.id.ll_right)
    private void getSearch(View view) {
        Intent intent = new Intent(HomeVideosDeatilUI.this, HomeNewsSearchUI.class);
        startActivity(intent);
    }

    /**
     * 评论
     *
     * @param view
     */
    @OnClick(R.id.rl_videos_detail_comment)
    private void getComment(View view) {
        makeText("评论");
    }

    /**
     * 顶
     *
     * @param view
     */
    @OnClick(R.id.ll_videos_detail_zan)
    private void getZan(View view) {
        makeText("顶");
    }

    /**
     * 踩
     *
     * @param view
     */
    @OnClick(R.id.ll_videos_detail_cai)
    private void getCai(View view) {
        makeText("踩");
    }

    /**
     * 收藏
     *
     * @param view
     */
    @OnClick(R.id.ll_videos_detail_collect)
    private void getCollect(View view) {
        makeText("收藏");
    }
}