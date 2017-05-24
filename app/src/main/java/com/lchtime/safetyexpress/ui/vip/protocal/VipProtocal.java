package com.lchtime.safetyexpress.ui.vip.protocal;

import android.text.TextUtils;

import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.AcountDetailBean;
import com.lchtime.safetyexpress.bean.AddSubscribBean;
import com.lchtime.safetyexpress.bean.MyAccountBean;
import com.lchtime.safetyexpress.bean.Result;
import com.lchtime.safetyexpress.bean.res.CircleBean;
import com.lchtime.safetyexpress.ui.circle.protocal.CircleProtocal;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.JsonUtils;
import com.mzhy.http.okhttp.OkHttpUtils;
import com.mzhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by android-cp on 2017/5/18.
 */

public class VipProtocal {
    /*
* 获取我的账户
* */
    public void getMyAcountInfo( String ub_id,final CircleProtocal.NormalListener listener) {
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.myaccount));
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
                        MyAccountBean bean = (MyAccountBean) JsonUtils.stringToObject(response, MyAccountBean.class);
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

    /*
* 获取我的账户
* */
    public void getAddAcount( String ub_id,String type,String name,String account,final CircleProtocal.NormalListener listener) {
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.addaccount));
        OkHttpUtils
                .post()
                .addParams("ub_id",ub_id)
                .addParams("type",type)
                .addParams("name",name)
                .addParams("account",account)
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
                        MyAccountBean bean = (MyAccountBean) JsonUtils.stringToObject(response, MyAccountBean.class);
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


    /*
* 获取账户明细
* */
    public void getAcountDetail( String ub_id,String page,final CircleProtocal.NormalListener listener) {
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.cashlist));
        OkHttpUtils
                .post()
                .addParams("ub_id",ub_id)
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
                        AcountDetailBean bean = (AcountDetailBean) JsonUtils.stringToObject(response, AcountDetailBean.class);
                        if (bean.result.code.equals("10")) {
                            if (listener != null) {
                                listener.normalResponse(bean);
                            }
                        } else {
                            CommonUtils.toastMessage(bean.result.info);
                            listener.normalResponse(bean);
                        }
                    }
                });
    }

    /*
* 提现
* */
    public void getTiXian( String ub_id,String num,String account,final CircleProtocal.NormalListener listener) {
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.tixian));
        OkHttpUtils
                .post()
                .addParams("ub_id",ub_id)
                .addParams("num",num)
                .addParams("account",account)
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
                        Result bean = (Result) JsonUtils.stringToObject(response, Result.class);
                        if (bean.result.code.equals("10")) {
                            if (listener != null) {
                                listener.normalResponse(bean);
                            }
                        } else {
                            CommonUtils.toastMessage(bean.result.info);
                            listener.normalResponse(bean);
                        }
                    }
                });
    }

    public interface NormalListener{
        void normalResponse(Object response);
    }
}
