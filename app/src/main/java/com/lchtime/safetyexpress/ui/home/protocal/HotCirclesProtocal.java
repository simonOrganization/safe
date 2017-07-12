package com.lchtime.safetyexpress.ui.home.protocal;


import android.text.TextUtils;

import com.google.gson.JsonObject;
import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.HotCircleBean;
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

public class HotCirclesProtocal {

    public void getCirclesList(final HotNewsListener listener){
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.hotNewsResponse(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.hotqz));

        OkHttpUtils
                .post()
                .url(url)
                .addParams("ub_id", SpTools.getString(MyApplication.getContext(), Constants.userId, ""))
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
                        HotCircleBean hotCircleBean = (HotCircleBean) JsonUtils.stringToObject(response,HotCircleBean.class);

                        if(hotCircleBean.result.code.equals("10")){
                            if (listener != null){
                                listener.hotNewsResponse(hotCircleBean);
                                listener.hotNewsResponse(null);
                            }
                        }else{
                            CommonUtils.toastMessage(hotCircleBean.result.info);
                            listener.hotNewsResponse(null);
                        }
                    }
                });
    }

    public interface HotNewsListener{
        void hotNewsResponse(HotCircleBean hotCircleBean);
    }
}
