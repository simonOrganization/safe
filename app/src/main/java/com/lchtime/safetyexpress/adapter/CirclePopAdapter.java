package com.lchtime.safetyexpress.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.CircleSelectBean;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yxn on 2017/4/21.
 */

public class CirclePopAdapter extends RecyclerView.Adapter {
    private Context context;
    private ArrayList<CircleSelectBean> mDatas;
    private PopItemInterfce popItemInterfce;

    public CirclePopAdapter(Context context,ArrayList<CircleSelectBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.circle_pop_item,parent,false);
        return new CirclePopHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        CircleSelectBean bean = mDatas.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popItemInterfce.setItem(position);
            }
        });
        if(holder instanceof CirclePopHolder){
            CirclePopHolder circlePopHolder = (CirclePopHolder) holder;
            if(bean.isSelect()){
                circlePopHolder.circle_pop_tv.setTextColor(Color.parseColor("#ff0000"));
                circlePopHolder.circle_pop_view.setBackgroundColor(Color.RED);
                circlePopHolder.circle_pop_image.setVisibility(View.VISIBLE);
            }else{
                circlePopHolder.circle_pop_tv.setTextColor(Color.parseColor("#000000"));
                circlePopHolder.circle_pop_view.setBackgroundColor(Color.WHITE);
                circlePopHolder.circle_pop_image.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
    class CirclePopHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.circle_pop_tv)
        TextView circle_pop_tv;
        @BindView(R.id.circle_pop_view)
        View circle_pop_view;
        @BindView(R.id.circle_pop_image)
        ImageView circle_pop_image;

        public CirclePopHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    public interface PopItemInterfce{
        void setItem(int position);
    }
    public void setItemInterface(PopItemInterfce popItemInterfce){
        this.popItemInterfce = popItemInterfce;
    }
}
