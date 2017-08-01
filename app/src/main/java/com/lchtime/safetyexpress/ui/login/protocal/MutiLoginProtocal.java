package com.lchtime.safetyexpress.ui.login.protocal;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Third1Bean;
import com.lchtime.safetyexpress.bean.Third2Bean;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.mzhy.http.okhttp.OkHttpUtils;
import com.mzhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by Dreamer on 2017/6/19.
 */

public class MutiLoginProtocal {
    private Gson gson = new Gson();

    public void postMutiLogin(String tp_openid,String tp_username,String tp_head,
                              String tp_gender,String type,String clientid,final MutiLoginListener listener){
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.normalResponse(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.tparty));
        if(clientid == null) clientid = "";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("tp_openid", tp_openid)
                .addParams("tp_username", tp_username)
                .addParams("tp_head", tp_head)
                .addParams("tp_gender", tp_gender)
                .addParams("type", type)
                .addParams("cid",clientid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        listener.normalResponse(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)){
//                            CommonUtils.toastMessage("没有数据返回");
                            listener.normalResponse(null);
                            return;
                        }
                        Third1Bean bean = null;
                        try {
                            bean = gson.fromJson(response, Third1Bean.class);
//                        Result bean = (Result) JsonUtils.stringToObject(response,Result.class);
                            if(bean.result.code.equals("10")){
                                if (listener != null){
                                    listener.normalResponse(response);
                                }
                            }else{
                                CommonUtils.toastMessage(bean.result.info);
                                listener.normalResponse(null);
                            }
                        }catch (Exception exception){
                            listener.normalResponse(null);
                        }

                    }
                });
    }


    public void postMutiNewLogin(String tp_openid,String tp_username,String tp_head,String tp_gender,String type,String ub_phone,
                                 String ud_pwd,String clientid ,final MutiLoginListener listener){
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.normalResponse(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.addtparty));
        OkHttpUtils
                .post()
                .url(url)
                .addParams("tp_openid", tp_openid)
                .addParams("tp_username", tp_username)
                .addParams("tp_head", tp_head)
                .addParams("tp_gender", tp_gender)
                .addParams("ub_phone", ub_phone)
                .addParams("ud_pwd", ud_pwd)
                .addParams("type", type)
                .addParams("cid",clientid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        listener.normalResponse(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)){
//                            CommonUtils.toastMessage("没有数据返回");
                            listener.normalResponse(null);
                            return;
                        }
                        Third2Bean bean = null;
                        try {
                            bean = (Third2Bean) gson.fromJson(response, Third2Bean.class);
                            if(bean.result.code.equals("10")){
                                if (listener != null){
                                    listener.normalResponse(response);
                                }
                            }else{
                                CommonUtils.toastMessage(bean.result.info);
                                listener.normalResponse(null);
                            }
                        }catch (Exception exception){
                            listener.normalResponse(null);
                        }

                    }
                });
    }


    public interface MutiLoginListener{
        void normalResponse(Object response);
    }
}
