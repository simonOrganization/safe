package com.lchtime.safetyexpress.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.WenDaBean;

import com.lchtime.safetyexpress.ui.circle.protocal.CirclePhone;

import com.lchtime.safetyexpress.ui.home.HomeQuestionUI;
import com.lchtime.safetyexpress.ui.home.HomeQuewstionDetail;

import com.lchtime.safetyexpress.views.MyGridView;

import java.util.ArrayList;
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


        if (ONE_PIC_TYPE == getItemViewType(position)) {
            //一张图
            myHolder.iv_question.setVisibility(View.VISIBLE);
            myHolder.mgv_question.setVisibility(View.GONE);
            Glide.with(context).load(bean.pic.get(0))
                    .placeholder(R.drawable.home_banner)
                    .error(R.drawable.home_banner)
                    .into(myHolder.iv_question);
            final ArrayList<String> picList = (ArrayList<String>) list.get(position).pic;

            myHolder.iv_question.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CirclePhone.class);
                    intent.putExtra("url", picList);
                    intent.putExtra("pos", "1");
                    context.startActivity(intent);
                }
            });
        } else {
            //多张图或者没图
            if (list.size() == 0) {
                myHolder.iv_question.setVisibility(View.GONE);
                myHolder.mgv_question.setVisibility(View.GONE);
            } else {
                final ArrayList<String> picList = (ArrayList<String>) list.get(position).pic;
                myHolder.iv_question.setVisibility(View.GONE);
                myHolder.mgv_question.setVisibility(View.VISIBLE);
                HomeImgAdapter adapter = new HomeImgAdapter(context, picList);
                myHolder.mgv_question.setAdapter(adapter);

            }
        }

        myHolder.tv_title.setText(bean.q_title);
        myHolder.tv_question.setText(bean.hdNum + "回答");
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //问答详情
                Intent intent = new Intent(context,HomeQuewstionDetail.class);
                intent.putExtra("q_id",bean.q_id);
                context.startActivityForResult(intent , HomeQuestionUI.HOME_QUESTION);
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
