package com.lchtime.safetyexpress.ui.home.protocal;

import android.text.TextUtils;

import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.res.NewsListRes;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.JsonUtils;
import com.mzhy.http.okhttp.OkHttpUtils;
import com.mzhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by Dreamer on 2017/6/19.
 */

public class PictureAdvantage {
    public void getFirstPic(String ub_id, final HotNewsListener listener){
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
           // CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.hotNewsResponse(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.lunbo));
        OkHttpUtils
                .post()
                .url(url)
                .addParams("ub_id", ub_id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        listener.hotNewsResponse(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)){
                            CommonUtils.toastMessage("没有数据返回");
                            listener.hotNewsResponse(null);
                            return;
                        }
                        NewsListRes newsListRes = (NewsListRes) JsonUtils.stringToObject(response,NewsListRes.class);
                        if(newsListRes.getResult().getCode().equals("10")){
                            if (listener != null){
                                listener.hotNewsResponse(response);
                            }
                        }else{
                            CommonUtils.toastMessage(newsListRes.getResult().getInfo());
                            listener.hotNewsResponse(null);
                        }
                    }
                });
    }

    /**
     * 获取圈子首页的轮播图
     * @param ub_id
     * @param listener
     */
    public void getQZPic(String ub_id, final HotNewsListener listener){
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
           // CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.hotNewsResponse(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.qzlunbo));
        OkHttpUtils
                .post()
                .url(url)
                .addParams("ub_id", ub_id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        listener.hotNewsResponse(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)){
                            CommonUtils.toastMessage("没有数据返回");
                            listener.hotNewsResponse(null);
                            return;
                        }
                        NewsListRes newsListRes = (NewsListRes) JsonUtils.stringToObject(response,NewsListRes.class);
                        if(newsListRes.getResult().getCode().equals("10")){
                            if (listener != null){
                                listener.hotNewsResponse(response);
                            }
                        }else{
                            CommonUtils.toastMessage(newsListRes.getResult().getInfo());
                            listener.hotNewsResponse(null);
                        }
                    }
                });
    }

    public interface HotNewsListener{
        void hotNewsResponse(String respose);
    }
}
