package com.lchtime.safetyexpress.ui.circle;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.hyphenate.easeui.bean.ContactBean;
import com.hyphenate.easeui.bean.ContactListBean;
import com.hyphenate.easeui.bean.EaseInitBean;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.home.protocal.HomeQuestionProtocal;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${Hongcha36} on 2017/11/28.
 */
@ContentView(R.layout.activity_my_friend)
public class MyFriendActivity extends BaseUI {


    @ViewInject(R.id.recyclerView)
    RecyclerView mFriendRy;

    private MyFriendAdapter mAdapter;
    private HomeQuestionProtocal protocal = new HomeQuestionProtocal();
    private Gson gson = new Gson();
    private List<ContactBean> mFriends = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected void setControlBasis() {
        setTitle("我的好友");
        mAdapter = new MyFriendAdapter(mContext , mFriends);
        mFriendRy.setLayoutManager(new LinearLayoutManager(mContext));
        mFriendRy.setAdapter(mAdapter);
    }

    @Override
    protected void prepareData() {
        protocal.getMyFriends(new HomeQuestionProtocal.QuestionListener() {
            @Override
            public void questionResponse(Object response) {
                ContactListBean bean = gson.fromJson((String) response, ContactListBean.class);
                if ("10".equals(bean.result.code)){
                    if (bean.friendlist == null || bean.friendlist.size() == 0){
                        return;
                    }
                    mFriends.clear();
                    mFriends.addAll(bean.friendlist);
                    mAdapter.notifyDataSetChanged();

                }else {
                    CommonUtils.toastMessage("请求好友数据失败，请稍后再试！");
                }
            }
        });
    }

    @Override
    protected void back() {
        finish();
    }
}
