package com.lchtime.safetyexpress.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * Created by user on 2017/4/14.
 */

public class MyScrollView extends ScrollView {
    private int page = 1;
    private MyScrollView.OnScroll onScroll;
    private MyScrollView.OnScrollLoad onScrollLoad;
    private boolean scroll = false;

    private int downX;
    private int downY;
    private int mTouchSlop;

    public MyScrollView(Context context) {
        super(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @SuppressLint({"NewApi"})
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        if(this.onScroll != null) {
            this.onScroll.onScroll(scrollX, scrollY, clampedX, clampedY);
        }

        if(scrollY > 0 && clampedY) {
            if(!this.scroll) {
                this.scroll = true;
                if(this.onScrollLoad != null) {
                    ++this.page;
                    this.onScrollLoad.onLoad(this.page);
                }
            }
        } else {
            this.scroll = false;
        }

    }

    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        return 0;
    }

    public void setOnScroll(MyScrollView.OnScroll onScroll) {
        this.onScroll = onScroll;
    }

    public void setOnScrollLoad(MyScrollView.OnScrollLoad onScrollLoad) {
        this.onScrollLoad = onScrollLoad;
    }

    public interface OnScrollLoad {
        void onLoad(int var1);
    }

    public interface OnScroll {
        void onScroll(int var1, int var2, boolean var3, boolean var4);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) e.getRawX();
                downY = (int) e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) e.getRawY();
                if (Math.abs(moveY - downY) > mTouchSlop) {
                    return true;
                }
        }
        return super.onInterceptTouchEvent(e);
    }

}
