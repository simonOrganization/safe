package com.lchtime.safetyexpress.ui.chat.hx.activity.protocal;

import android.icu.text.IDNA;
import android.text.TextUtils;

import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.ui.chat.hx.bean.AddBean;
import com.lchtime.safetyexpress.ui.chat.hx.bean.InfoBean;
import com.lchtime.safetyexpress.ui.chat.hx.fragment.protocal.AddCommandProtocal;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.JsonUtils;
import com.mzhy.http.okhttp.OkHttpUtils;
import com.mzhy.http.okhttp.builder.PostFormBuilder;
import com.mzhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

import static android.R.attr.action;
import static android.R.attr.type;

/**
 * Created by Dreamer on 2017/6/8.
 */

public class GetInfoProtocal {
    public void getInfo( String ub_id,String type,String idOrPhone,final AddCommandProtocal.NormalListener listener) {
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
           // CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.normalResponse(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.friendInfo));
        PostFormBuilder builder = OkHttpUtils
                .post()
                .addParams("ub_id", ub_id)
                //0群  1好友
                .addParams("action", type);
        if ("0".equals(type)){
            builder.addParams("groupid",idOrPhone);
        }else if ("1".equals(type)){
            builder.addParams("friend_ub_phone",idOrPhone);
        }

        builder.url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (listener != null){
                            listener.normalResponse(null);
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)) {
                            listener.normalResponse(null);
                            return;
                        }
                        if (listener != null) {
                            listener.normalResponse(response);
                        }

                    }
                });
    }


    public void getQuners( String ub_id,String groupid,final AddCommandProtocal.NormalListener listener) {
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
           // CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.normalResponse(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.getquners));
        PostFormBuilder builder = OkHttpUtils
                .post()
                .addParams("ub_id", ub_id)
                //0群  1好友
                .addParams("groupid", groupid);

        builder.url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (listener != null){
                            listener.normalResponse(null);
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)) {
                            listener.normalResponse(null);
                            return;
                        }
                        if (listener != null) {
                            listener.normalResponse(response);
                        }

                    }
                });
    }


    public void getCreateGrounp( String ub_id,String groupname,String desc,String publics, String members , String photo,String profession,String addr,final AddCommandProtocal.NormalListener listener) {
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
           // CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.normalResponse(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.qun));
        PostFormBuilder builder = OkHttpUtils
                .post()
                .addParams("ub_id", ub_id)
                //0群  1好友
                .addParams("action", "1")
                .addParams("groupname",groupname)
                .addParams("desc",desc)
                .addParams("Public",publics)
                .addParams("members",members)
                .addParams("photo",photo)
                .addParams("profession",profession)
                .addParams("addr",addr);


        builder.url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (listener != null){
                            listener.normalResponse(null);
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)) {
                            listener.normalResponse(null);
                            return;
                        }
                        if (listener != null) {
                            listener.normalResponse(response);
                        }

                    }
                });
    }


    public void  getUpdateGrounp( String ub_id,String groupname,String desc,String photo,String profession,String groupid,String addr,final AddCommandProtocal.NormalListener listener) {
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
            //CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.normalResponse(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.qun));
        PostFormBuilder builder = OkHttpUtils
                .post()
                .addParams("ub_id", ub_id)
                //0群  1好友
                .addParams("action", "2")
                .addParams("groupname",groupname)
                .addParams("desc",desc)
//                .addParams("Public",publics)
                .addParams("photo",photo)
                .addParams("profession",profession)
                .addParams("groupid",groupid)
                .addParams("addr",addr);


        builder.url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (listener != null){
                            listener.normalResponse(null);
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)) {
                            listener.normalResponse(null);
                            return;
                        }
                        if (listener != null) {
                            listener.normalResponse(response);
                        }

                    }
                });
    }

    /**
     * 删除群
     * @param ub_id
     * @param groupid
     * @param listener
     */
    public void getDeleteGrounp( String ub_id,String groupid,final AddCommandProtocal.NormalListener listener) {
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
           // CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.normalResponse(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.qun));
        PostFormBuilder builder = OkHttpUtils
                .post()
                .addParams("ub_id", ub_id)
                .addParams("groupid", groupid)
                //3 删除
                .addParams("action", "3");


        builder.url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (listener != null){
                            listener.normalResponse(null);
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)) {
                            listener.normalResponse(null);
                            return;
                        }
                        if (listener != null) {
                            listener.normalResponse(response);
                        }

                    }
                });
    }




    public void getDelMember( String ub_id,String groupid,String sns_quser,final AddCommandProtocal.NormalListener listener) {
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
            //CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.normalResponse(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.del));
        PostFormBuilder builder = OkHttpUtils
                .post()
                .addParams("ub_id", ub_id)
                .addParams("groupid", groupid)
                .addParams("sns_quser", sns_quser);

        builder.url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (listener != null){
                            listener.normalResponse(null);
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)) {
                            listener.normalResponse(null);
                            return;
                        }
                        if (listener != null) {
                            listener.normalResponse(response);
                        }

                    }
                });
    }


    public void getApply( String username,String groupid,String action,String message,String targetname,final AddCommandProtocal.NormalListener listener) {
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
           // CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.normalResponse(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.apply));
        PostFormBuilder builder = OkHttpUtils
                .post()
                .addParams("baseusername", username)
                .addParams("action", action)
                .addParams("message", message)
                .addParams("username", targetname);

        if ("0".equals(action)){
            //群
            builder.addParams("groupid",groupid);
        }
        builder.url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (listener != null){
                            listener.normalResponse(null);
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)) {
                            listener.normalResponse(null);
                            return;
                        }
                        if (listener != null) {
                            listener.normalResponse(response);
                        }

                    }
                });
    }


    public void getDeleteFriends(String owner_username,String friend_username,final AddCommandProtocal.NormalListener listener) {
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
           // CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.normalResponse(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.friendsdel));
        PostFormBuilder builder = OkHttpUtils
                .post()
                .addParams("owner_username", owner_username)
                .addParams("friend_username", friend_username);
        builder.url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (listener != null){
                            listener.normalResponse(null);
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)) {
                            listener.normalResponse(null);
                            return;
                        }
                        if (listener != null) {
                            listener.normalResponse(response);
                        }

                    }
                });
    }


    public void getApplyMessage(String username,final AddCommandProtocal.NormalListener listener) {
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
            //CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.normalResponse(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.showapply));
        PostFormBuilder builder = OkHttpUtils
                .post()
                .addParams("username", username);
        builder.url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (listener != null){
                            listener.normalResponse(null);
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)) {
                            listener.normalResponse(null);
                            return;
                        }
                        if (listener != null) {
                            listener.normalResponse(response);
                        }

                    }
                });
    }


    public void getApplyNum(String username,final AddCommandProtocal.NormalListener listener) {
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
           // CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.normalResponse(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.applynum));
        PostFormBuilder builder = OkHttpUtils
                .post()
                .addParams("username", username);
        builder.url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (listener != null){
                            listener.normalResponse(null);
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)) {
                            listener.normalResponse(null);
                            return;
                        }
                        if (listener != null) {
                            listener.normalResponse(response);
                        }

                    }
                });
    }


    public void getAccept( String owner_username,String friend_username,final AddCommandProtocal.NormalListener listener) {
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
           // CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.normalResponse(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.accept));
        PostFormBuilder builder = OkHttpUtils
                .post()
                .addParams("owner_username", owner_username)
                .addParams("friend_username", friend_username);
        builder.url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (listener != null){
                            listener.normalResponse(null);
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)) {
                            listener.normalResponse(null);
                            return;
                        }
                        if (listener != null) {
                            listener.normalResponse(response);
                        }

                    }
                });
    }

    public void getAcceptQun( String type,String ub_id,String groupid,String sns_quser,String friend_username,final AddCommandProtocal.NormalListener listener) {
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
           // CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.normalResponse(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.qunadmin));
        PostFormBuilder builder = OkHttpUtils
                .post()
                .addParams("ub_id", ub_id)
                .addParams("groupid", groupid)
                //用户环信id
                .addParams("sns_quser", sns_quser);
        if (!"3".equals(type)){
            //如果是公开随便的群，没有此字段，如果不是，要有这个字段,3代表公开随便
            builder.addParams("friend_username", friend_username);
        }
        builder.url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (listener != null){
                            listener.normalResponse(null);
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)) {
                            listener.normalResponse(null);
                            return;
                        }
                        if (listener != null) {
                            listener.normalResponse(response);
                        }

                    }
                });
    }

    public void getDelapply( String applyid,final AddCommandProtocal.NormalListener listener) {
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
           // CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.normalResponse(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.delapply));
        PostFormBuilder builder = OkHttpUtils
                .post()
                .addParams("applyid", applyid);
        builder.url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (listener != null){
                            listener.normalResponse(null);
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)) {
                            listener.normalResponse(null);
                            return;
                        }
                        if (listener != null) {
                            listener.normalResponse(response);
                        }

                    }
                });
    }

    public void getUp( String ub_id, String action,String account,final AddCommandProtocal.NormalListener listener) {
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
           // CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.normalResponse(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.stick));
        PostFormBuilder builder = OkHttpUtils
                .post()
                .addParams("ub_id", ub_id)
                .addParams("action", action);

        if (!"0".equals(action)){
            builder.addParams("hx_account", account);
        }
        builder.url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (listener != null){
                            listener.normalResponse(null);
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)) {
                            listener.normalResponse(null);
                            return;
                        }
                        if (listener != null) {
                            listener.normalResponse(response);
                        }

                    }
                });
    }

    /**
     * 获取已经加入的群组信息
     * @param ub_id
     */
    public void getJoinGroups(String ub_id , final AddCommandProtocal.NormalListener listener){
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
           // CommonUtils.toastMessage("您当前无网络，请联网再试");
            listener.normalResponse(null);
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.getqunlist));

        OkHttpUtils.post()
                .url(url)
                .addParams("ub_id" , ub_id)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                if (listener != null){
                    listener.normalResponse(null);
                }
            }

            @Override
            public void onResponse(String response , int i) {
                if (TextUtils.isEmpty(response)) {
                    listener.normalResponse(null);
                    return;
                }
                if (listener != null) {
                    listener.normalResponse(response);
                }
            }
        });

    }













}
