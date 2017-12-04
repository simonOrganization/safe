package com.lchtime.safetyexpress.weight;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.lchtime.safetyexpress.R;



/**
 * Created by ${Hongcha36} on 2017/8/31.
 */

public class UpdateDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private TextView mCancelTv , mUpdateTv;
    private String updateUrl;

    public UpdateDialog(Context context , String updateUrl) {
        super(context , R.style.MyDialogStyle);
        this.context = context;
        this.updateUrl = updateUrl;
    }


    public UpdateDialog(Context context) {
        super(context , R.style.MyDialogStyle);
        this.context = context;
    }

    public UpdateDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected UpdateDialog( Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_update);

        mUpdateTv = (TextView) findViewById(R.id.tv_update);
        mCancelTv = (TextView) findViewById(R.id.tv_cancel);
        mUpdateTv.setOnClickListener(this);
        mCancelTv.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_update:
                /*Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                //设置跳转到默认的应用市场
                goToMarket.setClassName()
                try {
                    context.startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                }
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri content_url = Uri.parse(updateUrl);
                intent.setData(content_url);
                context.startActivity(intent);*/

                goToMarket(context , context.getPackageName());
                dismiss();

                break;
        }
    }

    public void goToMarket(Context context, String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
}
