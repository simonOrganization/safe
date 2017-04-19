package com.lchtime.safetyexpress.pop;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;

/**
 * Created by user on 2017/4/18.
 */

public class SharePop extends CommentPopUtils implements View.OnClickListener {

    private LinearLayout ll_weixin;
    private LinearLayout ll_friend;
    private LinearLayout ll_sina;
    private LinearLayout ll_qq;
    private TextView tv_cancel;

    public SharePop(View v, Context context, int layout) {
        super(v, context, layout);
    }

    @Override
    public void initLayout(View v, Context context) {
        ll_weixin = (LinearLayout)v.findViewById(R.id.ll_share_weixin);
        ll_friend = (LinearLayout)v.findViewById(R.id.ll_share_friend);
        ll_sina = (LinearLayout)v.findViewById(R.id.ll_share_sina);
        ll_qq = (LinearLayout)v.findViewById(R.id.ll_share_qq);
        tv_cancel = (TextView)v.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ll_weixin.setOnClickListener(this);
        ll_friend.setOnClickListener(this);
        ll_qq.setOnClickListener(this);
        ll_sina.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (onClickListener != null){
            onClickListener.onClick(v);
        }
    }
}
