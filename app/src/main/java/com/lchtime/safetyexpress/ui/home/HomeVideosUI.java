package com.lchtime.safetyexpress.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.NewTypeBean;
import com.lchtime.safetyexpress.bean.res.VideoRes;
import com.lchtime.safetyexpress.pop.SharePop;
import com.lchtime.safetyexpress.shareapi.ShareConstants;
import com.lchtime.safetyexpress.shareapi.Util;
import com.lchtime.safetyexpress.shareapi.qq.ShareQQ;
import com.lchtime.safetyexpress.shareapi.wx.ShareWX;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.CallBackActivity;
import com.lchtime.safetyexpress.ui.home.fragment.VideosRecommendFragment;
import com.lchtime.safetyexpress.ui.home.protocal.VideoProtocal;
import com.lchtime.safetyexpress.ui.search.HomeNewsSearchUI;
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

import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * 视频
 * Created by user on 2017/4/18.
 */
@ContentView(R.layout.home_videos_ui)
public class HomeVideosUI extends BaseUI implements IWeiboHandler.Response {

    //XHorizontalScrollView
    @ViewInject(R.id.xhsv_home_videos)
    //private XHorizontalScrollView xhsv_home_videos;
    private TabLayout xhsv_home_videos;
    //ViewPager
    @ViewInject(R.id.vp_home_videos)
    private ViewPager vp_home_videos;

    @ViewInject(R.id.loading)
    RelativeLayout loading;
    @ViewInject(R.id.empty)
    RelativeLayout empty;
    @ViewInject(R.id.error)
    RelativeLayout error;
    @ViewInject(R.id.success)
    LinearLayout success;

    @ViewInject(R.id.error_btn_retry)
    Button error_btn_retry;

    private VideosPagerAdapter videosPagerAdapter;

    private VideoProtocal protocal;
    private List<NewTypeBean> titleList;
    private IWeiboShareAPI mWeiboShareAPI;
    public static IWXAPI api;
    private SharePop sharePop;

    @Override
    protected void back() {
        JCVideoPlayer.releaseAllVideos();
        finish();
    }

    @Override
    protected void setControlBasis() {
        setTitle("视频");
        rightVisible(R.drawable.news_search);
        error_btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLoadingVisiblity();
                prepareData();
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWeiBo(savedInstanceState);
        initWX();
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
        api = WXAPIFactory.createWXAPI(this, ShareConstants.WX_APP_ID, false);
        // 将该app注册到微信
        api.registerApp(ShareConstants.WX_APP_ID);
    }

    @Override
    protected void prepareData() {

        if (protocal == null){
            protocal = new VideoProtocal();
        }
        protocal.getVideoDir(new VideoProtocal.VideoDirListener() {
            @Override
            public void videoDirResponse(VideoRes response) {
                if (response == null){
                    //没数据或者是错误的时候
                    setErrorVisiblity();
                    return;
                }
                titleList = response.cms_dir;
                if (titleList == null || titleList.size() == 0){
                    setEmptyVisiblity();
                    return;
                }
                videosPagerAdapter = new VideosPagerAdapter(getSupportFragmentManager());
                vp_home_videos.setAdapter(videosPagerAdapter);


                xhsv_home_videos.setTabMode(TabLayout.MODE_SCROLLABLE);//MODE_FIXED
                xhsv_home_videos.setupWithViewPager(vp_home_videos);
                xhsv_home_videos.setTabsFromPagerAdapter(videosPagerAdapter);
                setSuccessVisiblity();
            }
        });

    }

    /**
     * 搜索
     *
     * @param view
     */
    @OnClick(R.id.ll_right)
    private void getSearch(View view) {
        Intent intent = new Intent(HomeVideosUI.this, HomeNewsSearchUI.class);
        intent.putExtra("type","2");
        startActivity(intent);
    }



    class VideosPagerAdapter extends FragmentPagerAdapter {


        public VideosPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
                Fragment fragment = new VideosRecommendFragment();
                Bundle bundle = new Bundle();
                bundle.putString("cd_id",titleList.get(arg0).cd_id);
                fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return titleList == null ? 0 :titleList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList == null ? "" : titleList.get(position).cd_name;
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
     * 分享
     *
     * @param view
     */
    public void getShare(View view) {
        sharePop.showAtLocation();
        sharePop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.ll_share_weixin:
                        sharePop.dismiss();
                        ShareWX.getShareWXInstance(api).share2Wx(true,title,des,shareUrl);
                        break;
                    case R.id.ll_share_friend:
                        sharePop.dismiss();
                        ShareWX.getShareWXInstance(api).share2Wx(false,title,des,shareUrl);
                        break;
                    case R.id.ll_share_sina:
                        sharePop.dismiss();
                        Intent intent = new Intent(HomeVideosUI.this,CallBackActivity.class);
                        intent.putExtra("shareUrl",shareUrl);
                        intent.putExtra("title",title);
                        intent.putExtra("des",des);
                        startActivity(intent);
//                        ShareWeiBo.getShareWeiboInstants().shareToWeibo(HomeVideosUI.this,"微博分享测试" + shareUrl,mWeiboShareAPI);
                        break;
                    case R.id.ll_share_qq:
                        sharePop.dismiss();
                        ShareQQ.getShareQQInstants().shareToQQ(HomeVideosUI.this,qqShareListener,title,des,shareUrl);
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, qqShareListener);
    }

    IUiListener qqShareListener = new IUiListener() {
        @Override
        public void onCancel() {
            Util.toastMessage(HomeVideosUI.this, "onCancel: ");
        }

        @Override
        public void onComplete(Object response) {
            // TODO Auto-generated method stub
            Util.toastMessage(HomeVideosUI.this, "onComplete: " + response.toString());
        }

        @Override
        public void onError(UiError e) {
            // TODO Auto-generated method stub
            Util.toastMessage(HomeVideosUI.this, "onError: " + e.errorMessage, "e");
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


    private String shareUrl = "";
    private String title = "";
    private String des = "";
    public void showPop(View view,String url,String title,String des){
        sharePop = new SharePop(view, HomeVideosUI.this, R.layout.pop_share);
        shareUrl = url;
        this.title = title;
        this.des = des;
        if (TextUtils.isEmpty(shareUrl)){
            shareUrl = Constants.SHARE;
        }else if (TextUtils.isEmpty(this.title)){
            this.title = "安全快车";
        }else if (TextUtils.isEmpty(this.des)){
            this.des = "安全快车";
        }
        getShare(view);
    }
}