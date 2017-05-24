package com.lchtime.safetyexpress.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lchtime.safetyexpress.MyApplication;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by user on 2017/4/17.
 */

public class CommonUtils {

    /**
     * 判断是否有网
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /** 获取手机的密度*/
    public static float getDensity(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.density;
    }

    public static int getDimen(Context context, int dimen) {
        return context.getResources().getDimensionPixelOffset(dimen);
    }

    public static Bitmap toRoundCorner(Bitmap bitmap) {
        int pSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
        Bitmap output = Bitmap.createBitmap(pSize, pSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        int color = -16777216;
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, pSize, pSize);
        RectF rectF = new RectF(rect);
        float roundPx = (float) (pSize / 2);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(-16777216);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception var3) {
            return null;
        }
    }

    public static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception var3) {
            return -1;
        }
    }
    //查看sd卡是否存在
    public static boolean ExistSDCard() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }

    /**
     * 判断是否有网
     * @param context
     * @return
     */

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isAvailable()){
            return true;//当前有可用网络
        }
        else{
            return false;//当前无可用网络
        }
    }


    public static void toastMessage(String msg){
        Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
    }


    /**
     * 判断手机号的正则表达式
     * @param username
     * @return
     */
    public static boolean isMobilePhone(String username) {
        String telRegex = "[1][34578]\\d{9}";
        return username.matches(telRegex);
    }

    public static String getStandardDate(String timeStr) {

        StringBuffer sb = new StringBuffer();

        long t = Long.parseLong(timeStr);
        long time = System.currentTimeMillis() - (t*1000);
        long mill = (long) Math.ceil(time /1000);//秒前

        long minute = (long) Math.ceil(time/60/1000.0f);// 分钟前

        long hour = (long) Math.ceil(time/60/60/1000.0f);// 小时

        long day = (long) Math.ceil(time/24/60/60/1000.0f);// 天前

        if (day - 1 > 0) {
            sb.append(day + "天");
        } else if (hour - 1 > 0) {
            if (hour >= 24) {
                sb.append("1天");
            } else {
                sb.append(hour + "小时");
            }
        } else if (minute - 1 > 0) {
            if (minute == 60) {
                sb.append("1小时");
            } else {
                sb.append(minute + "分钟");
            }
        } else if (mill - 1 > 0) {
            if (mill == 60) {
                sb.append("1分钟");
            } else {
                sb.append(mill + "秒");
            }
        } else {
            sb.append("刚刚");
        }
        if (!sb.toString().equals("刚刚")) {
            sb.append("前");
        }
        return sb.toString();
    }
    public static String getSpaceTime(Long millisecond) {

        long current=System.currentTimeMillis();//当前时间毫秒数
        long zero = current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数


        Calendar calendar = Calendar.getInstance();
        Long currentMillisecond = calendar.getTimeInMillis();


        //间隔秒
//        Long spaceSecond = (currentMillisecond - millisecond) / 1000;
        Long spaceSecond = (currentMillisecond - millisecond*1000) / 1000;
        //发布时间和今日零点相差的毫秒值
        long todaySecond = (millisecond*1000 - zero)/1000;
        //昨天
        long upSecond = (millisecond*1000 - zero)/1000 + 24*60*60;
        //前天
        long upUpSecond = (millisecond*1000 - zero)/1000 + 48*60*60;

        //一分钟之内
        if (spaceSecond >= 0 && spaceSecond < 60) {
            return "刚刚";
        }

        //如果是今天发布的
        else if (todaySecond > 0 ){
            //一小时之内
            if (spaceSecond / 60 > 0 && spaceSecond / 60 < 60) {
                return spaceSecond / 60 + "分钟前";
            }
            //一天之内
//            else if(spaceSecond / (60 * 60) > 0 && spaceSecond / (60 * 60) < 24) {
//                return spaceSecond / (60 * 60) + "小时之前";
//            }
            else{
                return spaceSecond / (60 * 60) + "小时前";
            }

        }
       else if(upSecond > 0){
            return "昨天";
        }
       else if (upUpSecond > 0){
            return "前天";
        }

        else {
            return getDateTimeFromMillisecond(millisecond*1000);
        }
    }

    public static String getDateTimeFromMillisecond(Long millisecond){
        Calendar calendar = Calendar.getInstance();
        Long currentMillisecond = calendar.getTimeInMillis();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(currentMillisecond);
        String dateStr = simpleDateFormat.format(date);

        Date date2 = new Date(millisecond);
        String dateStr2 = simpleDateFormat.format(date2);

        if (dateStr.substring(0,4).equals(dateStr2.substring(0,4))){
            return dateStr.substring(5,dateStr.length());
        }
        return dateStr.substring(0,dateStr.length());
    }

}
