package com.lchtime.safetyexpress.ui.vip;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 关于我们
 * Created by user on 2017/4/17.
 */
@ContentView(R.layout.vip_setting_about_us_ui)
public class VipSettingAboutUsUI extends BaseUI {

    @ViewInject(R.id.ll_setting_push)
    LinearLayout mCallPhoneLl; //打电话
    @ViewInject(R.id.tv_phone)
    TextView mPhoneTv;

    private AlertDialog mDialog;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        setTitle("关于我们");
        final String phone = mPhoneTv.getText().toString().trim();
        if (phone != null)
            mCallPhoneLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("确认拨打电话？")
                            .setMessage(phone)
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                        // TODO: Consider calling
                                        //    ActivityCompat#requestPermissions
                                        // here to request the missing permissions, and then overriding
                                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                        //                                          int[] grantResults)
                                        // to handle the case where the user grants the permission. See the documentation
                                        // for ActivityCompat#requestPermissions for more details.
                                        return;
                                    }
                                    startActivity(intent);
                            }
                        });
                mDialog = builder.create();
                mDialog.show();
            }
        });



    }

    @Override
    protected void prepareData() {

    }
}
