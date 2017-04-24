package com.lchtime.safetyexpress.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.ui.circle.PublicCircleUI;

import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by yxn on 2017/4/21.
 */

public class CirclePublicPop extends PopupWindow {
    private Context context;


    public CirclePublicPop(Context context) {
        super(context);
        this.context = context;
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.circle_public_layout,null);
        ButterKnife.bind(this,view);
        // 设置SelectPicPopupWindow的View
        this.setContentView(view);
//        view.findViewById(R.id.circle_public_tv).setOnClickListener(this);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);

    }
    @OnClick({R.id.circle_public_tv,R.id.circle_public_camera,R.id.circle_public_question})
    void setOnclick(View view){
        switch (view.getId()){
            case R.id.circle_public_tv:
                context.startActivity(new Intent(context, PublicCircleUI.class));
                break;
            case R.id.circle_public_camera:

                break;
            case R.id.circle_public_question:

                break;
        }
    }
    public void showPopData(View parent){
        if(!this.isShowing()){
            int[] location = new int[2];
            parent.getLocationInWindow(location);
            this.showAtLocation(parent,Gravity.NO_GRAVITY,location[0], location[1]+300);//-this.getHeight()
        }else{
            this.dismiss();
        }

    }


}
