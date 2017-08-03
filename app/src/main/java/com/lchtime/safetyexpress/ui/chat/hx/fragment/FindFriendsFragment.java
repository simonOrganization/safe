package com.lchtime.safetyexpress.ui.chat.hx.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.ui.chat.hx.adapter.AddFriendsCommendAdapter;
import com.lchtime.safetyexpress.ui.chat.hx.bean.AddBean;
import com.lchtime.safetyexpress.ui.chat.hx.fragment.protocal.AddCommandProtocal;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lchtime.safetyexpress.utils.refresh.PullLoadMoreRecyclerView;


/**
 * Created by Dreamer on 2017/6/7.
 */

public class FindFriendsFragment extends Fragment {
    private Context context;
    private AddCommandProtocal mProtocal;
    private String mUb_id;
    private RecyclerView mRc;
    private PullLoadMoreRecyclerView mPmRc;
    private RelativeLayout mLoading;
    private RelativeLayout mEmpty;
    private RelativeLayout mError;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(context, R.layout.add_friends_group_fragment,null);
        mLoading = (RelativeLayout) view.findViewById(R.id.loading);
        mEmpty = (RelativeLayout) view.findViewById(R.id.empty);
        mError = (RelativeLayout) view.findViewById(R.id.error);
        mPmRc = (PullLoadMoreRecyclerView) view.findViewById(R.id.add_friends_group_rc);
        mRc = (RecyclerView) view.findViewById(R.id.home_new_fragment_rc);
        return view;
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRc.setLayoutManager(new LinearLayoutManager(context));
        if (mProtocal == null){
            mProtocal = new AddCommandProtocal();
        }
        mUb_id = SpTools.getString(context, Constants.userId,"");
        initData();
        initListener();
    }

    private void initData() {
        if (!TextUtils.isEmpty(mUb_id)) {
            mProtocal.getFriendsCommend(mUb_id, "0", new AddCommandProtocal.NormalListener() {
                @Override
                public void normalResponse(Object response) {
                    if (response == null){
                        setErrorVisiblity();
                      //  CommonUtils.toastMessage("网络异常");
                        mPmRc.setPullLoadMoreCompleted();
                        return;
                    }else {
                        AddBean bean = (AddBean) response;
                        if (bean.user.size() > 0) {
                            AddFriendsCommendAdapter adapter = new AddFriendsCommendAdapter(FindFriendsFragment.this, bean.user, 0);
                            mRc.setAdapter(adapter);
                            setSuccessVisiblity();
                        }else {
                            setEmptyVisiblity();
                        }
                        mPmRc.setPullLoadMoreCompleted();
                    }
                }
            });
        }
    }

    private void initListener() {
        mPmRc.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                initData();
            }

            @Override
            public void onLoadMore() {
                mPmRc.setPullLoadMoreCompleted();
            }
        });
    }


    public void setLoadingVisiblity(){
        mLoading.setVisibility(View.VISIBLE);
        mEmpty.setVisibility(View.GONE);
        mError.setVisibility(View.GONE);
        mPmRc.setVisibility(View.GONE);
    }
    public void setEmptyVisiblity(){
        mLoading.setVisibility(View.GONE);
        mEmpty.setVisibility(View.VISIBLE);
        mError.setVisibility(View.GONE);
        mPmRc.setVisibility(View.GONE);
    }
    public void setErrorVisiblity(){
        mLoading.setVisibility(View.GONE);
        mEmpty.setVisibility(View.GONE);
        mError.setVisibility(View.VISIBLE);
        mPmRc.setVisibility(View.GONE);
    }
    public void setSuccessVisiblity(){
        mLoading.setVisibility(View.GONE);
        mEmpty.setVisibility(View.GONE);
        mError.setVisibility(View.GONE);
        mPmRc.setVisibility(View.VISIBLE);
    }
}
