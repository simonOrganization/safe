package com.lchtime.safetyexpress.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.WenDaBean;
import com.lchtime.safetyexpress.ui.Const;
import com.lchtime.safetyexpress.ui.home.HomeQuestionUI;
import com.lchtime.safetyexpress.ui.home.HomeQuewstionDetail;
import com.lchtime.safetyexpress.views.MyGridView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 2017/4/18.
 */

public class HomeQuestionAdapter extends RecyclerView.Adapter {

    private static final int ONE_PIC_TYPE = 0;
    private static final int MUTI_PIC_TYPE = 1;

    private Activity context;
    private LayoutInflater inflater;
    private List<WenDaBean.TwBean> list;

    public HomeQuestionAdapter(Activity context, List<WenDaBean.TwBean> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.home_question_item, null);
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        WenDaBean.TwBean bean = list.get(position);
        if (bean.pic.size() == 1) {
            return ONE_PIC_TYPE;
        } else {
            return MUTI_PIC_TYPE;
        }
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final WenDaBean.TwBean bean = list.get(position);
        ViewHolder myHolder = (ViewHolder) holder;

        ViewGroup.LayoutParams lp =  myHolder.iv_question.getLayoutParams();
        lp.width = 300;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        myHolder.iv_question.setLayoutParams(lp);
        myHolder.iv_question.setMaxWidth(300);
        //context.getWindowManager().getDefaultDisplay().getWidth() - 100
        myHolder.iv_question.setMaxHeight(300*5);
        if (ONE_PIC_TYPE == getItemViewType(position)) {
            //一张图
            myHolder.iv_question.setVisibility(View.VISIBLE);
            myHolder.mgv_question.setVisibility(View.GONE);
            Picasso.with(context).load(bean.pic.get(0)).into(myHolder.iv_question);
        } else {
            //多张图或者没图
            if (list.size() == 0) {
                myHolder.iv_question.setVisibility(View.GONE);
                myHolder.mgv_question.setVisibility(View.GONE);
            } else {
                myHolder.iv_question.setVisibility(View.GONE);
                myHolder.mgv_question.setVisibility(View.VISIBLE);
                HomeImgAdapter adapter = new HomeImgAdapter(context, bean.pic);
                myHolder.mgv_question.setAdapter(adapter);
            }
        }

        myHolder.tv_title.setText(bean.q_title);
        myHolder.tv_question.setText(bean.hd_num + "回答");
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //问答详情
                Intent intent = new Intent(context,HomeQuewstionDetail.class);
                intent.putExtra("q_id",bean.q_id);
                context.startActivity(intent);
            }
        });
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_home_question_title)
        TextView tv_title;
        @BindView(R.id.mgv_home_question)
        MyGridView mgv_question;
        @BindView(R.id.one_pic_home_question)
        ImageView iv_question;
        @BindView(R.id.tv_home_question_num)
        TextView tv_question;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
