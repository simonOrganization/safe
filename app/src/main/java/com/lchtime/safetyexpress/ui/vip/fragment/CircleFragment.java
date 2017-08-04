package com.lchtime.safetyexpress.ui.vip.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.CircleAdapter;
import com.lchtime.safetyexpress.bean.CircleItemUpBean;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.QzContextBean;
import com.lchtime.safetyexpress.bean.res.CircleBean;
import com.lchtime.safetyexpress.ui.circle.protocal.CircleProtocal;
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
    private CircleProtocal protocal;
    private String userid;

    @Override
    protected View initSuccessView() {
        recyclerView = new EmptyRecyclerView(MyApplication.getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    @Override
    public LoadingPager.LoadedResult initData() {
        getCircleList("0", "");

        if (loadedResult == null) {
            loadedResult = checkResult(list);
        }


        return loadedResult;
    }

    private void getCircleList(String type, String del_ids) {

        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
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
                    .addParams("ub_id", SpTools.getUserId(getContext()))
                    .addParams("type", type)
                    .addParams("del_ids", del_ids)
                    .build()
                    .execute();

            String myResponse = response.body().string();
            CircleBean bean = (CircleBean) JsonUtils.stringToObject(myResponse, CircleBean.class);
            if ("10".equals(bean.result.code)) {
                if (list != null) {
                    list.clear();
                    list = null;
                }

                list = bean.cms_context;

                Log.i("qaz", "getCircleList: " + list);
                adapter = new CircleAdapter(getActivity(), list);
                adapter.setShowDy(false);

                // home_new_fragment_rc.setAdapter(homeNewAdapter);
            } else {
                loadedResult = LoadingPager.LoadedResult.ERRO;
            }
        } catch (Exception e) {
            e.printStackTrace();
            loadedResult = LoadingPager.LoadedResult.ERRO;
        }
    }


    public void updataListView(boolean isDelete) {
        if (adapter != null) {
            adapter.setCheckBoxShow(this, isDelete);
            adapter.notifyDataSetChanged();
        }
    }

    public void updataDeleteNum(int num) {
        ((MyConllected) getActivity()).setDeleteNum(num);
    }

    public List<QzContextBean> getDeleteList() {
        return adapter.getUpdataList();
    }

    public void updataDeleteList() {
        List<QzContextBean> deleteList = getDeleteList();
        for (QzContextBean bean : deleteList) {
            list.remove(bean);
        }
        deleteList.clear();
        adapter.notifyDataSetChanged();
    }

    public void refreshItemData(final String qc_id) {
        if (protocal == null) {
            protocal = new CircleProtocal();
        }
        userid = SpTools.getUserId(getActivity());
        protocal.getItemInfo(userid, qc_id, new CircleProtocal.NormalListener() {
            @Override
            public void normalResponse(Object response) {
                if (response == null) {
                   // CommonUtils.toastMessage("更新数据失败");
                    return;
                }
                CircleItemUpBean circleItemUpBean = (CircleItemUpBean) response;
                QzContextBean qzContextBean = circleItemUpBean.qz_info.get(0);
                for (int i = 0; i < list.size(); i++) {
                    QzContextBean bean = list.get(i);
                    if (qc_id.equals(bean.qc_id)) {
                        list.set(i, qzContextBean);
                        break;
                    }
                }
                adapter.notifyDataSetChanged();

            }
        });


    }
}

