package com.lchtime.safetyexpress.ui.home;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.lchtime.safetyexpress.H5DetailUI;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.VideoH5Activity;
import com.lchtime.safetyexpress.adapter.HomeHotCircleAdapter;
import com.lchtime.safetyexpress.adapter.HomeNewAdapter;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.FirstPic;
import com.lchtime.safetyexpress.bean.HotCircleBean;
import com.lchtime.safetyexpress.bean.InitInfo;
import com.lchtime.safetyexpress.bean.NewsBean;
import com.lchtime.safetyexpress.bean.res.NewsListRes;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.home.protocal.PictureAdvantage;
import com.lchtime.safetyexpress.ui.search.HomeNewsSearchUI;
import com.lchtime.safetyexpress.ui.home.protocal.HotCirclesProtocal;
import com.lchtime.safetyexpress.ui.home.protocal.HotNewsProtocal;
import com.lchtime.safetyexpress.ui.news.HomeNewActivity;
import com.lchtime.safetyexpress.ui.news.MediaActivity;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lchtime.safetyexpress.utils.cacheutils.ACache;
import com.lchtime.safetyexpress.views.EmptyRecyclerView;
import com.lchtime.safetyexpress.views.MyGridView;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.sivin.Banner;
import com.sivin.BannerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页
 * Created by user on 2017/4/14.
 */
@ContentView(R.layout.home)
public class HomeUI extends BaseUI implements SwipeRefreshLayout.OnRefreshListener {

    //banner图
    @ViewInject(R.id.sb_home_banner)
    private Banner sb_home_banner;
    //热门圈子
    @ViewInject(R.id.mgv_home_hot_circle)
    private MyGridView mgv_home_hot_circle;
    //热点追踪
    @ViewInject(R.id.mlv_home_hot_track)
    private EmptyRecyclerView mlv_home_hot_track;
    //视频热点
    @ViewInject(R.id.mlv_home_hot_video)
    private EmptyRecyclerView mlv_home_hot_video;

    @ViewInject(R.id.home_swipeRefreshLayout)
    private SwipeRefreshLayout swipeRefreshLayout;

    private HomeHotCircleAdapter homeHotCircleAdapter;
    private HomeNewAdapter homeHotTrackAdapter;

    //首页视频的adapter
    private HomeNewAdapter homeHotVideoAdapter;
    private HomeToCircleInterface homeToCircleInterface;
    public static HomeUI homeUI_instance = null;

    //    private List<HomeBannerBean> mDatas = new ArrayList<>();
    private HotNewsProtocal protocal;
    //新闻热点的数据
    private NewsListRes newsListRes;
    private NewsListRes videoListRes;
    private ArrayList<NewsBean> hotNewsList;
    private ArrayList<NewsBean> vedioNewsList;
    //首页热门圈子的数据
    private List<HotCircleBean.HotBean> hotList;
    private List<List<HotCircleBean.HotBean>> hot;
    private Gson gson = new Gson();

    private ACache aCache;
    public final String hotCircleUrl = "HOT_CIRCLE_CACHE";
    public final String hotNewsUrl = "HOT_NEWS_CACHE";
    //private String hotNewsUrl = "wsdsfdsfdsfsfdfds";
    private String hotVideoNewsUrl = "HOT_VIDEO_NEWS_CACHE";
    private String advDataUrl = "ADV_DATA_CACHE";
    private String ub_id;


    @Override
    protected void back() {
        exit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (InitInfo.homeRefresh) {
            hotCircleIndex = 0;
            hot.clear();
            getHotCircleData(0);
            InitInfo.homeRefresh = false;
        }
    }

    @Override
    protected void setControlBasis() {
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark);
        swipeRefreshLayout.setOnRefreshListener(this);
        homeUI_instance = this;
        //轮播图

        BannerAdapter adapter = new BannerAdapter<FirstPic.LunboBean>(lunbo) {
            @Override
            protected void bindTips(TextView tv, FirstPic.LunboBean homeBannerBean) {
//                tv.setText(homeBannerBean.url);
            }

            @Override
            public void bindImage(ImageView imageView, FirstPic.LunboBean homeBannerBean) {
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                Glide.with(HomeUI.this)
                        .load(homeBannerBean.img)
                        .placeholder(R.drawable.banner_default)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .error(R.drawable.banner_default)
                        .into(imageView);
            }
        };
        sb_home_banner.setBannerAdapter(adapter);
        sb_home_banner.setOnBannerItemClickListener(new Banner.OnBannerItemClickListener() {
            @Override
            public void onItemClick(int position) {
//                makeText("广告" + position);
                if (position == 0) {
                    Intent intent = new Intent(HomeUI.this, GetMoneyActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(HomeUI.this, H5DetailUI.class);
                    intent.putExtra("type", "url");
                    intent.putExtra("url", lunbo.get(position).url + "?timestamp=" + System.currentTimeMillis());
                    startActivity(intent);
                }
            }
        });
        //热门圈子
        if (hotList == null) {
            hotList = new ArrayList<>();
        }
        homeHotCircleAdapter = new HomeHotCircleAdapter(HomeUI.this, hotList);
        mgv_home_hot_circle.setAdapter(homeHotCircleAdapter);

        //热点追踪
        if (hotNewsList == null) {
            hotNewsList = new ArrayList<>();
        }
        homeHotTrackAdapter = new HomeNewAdapter(HomeUI.this, hotNewsList);
        mlv_home_hot_track.setLayoutManager(new LinearLayoutManager(HomeUI.this));
        mlv_home_hot_track.setAdapter(homeHotTrackAdapter);
        /*mlv_home_hot_track.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HomeUI.this, H5DetailUI.class);
                startActivity(intent);
            }
        });*/

        //视频热点
        if (vedioNewsList == null) {
            vedioNewsList = new ArrayList<>();
        }
        homeHotVideoAdapter = new HomeNewAdapter(HomeUI.this, vedioNewsList);
        mlv_home_hot_video.setLayoutManager(new LinearLayoutManager(HomeUI.this));
        mlv_home_hot_video.setAdapter(homeHotVideoAdapter);
//        mlv_home_hot_video.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(HomeUI.this, HomeVideosDeatilUI.class);
//                startActivity(intent);
//            }
//        });

    }

    @Override
    protected void prepareData() {
        aCache = ACache.get(mContext);

        /*Log.i("fxp" , "1  == " + aCache.getAsString("111"));
        aCache.put("111" , "1234");
        Log.i("fxp" , "2  == " + aCache.getAsString("111"));*/

        //获取广告
        getAdvData();
        //首页热门圈子
        getHotCircleData(0);
        //首页热点追踪
        getHotNewsData("1");
        getHotNewsData("2");
    }


    /**
     * 获取广告
     */
    private PictureAdvantage picProtocal = new PictureAdvantage();
    private List<FirstPic.LunboBean> lunbo = new ArrayList<>();

    private void getAdvData() {
        //测试
//       从缓存中获取数据
        FirstPic bean = (FirstPic) aCache.getAsObject(advDataUrl);
        //String advdata = SpTools.getString(mContext , advDataUrl , "");
        //FirstPic bean = gson.fromJson(advdata , FirstPic.class);
        if (bean != null && bean.result != null && "10".equals(bean.result.code)) {
            lunbo.clear();
            lunbo.addAll(bean.lunbo);
            sb_home_banner.notifiDataHasChanged();
        }
        String ub_id = SpTools.getUserId(this);
        picProtocal.getFirstPic(ub_id, new PictureAdvantage.HotNewsListener() {
            @Override
            public void hotNewsResponse(String respose) {
                if (respose == null) {
                    //CommonUtils.toastMessage("请检查网络，获取推荐图片失败！");
                    return;
                }
                FirstPic bean = gson.fromJson(respose, FirstPic.class);

                if ("10".equals(bean.result.code)) {
                    SpTools.setString(mContext , advDataUrl , gson.toJson(bean));
                    lunbo.clear();
                    lunbo.addAll(bean.lunbo);
                    sb_home_banner.notifiDataHasChanged();
                    aCache.put(advDataUrl, bean);
                } else {
                    //CommonUtils.toastMessage("获取推荐图片失败，请刷新重试！");
                }


            }
        });
    }


    /**
     * 搜索
     *
     * @param view
     */
    @OnClick(R.id.ll_home_search)
    private void getSearch(View view) {
        Intent intent = new Intent(HomeUI.this, HomeNewsSearchUI.class);
        intent.putExtra("type", "0");
        startActivity(intent);
    }

    /**
     * 扫一扫
     *
     * @param view
     */
    @OnClick(R.id.ll_home_scan)
    private void getScan(View view) {
        makeText("扫一扫");
    }

    /**
     * 新闻中心
     *
     * @param view
     */
    @OnClick(R.id.ll_home_news)
    private void getNews(View view) {
        Intent intent = new Intent(HomeUI.this, HomeNewActivity.class);
        startActivity(intent);
    }

    /**
     * 视频中心
     *
     * @param view
     */
    @OnClick(R.id.ll_home_video)
    private void getVideo(View view) {
        Intent intent = new Intent(HomeUI.this, HomeVideosUI.class);
        startActivity(intent);
    }

    /**
     * 安全圈子
     *
     * @param view
     */
    @OnClick(R.id.ll_home_circle)
    private void getCircle(View view) {
        homeToCircleInterface.toCircleActivity();
        makeText("安全圈子");
    }

    /**
     * 疑难问答
     *
     * @param view
     */
    @OnClick(R.id.ll_home_question)
    private void getQuestion(View view) {
        /*if (TextUtils.isEmpty(SpTools.getString(MyApplication.getContext(), Constants.userId, ""))){
            CommonUtils.toastMessage("登陆后才能看问答");
            return;
        }*/
        Intent intent = new Intent(HomeUI.this, HomeQuestionUI.class);
        startActivity(intent);
    }
    /**
     * 热点追踪换一换
     *
     * @param view
     */
    private int hotIndex = 0;

    @OnClick(R.id.tv_hotnews_change)
    private void getHotNews(View view) {
        if (newsListRes != null) {
            if (hotNewsList == null) {
                return;
            }
            hotIndex++;
            //先清除
            hotNewsList.clear();
            //添加所有
            if (hotIndex >= newsListRes.hot.size()) {
                hotIndex = 0;
            }
            hotNewsList.addAll(newsListRes.hot.get(hotIndex));
            homeHotTrackAdapter.notifyDataSetChanged();

        }
    }

    /**
     * 视频中心换一换
     *
     * @param view
     */
    private int vedioIndex = 0;

    @OnClick(R.id.tv_vedio_change)
    private void getHotVedio(View view) {
        if (videoListRes != null) {
            if (vedioNewsList == null) {
                return;
            }
            vedioIndex++;
            //先清除
            vedioNewsList.clear();
            //添加所有
            if (vedioIndex >= videoListRes.hot.size()) {
                vedioIndex = 0;
            }
            vedioNewsList.addAll(videoListRes.hot.get(vedioIndex));
            homeHotVideoAdapter.notifyDataSetChanged();

        }
    }

    /**
     * 热门圈子换一换
     *
     * @param view
     */
    private int hotCircleIndex = 0;

    @OnClick(R.id.hot_circle_change)
    private void getHotCircle(View view) {
        hotCircleIndex++;
        //添加所有
        if (hot != null) {
            if (hotCircleIndex >= hot.size()) {
                hotCircleIndex = 0;
            }
            getHotCircleData(hotCircleIndex);
        }
    }

    public interface HomeToCircleInterface {
        void toCircleActivity();
    }

    public void setHomeToCircleInterface(HomeToCircleInterface homeToCircleInterface) {
        this.homeToCircleInterface = homeToCircleInterface;
    }

    /**
     * 获取新闻数据
     *
     * @param type
     */
    private void getHotNewsData(final String type) {
        NewsListRes newsListRes;
        //先从缓存中获取数据
        if (type.equals("1")) {
            newsListRes = (NewsListRes) aCache.getAsObject(hotNewsUrl);
            //newsListRes = gson.fromJson(SpTools.getString(mContext , hotNewsUrl , "") , NewsListRes.class);
        } else {
            newsListRes = (NewsListRes) aCache.getAsObject(hotVideoNewsUrl);
            //newsListRes = gson.fromJson(SpTools.getString(mContext , hotVideoNewsUrl , "") , NewsListRes.class);
        }
        if (newsListRes != null && newsListRes.getResult() != null) {
            initData(newsListRes, type);
        }

        if (protocal == null) {
            protocal = new HotNewsProtocal();
        }
        protocal.getNewsList(type, new HotNewsProtocal.HotNewsListener() {
            @Override
            public void hotNewsResponse(NewsListRes newsListRes) {
                if (newsListRes == null) {
                    String s = "";
                    if ("1".equals(type)) {
                        //s = "获取新闻失败";
                        isNewComplete = true;
                    } else {
                        //s = "获取视频失败";
                        isVideoComplete = true;
                    }
                    //CommonUtils.toastMessage(s);

                    if (isNewComplete && isVideoComplete && isCircleComplete) {
                        isNewComplete = false;
                        isVideoComplete = false;
                        isCircleComplete = false;
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    return;
                }
                initData(newsListRes, type);

                if ("1".equals(type)) {
                    //热点新闻
                    isNewComplete = true;
                } else {
                    isVideoComplete = true;
                }

                if (isNewComplete && isVideoComplete && isCircleComplete) {
                    isNewComplete = false;
                    isVideoComplete = false;
                    isCircleComplete = false;
                    swipeRefreshLayout.setRefreshing(false);
                }

            }
        });
    }

    private void initData(NewsListRes newsListRes, final String type) {
        List list = null;
        HomeNewAdapter adapter = null;
        if (hotNewsList == null) {
            return;
        }
        if ("1".equals(type)) {
            list = hotNewsList;
            adapter = homeHotTrackAdapter;
            HomeUI.this.newsListRes = newsListRes;
            aCache.put(hotNewsUrl, newsListRes);
            //SpTools.setString(mContext , hotNewsUrl , gson.toJson(newsListRes));
        } else {
            list = vedioNewsList;
            adapter = homeHotVideoAdapter;
            HomeUI.this.videoListRes = newsListRes;
            //SpTools.setString(mContext , hotVideoNewsUrl , gson.toJson(newsListRes));
            aCache.put(hotVideoNewsUrl, newsListRes);
        }
        //先清除
        list.clear();

        //添加所有---------------
        if (newsListRes.hot != null) {
            list.addAll(newsListRes.hot.get(0));
        }


        adapter.setNewItemInterface(new HomeNewAdapter.NewsItemInterface() {
            @Override
            public void setNewOnItem(int position) {
                if ("1".equals(type)) {
                    //新闻
                    Intent intent = new Intent(HomeUI.this, H5DetailUI.class);
                    intent.putExtra("newsId", hotNewsList.get(position).cc_id);
                    intent.putExtra("type", "news");
                    startActivity(intent);
                } else {
                    //视频
                    Intent intent = new Intent(HomeUI.this, VideoH5Activity.class);
                    intent.putExtra("newsId", vedioNewsList.get(position).cc_id);
                    intent.putExtra("type", "video");
                    intent.putExtra("videoUrl", vedioNewsList.get(position).media.get(1));
                    startActivity(intent);
                }

            }

            @Override
            public void setVideoPlay(String url) {
                Intent intent = new Intent(HomeUI.this, MediaActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });

        adapter.notifyDataSetChanged();
    }

    private HotCirclesProtocal hotCirclesProtocal;

    private void getHotCircleData(final int page) {
        ub_id = SpTools.getUserId(this);

        if (hotCirclesProtocal == null) {
            hotCirclesProtocal = new HotCirclesProtocal();
        }
        //aCache.clear();
        HotCircleBean hotCircleBean = (HotCircleBean) aCache.getAsObject(hotCircleUrl);
        if (hotCircleBean != null) {
            if (hot == null) {
                hot = hotCircleBean.hot;
            } else {
                hot.clear();
                hot.addAll(hotCircleBean.hot);
            }
            //先清除
            hotList.clear();
            //添加所有---------------
            hotList.addAll(hot.get(page));
            homeHotCircleAdapter.notifyDataSetChanged();
            hot.clear();
        }
        if (hot == null || hot.size() == 0) {
            hotCirclesProtocal.getCirclesList(ub_id, new HotCirclesProtocal.HotNewsListener() {
                @Override
                public void hotNewsResponse(HotCircleBean hotCircleBean) {
                    aCache.put(hotCircleUrl, hotCircleBean);
                    initHotCircleDate(page, hotCircleBean);
                }
            });
        } else {
            hotList.clear();
            //添加所有---------------
            hotList.addAll(hot.get(page));

            homeHotCircleAdapter.notifyDataSetChanged();
            isCircleComplete = true;
            if (isNewComplete && isVideoComplete && isCircleComplete) {
                isNewComplete = false;
                isVideoComplete = false;
                isCircleComplete = false;
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    }

    /**
     * 布置热门圈子数据
     *
     * @param hotCircleBean
     */
    private void initHotCircleDate(int page, HotCircleBean hotCircleBean) {
        isCircleComplete = true;
        if (hotCircleBean == null) {
            if (isNewComplete && isVideoComplete && isCircleComplete) {
                isNewComplete = false;
                isVideoComplete = false;
                isCircleComplete = false;
                swipeRefreshLayout.setRefreshing(false);
            }
            return;
        }
        if (hot == null) {
            hot = hotCircleBean.hot;
        } else {
            hot.clear();
            hot.addAll(hotCircleBean.hot);
        }

        //先清除

        hotList.clear();

        //添加所有---------------
        hotList.addAll(hot.get(page));

        homeHotCircleAdapter.notifyDataSetChanged();
        if (isNewComplete && isVideoComplete && isCircleComplete) {
            isNewComplete = false;
            isVideoComplete = false;
            isCircleComplete = false;
            swipeRefreshLayout.setRefreshing(false);
        }
    }


    private boolean isNewComplete = false;
    private boolean isVideoComplete = false;
    private boolean isCircleComplete = false;

    @Override
    public void onRefresh() {
        if (hot != null) {
            hot.clear();
        }
        prepareData();

    }


}
