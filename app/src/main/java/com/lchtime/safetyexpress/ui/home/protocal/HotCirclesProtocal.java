package com.lchtime.safetyexpress.ui.home.protocal;


import android.text.TextUtils;
import android.util.Log;

import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.HotCircleBean;
import com.lchtime.safetyexpress.ui.Const;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.JsonUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by android-cp on 2017/5/5.
 */

public class HotCirclesProtocal {

    public void getCirclesList(final String ub_id , final HotNewsListener listener){
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            //CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.hotNewsResponse(null);
            return;
        }
        final String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.hotqz));
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
                        HotCircleBean hotCircleBean = (HotCircleBean) JsonUtils.stringToObject(response,HotCircleBean.class);

                        if(hotCircleBean.result.code.equals("10")){
                            if (listener != null){
                                listener.hotNewsResponse(hotCircleBean);
                                //listener.hotNewsResponse(null);
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
