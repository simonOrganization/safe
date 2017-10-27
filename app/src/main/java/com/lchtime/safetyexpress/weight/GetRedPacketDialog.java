package com.lchtime.safetyexpress.weight;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.ui.vip.MyMoneyActivity;

/**
 * Created by ${Hongcha36} on 2017/10/24.
 * 获取到红包的dialog
 */
public class GetRedPacketDialog extends Dialog implements View.OnClickListener{
    private Context mContext;

    private TextView mMoneyTv;
    private String mMoney;

    public GetRedPacketDialog(Context context , String money) {
        super(context , R.style.MyDialogStyle);
        mContext = context;
        mMoney = money;
    }

    public GetRedPacketDialog(Context context,  int themeResId) {
        super(context, themeResId);
    }

    protected GetRedPacketDialog(Context context, boolean cancelable,  OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_get_red_packet);
        mMoneyTv = (TextView) findViewById(R.id.tv_money_num);

        mMoneyTv.setText(mMoney + "元！");
        findViewById(R.id.iv_close).setOnClickListener(this);
        findViewById(R.id.iv_look_money).setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.iv_look_money:
                Intent intent = new Intent(mContext , MyMoneyActivity.class);
                mContext.startActivity(intent);
                dismiss();
                break;
        }
    }
}
