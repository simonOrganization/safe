package com.lchtime.safetyexpress.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lchtime.safetyexpress.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yxn on 2017/4/20.
 */

public class CircleImageAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<String> circleTwoList;
    private IOnItemSelectListener pListener;

    public CircleImageAdapter(Context context, List<String> circleTwoList) {
        this.context = context;
        this.circleTwoList = circleTwoList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.circle_image_item,parent,false);
        return new CircleImageHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
       // ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        //layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        CircleImageHolder holder1 = (CircleImageHolder) holder;
//        ImageLoader.getInstance().displayImage(circleTwoList.get(position),holder1.circle_image,Option);
        if(!TextUtils.isEmpty(circleTwoList.get(position))){
            Glide.with(context).load(circleTwoList.get(position)).into(holder1.circle_image);
        }else {
            Glide.with(context).load(R.drawable.banner_default).into(holder1.circle_image);
        }
        holder1.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pListener != null){
                    pListener.onItemClick(v, position);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return circleTwoList.size();
    }



    class CircleImageHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.circle_image)
        ImageView circle_image;
        View view;
        public CircleImageHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this,itemView);
        }
    }
    public  interface IOnItemSelectListener{
        void onItemClick(View v,int pos);

    }
    public void setOnItemSelectLs(IOnItemSelectListener pListener){
        this.pListener = pListener;
    }
}
