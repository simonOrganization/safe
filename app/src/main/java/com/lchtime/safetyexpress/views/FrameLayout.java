package com.lchtime.safetyexpress.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by yxn on 2017/4/21.
 */

public class FrameLayout extends android.widget.FrameLayout {
    public FrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public FrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FrameLayout(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //核心就是下面这块代码块啦
        int width = getMeasuredWidth();
        setMeasuredDimension(width, width);
        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.height = width;
        setLayoutParams(lp);
    }
}
