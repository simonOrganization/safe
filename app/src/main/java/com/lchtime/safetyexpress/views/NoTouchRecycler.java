package com.lchtime.safetyexpress.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by yxn on 2017/4/28.
 */

public class NoTouchRecycler extends RecyclerView {
    public NoTouchRecycler(Context context) {
        super(context);
    }

    public NoTouchRecycler(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

//    public NoTouchRecycler(Context context, @Nullable AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//    }

}
