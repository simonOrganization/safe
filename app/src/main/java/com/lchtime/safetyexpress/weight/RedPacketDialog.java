package com.lchtime.safetyexpress.weight;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.ui.home.GetMoneyActivity;

/**
 * Created by ${Hongcha36} on 2017/10/19.
 */

public class RedPacketDialog extends Dialog implements View.OnClickListener{

    private Context mContext;

    public RedPacketDialog(@NonNull Context context) {
        super(context , R.style.MyDialogStyle);
        mContext = context;
    }

    public RedPacketDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected RedPacketDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_redpacket);

        findViewById(R.id.iv_close).setOnClickListener(this);
        findViewById(R.id.iv_red_get).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.iv_red_get:
                Intent intent = new Intent(mContext , GetMoneyActivity.class);
                mContext.startActivity(intent);
                dismiss();
                break;
        }
    }
}
