package com.lchtime.safetyexpress.ui.circle.protocal;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.AddSubscribBean;
import com.lchtime.safetyexpress.bean.CircleItemUpBean;
import com.lchtime.safetyexpress.bean.CircleRedPointBean;
import com.lchtime.safetyexpress.bean.MyCircleActiveBean;
import com.lchtime.safetyexpress.bean.MydyBean;
import com.lchtime.safetyexpress.bean.Result;
import com.lchtime.safetyexpress.bean.SingleInfoBean;
import com.lchtime.safetyexpress.bean.UpdateResponse;
import com.lchtime.safetyexpress.bean.res.CircleBean;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.DialogUtil;
import com.lchtime.safetyexpress.utils.JsonUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by android-cp on 2017/5/8.
 */

public class CircleProtocal {
    public void getCircleList( String ub_id,String page,String type,String order,final CircleListener listener) {
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
           // CommonUtils.toastMessage("您当前无网络，请联网再试");
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.qzlist));
        OkHttpUtils
                .post()
                .addParams("ub_id",ub_id)
                //页码
                .addParams("page",page)
                //0 对应标签的 1 获取附近的圈子 2 获取我关注的圈子 3 获取互相关注的圈子 4 获取全部圈子
                .addParams("type",type)
                //0: 按最新排序 1: 按热门排序 2: 按订阅量排序
                .addParams("order",order)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)) {
                            CommonUtils.toastMessage("没有数据返回");
                            return;
                        }
                        CircleBean circleBean = (CircleBean) JsonUtils.stringToObject(response, CircleBean.class);
                        if (circleBean.result.code.equals("10")) {
                            if (listener != null) {
                                listener.circleResponse(circleBean);
                            }
                        } else {
                            CommonUtils.toastMessage(circleBean.result.getInfo());
                        }
                    }
                });
    }


    public void getCircleSelectedList( String ub_id,String page,String type,String ud_profession,String ud_post,String ud_addr,String order,final CircleListener listener) {
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
           // CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.circleResponse(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.qzlist));
        OkHttpUtils
                .post()
                .addParams("ub_id",ub_id)
                //页码
                .addParams("page",page)
                //0 对应标签的 1 获取附近的圈子 2 获取我关注的圈子 3 获取互相关注的圈子 4 获取全部圈子
                .addParams("type",type)
                //行业
                .addParams("ud_profession",ud_profession)
                //岗位
                .addParams("ud_post",ud_post)
                //地址
                .addParams("ud_addr",ud_addr)
                //0: 按最新排序 1: 按热门排序 2: 按订阅量排序
                .addParams("order",order)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        CommonUtils.toastMessage("错误");
                        listener.circleResponse(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)) {
                            CommonUtils.toastMessage("没有数据返回");
                            return;
                        }
                        CircleBean circleBean = (CircleBean) JsonUtils.stringToObject(response, CircleBean.class);
                        if (circleBean.result.code.equals("10")) {
                            if (listener != null) {
                                listener.circleResponse(circleBean);
                            }
                        } else {
                            CommonUtils.toastMessage(circleBean.result.getInfo());
                            listener.circleResponse(null);
                        }
                    }
                });
    }

    public void changeSubscribe( String ub_id,String f_ub_id,String action,final CircleListener listener) {
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
           // CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.circleResponse(null);
            return;
        }
        //Log.i("qaz", "onClick: " +ub_id+"-----"+f_ub_id+"-------"+ action);
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.qzdy));
        OkHttpUtils
                .post()
                .addParams("ub_id",ub_id)
                //页码
                .addParams("f_ub_id",f_ub_id)
                //0: 按最新排序 1: 按热门排序 2: 按订阅量排序
                .addParams("action",action)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        listener.circleResponse(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)) {
                            CommonUtils.toastMessage("没有数据返回");
                            listener.circleResponse(null);
                            return;
                        }
                        CircleBean circleBean = (CircleBean) JsonUtils.stringToObject(response, CircleBean.class);
                        if (circleBean.result.code.equals("10")) {
                            if (listener != null) {
                                listener.circleResponse(circleBean);
                            }
                        } else {
                            CommonUtils.toastMessage(circleBean.result.info);
                            //listener.circleResponse(null);
                        }
                    }
                });
    }

    public void updataZanOrCai( String ub_id,String qc_id,String qc_agr,String qc_aga,String action ,final NormalListener listener) {
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
            //CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.normalResponse(null);
            return;
        }
        Log.i("qaz", "onClick: 点赞" + ub_id  + "---ub_id----" + qc_id  +"-----qc_id----"+ qc_agr  + "----qc_agr----" + qc_aga
                +"-----qc_aga----"+ action  + "------action" );

        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.newsdz));
        PostFormBuilder builder = OkHttpUtils
                .post()
                .addParams("ub_id", ub_id)
                //新闻id
                .addParams("cc_id", qc_id);
                //
               // .addParams("action", action);
        if ("1".equals(qc_agr)) {
            builder
                    //同意
                    .addParams("qc_agr", qc_agr);

        }else {
            builder
                   // 反对
                    .addParams("qc_aga", qc_aga);
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
        builder
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        listener.normalResponse(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)) {
                            CommonUtils.toastMessage("没有数据返回");
                            listener.normalResponse(null);
                            return;
                        }
                        Result result = (Result) JsonUtils.stringToObject(response, Result.class);
                        if (result.result.code.equals("10")) {
                            if (listener != null) {
                                listener.normalResponse(result);
                            }
                        } else {
                            listener.normalResponse(null);
                           // CommonUtils.toastMessage(result.result.getInfo());
                        }
                    }
                });
    }


    public void getItemInfo( String ub_id,String qc_id,final NormalListener listener) {
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
           // CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.normalResponse(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.clickdz));
        PostFormBuilder builder = OkHttpUtils
                .post()
                .addParams("ub_id", ub_id)
                //新闻id
                .addParams("qc_id", qc_id);
        builder
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        listener.normalResponse(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)) {
                            CommonUtils.toastMessage("没有数据返回");
                            listener.normalResponse(null);
                            return;
                        }
                        CircleItemUpBean result = (CircleItemUpBean) JsonUtils.stringToObject(response, CircleItemUpBean.class);
                        if (result.result.code.equals("10")) {
                            if (listener != null) {
                                listener.normalResponse(result);
                            }
                        } else {
                            listener.normalResponse(result);
                            // CommonUtils.toastMessage(result.result.getInfo());
                        }
                    }
                });
    }

    public void getMySubscribe( String page,String ub_id,final NormalListener listener) {
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
           // CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.normalResponse(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.mydy));
        OkHttpUtils
                .post()
                .addParams("ub_id",ub_id)
                .addParams("page",page)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        listener.normalResponse(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)) {
                            CommonUtils.toastMessage("没有数据返回");
                            listener.normalResponse(null);
                            return;
                        }
                        MydyBean mydyBean = (MydyBean) JsonUtils.stringToObject(response, MydyBean.class);
                        if (mydyBean.result.code.equals("10")) {
                            if (listener != null) {
                                listener.normalResponse(mydyBean);
                            }
                        } else {
                            CommonUtils.toastMessage(mydyBean.result.info);
                            listener.normalResponse(null);
                        }
                    }
                });
    }

    public void getAddDyData( String ub_id,String hy,String gw,String addr,String action,String page,final NormalListener listener) {
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
           // CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.normalResponse(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.adddy));
        OkHttpUtils
                .post()
                .addParams("ub_id",ub_id)
                .addParams("hy",hy)
                .addParams("gw",gw)
                .addParams("addr",addr)
                .addParams("action",action)
                .addParams("page",page)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        listener.normalResponse(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)) {
                            CommonUtils.toastMessage("没有数据返回");
                            listener.normalResponse(null);
                            return;
                        }
                        try {
                            AddSubscribBean addSubscribBean = (AddSubscribBean) JsonUtils.stringToObject(response, AddSubscribBean.class);
                            if (addSubscribBean.result.code.equals("10")) {
                                if (listener != null) {
                                    listener.normalResponse(addSubscribBean);
                                }
                            } else {
                                listener.normalResponse(null);
                                CommonUtils.toastMessage(addSubscribBean.result.info);
                            }
                        }catch (Exception exception){
                            listener.normalResponse(null);
                        }
                    }
                });
    }

    public void getUpdataVideoData(String ub_id, String qc_context, String qc_video, String pic, final DialogUtil dialog , final NormalListener listener) {
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
           // CommonUtils.toastMessage("您当前无网络，请联网再试");
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.qzfb));
        OkHttpUtils
                .post()
                .addParams("ub_id",ub_id)
                .addParams("qc_context",qc_context)
                .addParams("qc_video",qc_video)
                .addParams("pic",pic)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        dialog.dissmiss();
                        CommonUtils.toastMessage("错误");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        dialog.dissmiss();
                        if (TextUtils.isEmpty(response)) {
                            CommonUtils.toastMessage("没有数据返回");
                            return;
                        }
                        Result bean = (Result) JsonUtils.stringToObject(response, Result.class);
                        if (bean.result.code.equals("10")) {
                            if (listener != null) {
                                listener.normalResponse(bean);
                            }
                        } else {
                            CommonUtils.toastMessage(bean.result.info);
                        }
                    }
                });
    }

    public void getUpdataCommonData(String ub_id, String qc_context, String pic , final DialogUtil mDialog , final NormalListener listener) {
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
            //CommonUtils.toastMessage("您当前无网络，请联网再试");
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.qzfb));
        OkHttpUtils
                .post()
                .addParams("ub_id",ub_id)
                .addParams("qc_context",qc_context)
                .addParams("qc_video","")
                .addParams("pic",pic)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mDialog.dissmiss();
                        CommonUtils.toastMessage("错误");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mDialog.dissmiss();
                        if (TextUtils.isEmpty(response)) {
                            CommonUtils.toastMessage("没有数据返回");
                            return;
                        }
                        Result bean = (Result) JsonUtils.stringToObject(response, Result.class);
                        if (bean.result.code.equals("10")) {
                            if (listener != null) {
                                listener.normalResponse(bean);
                            }
                        } else {
                            CommonUtils.toastMessage(bean.result.info);
                        }
                    }
                });
    }

    public void getMyCircleList( String ub_id,String uid,final NormalListener listener) {
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
            //CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.normalResponse(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.qzdt));
        OkHttpUtils
                .post()
                .addParams("ub_id",ub_id)
                //别人的id
                .addParams("uid",uid)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        listener.normalResponse(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)) {
                            CommonUtils.toastMessage("没有数据返回");
                            listener.normalResponse(null);
                            return;
                        }
                        try {
                            MyCircleActiveBean bean = (MyCircleActiveBean) JsonUtils.stringToObject(response, MyCircleActiveBean.class);
                            if (bean.result.code.equals("10")) {
                                if (listener != null) {
                                    listener.normalResponse(bean);
                                }
                            } else {
                                CommonUtils.toastMessage(bean.result.info);
                                listener.normalResponse(null);
                            }
                        }catch (Exception exception){
                            listener.normalResponse(null);
                        }
                    }
                });
    }
/*
* 删除圈子
* */
    public void getDeleteCircle( String ub_id,String del_id,final NormalListener listener) {
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
           // CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.normalResponse(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.qzdel));
        OkHttpUtils
                .post()
                .addParams("ub_id",ub_id)
                //0:删除圈子 1:评论
                .addParams("action","0")
                .addParams("del_id",del_id)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        listener.normalResponse(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)) {
                            CommonUtils.toastMessage("没有数据返回");
                            listener.normalResponse(null);
                            return;
                        }
                        Result  bean = (Result) JsonUtils.stringToObject(response, Result.class);
                        if (bean.result.code.equals("10")) {
                            if (listener != null) {
                                listener.normalResponse(bean);
                            }
                        } else {
                            CommonUtils.toastMessage(bean.result.info);
                            listener.normalResponse(null);
                        }
                    }
                });
    }

     /*
 * 获取个人信息列表
 * */
    public void getSingleInfoList( String ub_id,String uid,final NormalListener listener) {
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {

          //  CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.normalResponse(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.person));
        OkHttpUtils
                .post()
                .addParams("ub_id",ub_id)
                .addParams("uid",uid)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        listener.normalResponse(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)) {
                            CommonUtils.toastMessage("没有数据返回");
                            listener.normalResponse(null);
                            return;
                        }
                        try {
                            SingleInfoBean bean = (SingleInfoBean) JsonUtils.stringToObject(response, SingleInfoBean.class);
                            if (bean.result.code.equals("10")) {
                                if (listener != null) {
                                    listener.normalResponse(bean);
                                }
                            } else {
                                CommonUtils.toastMessage(bean.result.info);
                                listener.normalResponse(null);
                            }
                        }catch (Exception exception){
                            listener.normalResponse(null);
                        }
                    }
                });
    }

    /*
 * 获取他人订阅列表
 * */
    public void getOtherSubscribeList( String ub_id,String uid,final NormalListener listener) {
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
           // CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.normalResponse(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.otherdy));
        OkHttpUtils
                .post()
                .addParams("ub_id",ub_id)
                .addParams("uid",uid)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        listener.normalResponse(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)) {
                            CommonUtils.toastMessage("没有数据返回");
                            listener.normalResponse(null);
                            return;
                        }

                        try {
                            AddSubscribBean bean = (AddSubscribBean) JsonUtils.stringToObject(response, AddSubscribBean.class);
                            if (bean.result.code.equals("10")) {
                                if (listener != null) {
                                    listener.normalResponse(bean);
                                }
                            } else {
                                CommonUtils.toastMessage(bean.result.info);
                                listener.normalResponse(null);
                            }
                        }catch (Exception exception){
                            listener.normalResponse(null);
                        }
                    }
                });
    }


    /*
* 圈子和订阅上面的小红线
* */
    public void getDyIsShowRedPoint( String ub_id,final NormalListener listener) {
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
           // CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.normalResponse(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.dynotice));
        OkHttpUtils
                .post()
                .addParams("ub_id",ub_id)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        listener.normalResponse(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)) {
                            CommonUtils.toastMessage("没有数据返回");
                            listener.normalResponse(null);
                            return;
                        }

                        try {
                            CircleRedPointBean bean = (CircleRedPointBean) JsonUtils.stringToObject(response, CircleRedPointBean.class);
                            if (bean.result.code.equals("10")) {
                                if (listener != null) {
                                    listener.normalResponse(bean);
                                }
                            } else {
                                CommonUtils.toastMessage(bean.result.info);
                                listener.normalResponse(null);
                            }
                        }catch (Exception exception){
                            listener.normalResponse(null);
                        }
                    }
                });
    }

    /**
     * 获取升级信息
     * @param listener
     */
    public void getUpDate(final UpdateListener listener){
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.update_app));

        OkHttpUtils.post()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        listener.updateResponse(null);
                    }

                    @Override
                    public void onResponse(String response, int i) {
                        if (TextUtils.isEmpty(response)) {
                            listener.updateResponse(null);
                            return;
                        }
                        UpdateResponse bean = new Gson().fromJson(response , UpdateResponse.class);
                        listener.updateResponse(bean);
                    }
                });


    }

    public interface CircleListener{
        void circleResponse(CircleBean response);
    }
    public interface NormalListener{
        void normalResponse(Object response);
    }
    public interface UpdateListener{
        void updateResponse(UpdateResponse bean);
    }
}
