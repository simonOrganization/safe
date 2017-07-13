package com.lchtime.safetyexpress.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lchtime.safetyexpress.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yxn on 2017/4/20.
 */

public class CircleImageAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<String> circleTwoList;

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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
       // ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        //layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        CircleImageHolder holder1 = (CircleImageHolder) holder;
//        ImageLoader.getInstance().displayImage(circleTwoList.get(position),holder1.circle_image,Option);
        if(!TextUtils.isEmpty(circleTwoList.get(position))){
            Picasso.with(context).load(circleTwoList.get(position)).into(holder1.circle_image);
        }else {
            Picasso.with(context).load(R.drawable.banner_default).into(holder1.circle_image);
        }

    }



//    public void setPicClick(String newsId,String type,String qc_id){
//        Intent intent = new Intent(context, H5DetailUI.class);
//        intent.putExtra("newsId",qc_id);
//        intent.putExtra("type","circle");
//        context.startActivity(intent);
//    }

    @Override
    public int getItemCount() {
        return circleTwoList.size();
    }
    class CircleImageHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.circle_image)
        ImageView circle_image;

        public CircleImageHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
