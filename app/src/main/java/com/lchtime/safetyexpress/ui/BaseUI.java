package com.lchtime.safetyexpress.ui;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.utils.ScreenUtil;
import com.lidroid.xutils.ViewUtils;

/**
 * Created by user on 2017/3/9.
 * 基类
 */

public abstract class BaseUI extends FragmentActivity {

    private MyApplication application;
    private long exitTime = 0;
    private LinearLayout.LayoutParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        application = (MyApplication) getApplication();
        setStatus2();
        setControlBasis();
        LinearLayout back = (LinearLayout) findViewById(R.id.ll_back);
        if (back != null) {
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    back();
                }
            });
        }
        prepareData();
    }

    /**
     * 描述：返回
     */
    protected abstract void back();

    /**
     * 描述：设置控件基础
     */
    protected abstract void setControlBasis();

    /**
     * 描述：准备数据
     */
    protected abstract void prepareData();

    /**
     * 描述：设置标题
     *
     * @param text 标题
     */
    protected void setTitle(String text) {
        TextView tv_title = (TextView) findViewById(R.id.title);
        if (tv_title != null) {
            tv_title.setText(text);
        }
    }

    /**
     * 描述:隐藏返回按钮
     */
    protected void backGone() {
        LinearLayout back = (LinearLayout) findViewById(R.id.ll_back);
        if (back != null) {
            back.setVisibility(View.GONE);
        }
    }

    /**
     * 描述:显示右菜单全部
     */
    protected void rightVisible(String title, int drawable) {
        rightVisible(title);
        rightVisible(drawable);
    }

    /**
     * 描述:显示右菜单文字
     */
    protected void rightVisible(String title) {
        LinearLayout ll_right = (LinearLayout) findViewById(R.id.ll_right);
        if (ll_right != null) {
            ll_right.setVisibility(View.VISIBLE);
        }
        TextView tv_right = (TextView) findViewById(R.id.tv_right);
        if (tv_right != null) {
            tv_right.setVisibility(View.VISIBLE);
            tv_right.setText(title);
        }
    }

    /**
     * 描述:显示右菜单图片
     */
    protected void rightVisible(int drawable) {
        LinearLayout ll_right = (LinearLayout) findViewById(R.id.ll_right);
        if (ll_right != null) {
            ll_right.setVisibility(View.VISIBLE);
        }
        ImageView iv_right = (ImageView) findViewById(R.id.iv_right);
        if (iv_right != null) {
            iv_right.setVisibility(View.VISIBLE);
            iv_right.setImageResource(drawable);
        }
    }

    protected void makeText(final String content) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), content, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            back();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 描述：退出程序
     */
    protected void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

    /**
     * 设置状态栏
     */
    protected void setStatus() {
        TextView v_title = (TextView) findViewById(R.id.v_title);
        if (v_title == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setImmerseLayout();
            int statusBarHeight = ScreenUtil.getStatusBarHeight(this);
            v_title.setHeight(statusBarHeight);
        } else {
            v_title.setHeight(0);
        }
    }

    protected void setStatus2() {
        LinearLayout ll_home = (LinearLayout) findViewById(R.id.ll_home);
        if(ll_home != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                // 透明状态栏
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                // 透明导航栏
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                ll_home.setVisibility(View.VISIBLE);
                int statusBarHeight = ScreenUtil.getStatusBarHeight(this);
                //动态的设置隐藏布局的高度
                params = (LinearLayout.LayoutParams) ll_home.getLayoutParams();
                params.height = statusBarHeight;
                ll_home.setLayoutParams(params);
            }
        }
    }

    protected void setImmerseLayout(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int statusBarHeight = ScreenUtil.getStatusBarHeight(this.getBaseContext());
            view.setPadding(0, statusBarHeight, 0, 0);
        }
    }

    protected void setImmerseLayout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

}
