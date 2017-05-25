package com.lchtime.safetyexpress.ui.home;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.pop.SharePop;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.Const;
import com.lchtime.safetyexpress.ui.circle.SingleInfoUI;
import com.lchtime.safetyexpress.ui.search.HomeNewsSearchUI;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 新闻中心
 * Created by user on 2017/4/17.
 */
@ContentView(R.layout.home_news_detail_ui)
public class HomeNewsDetailUI extends BaseUI {

    //分享
    @ViewInject(R.id.ll_right)
    private LinearLayout ll_right;
    //评论数
    @ViewInject(R.id.tv_news_detail_comment)
    private TextView tv_news_detail_comment;
    @ViewInject(R.id.home_news_detailed_web)
    private WebView home_news_detailed_web;

    private SharePop sharePop;
    private String cc_id;
    //类型，是新闻还是视频
    private String type;
    private String baseUrl = "";

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        String ub_id = SpTools.getString(this, Constants.userId,"");
        cc_id = getIntent().getStringExtra("newsId");
        type = getIntent().getStringExtra("type");

        if ("news".equals(type)){
            baseUrl = Const.HOST+"cms/pagenews?cc_id=" + cc_id;
            setTitle("新闻中心");
        }else if ("video".equals(type)){
            baseUrl = Const.HOST+"cms/videoinfo?cc_id=" + cc_id;
            setTitle("视频中心");
        }
        if (!TextUtils.isEmpty(ub_id)){
            baseUrl = baseUrl + "&ub_id=" +ub_id;
        }
        rightVisible(R.drawable.news_share);
        sharePop = new SharePop(ll_right, HomeNewsDetailUI.this, R.layout.pop_share);
        init();
    }



    @Override
    protected void prepareData() {

    }
    private void init(){
        //WebView加载web资源


        home_news_detailed_web.loadUrl(baseUrl);
        //启用支持javascript
        WebSettings settings = home_news_detailed_web.getSettings();
        settings.setJavaScriptEnabled(true);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        home_news_detailed_web.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                //view.loadUrl(url);
                return true;
            }
        });

        home_news_detailed_web.addJavascriptInterface(this,"android");
    }

//搜索
    @JavascriptInterface
    public void getKeyWord(final String text){
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(HomeNewsDetailUI.this,HomeNewsSearchUI.class);
                intent.putExtra("content",text);
                startActivity(intent);

            }
        });
    }

    @JavascriptInterface
    public void getUserId(final String ub_id){
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(HomeNewsDetailUI.this,SingleInfoUI.class);
                intent.putExtra("uid",ub_id);
                startActivity(intent);

            }
        });
    }
    /**
     * 分享
     *
     * @param view
     */
    @OnClick(R.id.ll_right)
    private void getShare(View view) {
        sharePop.showAtLocation();
        sharePop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.ll_share_weixin:
                        sharePop.dismiss();
                        break;
                    case R.id.ll_share_friend:
                        sharePop.dismiss();
                        break;
                    case R.id.ll_share_sina:
                        sharePop.dismiss();
                        break;
                    case R.id.ll_share_qq:
                        sharePop.dismiss();
                        break;
                }
            }
        });
    }

    /**
     * 评论
     *
     * @param view
     */
    @OnClick(R.id.rl_news_detail_comment)
    private void getComment(View view) {
        makeText("评论");
    }

    /**
     * 顶
     *
     * @param view
     */
    @OnClick(R.id.ll_news_detail_zan)
    private void getZan(View view) {
        makeText("顶");
    }

    /**
     * 踩
     *
     * @param view
     */
    @OnClick(R.id.ll_news_detail_cai)
    private void getCai(View view) {
        makeText("踩");
    }

    /**
     * 收藏
     *
     * @param view
     */
    @OnClick(R.id.ll_news_detail_collect)
    private void getCollect(View view) {
        makeText("收藏");
    }

}
