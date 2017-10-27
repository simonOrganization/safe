package com.lchtime.safetyexpress.weight;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;
import com.lidroid.xutils.view.annotation.event.OnClick;

import static com.igexin.sdk.GTServiceManager.context;

/**
 * Created by ${Hongcha36} on 2017/8/30.
 */

public class LoginDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private onClickLogin listener;

    private ImageView mCloseIv;
    private TextView mLoginTv;


    public LoginDialog(Context context , onClickLogin listener){
        super(context , R.style.MyDialogStyle);
        mContext = context;
        this.listener = listener;
    }

    public LoginDialog(Context context) {
        super(context);
    }

    public LoginDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected LoginDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_login);
        mCloseIv = (ImageView) findViewById(R.id.iv_close);
        mLoginTv = (TextView) findViewById(R.id.tv_login);

        mCloseIv.setOnClickListener(this);
        mLoginTv.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.tv_login:
                listener.OnClickLogin();
                dismiss();
                break;
            case R.id.iv_close:
                dismiss();
                break;
        }
    }


    public interface onClickLogin {
        void OnClickLogin();
    }

}
