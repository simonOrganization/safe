package com.lchtime.safetyexpress.ui.home.protocal;

import android.text.TextUtils;

import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.Result;
import com.lchtime.safetyexpress.bean.WenDaBean;
import com.lchtime.safetyexpress.bean.WenDaDetailBean;
import com.lchtime.safetyexpress.bean.res.NewsListRes;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.JsonUtils;
import com.lchtime.safetyexpress.utils.SpTools;
import com.mzhy.http.okhttp.OkHttpUtils;
import com.mzhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by android-cp on 2017/5/11.
 */

public class HomeQuestionProtocal {
    public void getWenDaList( String page,final QuestionListener listener){
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.shouye));
        OkHttpUtils
                .post()
                .url(url)
                .addParams("ub_id", SpTools.getString(MyApplication.getContext(), Constants.userId, ""))
                .addParams("page", page)
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
                        WenDaBean bean = (WenDaBean) JsonUtils.stringToObject(response,WenDaBean.class);
                        if(bean.result.code.equals("10")){
                            if (listener != null){
                                listener.questionResponse(bean);
                            }
                        }else{
                            CommonUtils.toastMessage(bean.result.info);
                        }
                    }
                });
    }


    public void getWenDaDetail(String q_id ,final QuestionListener listener){
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.wdinfo));
        OkHttpUtils
                .post()
                .url(url)
                .addParams("ub_id", SpTools.getString(MyApplication.getContext(), Constants.userId, ""))
                .addParams("q_id", q_id)
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
                        WenDaDetailBean bean = (WenDaDetailBean) JsonUtils.stringToObject(response,WenDaDetailBean.class);
                        if(bean.result.code.equals("10")){
                            if (listener != null){
                                listener.questionResponse(bean);
                            }
                        }else{
                            CommonUtils.toastMessage(bean.result.info);
                        }
                    }
                });
    }

    public void postTiWenContent(String q_title,String q_description,String pic,final QuestionListener listener){
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.tiwen));
        OkHttpUtils
                .post()
                .url(url)
                .addParams("ub_id", SpTools.getString(MyApplication.getContext(), Constants.userId, ""))
                .addParams("q_title", q_title)
                .addParams("q_description", q_description)
                .addParams("pic", pic)
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
                        Result bean = (Result) JsonUtils.stringToObject(response,Result.class);
                        if(bean.result.code.equals("10")){
                            if (listener != null){
                                listener.questionResponse(bean);
                            }
                        }else{
                            CommonUtils.toastMessage(bean.result.info);
                        }
                    }
                });
    }

    public void postAnswerContent(String q_id,String context,String pic,final QuestionListener listener){
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.answer));
        OkHttpUtils
                .post()
                .url(url)
                .addParams("ub_id", SpTools.getString(MyApplication.getContext(), Constants.userId, ""))
                .addParams("q_id", q_id)
                .addParams("context", context)
                .addParams("pic", pic)
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
                        Result bean = (Result) JsonUtils.stringToObject(response,Result.class);
                        if(bean.result.code.equals("10")){
                            if (listener != null){
                                listener.questionResponse(bean);
                            }
                        }else{
                            CommonUtils.toastMessage(bean.result.info);
                        }
                    }
                });
    }

    public void postGuanZhu(String q_id,String type,final QuestionListener listener){
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.guanzhu));
        OkHttpUtils
                .post()
                .url(url)
                .addParams("ub_id", SpTools.getString(MyApplication.getContext(), Constants.userId, ""))
                .addParams("q_id", q_id)
                .addParams("type", type)
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
                        Result bean = (Result) JsonUtils.stringToObject(response,Result.class);
                        if(bean.result.code.equals("10")){
                            if (listener != null){
                                listener.questionResponse(bean);
                            }
                        }else{
                            CommonUtils.toastMessage(bean.result.info);
                        }
                    }
                });
    }
    public interface QuestionListener{
        void questionResponse(Object response);
    }
}
