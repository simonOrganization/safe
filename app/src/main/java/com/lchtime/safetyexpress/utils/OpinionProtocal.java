package com.lchtime.safetyexpress.utils;

import android.support.design.widget.TabLayout;
import android.util.Log;
import android.widget.Toast;

import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.NewsFragmentAdapter;
import com.lchtime.safetyexpress.bean.AdviceBean;
import com.lchtime.safetyexpress.bean.BasicResult;
import com.lchtime.safetyexpress.bean.NewTypeBean;
import com.lchtime.safetyexpress.bean.Result;
import com.lchtime.safetyexpress.bean.res.NewsRes;
import com.lchtime.safetyexpress.ui.Const;
import com.lchtime.safetyexpress.ui.news.HomeNewActivity;
import com.lchtime.safetyexpress.ui.news.HomeNewsFragment;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;

import okhttp3.Call;

/**
 * Created by android-cp on 2017/5/2.
 */

public class OpinionProtocal {
    public OpinionResultListener mListener;
    public void getDataInternet(String advice,String filedid,String userID,String phone,OpinionResultListener listener){
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            return;
        }
        this.mListener = listener;
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.advice));
        OkHttpUtils
                .post()
                .url(url)
                .addParams("advice",advice)
                .addParams("fileid",filedid)
                .addParams("phone",phone)
                .addParams("ub_id",userID)
                .addParams("uo_long","")
                .addParams("uo_lat","")
                .addParams("uo_high","")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.i("--------","getTabData======="+response);
                        AdviceBean result = (AdviceBean) JsonUtils.stringToObject(response, AdviceBean.class);
                        if (result == null){
                            Toast.makeText(MyApplication.getContext(),"请求失败",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if("10".equals(result.result.code)){
                            if (mListener != null) {
                                mListener.onResponseMessage(result);
                            }
                            Toast.makeText(MyApplication.getContext(),"提交反馈成功",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(MyApplication.getContext(),"提交反馈失败，请再次提交！",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public interface OpinionResultListener{
        void onResponseMessage(Object result);
    }

}
