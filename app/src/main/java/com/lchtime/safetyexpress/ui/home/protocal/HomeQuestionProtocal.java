package com.lchtime.safetyexpress.ui.home.protocal;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.Result;
import com.lchtime.safetyexpress.bean.WenDaBean;
import com.lchtime.safetyexpress.bean.WenDaDetailBean;
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
            listener.questionResponse(null);
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
                        listener.questionResponse(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)){
                            CommonUtils.toastMessage("没有数据返回");
                            listener.questionResponse(null);
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


    public void getWenDaDetail(String page,String q_id ,final QuestionListener listener){
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.questionResponse(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.wdinfo));
        OkHttpUtils
                .post()
                .url(url)
                .addParams("ub_id", SpTools.getString(MyApplication.getContext(), Constants.userId, ""))
                .addParams("q_id", q_id)
                .addParams("page", page)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        listener.questionResponse(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)){
                            CommonUtils.toastMessage("没有数据返回");
                            listener.questionResponse(null);
                            return;
                        }
                        WenDaDetailBean bean = (WenDaDetailBean) JsonUtils.stringToObject(response,WenDaDetailBean.class);
                        if(bean.result.code.equals("10")){
                            if (listener != null){
                                listener.questionResponse(bean);
                            }
                        }else{
                            listener.questionResponse(null);
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

    private Gson gson = new Gson();
    public void postAnswerContent(String q_id,String context,String pic,final QuestionListener listener){
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.tjanswer));
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
                        listener.questionResponse(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)){
                            CommonUtils.toastMessage("没有数据返回");
                            listener.questionResponse(null);
                            return;
                        }
                        Result bean = null;
                        try {
                            bean = (Result) gson.fromJson(response, Result.class);
//                        Result bean = (Result) JsonUtils.stringToObject(response,Result.class);
                            if(bean.result.code.equals("10")){
                                if (listener != null){
                                    listener.questionResponse(bean);
                                }
                            }else{
                                CommonUtils.toastMessage(bean.result.info);
                                listener.questionResponse(null);
                            }
                        }catch (Exception exception){
                            listener.questionResponse(null);
                        }

                    }
                });
    }

    public void editAnswerContent(String a_id,String info,String pic,final QuestionListener listener){
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.questionResponse(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.tjanswer));
        OkHttpUtils
                .post()
                .url(url)
                .addParams("ub_id", SpTools.getString(MyApplication.getContext(), Constants.userId, ""))
                .addParams("a_id", a_id)
                .addParams("info", info)
                .addParams("pic", pic)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        listener.questionResponse(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)){
                            CommonUtils.toastMessage("没有数据返回");
                            listener.questionResponse(null);
                            return;
                        }
                        Result bean = null;
                        try {
                            bean = (Result) gson.fromJson(response, Result.class);
//                        Result bean = (Result) JsonUtils.stringToObject(response,Result.class);
                            if(bean.result.code.equals("10")){
                                if (listener != null){
                                    listener.questionResponse(bean);
                                }
                            }else{
                                CommonUtils.toastMessage(bean.result.info);
                                listener.questionResponse(null);
                            }
                        }catch (Exception exception){
                            listener.questionResponse(null);
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

    public void postInviteFriends(String q_id,String friend_username,final QuestionListener listener){
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.invite));
        OkHttpUtils
                .post()
                .url(url)
                .addParams("ub_id", SpTools.getString(MyApplication.getContext(), Constants.userId, ""))
                .addParams("q_id", q_id)
                .addParams("friend_username", friend_username)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        listener.questionResponse(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)){
                            CommonUtils.toastMessage("没有数据返回");
                            listener.questionResponse(null);
                            return;
                        }
                        try {
                            Result bean = (Result) JsonUtils.stringToObject(response, Result.class);
                            if (bean.result.code.equals("10")) {
                                if (listener != null) {
                                    listener.questionResponse(response);
                                }
                            } else {
                                CommonUtils.toastMessage(bean.result.info);
                                listener.questionResponse(null);
                            }
                        }catch (Exception exception){
                            listener.questionResponse(null);
                        }
                    }
                });
    }


    public void getMyFriends(final QuestionListener listener){
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.questionResponse(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.gzlist));
        OkHttpUtils
                .post()
                .url(url)
                .addParams("ub_id", SpTools.getString(MyApplication.getContext(), Constants.userId, ""))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        listener.questionResponse(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)){
                            CommonUtils.toastMessage("没有数据返回");
                            listener.questionResponse(null);
                            return;
                        }
                        try {
                            Result bean = (Result) JsonUtils.stringToObject(response, Result.class);
                            if (bean.result.code.equals("10")) {
                                if (listener != null) {
                                    listener.questionResponse(response);
                                }
                            } else {
                                CommonUtils.toastMessage(bean.result.info);
                                listener.questionResponse(null);
                            }
                        }catch (Exception exception){
                            listener.questionResponse(null);
                        }
                    }
                });
    }

    public interface QuestionListener{
        void questionResponse(Object response);
    }
}
