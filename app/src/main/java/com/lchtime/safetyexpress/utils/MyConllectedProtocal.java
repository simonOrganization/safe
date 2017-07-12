package com.lchtime.safetyexpress.utils;

import android.text.TextUtils;
import android.widget.Toast;

import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.res.NewsListRes;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by android-cp on 2017/5/4.
 */

public class MyConllectedProtocal {

    public static void requestDelete(String type, String del_ids, String module,final DeleteResponse deleteResponse){
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            CommonUtils.toastMessage("请检查网络");
            return;
        }

        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.attention));
        OkHttpUtils
                .post()
                .url(url)
                .addParams("module", module + "")
                .addParams("ub_id", SpTools.getString(MyApplication.getContext(), Constants.userId, ""))
                .addParams("type", type)
                .addParams("del_ids", del_ids)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(MyApplication.getContext(),"请检查网络设置",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)){
                            Toast.makeText(MyApplication.getContext(),"请检查网络设置",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        NewsListRes newsListRes = (NewsListRes) JsonUtils.stringToObject(response,NewsListRes.class);
                        deleteResponse.onDeleteResponse(newsListRes);
                    }
                });
    }

    public interface DeleteResponse{
        void onDeleteResponse(NewsListRes newsListRes);
    }
}
