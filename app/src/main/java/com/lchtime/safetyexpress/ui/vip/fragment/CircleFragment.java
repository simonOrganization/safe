package com.lchtime.safetyexpress.ui.vip.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.CircleAdapter;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.QzContextBean;
import com.lchtime.safetyexpress.bean.res.CircleBean;
import com.lchtime.safetyexpress.ui.vip.MyConllected;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.JsonUtils;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lchtime.safetyexpress.views.EmptyRecyclerView;
import com.lchtime.safetyexpress.views.LoadingPager;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * Created by android-cp on 2017/4/21.
 */

public class CircleFragment extends BaseFragment {

    private CircleAdapter adapter;
    private EmptyRecyclerView recyclerView;
    private LoadingPager.LoadedResult loadedResult;
    private List<QzContextBean> list = new ArrayList<>();
    @Override
    protected View initSuccessView() {
        recyclerView = new EmptyRecyclerView(MyApplication.getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    @Override
    public LoadingPager.LoadedResult initData() {
        getCircleList("","");

        if (loadedResult == null) {
            loadedResult = checkResult(list);
        }


        return loadedResult;
    }

    private void getCircleList(String type,String del_ids) {

        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            loadedResult = LoadingPager.LoadedResult.ERRO;
            return;
        }

        String url = MyApplication.getContext().getResources().getString(R.string.service_host_address)
                .concat(MyApplication.getContext().getResources().getString(R.string.attention));
        try {
            Response response = OkHttpUtils
                    .post()
                    .url(url)
                    .addParams("module", 5 + "")
                    .addParams("ub_id", SpTools.getString(getContext(), Constants.userId, ""))
                    .addParams("type", type)
                    .addParams("del_ids", del_ids)
                    .build()
                    .execute();

            String myResponse = response.body().string();
            CircleBean bean = (CircleBean) JsonUtils.stringToObject(myResponse,CircleBean.class);
            if("10".equals(bean.result.code)){
                if(list!=null){
                    list.clear();
                    list = null;
                }

                list = bean.cms_context;


                adapter = new CircleAdapter(getActivity(),list);
                adapter.setShowDy(false);

                // home_new_fragment_rc.setAdapter(homeNewAdapter);
            }else{
                loadedResult = LoadingPager.LoadedResult.ERRO;
            }
        } catch (Exception e) {
            e.printStackTrace();
            loadedResult = LoadingPager.LoadedResult.ERRO;
        }
    }


    public void updataListView(boolean isDelete){
        if(adapter != null){
            adapter.setCheckBoxShow(this,isDelete);
            adapter.notifyDataSetChanged();
        }
    }

    public void updataDeleteNum(int num){
        ((MyConllected)getActivity()).setDeleteNum(num);
    }

    public List<QzContextBean> getDeleteList(){
        return adapter.getUpdataList();
    }

    public void updataDeleteList(){
        List<QzContextBean> deleteList = getDeleteList();
        for (QzContextBean bean: deleteList){
            list.remove(bean);
        }
        deleteList.clear();
        adapter.notifyDataSetChanged();
    }
}
