package com.lchtime.safetyexpress.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

/**
 * Created by user on 2017/4/14.
 */

public class CircleImageView extends MaskedImage {
    public CircleImageView(Context paramContext) {
        super(paramContext);
    }

    public CircleImageView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public CircleImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
    }

    public Bitmap createMask() {
        int i = this.getWidth() >= this.getHeight()?this.getHeight():this.getWidth();
        Bitmap.Config localConfig = Bitmap.Config.ARGB_8888;
        Bitmap localBitmap = Bitmap.createBitmap(i, i, localConfig);
        Canvas localCanvas = new Canvas(localBitmap);
        Paint localPaint = new Paint(1);
        localPaint.setColor(-16777216);
        float f1 = this.getWidth() >= this.getHeight()?(float)this.getHeight():(float)this.getWidth();
        RectF localRectF = new RectF(0.0F, 0.0F, f1, f1);
        localCanvas.drawOval(localRectF, localPaint);
        return localBitmap;
    }
}