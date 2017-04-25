package com.lchtime.safetyexpress.pop;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;

/**
 * 完善个人信息提示
 * Created by user on 2017/4/17.
 */

public class VipInfoHintPop extends CommentPopUtils implements View.OnClickListener {

    private TextView tv_perfect;
    private TextView tv_jump;

    public VipInfoHintPop(View v, Context context, int layout) {
        super(v, context, layout);
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
