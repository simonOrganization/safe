package com.lchtime.safetyexpress.pop;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;

/**
 * 完善个人信息提示
 * Created by user on 2017/4/17.
 */

public class VipInfoHintPop extends CommentPopUtils implements View.OnClickListener {

    private TextView tv_perfect;
    public TextView tv_jump;
    private Activity activity;

    public VipInfoHintPop(View v, Activity activity, int layout) {
        super(v, activity, layout);
        this.activity = activity;
    }

    @Override
    public void initLayout(View v, Context context) {
        tv_perfect = (TextView) v.findViewById(R.id.tv_perfect);
        tv_jump = (TextView) v.findViewById(R.id.tv_jump);
        //设置完善按钮点击事件
        tv_perfect.setOnClickListener(this);
        //设置跳过按钮点击事件
        tv_jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity != null){
                    activity.finish();
                }
                dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (onClickListener != null){
            onClickListener.onClick(v);
        }
    }
}
