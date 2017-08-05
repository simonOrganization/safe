package com.lchtime.safetyexpress.ui.home.protocal;

import android.text.TextUtils;

import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.H5Bean;
import com.lchtime.safetyexpress.bean.Result;
import com.lchtime.safetyexpress.bean.WdMessageBean;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.JsonUtils;
import com.lchtime.safetyexpress.utils.SpTools;
import com.mzhy.http.okhttp.OkHttpUtils;
import com.mzhy.http.okhttp.builder.PostFormBuilder;
import com.mzhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by Dreamer on 2017/6/26.
 */

public class H5Protocal {

    public void getH5Info(String cc_id,String type,String a_id,final H5Listener listener){
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
           // CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.H5Response(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.h5));
        OkHttpUtils
                .post()
                .url(url)
                .addParams("ub_id", SpTools.getUserId(MyApplication.getContext()))
                //新闻id  type=3时是圈子id type=4是问题id
                .addParams("cc_id", cc_id)
//              1 新闻  2 视频   3 圈子  4问答
                .addParams("type", type)
                //type=4传  反之传0
                .addParams("a_id", a_id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        listener.H5Response(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)){
                            CommonUtils.toastMessage("没有数据返回");
                            listener.H5Response(null);
                            return;
                        }
                        try {
                            H5Bean bean = (H5Bean) JsonUtils.stringToObject(response, H5Bean.class);
                            if (bean.result.code.equals("10")) {
                                if (listener != null) {
                                    listener.H5Response(response);
                                }
                            } else {
                                CommonUtils.toastMessage(bean.result.info);
                                listener.H5Response(null);
                            }
                        }catch (Exception exception){
                            listener.H5Response(null);
                        }
                    }
                });
    }

    public void setDzDc(String cc_id,String type,String action,final H5Listener listener){
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
           // CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.H5Response(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.dzdc));
        OkHttpUtils
                .post()
                .url(url)
                .addParams("ub_id", SpTools.getUserId(MyApplication.getContext()))
                .addParams("cc_id", cc_id)
//              0赞  1踩
                .addParams("type", type)
                //0 执行   1 取消
                .addParams("action", action)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        listener.H5Response(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)){
                            CommonUtils.toastMessage("没有数据返回");
                            listener.H5Response(null);
                            return;
                        }
                        try {
                            Result bean = (Result) JsonUtils.stringToObject(response, Result.class);
                            if (bean.result.code.equals("10")) {
                                if (listener != null) {
                                    listener.H5Response(response);
                                }
                            } else {
                                CommonUtils.toastMessage(bean.result.info);
                                listener.H5Response(null);
                            }
                        }catch (Exception exception){
                            listener.H5Response(null);
                        }
                    }
                });
    }


    public void setQZDzDc(String cc_id,String type ,String action ,final H5Listener listener){
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
           // CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.H5Response(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.qznewsdz));
        PostFormBuilder builder = OkHttpUtils
                .post()
                .url(url)
                .addParams("ub_id", SpTools.getUserId(MyApplication.getContext()))
                .addParams("cc_id", cc_id);
                //0 执行   1 取消
//              0赞  1踩
                if ("0".equals(type)){
                    builder.addParams("qc_agr","0");
                }else {
                   // builder.addParams("qc_aga","0");
                }
                if ("1".equals(action)) {
                     builder
                //取消
                .addParams("action", action);

                 }else {
        //  builder
        //反对
        //  .addParams("action", action);
                }

                builder.build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        listener.H5Response(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)){
                            CommonUtils.toastMessage("没有数据返回");
                            listener.H5Response(null);
                            return;
                        }
                        try {
                            Result bean = (Result) JsonUtils.stringToObject(response, Result.class);
                            if (bean.result.code.equals("10")) {
                                if (listener != null) {
                                    listener.H5Response(response);
                                }
                            } else {
                                CommonUtils.toastMessage(bean.result.info);
                                listener.H5Response(null);
                            }
                        }catch (Exception exception){
                            listener.H5Response(null);
                        }
                    }
                });
    }


    public void setWDDzDc(String a_id,String type,String action,final H5Listener listener){
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
           // CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.H5Response(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.wddzdc));
        PostFormBuilder builder = OkHttpUtils
                .post()
                .url(url)
                .addParams("ub_id", SpTools.getUserId(MyApplication.getContext()))
                .addParams("a_id", a_id);
//              0赞  1踩
        builder.addParams("type",type)
                //0 执行   1 取消
                .addParams("action",action);

        builder.build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        listener.H5Response(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)){
                            CommonUtils.toastMessage("没有数据返回");
                            listener.H5Response(null);
                            return;
                        }
                        try {
                            Result bean = (Result) JsonUtils.stringToObject(response, Result.class);
                            if (bean.result.code.equals("10")) {
                                if (listener != null) {
                                    listener.H5Response(response);
                                }
                            } else {
                                CommonUtils.toastMessage(bean.result.info);
                                listener.H5Response(null);
                            }
                        }catch (Exception exception){
                            listener.H5Response(null);
                        }
                    }
                });
    }


    public void setCollect(String cc_id,String type,String uc_module,final H5Listener listener){
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
           // CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.H5Response(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.collect));
        PostFormBuilder builder = OkHttpUtils
                .post()
                .url(url)
                .addParams("ub_id", SpTools.getUserId(MyApplication.getContext()))
                .addParams("cc_id", cc_id);
//              0收藏  1取消收藏
        builder.addParams("type",type)
                //0 新闻   9视频 5圈子
                .addParams("uc_module",uc_module);

        builder.build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        listener.H5Response(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)){
                            CommonUtils.toastMessage("没有数据返回");
                            listener.H5Response(null);
                            return;
                        }
                        try {
                            Result bean = (Result) JsonUtils.stringToObject(response, Result.class);
                            if (bean.result.code.equals("10")) {
                                if (listener != null) {
                                    listener.H5Response(response);
                                }
                            } else {
                                CommonUtils.toastMessage(bean.result.info);
                                listener.H5Response(null);
                            }
                        }catch (Exception exception){
                            listener.H5Response(null);
                        }
                    }
                });
    }


    public void setNewsCommen(String ccp_cs_id , String ccp_cc_id , String ccp_info , final H5Listener listener){
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
           // CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.H5Response(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.fbpl));
        PostFormBuilder builder = OkHttpUtils
                .post()
                .url(url)
                .addParams("ub_id", SpTools.getUserId(MyApplication.getContext()))
                //回复评论上级ID，如果一级评论为0
                .addParams("ccp_cs_id", ccp_cs_id)
                //新闻或视频ID
                .addParams("ccp_cc_id", ccp_cc_id)
                //评论内容
                .addParams("ccp_info", ccp_info);
//

        builder.build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        listener.H5Response(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)){
                            CommonUtils.toastMessage("没有数据返回");
                            listener.H5Response(null);
                            return;
                        }
                        try {
                            Result bean = (Result) JsonUtils.stringToObject(response, Result.class);
                            if (bean.result.code.equals("10")) {
                                if (listener != null) {
                                    listener.H5Response(response);
                                }
                            } else {
                                CommonUtils.toastMessage(bean.result.info);
                                listener.H5Response(null);
                            }
                        }catch (Exception exception){
                            listener.H5Response(null);
                        }
                    }
                });
    }

    public void setCircleCommen(String qc_id,String qz_pinglun,String qp_hf_param,final H5Listener listener){
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
           // CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.H5Response(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.qzfbpl));
        PostFormBuilder builder = OkHttpUtils
                .post()
                .url(url)
                .addParams("ub_id", SpTools.getUserId(MyApplication.getContext()))
                //圈子ID
                .addParams("qc_id", qc_id)
                //评论内容
                .addParams("qz_pinglun", qz_pinglun)
                //一级为0  二级为要回复的评论的ID
                .addParams("qp_hf_param", qp_hf_param);
//

        builder.build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        listener.H5Response(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)){
                            CommonUtils.toastMessage("没有数据返回");
                            listener.H5Response(null);
                            return;
                        }
                        try {
                            Result bean = (Result) JsonUtils.stringToObject(response, Result.class);
                            if (bean.result.code.equals("10")) {
                                if (listener != null) {
                                    listener.H5Response(response);
                                }
                            } else {
                                CommonUtils.toastMessage(bean.result.info);
                                listener.H5Response(null);
                            }
                        }catch (Exception exception){
                            listener.H5Response(null);
                        }
                    }
                });
    }


    public void setWDCommen(String a_id , String info , String ar_cs_id  , final H5Listener listener){
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            //CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.H5Response(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.wdfbpl));
        PostFormBuilder builder = OkHttpUtils
                .post()
                .url(url)
                .addParams("ub_id", SpTools.getUserId(MyApplication.getContext()))
                //回答ID
                .addParams("a_id", a_id)
                //评论内容
                .addParams("info", info)
                //一级为0  二级为要回复的评论的ID
                .addParams("ar_cs_id", ar_cs_id);
//

        builder.build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        listener.H5Response(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)){
                            CommonUtils.toastMessage("没有数据返回");
                            listener.H5Response(null);
                            return;
                        }
                        try {
                            Result bean = (Result) JsonUtils.stringToObject(response, Result.class);
                            if (bean.result.code.equals("10")) {
                                if (listener != null) {
                                    listener.H5Response(response);
                                }
                            } else {
                                CommonUtils.toastMessage(bean.result.info);
                                listener.H5Response(null);
                            }
                        }catch (Exception exception){
                            listener.H5Response(null);
                        }
                    }
                });
    }
    public interface H5Listener{
        void H5Response(String response);
    }
    public void getWDMessage(String a_id  , final H5Listener listener){
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            //CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.H5Response(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.xiangqing));
        PostFormBuilder builder = OkHttpUtils
                .post()
                .url(url)
                .addParams("ub_id", SpTools.getUserId(MyApplication.getContext()))
                //回答ID
                .addParams("a_id", a_id);
//
        builder.build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        listener.H5Response(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)){
                            CommonUtils.toastMessage("没有数据返回");
                            listener.H5Response(null);
                            return;
                        }
                        try {
                            WdMessageBean bean = (WdMessageBean) JsonUtils.stringToObject(response, WdMessageBean.class);
                            if (bean.getResult().getCode().equals("10")) {
                                if (listener != null) {
                                    listener.H5Response(response);
                                }
                            } else {
                                //CommonUtils.toastMessage(bean.WdMessageBean.info);
                                listener.H5Response(null);
                            }
                        }catch (Exception exception){
                            listener.H5Response(null);
                        }
                    }
                });
    }

}
