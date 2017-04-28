package com.lchtime.safetyexpress.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by android-cp on 2017/4/28.
 */

public class BitmapUtils {
    public static Bitmap getBitmap (String path){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高
        Bitmap bitmap = BitmapFactory.decodeFile(path, options); //此时的bitmap为null
        //更改
        options.inJustDecodeBounds = false;
        //计算缩放比
        int be = (int)(options.outHeight / (float)200);
        if (be < 0){
            be = 1;
        }
        options.inSampleSize = be;
        bitmap=BitmapFactory.decodeFile(path,options);//此时的bitmap不为null
        return bitmap;
    }
}
