package com.lchtime.safetyexpress.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Administrator on 2017/7/12.
 */

public class DialogUtil {

    private ProgressDialog mDialog;

    public DialogUtil(Context mContext) {
        this(mContext , "请稍等...");
    }

    public DialogUtil(Context mContext , String message) {
        mDialog = new ProgressDialog(mContext);
        mDialog.setMessage(message);
    }

    private void show(){
        mDialog.show();
    }

    private void dissmiss(){
        mDialog.dismiss();
    }


}
