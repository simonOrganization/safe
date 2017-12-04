package com.lchtime.safetyexpress;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.cloud.media.player.IMediaPlayer;
import com.google.gson.Gson;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.H5Bean;
import com.lchtime.safetyexpress.bean.H5BottomBean;
import com.lchtime.safetyexpress.bean.InitInfo;
import com.lchtime.safetyexpress.bean.Result;
import com.lchtime.safetyexpress.pop.SharePop;
import com.lchtime.safetyexpress.shareapi.ShareConstants;
import com.lchtime.safetyexpress.shareapi.Util;
import com.lchtime.safetyexpress.shareapi.qq.ShareQQ;
import com.lchtime.safetyexpress.shareapi.wx.ShareWX;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.CallBackActivity;
import com.lchtime.safetyexpress.ui.Const;
import com.lchtime.safetyexpress.ui.circle.SingleInfoUI;
import com.lchtime.safetyexpress.ui.home.AnswerQuestionActivity;
import com.lchtime.safetyexpress.ui.home.HomeUI;
import com.lchtime.safetyexpress.ui.home.protocal.H5Protocal;
import com.lchtime.safetyexpress.ui.home.protocal.ShareProtocal;
import com.lchtime.safetyexpress.ui.login.LoginUI;
import com.lchtime.safetyexpress.ui.search.HomeNewsSearchUI;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.DialogUtil;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lchtime.safetyexpress.weight.LoginDialog;
import com.lchtime.safetyexpress.weight.bar.SimpleMediaController;
import com.lchtime.safetyexpress.weight.bd_videoplayer.BDCloudVideoView;
import com.lchtime.safetyexpress.weight.popview.FullScreenUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Timer;
import java.util.TimerTask;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

import static android.R.attr.action;

/**
 * Created by android-cp on 2017/7/6.
 * 上面为固定视频，下方为h5 的界面
 */
@ContentView(R.layout.activity_video_h5)
public class VideoH5Activity extends BaseUI implements IWeiboHandler.Response,
                                                        IMediaPlayer.OnPreparedListener,
                                                        IMediaPlayer.OnCompletionListener,
                                                        IMediaPlayer.OnErrorListener,
                                                        IMediaPlayer.OnInfoListener,
                                                        IMediaPlayer.OnBufferingUpdateListener,
                                                        BDCloudVideoView.OnPlayerStateListener {

    //分享
    @ViewInject(R.id.iv_right)
    ImageView ll_right;
    //返回
    @ViewInject(R.id.iv_back)
    ImageView iv_back;

    @ViewInject(R.id.home_news_detailed_web)
    WebView mWebView;

    @ViewInject(R.id.bottom_zan_or_common)
    LinearLayout bottom_zan_or_common;


    //评论数
    @ViewInject(R.id.tv_news_detail_comment)
    TextView tv_news_detail_comment;
    //点赞个数
    @ViewInject(R.id.tv_news_detail_zan)
    TextView tv_news_detail_zan;
    //反对个数
    @ViewInject(R.id.tv_news_detail_fd)
    TextView tv_news_detail_fd;


    @ViewInject(R.id.cb_news_detail_zan)
    CheckBox cb_news_detail_zan;
    @ViewInject(R.id.cb_news_detail_cai)
    CheckBox cb_news_detail_cai;
    @ViewInject(R.id.cb_news_detail_collect)
    CheckBox cb_news_detail_collect;
    @ViewInject(R.id.rl_pl)
    RelativeLayout rl_pl;
    @ViewInject(R.id.ll_collect)
    LinearLayout ll_collect;
    @ViewInject(R.id.et_common)
    EditText et_common;

    /*@ViewInject(R.id.loading)
    View mLoadingView;*/
    @ViewInject(R.id.empty)
    RelativeLayout empty;
    @ViewInject(R.id.error)
    RelativeLayout error;
    @ViewInject(R.id.success)
    RelativeLayout success;

    @ViewInject(R.id.fl_root)
    FrameLayout mFrameLayout; //根布局
    /*@ViewInject(R.id.iv_recommend_img)
    JCVideoPlayerStandard mVideoPaler;*/
    @ViewInject(R.id.rl_videoplayer)
    RelativeLayout mVideoRl;
    @ViewInject(R.id.media_controller_bar)
    SimpleMediaController mediaController;//播放控制器
    /*@ViewInject(R.id.ibtn_screen_control)
    ImageView mScreenIv; //全屏按钮*/
    private BDCloudVideoView mVideoPlayer = null;

    private SharePop sharePop;
    private String cc_id;
    //类型，是新闻还是视频
    private String type;
    private String baseUrl = "";
    private String videoUrl;

    private IWeiboShareAPI mWeiboShareAPI;
    public static IWXAPI api;
    private String ub_id;
    public static String qc_id;
    private InsideWebChromeClient mInsideWebChromeClient;
    private String actions;
    private Timer barTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rl_pl.setVisibility(View.GONE);
        //mDialog = new DialogUtil(mContext);
        setLoadingVisiblity();
        //mediaController = (SimpleMediaController) findViewById(R.id.media_controller_bar);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        initWeiBo(savedInstanceState);
        initWX();
        myInit();
    }

    private void initWeiBo(Bundle savedInstanceState) {
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, ShareConstants.WEIBO_APP_KEY);
        mWeiboShareAPI.registerApp();
        if (savedInstanceState != null) {
            mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
        }
    }

    private void initWX() {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, ShareConstants.WX_APP_ID,false);
        // 将该app注册到微信
        api.registerApp(ShareConstants.WX_APP_ID);
    }

    private void myInit(){
        String ub_id = SpTools.getUserId(this);
        cc_id = getIntent().getStringExtra("newsId");
        type = getIntent().getStringExtra("type");
        if ("video".equals(type)){
            baseUrl = Const.HOST+"cms/videoinfo?cc_id=" + cc_id + "&ub_id=" + ub_id +  "&timestamp=" + System.currentTimeMillis();
            //setTitle("视频");
            bottom_zan_or_common.setVisibility(View.VISIBLE);
            videoUrl = getIntent().getStringExtra("videoUrl");

        }
        if (!TextUtils.isEmpty(ub_id)){
            baseUrl = baseUrl + "&ub_id=" +ub_id;
        }

        sharePop = new SharePop(ll_right, mContext, R.layout.pop_share);
        init();
        if ("news".equals(type) || "video".equals(type)||"circle".equals(type) || "wenda".equals(type)) {
            initH5Info();
        }
    }

    private H5Protocal protocal = new H5Protocal();
    private Gson gson = new Gson();
    private void initH5Info() {
        String param1 = "";
        String param2 = "";
        String param3 = "";
       if ("video".equals(type)){
            param1 = cc_id;
            param2 = "2";
            param3 = "0";
            if(videoUrl != null && !videoUrl.equals("")){
                mVideoPlayer = new BDCloudVideoView(this);
                mVideoPlayer.setVideoPath(videoUrl);
                /*if (SharedPrefsStore.isPlayerFitModeCrapping(getApplicationContext())) {

                } else {
                }*/
                //mVideoPlayer.setVideoScalingMode(BDCloudVideoView.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                //mVideoPlayer.setVideoScalingMode(BDCloudVideoView.VIDEO_SCALING_MODE_SCALE_TO_FIT);
                mVideoPlayer.setVideoScalingMode(BDCloudVideoView.VIDEO_SCALING_MODE_SCALE_TO_MATCH_PARENT);
                RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(-1, -1);
                rllp.addRule(RelativeLayout.CENTER_IN_PARENT);
                mVideoRl.addView(mVideoPlayer, rllp);

                mVideoPlayer.setOnPreparedListener(this);
                mVideoPlayer.setOnCompletionListener(this);
                mVideoPlayer.setOnErrorListener(this);
                mVideoPlayer.setOnInfoListener(this);
                mVideoPlayer.setOnBufferingUpdateListener(this);
                mVideoPlayer.setOnPlayerStateListener(this);

                mediaController.setMediaPlayerControl(mVideoPlayer);
                /*mScreenIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isFullScreen) {
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            FullScreenUtils.toggleHideyBar(VideoH5Activity.this);
                            // to mini size, to portrait
                            *//*fullHeaderRl.removeAllViews();
                            fullControllerRl.removeAllViews();
                            normalHeaderRl.addView(headerBar);
                            normalControllerRl.addView(mediaController);
                            ibScreen.setBackgroundResource(R.drawable.btn_to_fullscreen);*//*
                            isFullScreen = false;
                        } else {
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                            FullScreenUtils.toggleHideyBar(VideoH5Activity.this);
                            *//*normalHeaderRl.removeAllViews();
                            normalControllerRl.removeAllViews();
                            fullHeaderRl.addView(headerBar);
                            fullControllerRl.addView(mediaController);

                            ibScreen.setBackgroundResource(R.drawable.btn_to_mini);*//*
                            isFullScreen = true;
                            hideOuterAfterFiveSeconds();
                        }
                    }
                });*/
                //mVideoPlayer.start();
                //mVideoPaler.setVisibility(View.VISIBLE);
                //mVideoPaler.setUp(videoUrl , JCVideoPlayer.SCREEN_LAYOUT_LIST , "");

            }
        }
        protocal.getH5Info(param1, param2, param3, new H5Protocal.H5Listener() {
            @Override
            public void H5Response(String response) {
                if (TextUtils.isEmpty(response)){
                    CommonUtils.toastMessage("加载评论和赞失败！");
                    return;
                }
                H5Bean bean = gson.fromJson(response, H5Bean.class);
                if ("10".equals(bean.result.code)){
                    tv_news_detail_comment.setText(bean.plNum);
                    tv_news_detail_zan.setText(bean.dzNum);
                    tv_news_detail_fd.setText(bean.dcNum);

                    if (Integer.parseInt(bean.plNum) > 0){
                        tv_news_detail_comment.setVisibility(View.VISIBLE);
                    }

                    if (Integer.parseInt(bean.dzNum) > 0){
                        tv_news_detail_zan.setVisibility(View.VISIBLE);
                    }

                    if (Integer.parseInt(bean.dcNum) > 0){
                        tv_news_detail_fd.setVisibility(View.VISIBLE);
                    }
                    cb_news_detail_zan.setChecked("1".equals(bean.dz));
                    cb_news_detail_cai.setChecked("1".equals(bean.dc));
                    cb_news_detail_collect.setChecked("1".equals(bean.collect));
                }else {
                    CommonUtils.toastMessage("加载评论和赞失败！");
                }

            }
        });
    }

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {

    }

    @Override
    protected void prepareData() {

    }

    /**
     * 初始化webView的参数
     */
    private void initWebView() {
        WebSettings settings = mWebView.getSettings();

        //启用支持javascript
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        //settings.setPluginsEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        //settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setBlockNetworkImage(false);
        settings.setDomStorageEnabled(true);


        //表示可以同时加载http  和  https
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            //settings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        mInsideWebChromeClient = new InsideWebChromeClient();
        mWebView.setWebChromeClient(mInsideWebChromeClient);
    }
    /**
     * 检测'点击'空白区的事件，若播放控制控件未显示，设置为显示，否则隐藏。
     *
     * @param v
     */
    public void onClickEmptyArea(View v) {
        if (barTimer != null) {
            barTimer.cancel();
            barTimer = null;
        }
        if (this.mediaController != null) {
            if (mediaController.getVisibility() == View.VISIBLE) {
                mediaController.hide();
            } else {
                mediaController.show();
                hideOuterAfterFiveSeconds();
            }
        }
    }

    private void hideOuterAfterFiveSeconds() {
        if (barTimer != null) {
            barTimer.cancel();
            barTimer = null;
        }
        barTimer = new Timer();
        barTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                if (mediaController != null) {
                    mediaController.getMainThreadHandler().post(new Runnable() {

                        @Override
                        public void run() {
                            mediaController.hide();
                        }

                    });
                }
            }

        }, 5 * 1000);

    }
    /**
     * 当视频空间解析完成数据
     * @param iMediaPlayer
     */
    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {

    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {

    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int percent) {
        if (mediaController != null && mVideoPlayer != null) {
            mediaController.onTotalCacheUpdate(percent * mVideoPlayer.getDuration() / 100);
        }
    }

    @Override
    public void onPlayerStateChanged(BDCloudVideoView.PlayerState nowState) {
        if (mediaController != null) {
            mediaController.changeState();
        }
    }

    private class InsideWebChromeClient extends WebChromeClient {
        private View mCustomView;
        private CustomViewCallback mCustomViewCallback;

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            super.onShowCustomView(view, callback);
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            mCustomView = view;
            mFrameLayout.addView(mCustomView);
            mCustomViewCallback = callback;
            mWebView.setVisibility(View.GONE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        public void onHideCustomView() {
            mWebView.setVisibility(View.VISIBLE);
            if (mCustomView == null) {
                return;
            }
            mCustomView.setVisibility(View.GONE);
            mFrameLayout.removeView(mCustomView);
            mCustomViewCallback.onCustomViewHidden();
            mCustomView = null;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            super.onHideCustomView();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        switch (config.orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
        //mVideoPaler.release();
        mVideoPlayer.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
        mVideoPlayer.start();
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return;
        }
        /*if(mVideoPaler.backPress()){
            return;
        }*/
        super.onBackPressed();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        if (mVideoPlayer != null) {
            mVideoPlayer.enterForeground();
        }
    }

    @Override
    protected void onStop() {
        if (mVideoPlayer != null) {
            mVideoPlayer.enterBackground();
        }

        super.onStop();
    }
    @Override
    public void onDestroy() {
        mWebView.destroy();
        if (mVideoPlayer != null) {
            mVideoPlayer.stopPlayback();
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            boolean flag = JCVideoPlayer.backPress();
            if (!flag){
                JCVideoPlayer.releaseAllVideos();
                finish();

            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 初始化
     */
    private void init(){
        //初始化WebView的设置
        initWebView();
        //mDialog.show();
        //WebView加载web资源
        mWebView.loadUrl(baseUrl);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                //view.loadUrl(url);
                if(!url.equals("wvjbscheme://__BRIDGE_LOADED__")){
                    mWebView.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
                setSuccessVisiblity();
                //结束
                mWebView.loadUrl("javascript:info()");
                super.onPageFinished(view, url);
                //mDialog.dissmiss();
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                //开始
                super.onPageStarted(view, url, favicon);
            }
        });

        mWebView.addJavascriptInterface(this,"android");

    }

    String title = "";
    String des = "";
    //得到标题和描述
    @JavascriptInterface
    public void getInfo(final String title, final String des){
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                try {
                    VideoH5Activity.this.title = URLDecoder.decode(title, "UTF-8");
                    VideoH5Activity.this.des = URLDecoder.decode(des, "UTF-8");
                }catch (Exception exception){
                    VideoH5Activity.this.title = "";
                    VideoH5Activity.this.des = "";
                }

            }
        });
    }

    /**
     * 视频界面的  推荐内容
     * @param cc_id
     * @param title
     * @param desc
     */
    @JavascriptInterface
    public void getPageId(String cc_id , String title , String desc , String videoUrl){
        /*this.cc_id = cc_id;
        this.videoUrl = videoUrl;
        this.title = title;
        this.des = desc;
        initH5Info();*/
        //mVideoPaler.release();
        //JCVideoPlayer.releaseAllVideos();

        /*mVideoPlayer.stopPlayback();// 释放上一个视频源
        mVideoPlayer.reSetRender();// 清除上一个播放源的最后遗留的一帧
        mVideoPlayer.setVideoPath(videoUrl);
        mVideoPlayer.start();*/

        Intent intent = new Intent(mContext , VideoH5Activity.class);
        intent.putExtra("newsId", cc_id);
        intent.putExtra("type", "video");
        intent.putExtra("videoUrl", videoUrl);
        startActivity(intent);
    }

    //搜索
    @JavascriptInterface
    public void getKeyWord(final String text){
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(mContext,HomeNewsSearchUI.class);
                intent.putExtra("content",text);
                startActivity(intent);

            }
        });
    }

    //跳转个人界面
    @JavascriptInterface
    public void getUserId(final String ub_id){
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(mContext,SingleInfoUI.class);
                intent.putExtra("uid",ub_id);
                startActivity(intent);

            }
        });
    }
    //问答界面删除
    @JavascriptInterface
    public void getDelId (final String ub_id){
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (!TextUtils.isEmpty(ub_id)){
                    InitInfo.wendaDetail = true;
                    finish();
                }else {
                    CommonUtils.toastMessage("删除失败");
                }

            }
        });
    }

    //点击了h5界面的回复
    @JavascriptInterface
    public void getPlId (final String data){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rl_pl.setVisibility(View.VISIBLE);
                god = data;
            }
        });
    }
    /**
     * 分享
     *
     * @param view
     */

    public static int flag = 0;
    @OnClick(R.id.iv_right)
    private void getShare(View view) {
        sharePop.showAtLocation();
        sharePop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 1;
                String a = title;
                String b = des;
                if (TextUtils.isEmpty(a)){
                    a = "安全快车";
                }

                if (TextUtils.isEmpty(b)){
                    b = "安全快车";
                }
                baseUrl = baseUrl + "&app=1";
                switch (v.getId()) {
                    case R.id.ll_share_weixin:
                        sharePop.dismiss();
                        ShareWX.getShareWXInstance(api).share2Wx(true , a , b,baseUrl);
                        break;
                    case R.id.ll_share_friend:
                        sharePop.dismiss();
                        ShareWX.getShareWXInstance(api).share2Wx(false, a , b,baseUrl);
                        break;
                    case R.id.ll_share_sina:
                        sharePop.dismiss();
//                        ShareWeiBo.getShareWeiboInstants().shareToWeibo(H5DetailUI.this,"微博分享测试" + baseUrl,mWeiboShareAPI);
                        Intent intent = new Intent(mContext,CallBackActivity.class);
                        intent.putExtra("shareUrl",baseUrl);
                        intent.putExtra("qc_id",cc_id);
                        intent.putExtra("title",a);
                        intent.putExtra("des",b);
                        startActivity(intent);
                        break;
                    case R.id.ll_share_qq:
                        sharePop.dismiss();
                        ShareQQ.getShareQQInstants().shareToQQ(VideoH5Activity.this,qqShareListener,a,b,baseUrl);
                        break;
                }
            }
        });
    }
    /**
     * 显示登录的Dialog
     */
    private void showLoginDialog() {
        LoginDialog dialog = new LoginDialog(mContext, new LoginDialog.onClickLogin() {
            @Override
            public void OnClickLogin() {
                Intent intent = new Intent(mContext,LoginUI.class);
                startActivity(intent);
            }
        });
        dialog.show();
    }
    /**
     * 评论
     *
     * @param view
     */
    @OnClick(R.id.rl_news_detail_comment)
    private void getComment(View view) {
        mWebView.loadUrl("javascript:onclickComments()");
    }
    /**
     * 返回
     */
    @OnClick(R.id.iv_back)
    private void onBack(View view){
        finish();
    }
    /**
     * 取消评论
     *
     * @param view
     */
    @OnClick(R.id.tv_cancel)
    private void getCancel(View view) {
        rl_pl.setVisibility(View.GONE);

    }

    /**
     * 发布评论
     *
     * @param view
     */

    String god = "0";
    @OnClick(R.id.tv_confirm)
    private void getConfirm(View view) {
        String content = et_common.getText().toString().trim();
        if (TextUtils.isEmpty(content)){
            CommonUtils.toastMessage("评论内容不能为空");
            return;
        }
        rl_pl.setVisibility(View.GONE);
        et_common.setText("");

        if ("news".equals(type)||"video".equals(type)){

            newsCommon(content,god);

        }else if (("circle".equals(type))){

            circleCommon(content,god);

        }
        god = "0";


    }

    private void circleCommon(String content,String god) {
        protocal.setCircleCommen(cc_id, content, god, new H5Protocal.H5Listener() {
            @Override
            public void H5Response(String response) {
                if (TextUtils.isEmpty(response)){
                    CommonUtils.toastMessage("评论失败，请重新评论！");
                    return;
                }

                H5BottomBean bean = gson.fromJson(response,H5BottomBean.class);
                if ("10".equals(bean.result.code)){
                    CommonUtils.toastMessage(bean.result.info);
                    if (Integer.parseInt(bean.plNum) > 0){
                        tv_news_detail_comment.setVisibility(View.VISIBLE);
                    }else {
                        tv_news_detail_comment.setVisibility(View.GONE);
                    }
                    tv_news_detail_comment.setText(bean.plNum);
                    mWebView.loadUrl(baseUrl + "&comment_area");
                }else {
                    CommonUtils.toastMessage(bean.result.info);
                }
            }
        });
    }

    private void newsCommon(String content,String god) {
        protocal.setNewsCommen(god, cc_id, content, new H5Protocal.H5Listener() {
            @Override
            public void H5Response(String response) {
                if (TextUtils.isEmpty(response)){
                    CommonUtils.toastMessage("评论失败，请重新评论！");
                    return;
                }

                H5BottomBean bean = gson.fromJson(response,H5BottomBean.class);
                if ("10".equals(bean.result.code)){
                    CommonUtils.toastMessage(bean.result.info);
                    if (Integer.parseInt(bean.plNum) > 0){
                        tv_news_detail_comment.setVisibility(View.VISIBLE);
                    }else {
                        tv_news_detail_comment.setVisibility(View.GONE);
                    }
                    tv_news_detail_comment.setText(bean.plNum);
                    //init();
                    mWebView.loadUrl(baseUrl + "&comment_area");
                }else {
                    CommonUtils.toastMessage(bean.result.info);
                }
            }
        });
    }

    /**
     * 顶
     *
     * @param view
     */@OnClick(R.id.cb_news_detail_zan)
    private void getZan(View view) {

        Log.i("qaz", "getZan: "+ "-------------");
        if (TextUtils.isEmpty(ub_id)) {
            ub_id = SpTools.getUserId(this);
        }
        if ("news".equals(type) || "video".equals(type)){
            //如果是新闻或者是视频
            requestNewsData(cb_news_detail_zan,"0");
        }else if (("circle".equals(type))){//圈子
            if (TextUtils.isEmpty(ub_id)){
                showLoginDialog();
                return;
            }
            requestNewsDataCicle(cb_news_detail_zan,"0");

        }
    }

    /**
     * 踩
     *
     * @param view
     */
    @OnClick(R.id.cb_news_detail_cai)
    private void getCai(View view) {
        if (TextUtils.isEmpty(ub_id)) {
            ub_id = SpTools.getUserId(this);
        }

        if ("news".equals(type) || "video".equals(type)){
            //如果是新闻或者是视频
            requestNewsData(cb_news_detail_cai,"1");
        }else if (("circle".equals(type))){
            if (TextUtils.isEmpty(ub_id)){
                showLoginDialog();
            }
            requestNewsDataCicle(cb_news_detail_cai,"1");

        }
    }
    /**
     * 视频  和  新闻的赞和踩
     * @param cb
     * @param zanOrCai
     */
    private void requestNewsData(final CheckBox cb, final String zanOrCai) {
        String action = "0";
        if (!cb.isChecked()){
            //取消赞
            action = "1";
        }
        //type 0赞  1踩
        protocal.setDzDc(cc_id, zanOrCai, action, new H5Protocal.H5Listener() {
            @Override
            public void H5Response(String response) {
                if (response == null){
                    CommonUtils.toastMessage("操作失败，请重试！");
                    //cb.setChecked(!cb.isChecked());
                    return;
                }
                H5BottomBean bean = gson.fromJson(response,H5BottomBean.class);
                if ("10".equals(bean.result.code)){
                    //CommonUtils.toastMessage("操作成功！");
                    if ("0".equals(zanOrCai)){
                        //赞
                        if (Integer.parseInt(bean.Num) > 0){
                            tv_news_detail_zan.setVisibility(View.VISIBLE);
                        }else {
                            tv_news_detail_zan.setVisibility(View.GONE);
                        }
                        tv_news_detail_zan.setText(bean.Num);
                    }else {// 踩
                        if (Integer.parseInt(bean.Num) > 0){
                            tv_news_detail_fd.setVisibility(View.VISIBLE);
                        }else {
                            tv_news_detail_fd.setVisibility(View.GONE);
                        }
                        tv_news_detail_fd.setText(bean.Num);
                    }
                }else {
                    CommonUtils.toastMessage("操作失败，请重试！");
                    cb.setChecked(!cb.isChecked());
                }
            }
        });
    }

    /**
     * 圈子的赞和踩
     * @param cb
     * @param type
     */
    private void requestNewsDataCicle(final CheckBox cb, final String type) {
        //type 0是赞
         actions = "0";
        if (!cb.isChecked()){
            //取消赞
            actions = "1";
        }
        Log.i("qaz", "requestNewsDataCicle: " +  actions);
        protocal.setQZDzDc(cc_id, type, actions, new H5Protocal.H5Listener() {
            @Override
            public void H5Response(String response) {
                if (TextUtils.isEmpty(response)){
                    CommonUtils.toastMessage("操作失败！请稍后重试！");
                    cb.setChecked(!cb.isChecked());
                    return;
                }
                H5BottomBean bean = gson.fromJson(response,H5BottomBean.class);
                if ("10".equals(bean.result.code)){
                    CommonUtils.toastMessage(bean.result.info);
                    if ( actions.equals("1")) {
                        cb.setChecked(false);
                    }else{
                        cb.setChecked(true);
                    }

                    if ("0".equals(type)){
                        //赞
                        if (Integer.parseInt(bean.dzNum) > 0){
                            tv_news_detail_zan.setVisibility(View.VISIBLE);
                        }else {
                            tv_news_detail_zan.setVisibility(View.GONE);
                        }
                        tv_news_detail_zan.setText(bean.dzNum);
                    }else {
                        if (Integer.parseInt(bean.dcNum) > 0){
                            tv_news_detail_fd.setVisibility(View.VISIBLE);
                        }else {
                            tv_news_detail_fd.setVisibility(View.GONE);
                        }
                        tv_news_detail_fd.setText(bean.dcNum);
                    }
                }else {
                    CommonUtils.toastMessage(bean.result.info);
                    cb.setChecked(!cb.isChecked());
                }
            }
        });
    }


    /**
     * 收藏
     *
     * @param view
     */
    @OnClick(R.id.cb_news_detail_collect)
    private void getCollect(View view) {
        if (TextUtils.isEmpty(ub_id)) {
            ub_id = SpTools.getUserId(this);
        }

        if (TextUtils.isEmpty(ub_id)){
            showLoginDialog();
            cb_news_detail_collect.setChecked(false);
            return;
        }

        requestConllect(cb_news_detail_collect);
//        makeText("收藏");
    }


    /**
     * 评论
     *
     * @param view
     */
    @OnClick(R.id.tv_common)
    private void getCommon(View view) {
        if (TextUtils.isEmpty(ub_id)) {
            ub_id = SpTools.getUserId(this);
        }

        if (TextUtils.isEmpty(ub_id)){
            showLoginDialog();
            return;
        }

        rl_pl.setVisibility(View.VISIBLE);

        et_common.setFocusable(true);
        et_common.setFocusableInTouchMode(true);
        et_common.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0 , InputMethodManager.HIDE_NOT_ALWAYS);
//        makeText("收藏");
    }

    private void requestConllect(final CheckBox cb) {
//        0收藏  1取消收藏
        String type1 = "0";
        if (!cb.isChecked()){
            type1 = "1";
        }
//        0 新闻   9视频 5圈子
        String uc_moudle = "0";
        String id = "";
        if ("news".equals(type)){
            uc_moudle = "0";
            id = cc_id;
        }else if ("video".equals(type)){
            uc_moudle = "9";
            id = cc_id;
        }else if (("circle".equals(type))){
            uc_moudle = "5";
            id = cc_id;
        }else if (("wenda".equals(type))){

        }

        protocal.setCollect(id, type1, uc_moudle, new H5Protocal.H5Listener() {
            @Override
            public void H5Response(String response) {
                if (TextUtils.isEmpty(response)){
                    CommonUtils.toastMessage("操作失败！请稍后再试");
                    cb.setChecked(!cb.isChecked());
                    return;
                }

                Result bean = gson.fromJson(response,Result.class);
                if ("10".equals(bean.result.code)){
                    CommonUtils.toastMessage(bean.result.info);
                }else {
                    CommonUtils.toastMessage(bean.result.info);
                    cb.setChecked(!cb.isChecked());
                }
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, qqShareListener);
    }


    ShareProtocal mShareProtocal = new ShareProtocal();
    IUiListener qqShareListener = new IUiListener() {
        @Override
        public void onCancel() {
            Util.toastMessage(VideoH5Activity.this, "取消 ");
        }

        @Override
        public void onComplete(Object response) {
            // TODO Auto-generated method stub
            if (flag == 1) {
                flag = 0;
                mShareProtocal.postCircleShare(cc_id,new ShareProtocal.ShareInfo() {
                    @Override
                    public void shareResponse(String response) {
                        if (TextUtils.isEmpty(response)){
                            CommonUtils.toastMessage("请重新分享已确保获得奖励。");
                            return;
                        }

                        Result bean = gson.fromJson(response,Result.class);
                        if ("10".equals(bean.result.code)){
                            CommonUtils.toastMessage(bean.result.info);
                        }else {
                            CommonUtils.toastMessage(bean.result.info);
                        }
                    }
                });
            }

            Util.toastMessage(VideoH5Activity.this, "onComplete: " + response.toString());
        }

        @Override
        public void onError(UiError e) {
            // TODO Auto-generated method stub
            Util.toastMessage(VideoH5Activity.this, "onError: " + e.errorMessage, "e");
        }
    };



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
        mWeiboShareAPI.handleWeiboResponse(intent, this);
    }

    /**
     * 接收微客户端博请求的数据。
     * 当微博客户端唤起当前应用并进行分享时，该方法被调用。
     *
     * @param baseResp 微博请求数据对象
     * @see {@link IWeiboShareAPI#handleWeiboRequest}
     */
    @Override
    public void onResponse(BaseResponse baseResp) {
        if (baseResp != null) {
            switch (baseResp.errCode) {
                case WBConstants.ErrorCode.ERR_OK:
                    Toast.makeText(this, "success", Toast.LENGTH_LONG).show();
                    break;
                case WBConstants.ErrorCode.ERR_CANCEL:
                    Toast.makeText(this, "cancel", Toast.LENGTH_LONG).show();
                    break;
                case WBConstants.ErrorCode.ERR_FAIL:
                    Toast.makeText(this, "Error Message: " + baseResp.errMsg,
                            Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }



    public void setLoadingVisiblity(){
        //mLoadingView.setVisibility(View.VISIBLE);
        empty.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        success.setVisibility(View.GONE);
    }
    public void setEmptyVisiblity(){
        //mLoadingView.setVisibility(View.GONE);
        empty.setVisibility(View.VISIBLE);
        error.setVisibility(View.GONE);
        success.setVisibility(View.GONE);
    }
    public void setErrorVisiblity(){
        //mLoadingView.setVisibility(View.GONE);
        empty.setVisibility(View.GONE);
        error.setVisibility(View.VISIBLE);
        success.setVisibility(View.GONE);
    }
    public void setSuccessVisiblity(){
        //mLoadingView.setVisibility(View.GONE);
        empty.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        success.setVisibility(View.VISIBLE);
    }



}
