package com.lchtime.safetyexpress.weight;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.sina.weibo.sdk.utils.LogUtil;

/**
 * Created by ${Hongcha36} on 2017/8/18.
 */

/**
 * Created by Administrator on 2017/5/10.
 * Glide 加载圆形图片
 */
public class GlideCircleTransform extends BitmapTransformation {

    private static float radius = 0f;
    public GlideCircleTransform(Context context) {
        super(context);
    }
    public GlideCircleTransform(Context context , int dp) {
        super(context);
        this.radius = Resources.getSystem().getDisplayMetrics().density * dp;
    }
    public GlideCircleTransform(BitmapPool bitmapPool) {
        super(bitmapPool);
    }
    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        if(radius == 0f){
            return circleCrop(pool, toTransform);
        }
        return roundCrop(pool, toTransform);
    }
    @Override
    public String getId() {
        return getClass().getName();
    }
    /**
     * 画圆形
     * @param pool
     * @param source
     * @return
     */
    private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;
        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;
        // TODO this could be acquired from the pool too
        Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);
        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);
        LogUtil.i("fxp", "返回数据");
        return result;
    }
    /**
     * 画圆角矩形
     * @param pool
     * @param source
     * @return
     */
    private static Bitmap roundCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;
        LogUtil.i("fxp" , "加载矩形");
        Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
        canvas.drawRoundRect(rectF, radius, radius, paint);
        return result;
    }
}