package com.lchtime.safetyexpress.ui.home.protocal;

import android.text.TextUtils;

import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.res.VideoRes;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.JsonUtils;
import com.lchtime.safetyexpress.utils.SpTools;
import com.mzhy.http.okhttp.OkHttpUtils;
import com.mzhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by Dreamer on 2017/6/27.
 */

public class ShareProtocal {

    public void postShare( final ShareInfo listener){
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.shareResponse(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.share));
        OkHttpUtils
                .post()
                .url(url)
                .addParams("ub_id", SpTools.getUserId(MyApplication.getContext()))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        listener.shareResponse(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)){
                            CommonUtils.toastMessage("没有数据返回");
                            listener.shareResponse(null);
                            return;
                        }
                        VideoRes videoRes = (VideoRes) JsonUtils.stringToObject(response,VideoRes.class);
                        if(videoRes.getResult().getCode().equals("10")){
                            if (listener != null){
                                listener.shareResponse(response);
                            }
                        }else{
                            CommonUtils.toastMessage(videoRes.getResult().getInfo());
                            listener.shareResponse(null);
                        }
                    }
                });
    }


    public void postGetMoney( final ShareInfo listener){
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.shareResponse(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.regreward));
        OkHttpUtils
                .post()
                .url(url)
                .addParams("ub_id", SpTools.getUserId(MyApplication.getContext()))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        listener.shareResponse(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)){
                            CommonUtils.toastMessage("没有数据返回");
                            listener.shareResponse(null);
                            return;
                        }
                        VideoRes videoRes = (VideoRes) JsonUtils.stringToObject(response,VideoRes.class);
                        if(videoRes.getResult().getCode().equals("10")){
                            if (listener != null){
                                listener.shareResponse(response);
                            }
                        }else{
                            CommonUtils.toastMessage(videoRes.getResult().getInfo());
                            listener.shareResponse(null);
                        }
                    }
                });
    }


    public void postCircleShare( String qc_id,final ShareInfo listener){
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.shareResponse(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.qzshare));
        OkHttpUtils
                .post()
                .url(url)
                .addParams("ub_id", SpTools.getUserId(MyApplication.getContext()))
                .addParams("qc_id", qc_id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        listener.shareResponse(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)){
                            CommonUtils.toastMessage("没有数据返回");
                            listener.shareResponse(null);
                            return;
                        }
                        VideoRes videoRes = (VideoRes) JsonUtils.stringToObject(response,VideoRes.class);
                        if(videoRes.getResult().getCode().equals("10")){
                            if (listener != null){
                                listener.shareResponse(response);
                            }
                        }else{
                            CommonUtils.toastMessage(videoRes.getResult().getInfo());
                            listener.shareResponse(null);
                        }
                    }
                });
    }


    public interface ShareInfo{
        void shareResponse(String response);
    }
}
