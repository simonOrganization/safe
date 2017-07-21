package com.lchtime.safetyexpress;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.lchtime.safetyexpress.ui.home.HomeQuewstionDetail;
import com.lchtime.safetyexpress.ui.home.protocal.H5Protocal;
import com.lchtime.safetyexpress.ui.home.protocal.ShareProtocal;
import com.lchtime.safetyexpress.ui.search.HomeNewsSearchUI;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.SpTools;
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


/**
 * 所有的详情h5界面
 * Created by user on 2017/4/17.
 */
@ContentView(R.layout.home_news_detail_ui)
public class H5DetailUI extends BaseUI implements IWeiboHandler.Response{

    public static final int EDIT = 105;

    //分享
    @ViewInject(R.id.ll_right)
    private LinearLayout ll_right;

    @ViewInject(R.id.home_news_detailed_web)
    private WebView mWebView;

    @ViewInject(R.id.bottom_zan_or_common)
    private LinearLayout bottom_zan_or_common;


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

    @ViewInject(R.id.loading)
    RelativeLayout loading;
    @ViewInject(R.id.empty)
    RelativeLayout empty;
    @ViewInject(R.id.error)
    RelativeLayout error;
    @ViewInject(R.id.success)
    RelativeLayout success;

    @ViewInject(R.id.fl_root)
    FrameLayout mFrameLayout; //根布局
    //@ViewInject(R.id.iv_recommend_img)
    //JCVideoPlayerStandard mVideoPaler;


    private SharePop sharePop;
    private String cc_id;   //新闻id
    private String a_id;
    private String aq_id;
    private String url;
    //类型，是新闻还是视频
    private String type;
    private String baseUrl = "";
    //private String videoUrl;

    private IWeiboShareAPI mWeiboShareAPI;
    public static IWXAPI api;
    private String mUb_id;
    private String ub_id;       //用户id
    public static String qc_id;
    //private InsideWebChromeClient mInsideWebChromeClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setLoadingVisiblity();
        rl_pl.setVisibility(View.GONE);

        //mVideoPaler = (JCVideoPlayerStandard) findViewById(R.id.iv_recommend_img);

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
        String ub_id = SpTools.getString(this, Constants.userId,"");
        cc_id = getIntent().getStringExtra("newsId"); //
        a_id = getIntent().getStringExtra("a_id");
        aq_id = getIntent().getStringExtra("aq_id");
        url = getIntent().getStringExtra("url");
        type = getIntent().getStringExtra("type");

        rightVisible(R.drawable.news_share);
        if ("news".equals(type)){
            baseUrl = Const.HOST+"cms/pagenews?cc_id=" + cc_id + "&timestamp=" + System.currentTimeMillis();
            setTitle("新闻中心");
            bottom_zan_or_common.setVisibility(View.VISIBLE);
        }else if ("video".equals(type)){
            baseUrl = Const.HOST+"cms/videoinfo?cc_id=" + cc_id + "&timestamp=" + System.currentTimeMillis();
            setTitle("视频中心");
            bottom_zan_or_common.setVisibility(View.VISIBLE);
            /*videoUrl = getIntent().getStringExtra("videoUrl");
            if(videoUrl != null && !videoUrl.equals("")){
                mVideoPaler.setVisibility(View.VISIBLE);
                mVideoPaler.setUp(videoUrl , JCVideoPlayer.SCREEN_LAYOUT_LIST , "");
            }*/
        }else if (("circle".equals(type))){
            //圈子的baseurl
            baseUrl = Const.HOST + "quanzi/qzinfo?qc_id=" + cc_id + "&timestamp=" + System.currentTimeMillis();
            qc_id = cc_id;
            setTitle("圈子详情");
            bottom_zan_or_common.setVisibility(View.VISIBLE);
        }else if (("wenda".equals(type))){
            //问答的baseurl

            String num = getIntent().getStringExtra("num");
            if (!TextUtils.isEmpty(num)){
                setTitle("查看全部"+num+"个回答");
            }else {
                setTitle("查看全部回答");
            }
            baseUrl = Const.HOST + "wenda/myhuida?a_id= "+a_id+"&q_id=" + aq_id + "&timestamp=" + System.currentTimeMillis();
            bottom_zan_or_common.setVisibility(View.VISIBLE);
            ll_collect.setVisibility(View.GONE);

        }
        if (!TextUtils.isEmpty(ub_id)){
            baseUrl = baseUrl + "&ub_id=" +ub_id + "&timestamp=" + System.currentTimeMillis();
        }

        if ("url".equals(type)){
            if (!TextUtils.isEmpty(url)) {
                baseUrl = url;
                bottom_zan_or_common.setVisibility(View.GONE);
            }
        }else if ("agreement".equals(type)){
            setTitle("用户协议");
            baseUrl = getIntent().getStringExtra("url");
            rightGone();
            bottom_zan_or_common.setVisibility(View.GONE);
        }else if (("urls".equals(type))){
            //圈子的baseurl
            if (!TextUtils.isEmpty(url)) {
                baseUrl = url;
            }
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
        if ("wenda".equals(type)){
            param1 = aq_id;
            param2 = "4";
            param3 = a_id;
        }else if ("news".equals(type)){
            param1 = cc_id;
            param2 = "1";
            param3 = "0";
        }else if ("video".equals(type)){
            param1 = cc_id;
            param2 = "2";
            param3 = "0";
        }else if ("circle".equals(type)){
            param1 = cc_id;
            param2 = "3";
            param3 = "0";
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
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        /*mInsideWebChromeClient = new InsideWebChromeClient();
        InsideWebViewClient mInsideWebViewClient = new InsideWebViewClient();
        //javascriptInterface = new JavascriptInterface();
        //mWebView.addJavascriptInterface(javascriptInterface, "java2js_laole918");
        mWebView.setWebChromeClient(mInsideWebChromeClient);
        mWebView.setWebViewClient(mInsideWebViewClient);*/
    }

   /* private class InsideWebChromeClient extends WebChromeClient {
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
    private class InsideWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            //mWebView.loadUrl(javascript);
        }

    }*/

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
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
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
    public void onDestroy() {
        mWebView.destroy();
        super.onDestroy();
    }

    /**
     * 初始化
     */
    private void init(){
        //初始化WebView的设置
        initWebView();

        //WebView加载web资源
        //mWebView.loadUrl("http://www.baidu.com");
        mWebView.loadUrl(baseUrl);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                //String u = url;
                if(!url.equals("wvjbscheme://__BRIDGE_LOADED__")){
                    mWebView.loadUrl(url);
                }
                if(url.contains(".pdf")){
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse(url);
                    intent.setData(content_url);
                    startActivity(intent);
                    return false;
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
                //结束
                super.onPageFinished(view, url);
                mWebView.loadUrl("javascript:info()");
                setSuccessVisiblity();

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
                    H5DetailUI.this.title = URLDecoder.decode(title, "UTF-8");
                    H5DetailUI.this.des = URLDecoder.decode(des, "UTF-8");
                }catch (Exception exception){
                    H5DetailUI.this.title = "";
                    H5DetailUI.this.des = "";
                }

            }
        });
    }

    /**
     * 点击了新闻界面或者视频界面的  推荐内容
     * @param cc_id
     * @param title
     * @param desc
     */
    @JavascriptInterface
    public void getPageId(String cc_id , String title , String desc){
        this.cc_id = cc_id;
        this.title = title;
        this.des = desc;
        //makeText("cc_id" + cc_id + " title" + title + "desc" + desc);
        initH5Info();
    }


    //搜索
    @JavascriptInterface
    public void getKeyWord(final String text){
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(mContext ,HomeNewsSearchUI.class);
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
                Intent intent = new Intent(mContext ,SingleInfoUI.class);
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
                if (ub_id != null && ub_id.equals("1")){
                    InitInfo.wendaDetail = true;
                    setResult(HomeQuewstionDetail.QUEWSTION_DETAIL_RESULT);
                    finish();
                }else {
                    CommonUtils.toastMessage("删除失败");
                }

            }
        });
    }

    //问答界面编辑
    @JavascriptInterface
    public void getWdEditId (final String eid){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String title = "";
                try {
                    title = URLDecoder.decode(eid, "UTF-8");
                    Intent intent = new Intent(mContext , AnswerQuestionActivity.class);
                    intent.putExtra("a_id",a_id);
                    intent.putExtra("title",title);
                    startActivityForResult(intent , EDIT);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
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
                et_common.setFocusable(true);
                et_common.setFocusableInTouchMode(true);
                et_common.requestFocus();
                //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0 , InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
    }
    /**
     * 分享
     *
     * @param view
     */

    public static int flag = 0;
    @OnClick(R.id.ll_right)
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
                switch (v.getId()) {
                    case R.id.ll_share_weixin:
                        sharePop.dismiss();
                        ShareWX.getShareWXInstance(api).share2Wx(true,a,b,baseUrl);
                        break;
                    case R.id.ll_share_friend:
                        sharePop.dismiss();
                        ShareWX.getShareWXInstance(api).share2Wx(false,a,b,baseUrl);
                        break;
                    case R.id.ll_share_sina:
                        sharePop.dismiss();
//                        ShareWeiBo.getShareWeiboInstants().shareToWeibo(mContext ,"微博分享测试" + baseUrl,mWeiboShareAPI);
                        Intent intent = new Intent(mContext , CallBackActivity.class);
                        intent.putExtra("shareUrl",baseUrl);
                        intent.putExtra("qc_id",cc_id);
                        intent.putExtra("title",a);
                        intent.putExtra("des",b);
                        startActivity(intent);
                        break;
                    case R.id.ll_share_qq:
                        sharePop.dismiss();
                        ShareQQ.getShareQQInstants().shareToQQ(H5DetailUI.this,qqShareListener,a,b,baseUrl);
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
        /*if (TextUtils.isEmpty(ub_id)) {
            ub_id = SpTools.getString(this, Constants.userId, "");
        }

        if (TextUtils.isEmpty(ub_id)){
            CommonUtils.toastMessage("请登陆后再进行相关操作！");
            return;
        }
        rl_pl.setVisibility(View.VISIBLE);*/
//        makeText("评论");
        //调用JS的方法
        mWebView.loadUrl("javascript:onclickComments()");
    }

    /**
     * 取消评论
     *
     * @param view
     */
    @OnClick(R.id.tv_cancel)
    private void getCancel(View view) {
        rl_pl.setVisibility(View.GONE);
        //bottom_zan_or_common.setVisibility(View.VISIBLE);
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

            newsCommon(content , god);

        }else if (("circle".equals(type))){

            circleCommon( content , god);

        }else if (("wenda".equals(type))){

            WDCommon( content , god);

        }

        god = "0";


    }

    private void WDCommon(String content, String god) {
        protocal.setWDCommen(a_id, content, god, new H5Protocal.H5Listener() {
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
                    init();
                }else {
                    CommonUtils.toastMessage(bean.result.info);
                }
            }
        });
    }

    /**
     * 圈子评论
     * @param content
     * @param god
     */
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
                    CommonUtils.toastMessage("评论成功");
                    if (Integer.parseInt(bean.plNum) > 0){
                        tv_news_detail_comment.setVisibility(View.VISIBLE);
                    }else {
                        tv_news_detail_comment.setVisibility(View.GONE);
                    }
                    tv_news_detail_comment.setText(bean.plNum);
                    init();
                }else {
                    CommonUtils.toastMessage(bean.result.info);
                }
            }
        });
    }

    /**
     * 新闻评论
     * @param content
     * @param god
     */
    private void newsCommon(String content,String god) {
        protocal.setNewsCommen(god , cc_id, content, new H5Protocal.H5Listener() {
            @Override
            public void H5Response(String response) {
                if (TextUtils.isEmpty(response)){
                    CommonUtils.toastMessage("评论失败，请重新评论！");
                    return;
                }

                H5BottomBean bean = gson.fromJson(response,H5BottomBean.class);
                if ("10".equals(bean.result.code)){
                    CommonUtils.toastMessage("评论成功");
                    if (Integer.parseInt(bean.plNum) > 0){
                        tv_news_detail_comment.setVisibility(View.VISIBLE);
                    }else {
                        tv_news_detail_comment.setVisibility(View.GONE);
                    }
                    tv_news_detail_comment.setText(bean.plNum);
                    init();
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
        if (TextUtils.isEmpty(ub_id)) {
            ub_id = SpTools.getString(this, Constants.userId, "");
        }

        if ("news".equals(type) || "video".equals(type)){
            //如果是新闻或者是视频
            requestNewsData(cb_news_detail_zan,"0");
        }else if (("circle".equals(type))){
            if (TextUtils.isEmpty(ub_id)){
                CommonUtils.toastMessage("请登陆后再进行相关操作！");
                cb_news_detail_zan.setChecked(false);
                return;
            }
            if (!cb_news_detail_zan.isChecked()){
                cb_news_detail_zan.setChecked(true);
                //return;
            }
            requestNewsDataCicle(cb_news_detail_zan,"0");

        }else if (("wenda".equals(type))){
            requestNewsDataWD(cb_news_detail_zan,"0");
        }


//        makeText("顶");
    }

    /**
     * 踩
     *
     * @param view
     */
    @OnClick(R.id.cb_news_detail_cai)
    private void getCai(View view) {
        if (TextUtils.isEmpty(ub_id)) {
            ub_id = SpTools.getString(this, Constants.userId, "");
        }
        if ("news".equals(type) || "video".equals(type)){
            //如果是新闻或者是视频
            requestNewsData(cb_news_detail_cai,"1");
        }else if (("circle".equals(type))){
            if (TextUtils.isEmpty(ub_id)){
                CommonUtils.toastMessage("请登陆后再进行相关操作！");
                cb_news_detail_cai.setChecked(false);
                return;
            }
            if (!cb_news_detail_cai.isChecked()){
                cb_news_detail_cai.setChecked(true);
                return;
            }
            requestNewsDataCicle(cb_news_detail_cai,"1");

        }else if (("wenda".equals(type))){
            requestNewsDataWD(cb_news_detail_cai,"1");
        }
//        makeText("踩");
    }

    /**
     * 问答 的 赞和踩
     * @param cb
     * @param type
     */
    private void requestNewsDataWD(final CheckBox cb, final String type) {
        String action = "0";
        if (!cb.isChecked()){
            //取消赞
            action = "1";
        }
        //0 赞  1踩
        protocal.setWDDzDc(a_id, type, action, new H5Protocal.H5Listener() {
            @Override
            public void H5Response(String response) {
                if (TextUtils.isEmpty(response)){
                    CommonUtils.toastMessage("操作失败，请稍后重试！");
                    cb.setChecked(!cb.isChecked());
                    return;
                }

                H5BottomBean bean = gson.fromJson(response,H5BottomBean.class);
                if ("10".equals(bean.result.code)){
                    //CommonUtils.toastMessage(bean.result.info);
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
                    cb.setChecked(!cb.isChecked());
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
                    }else {
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
        protocal.setQZDzDc(cc_id, type, new H5Protocal.H5Listener() {
            @Override
            public void H5Response(String response) {
                if (TextUtils.isEmpty(response)){
                    CommonUtils.toastMessage("操作失败！请稍后重试！");
                    cb.setChecked(!cb.isChecked());
                    return;
                }
                H5BottomBean bean = gson.fromJson(response,H5BottomBean.class);
                if ("10".equals(bean.result.code)){
                    //CommonUtils.toastMessage(bean.result.info);
                    cb.setChecked(true);
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
            ub_id = SpTools.getString(this, Constants.userId, "");
        }

        if (TextUtils.isEmpty(ub_id)){
            CommonUtils.toastMessage("请登陆后再进行相关操作！");
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
            ub_id = SpTools.getString(this, Constants.userId, "");
        }

        if (TextUtils.isEmpty(ub_id)){
            CommonUtils.toastMessage("请登陆后再进行相关操作！");
            return;
        }

        rl_pl.setVisibility(View.VISIBLE);
        //et_common.setVisibility(View.VISIBLE);
        //et_common.clearFocus();

        et_common.setFocusable(true);
        et_common.setFocusableInTouchMode(true);
        et_common.requestFocus();
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0 , InputMethodManager.HIDE_NOT_ALWAYS);
        //et_common.performClick();
        //bottom_zan_or_common.setVisibility(View.GONE);
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
        if(requestCode == EDIT && resultCode == RESULT_OK){
            setResult(HomeQuewstionDetail.QUEWSTION_DETAIL_RESULT);
            finish();
        }
    }


    ShareProtocal mShareProtocal = new ShareProtocal();
    IUiListener qqShareListener = new IUiListener() {
        @Override
        public void onCancel() {
            Util.toastMessage(H5DetailUI.this, "onCancel: ");
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

            Util.toastMessage(H5DetailUI.this, "onComplete: " + response.toString());
        }

        @Override
        public void onError(UiError e) {
            // TODO Auto-generated method stub
            Util.toastMessage(H5DetailUI.this, "onError: " + e.errorMessage, "e");
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
        loading.setVisibility(View.VISIBLE);
        empty.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        success.setVisibility(View.GONE);
    }
    public void setEmptyVisiblity(){
        loading.setVisibility(View.GONE);
        empty.setVisibility(View.VISIBLE);
        error.setVisibility(View.GONE);
        success.setVisibility(View.GONE);
    }
    public void setErrorVisiblity(){
        loading.setVisibility(View.GONE);
        empty.setVisibility(View.GONE);
        error.setVisibility(View.VISIBLE);
        success.setVisibility(View.GONE);
    }
    public void setSuccessVisiblity(){
        loading.setVisibility(View.GONE);
        empty.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        success.setVisibility(View.VISIBLE);
    }



}
