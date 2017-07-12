package com.lchtime.safetyexpress.ui.vip;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.igexin.sdk.PushManager;
import com.lchtime.safetyexpress.H5DetailUI;
import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.InitInfo;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.chat.hx.DemoHelper;
import com.lchtime.safetyexpress.ui.login.LoginUI;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lchtime.safetyexpress.utils.cacheutils.AppConfig;
import com.lchtime.safetyexpress.utils.cacheutils.DataCleanManager;
import com.lchtime.safetyexpress.utils.cacheutils.FileUtil;
import com.lchtime.safetyexpress.utils.cacheutils.MethodsCompat;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.kymjs.kjframe.Core;

import java.io.File;
import java.util.Properties;

import static com.igexin.sdk.GTServiceManager.context;

/**
 * 设置
 * Created by user on 2017/4/17.
 */
@ContentView(R.layout.vip_setting_ui)
public class VipSettingUI extends BaseUI {

    @ViewInject(R.id.tv_setting_version)
    private TextView tv_version;

    @ViewInject(R.id.tv_setting_cache)
    private TextView tvCache;

    @ViewInject(R.id.pb_progress)
    private ProgressBar pb_progress;


    @ViewInject(R.id.cb_setting_push)
    private CheckBox cb_setting_push;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        setTitle("设置");
        setVersion();
        //推送服务初始化

        initListener();


        long cache = caculateCacheSize();
        long unknowCache = SpTools.getLong(this, "unknowCache", 0L);
        if (unknowCache < 0 ){
            unknowCache = 0;
        }
        cache = cache - unknowCache;
        String cacheSize = FileUtil.formatFileSize(cache);
        if (cache > 0) {
            cacheSize = FileUtil.formatFileSize(cache);
        }else {
            cacheSize = "0KB";
        }
        tvCache.setText(cacheSize);

    }

    private void initListener() {
        //初始化推送
//        PushManager.getInstance().initialize(VipSettingUI.this, DemoPushService.class);
        final PushManager pushManager = PushManager.getInstance();
        cb_setting_push.setChecked(pushManager.isPushTurnedOn(this));
        cb_setting_push.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    pushManager.turnOnPush(VipSettingUI.this);
                }else {
                    pushManager.turnOffPush(VipSettingUI.this);
                }
            }
        });
    }

    @Override
    protected void prepareData() {

    }





//    用户协议
    @OnClick(R.id.ll_setting_agreement)
    private void getAgreement(View view){
        Intent intent = new Intent(VipSettingUI.this,H5DetailUI.class);
        intent.putExtra("type","agreement");
        intent.putExtra("url","http://fcar.lchtime.cn:8001/H5/agreement.html" + "?timestamp=" + System.currentTimeMillis());
        startActivity(intent);
    }



    @OnClick(R.id.ll_setting_about)
    private void getAbout(View view){
        Intent intent = new Intent(VipSettingUI.this,VipSettingAboutUsUI.class);
        startActivity(intent);
    }

    /**
     *
     * 退出登录
     *
     * */
    @OnClick(R.id.tv_info_phone_valid)
    private void getOutLog(View view){
        backgroundAlpha(0.5f);
        pb_progress.setVisibility(View.VISIBLE);
        PushManager.getInstance().unBindAlias(context,SpTools.getString(this,Constants.userId,""),true);
        logout();

//        Toast.makeText(this,"正在退出登录",Toast.LENGTH_SHORT).show();

    }

    void logout() {
//        final ProgressDialog pd = new ProgressDialog(this);
//        String st = getResources().getString(R.string.Are_logged_out);
//        pd.setMessage(st);
//        pd.setCanceledOnTouchOutside(false);
//        pd.show();
        DemoHelper.getInstance().logout(false,new EMCallBack() {

            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    public void run() {
//                        pd.dismiss();
                        // show login screen
//                        finish();

                        SpTools.setString(VipSettingUI.this, Constants.userId, null);//存储用户的ub_id
                        SpTools.setString(VipSettingUI.this, Constants.phoneNum, null);//存储用户的手机号码
                        SpTools.setString(VipSettingUI.this, Constants.password, null);//存储用户的密码
                        File file = new File(MyApplication.getContext().getFilesDir(),Constants.photo_name);//将要保存图片的路径
                        file.delete();

                        InitInfo.vipInfoBean = null;
                        InitInfo.isLogin = false;
                        InitInfo.phoneNumber = null;
                        startActivity(new Intent(VipSettingUI.this, LoginUI.class));
                        backgroundAlpha(1f);
                        pb_progress.setVisibility(View.GONE);
                        finish();

                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
//                        pd.dismiss();
                        backgroundAlpha(1f);
                        pb_progress.setVisibility(View.GONE);
                        Toast.makeText(VipSettingUI.this, "unbind devicetokens failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void setVersion() {
        String version = "未设置";
        try {
            version = getPackageManager().getPackageInfo(getPackageName(),0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        tv_version.setText(version);
    }

    /**
     *
     * 清除缓存
     *
     * */
    @OnClick(R.id.ll_setting_cache)
    private void clearCache(View view){
        onClickCleanCache();
    }

    //------****** 缓存相关****----------
    private final int CLEAN_SUC=1001;
    private final int CLEAN_FAIL=1002;
    private void onClickCleanCache() {
        getConfirmDialog(this, "是否清空缓存?", new DialogInterface.OnClickListener
                () {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                clearAppCache();
                tvCache.setText("0KB");
            }
        }).show();
    }
    public static AlertDialog.Builder getConfirmDialog(Context context, String message, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setMessage(Html.fromHtml(message));
        builder.setPositiveButton("确定", onClickListener);
        builder.setNegativeButton("取消", null);
        return builder;
    }
    public static AlertDialog.Builder getDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        return builder;
    }


    /**
     * 计算缓存的大小
     */
    private long caculateCacheSize() {
        long fileSize = 0;
        String cacheSize = "0KB";
        File filesDir = getFilesDir();
        File cacheDir = getCacheDir();

        fileSize += FileUtil.getDirSize(filesDir);
        fileSize += FileUtil.getDirSize(cacheDir);
        // 2.2版本才有将应用缓存转移到sd卡的功能
        if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
            File externalCacheDir = MethodsCompat
                    .getExternalCacheDir(this);
            fileSize += FileUtil.getDirSize(externalCacheDir);
            fileSize += FileUtil.getDirSize(new File(
                    org.kymjs.kjframe.utils.FileUtils.getSDCardPath()
                            + File.separator + "KJLibrary/cache"));
        }
        if (fileSize > 0){
            return fileSize;
        }


        return 0;
    }

    public static boolean isMethodsCompat(int VersionCode) {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }
    /**
     * 清除app缓存
     */
    public void myclearaAppCache() {
        DataCleanManager.cleanDatabases(this);
        // 清除数据缓存
        DataCleanManager.cleanInternalCache(this);
        // 2.2版本才有将应用缓存转移到sd卡的功能
        if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
            DataCleanManager.cleanCustomCache(MethodsCompat.getExternalCacheDir(this).getAbsolutePath());
        }
        // 清除编辑器保存的临时内容
        Properties props = getProperties();
        for (Object key : props.keySet()) {
            String _key = key.toString();
            if (_key.startsWith("temp"))
                removeProperty(_key);
        }
        Core.getKJBitmap().cleanCache();
    }

    /**
     * 清除保存的缓存
     */
    public Properties getProperties() {
        return AppConfig.getAppConfig(this).get();
    }
    public void removeProperty(String... key) {
        AppConfig.getAppConfig(this).remove(key);
    }
    /**
     * 清除app缓存
     *
     * @param
     */
    public void clearAppCache() {

        new Thread() {
            @Override
            public void run() {
                Message msg = new Message();
                try {
                    myclearaAppCache();
                    long cache = caculateCacheSize();
                    SpTools.setLong(VipSettingUI.this,"unknowCache",cache);
                    //成功
                    msg.what = CLEAN_SUC;
                } catch (Exception e) {
                    e.printStackTrace();
                    //失败
                    msg.what = CLEAN_FAIL;
                }
                handler.sendMessage(msg);
            }
        }.start();
    }
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case CLEAN_FAIL:
                    CommonUtils.toastMessage("清除失败");
                    break;
                case CLEAN_SUC:
                    CommonUtils.toastMessage("清除成功");
                    break;
            }
        };
    };

    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

}
