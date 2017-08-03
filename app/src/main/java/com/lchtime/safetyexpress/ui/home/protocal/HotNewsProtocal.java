package com.lchtime.safetyexpress.ui.home.protocal;


import android.text.TextUtils;

import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.res.NewsListRes;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.JsonUtils;
import com.lchtime.safetyexpress.utils.SpTools;
import com.mzhy.http.okhttp.OkHttpUtils;
import com.mzhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;


/**
 * Created by android-cp on 2017/5/5.
 */

public class HotNewsProtocal {

    public void getNewsList(String type, final HotNewsListener listener){
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.hotNewsResponse(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.indexhot));
        OkHttpUtils
                .post()
                .url(url)
                .addParams("ub_id", SpTools.getUserId(MyApplication.getContext()))
                .addParams("type", type)
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

                        try {
                            NewsListRes newsListRes = (NewsListRes) JsonUtils.stringToObject(response, NewsListRes.class);
                            if (newsListRes.getResult().getCode().equals("10")) {
                                if (listener != null) {
                                    listener.hotNewsResponse(newsListRes);
                                }
                            } else {
                                CommonUtils.toastMessage(newsListRes.getResult().getInfo());
                                listener.hotNewsResponse(null);
                            }
                        }catch (Exception exception){
                            listener.hotNewsResponse(null);
                        }
                    }
                });
    }

    public interface HotNewsListener{
        void hotNewsResponse(NewsListRes newsListRes);
    }
}
