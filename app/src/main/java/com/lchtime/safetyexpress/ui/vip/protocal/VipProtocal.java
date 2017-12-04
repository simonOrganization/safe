package com.lchtime.safetyexpress.ui.vip.protocal;

import android.content.Context;
import android.text.TextUtils;

import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.AcountDetailBean;
import com.lchtime.safetyexpress.bean.MyAccountBean;
import com.lchtime.safetyexpress.bean.Result;
import com.lchtime.safetyexpress.ui.circle.protocal.CircleProtocal;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.DialogUtil;
import com.lchtime.safetyexpress.utils.JsonUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

import static com.igexin.sdk.GTServiceManager.context;
import static com.lchtime.safetyexpress.R.id.dialog;


/**
 * Created by android-cp on 2017/5/18.
 */

public class VipProtocal {


    /*
            * 获取我的账户
            * */
    public void getMyAcountInfo(String ub_id , final CircleProtocal.NormalListener listener) {
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
            //CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.normalResponse(null);
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
                            MyAccountBean bean = (MyAccountBean) JsonUtils.stringToObject(response, MyAccountBean.class);
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
    * 获取我的账户
    */
    public void getAddAcount( String ub_id,String type,String name,String account,final CircleProtocal.NormalListener listener) {
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
            //CommonUtils.toastMessage("您当前无网络，请联网再试");
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
    public void getAcountDetail( String ub_id , String page, final CircleProtocal.NormalListener listener) {
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
           // CommonUtils.toastMessage("您当前无网络，请联网再试");
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
                        if(listener != null)
                            listener.normalResponse(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)) {
                            CommonUtils.toastMessage("没有数据返回");
                            if(listener != null)
                                listener.normalResponse(null);
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
    **/
    public void getTiXian(String ub_id , String num , String account , String pwd , final DialogUtil dialog ,final CircleProtocal.NormalListener listener) {
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
           // CommonUtils.toastMessage("您当前无网络，请联网再试");
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.tixian));
        OkHttpUtils
                .post()
                .url(url)
                .addParams("ub_id",ub_id)
                .addParams("num",num)
                .addParams("account",account)
                .addParams("pwd" , pwd)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        dialog.dissmiss();
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
                            listener.normalResponse(bean);
                        }
                    }
                });
    }





    public interface NormalListener{
        void normalResponse(Object response);
    }
}
