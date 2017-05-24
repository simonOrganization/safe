package com.lchtime.safetyexpress.pop;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;

/**
 * 完善个人信息提示
 * Created by user on 2017/4/17.
 */

public class GuiZePop extends CommentPopUtils implements View.OnClickListener {

    private TextView content;
    private TextView tv_perfect;

    public GuiZePop(View v, Activity activity, int layout) {
        super(v, activity, layout);
    }

    @Override
    public void initLayout(View v, Context context) {
        content = (TextView) v.findViewById(R.id.content);
        tv_perfect = (TextView) v.findViewById(R.id.tv_perfect);
        //设置知道按钮点击事件
        tv_perfect.setOnClickListener(new View.OnClickListener() {
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
