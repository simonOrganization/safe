package com.lchtime.safetyexpress.ui.search.protocal;

import android.text.TextUtils;

import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.QJSearchBean;
import com.lchtime.safetyexpress.bean.QZSearchBean;
import com.lchtime.safetyexpress.bean.WordSerchBean;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.JsonUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by Dreamer on 2017/5/20.
 */

public class SerchProtocal {
    public void getHotSerchData(String type, final NormalListener listener){
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
           // CommonUtils.toastMessage("您当前无网络，请联网再试");
            return;
        }

        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.hotsearch));
        OkHttpUtils
                .post()
                .url(url)
                .addParams("type",type)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        //Toast.makeText(HomeNewsSearchUI.this, "网络不稳定，请检查后重试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        if (TextUtils.isEmpty(response)) {
                            CommonUtils.toastMessage("没有数据返回");
                            return;
                        }
                        WordSerchBean bean = (WordSerchBean) JsonUtils.stringToObject(response, WordSerchBean.class);
                        if (bean.result.code.equals("10")) {
                            if (listener != null) {
                                listener.normalResponse(bean);
                            }
                        } else {
                            CommonUtils.toastMessage(bean.result.info);
                        }
//
                    }
                });
    }

    //搜索
    public void getSearchResult(String search, final int urlType, String type, final NormalListener listener){
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            //CommonUtils.toastMessage("您当前无网络，请联网再试");
            return;
        }

        final String baseUrl = MyApplication.getContext().getResources().getString(R.string.service_host_address);
        String url = baseUrl;
        if (urlType == 0){
          //全局搜索
            url = baseUrl.concat(MyApplication.getContext().getResources().getString(R.string.qjsearch));
        }else if (urlType == 3){
          //圈子搜索
            url = baseUrl.concat(MyApplication.getContext().getResources().getString(R.string.qzsearch));
        }else if (urlType == 1 || urlType == 2){
          //新闻视频搜索
            url = baseUrl.concat(MyApplication.getContext().getResources().getString(R.string.xwspsearch));
        }else {
            url = baseUrl.concat(MyApplication.getContext().getResources().getString(R.string.qjsearch));
        }

        PostFormBuilder builder = OkHttpUtils
                                            .post()
                                            .url(url)
                                            .addParams("search",search);
        if (urlType == 1 || urlType == 2){
            builder.addParams("type", type);
        }
         builder.build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        //Toast.makeText(HomeNewsSearchUI.this, "网络不稳定，请检查后重试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        if (TextUtils.isEmpty(response)) {
                            CommonUtils.toastMessage("没有数据返回");
                            return;
                        }
                        Object bean = null;
                        switch (urlType){
                            case 0:
                                //全局搜索
                                bean = JsonUtils.stringToObject(response, QJSearchBean.class);
                                break;
                            case 1:
                            case 2:
                                //新闻视频
                                bean = JsonUtils.stringToObject(response, QJSearchBean.class);
                                break;
                            case 3:
                                //圈子搜索
                                bean = JsonUtils.stringToObject(response, QZSearchBean.class);
                                break;
                        }

                        listener.normalResponse(bean);
//
                    }
                });
    }





    public interface NormalListener{
        void normalResponse(Object response);
    }
}
