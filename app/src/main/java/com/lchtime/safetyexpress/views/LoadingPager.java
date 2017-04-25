package com.lchtime.safetyexpress.views;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;


import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.proxy.ThreadPoolFactory;
import com.lchtime.safetyexpress.utils.UIUtils;

/**
 * Created by Dreamer on 2017/3/1.
 */

public abstract class LoadingPager extends FrameLayout {

    public static final int STATE_LOADING = 0;
    public static final int STATE_ERRO = 1;
    public static final int STATE_SUCCESS = 2;
    public static final int STATE_EMPTY = 3;

    public int mCurState = STATE_LOADING;

    private View mLoadingView;
    private View mEmptyView;
    private View mErroView;
    private View mSuccessView;
    private LoadDataTask mLoadDataTask;


    public LoadingPager(Context context) {
        super(context);
        initCommonView();
    }

    private void initCommonView() {

        mLoadingView = View.inflate(UIUtils.getContext(), R.layout.pager_loading,null);
        this.addView(mLoadingView);


        mEmptyView = View.inflate(UIUtils.getContext(), R.layout.pager_empty,null);
        this.addView(mEmptyView);


        mErroView = View.inflate(UIUtils.getContext(), R.layout.pager_error,null);
        this.addView(mErroView);

        mErroView.findViewById(R.id.error_btn_retry).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                triggerLoadData();
            }
        });

        refreshViewByState();
    }

    private void refreshViewByState() {

        if (mCurState == STATE_LOADING){
            mLoadingView.setVisibility(View.VISIBLE);
        }else {
            mLoadingView.setVisibility(View.GONE);
        }

        if (mCurState == STATE_EMPTY){
            mEmptyView.setVisibility(View.VISIBLE);
        }else {
            mEmptyView.setVisibility(View.GONE);
        }

        if (mCurState == STATE_ERRO){
            mErroView.setVisibility(View.VISIBLE);
        }else {
            mErroView.setVisibility(View.GONE);
        }


        if (mSuccessView == null && mCurState == STATE_SUCCESS){
            mSuccessView = initSuccessView();
            this.addView(mSuccessView);
        }

        if (mSuccessView != null){
            if (mCurState == STATE_SUCCESS){
                mSuccessView.setVisibility(View.VISIBLE);
            }else{
                mSuccessView.setVisibility(View.GONE);
            }
        }
    }


    public void triggerLoadData(){
        if (mCurState != STATE_SUCCESS){

            if (mLoadDataTask == null){

                mCurState = STATE_LOADING;

                refreshViewByState();

                mLoadDataTask = new LoadDataTask();

                ThreadPoolFactory.getNormalThreadPoolProxy().submit(mLoadDataTask);

            }
        }
    }

    class LoadDataTask implements Runnable{
        @Override
        public void run() {

            LoadedResult loadedResult = initData();

            mCurState = loadedResult.getState();
            MyApplication.getMainThreadHandler().post(new Runnable() {
                @Override
                public void run() {

                    refreshViewByState();
                }
            });

            mLoadDataTask = null;
        }
    }

    public abstract LoadedResult initData();

    public abstract View initSuccessView() ;


    public enum LoadedResult {
        SUCCESS(STATE_SUCCESS),ERRO(STATE_ERRO),EMPTY(STATE_EMPTY);

        public int getState() {
            return state;
        }

        private int state;
        LoadedResult (int state){
            this.state = state;
        }
    }


}
