package com.lchtime.safetyexpress.ui.chat.hx.fragment.protocal;

import android.text.TextUtils;

import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.ui.chat.hx.bean.AddBean;
import com.lchtime.safetyexpress.ui.chat.hx.bean.SearchResultBean;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.JsonUtils;
import com.mzhy.http.okhttp.OkHttpUtils;
import com.mzhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by Dreamer on 2017/6/7.
 */

public class AddCommandProtocal {
    public void getFriendsCommend( String ub_id,String type,final NormalListener listener) {
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.syadd));
        OkHttpUtils
                .post()
                .addParams("ub_id",ub_id)
                //0找人  1找群
                .addParams("action",type)
                .url(url)
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
                        AddBean bean = (AddBean) JsonUtils.stringToObject(response, AddBean.class);
                        if (listener != null) {
                            listener.normalResponse(bean);
                        }

                    }
                });
    }

    public void getSearchFriends( String ub_id,String type,String word,final NormalListener listener) {
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            return;
        }
        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.qunsearch));
        OkHttpUtils
                .post()
                .addParams("ub_id",ub_id)
                //1好友  0群
                .addParams("action",type)
                .addParams("word",word)
                .url(url)
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
                        SearchResultBean bean = (SearchResultBean) JsonUtils.stringToObject(response, SearchResultBean.class);
                        if (listener != null) {
                            listener.normalResponse(bean);
                        }

                    }
                });
    }

    public interface NormalListener{
        void normalResponse(Object response);
    }
}
