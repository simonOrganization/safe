package com.lchtime.safetyexpress.ui.home.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.WenDaAdapter;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.MyWenDaBean;
import com.lchtime.safetyexpress.ui.home.MyQuestion;
import com.lchtime.safetyexpress.ui.vip.fragment.BaseFragment;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.JsonUtils;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lchtime.safetyexpress.views.LoadingPager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * Created by android-cp on 2017/5/23.
 */

public class GuanZhuFragment extends BaseFragment {

    private  List<MyWenDaBean.ItemBean> items ;
    private RecyclerView recyclerView;
    private Handler handler = new Handler();
    private String otherid = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() instanceof  MyQuestion) {
            otherid = ((MyQuestion) getActivity()).otherid;
        }
        recyclerView = new RecyclerView(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        items = new ArrayList<>();
        adapter = new WenDaAdapter(this,items);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected View initSuccessView() {

        return recyclerView;
    }

    @Override
    public LoadingPager.LoadedResult initData() {
        if (items != null){
            items.clear();
        }
        getData("0");
        return loadedResult;
    }


    //0  关注 1  回答 2  提问


    private LoadingPager.LoadedResult loadedResult;
    private WenDaAdapter adapter;
    private void getData(String page){
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            loadedResult = LoadingPager.LoadedResult.ERRO;
            return;
        }

        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.mywd));
        try {

            PostFormBuilder builder = OkHttpUtils
                    .post()
                    .url(url)
                    .addParams("page", page)
                    .addParams("type", "0")
                    .addParams("ub_id", SpTools.getString(getContext(), Constants.userId, ""));

            if (!TextUtils.isEmpty(otherid)){
                builder.addParams("otherid",otherid);
            }
            Response response = builder
                    .build()
                    .execute();

            String myResponse = response.body().string();
            final MyWenDaBean bean = (MyWenDaBean) JsonUtils.stringToObject(myResponse,MyWenDaBean.class);

            handler.post(new Runnable() {
                @Override
                public void run() {
                    ((MyQuestion)getActivity()).setText("关注问题(" + bean.num + ")");
                }
            });

            if(bean.result.code.equals("10")){
                if (items == null){
                    items = new ArrayList<>();
                }
                items.addAll(bean.item);
                if (adapter == null) {
                    adapter = new WenDaAdapter(this,items);
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });

                loadedResult = LoadingPager.LoadedResult.SUCCESS;
            }else{
                loadedResult = LoadingPager.LoadedResult.ERRO;
            }


        } catch (Exception e) {
            e.printStackTrace();
            loadedResult = LoadingPager.LoadedResult.ERRO;
        }
    }

}
