package com.lchtime.safetyexpress.weight;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;

/**
 * Created by Administrator on 2017/7/27.
 * 仿微信朋友圈文字展开全文功能
 */

public class ShowMoreTextView extends LinearLayout {

    //    用来标记是否为展开状态
    private boolean isShowAll = false;
    private TextView textView;
    private TextView button;


    public ShowMoreTextView(Context context) {
        super(context);
    }

    public ShowMoreTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
//        实例化layoutInflater对象，获取到布局填充服务
        LayoutInflater layoutInflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        填充自定义的布局xml文件
        layoutInflater.inflate(R.layout.showmore,this);
        textView = (TextView)findViewById(R.id.content);
        button=(TextView) findViewById(R.id.hide_show);
        button.setText("显示更多");
//        隐藏或显示
        hideOrShow();

    }

    public ShowMoreTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    //    创建setContent方法为TextView填充内容
    public void setText(String content) {
        textView.setText(content);
    }

    /**
     * 控制点击之后的显示效果
     */
    public void hideOrShow() {
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //由hideOrShow的值确定按钮和textview的状态
                if (isShowAll) {
                    button.setText("显示更多");
                    textView.setMaxLines(6);
                }else {
                    button.setText("收起");
                    textView.setMaxLines(1000);
                }
                isShowAll = !isShowAll;
            }
        });
    }


}
