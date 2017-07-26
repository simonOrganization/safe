package com.lchtime.safetyexpress.ui.home.protocal;

import android.text.TextUtils;

import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.res.VideoRes;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.JsonUtils;
import com.mzhy.http.okhttp.OkHttpUtils;
import com.mzhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by android-cp on 2017/5/8.
 */

public class VideoProtocal {
    public void getVideoDir( final VideoDirListener listener){
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.videoDirResponse(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.videodir));
        OkHttpUtils
                .post()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        listener.videoDirResponse(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)){
                            CommonUtils.toastMessage("没有数据返回");
                            listener.videoDirResponse(null);
                            return;
                        }
                        VideoRes videoRes = (VideoRes) JsonUtils.stringToObject(response,VideoRes.class);
                        if(videoRes.getResult().getCode().equals("10")){
                            if (listener != null){
                                listener.videoDirResponse(videoRes);
                            }
                        }else{
                            CommonUtils.toastMessage(videoRes.getResult().getInfo());
                        }
                    }
                });
    }

    public void getVideoList( String page,String cd_id,final VideoListListener listener){
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.videolist));
        OkHttpUtils
                .post()
                .url(url)
                .addParams("cd_id",cd_id)
                .addParams("page",page)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)){
                            CommonUtils.toastMessage("没有数据返回");
                            return;
                        }
                        VideoRes videoRes = (VideoRes) JsonUtils.stringToObject(response,VideoRes.class);
                        if(videoRes.getResult().getCode().equals("10")){
                            if (listener != null){
                                listener.videoListResponse(videoRes);
                            }
                        }else{
                            CommonUtils.toastMessage(videoRes.getResult().getInfo());
                        }
                    }
                });
    }

    public interface VideoDirListener{
        void videoDirResponse(VideoRes response);
    }

    public interface VideoListListener{
        void videoListResponse(VideoRes videoRes);
    }
}
