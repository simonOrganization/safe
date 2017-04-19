package com.lchtime.safetyexpress.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by user on 2017/4/14.
 */

public abstract class MaskedImage extends ImageView {
    private static final Xfermode MASK_XFERMODE;
    private Bitmap mask;
    private Paint paint;

    public MaskedImage(Context paramContext) {
        super(paramContext);
    }

    public MaskedImage(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public MaskedImage(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
    }

    public abstract Bitmap createMask();

    protected void onDraw(Canvas paramCanvas) {
        Drawable localDrawable = this.getDrawable();
        if(localDrawable != null) {
            try {
                if(this.paint == null) {
                    Paint localException = new Paint();
                    this.paint = localException;
                    this.paint.setFilterBitmap(false);
                    Paint localStringBuilder1 = this.paint;
                    Xfermode i = MASK_XFERMODE;
                    localStringBuilder1.setXfermode(i);
                }

                float localException1 = this.getWidth() >= this.getHeight()?(float)this.getHeight():(float)this.getWidth();
                float localStringBuilder2 = (float)this.getHeight();
                int i1 = paramCanvas.saveLayer(0.0F, 0.0F, localException1, localException1, (Paint)null, 31);
                int j = this.getWidth() >= this.getHeight()?this.getHeight():this.getWidth();
                int k = this.getHeight();
                localDrawable.setBounds(0, 0, j, j);
                localDrawable.draw(paramCanvas);
                Bitmap localBitmap2;
                if(this.mask == null || this.mask.isRecycled()) {
                    localBitmap2 = this.createMask();
                    this.mask = localBitmap2;
                }

                localBitmap2 = this.mask;
                Paint localPaint3 = this.paint;
                paramCanvas.drawBitmap(localBitmap2, 0.0F, 0.0F, localPaint3);
                paramCanvas.restoreToCount(i1);
            } catch (Exception var10) {
                StringBuilder localStringBuilder = (new StringBuilder()).append("Attempting to draw with recycled bitmap. View ID = ");
                System.out.println("localStringBuilder==" + localStringBuilder);
            }
        }
    }

    static {
        PorterDuff.Mode localMode = PorterDuff.Mode.DST_IN;
        MASK_XFERMODE = new PorterDuffXfermode(localMode);
    }
}