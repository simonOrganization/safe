package com.lchtime.safetyexpress.ui.news;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by yxn on 2017/4/18.
 */

public abstract  class BaseFragment extends Fragment {
    /** Fragment当前状态是否可见 */
    protected boolean isVisible;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }
    /**
     * 可见
     */
    protected void onVisible() {
        lazyLoad();
    }


    /**
     * 不可见
     */
    protected void onInvisible() {


    }


    /**
     * 延迟加载
     * 子类必须重写此方法
     */
    protected abstract void lazyLoad();
    private Dialog mProgressDialog;
    private int count = 0;
//    public void showProgressDialog(Context mContext) {
//        if (mProgressDialog != null && mProgressDialog.isShowing()) {
//            count++;
//        } else {
//            mProgressDialog = CommonUtils.createLoadingDialog(mContext,"加載中...");
//            mProgressDialog.show();
//        }
//    }

    public void dismissProgressDialog() {
        if (count > 0) {
            count--;
            return;
        }
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        count = 0;
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    public class ResultStringCallback extends StringCallback {
        private Context mContext;

        public ResultStringCallback(Context context) {
            this.mContext = context;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
//            UserApplication.toast("网络访问错误");
        }

        @Override
        public void onResponse(String response, int id) {
        }

        @Override
        public void onBefore(Request request, int id) {
            super.onBefore(request, id);
//            showProgressDialog(mContext);
        }

        @Override
        public void onAfter(int id) {
            super.onAfter(id);
            dismissProgressDialog();
        }
    }
}
