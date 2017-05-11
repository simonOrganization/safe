package com.lchtime.safetyexpress.ui.circle.protocal;

import android.text.TextUtils;

import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.AddSubscribBean;
import com.lchtime.safetyexpress.bean.BasicResult;
import com.lchtime.safetyexpress.bean.MydyBean;
import com.lchtime.safetyexpress.bean.Result;
import com.lchtime.safetyexpress.bean.res.CircleBean;
import com.lchtime.safetyexpress.bean.res.VideoRes;
import com.lchtime.safetyexpress.ui.home.protocal.VideoProtocal;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.JsonUtils;
import com.mzhy.http.okhttp.OkHttpUtils;
import com.mzhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by android-cp on 2017/5/8.
 */

public class CircleProtocal {
    public void getCircleList( String ub_id,String page,String type,String order,final CircleListener listener) {
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
            CommonUtils.toastMessage("您当前无网络，请联网再试");
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
            CommonUtils.toastMessage("您当前无网络，请联网再试");
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

    public void changeSubscribe( String ub_id,String f_ub_id,String action,final CircleListener listener) {
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            return;
        }
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

    public void updataZanOrCai( String ub_id,String qc_id,String qc_agr,String qc_aga ,final NormalListener listener) {
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.newsdz));
        OkHttpUtils
                .post()
                .addParams("ub_id",ub_id)
                //新闻id
                .addParams("qc_id",qc_id)
                //同意
                .addParams("qc_agr",qc_agr)
                //反对
                .addParams("qc_aga",qc_aga)
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
                        Result result = (Result) JsonUtils.stringToObject(response, Result.class);
                        if (result.result.code.equals("10")) {
                            if (listener != null) {
                                listener.normalResponse(result.result);
                            }
                        } else {
                            listener.normalResponse(result.result);
                           // CommonUtils.toastMessage(result.result.getInfo());
                        }
                    }
                });
    }

    public void getMySubscribe( String ub_id,final NormalListener listener) {
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.mydy));
        OkHttpUtils
                .post()
                .addParams("ub_id",ub_id)
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
                        MydyBean mydyBean = (MydyBean) JsonUtils.stringToObject(response, MydyBean.class);
                        if (mydyBean.result.code.equals("10")) {
                            if (listener != null) {
                                listener.normalResponse(mydyBean);
                            }
                        } else {
                            CommonUtils.toastMessage(mydyBean.result.info);
                        }
                    }
                });
    }

    public void getAddDyData( String ub_id,String hy,String gw,String addr,String action,String page,final NormalListener listener) {
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
            CommonUtils.toastMessage("您当前无网络，请联网再试");
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

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)) {
                            CommonUtils.toastMessage("没有数据返回");
                            return;
                        }
                        AddSubscribBean addSubscribBean = (AddSubscribBean) JsonUtils.stringToObject(response, AddSubscribBean.class);
                        if (addSubscribBean.result.code.equals("10")) {
                            if (listener != null) {
                                listener.normalResponse(addSubscribBean);
                            }
                        } else {
                            CommonUtils.toastMessage(addSubscribBean.result.info);
                        }
                    }
                });
    }

    public interface CircleListener{
        void circleResponse(CircleBean response);
    }
    public interface NormalListener{
        void normalResponse(Object response);
    }
}
