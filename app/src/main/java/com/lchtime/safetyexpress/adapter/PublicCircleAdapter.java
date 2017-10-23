package com.lchtime.safetyexpress.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.CircleTwoBean;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yxn on 2017/4/23.
 */

public class PublicCircleAdapter extends RecyclerView.Adapter<PublicCircleAdapter.PublicCircleHolder> {
    private Context context;
    private ArrayList<CircleTwoBean> mDatas;
    private OnItemClick onItemClick;

    public PublicCircleAdapter(ArrayList<CircleTwoBean> mDatas, Context context) {
        this.mDatas = mDatas;
        this.context = context;
        onItemClick = (OnItemClick) context;
    }

    @Override
    public PublicCircleAdapter.PublicCircleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.circle_image_item,parent,false);
        return new PublicCircleHolder(view);
    }

    @Override
    public void onBindViewHolder(PublicCircleAdapter.PublicCircleHolder holder, final int position) {
        Log.i("yang","onBindViewHolder   ===");
        if(position != mDatas.size()){
            Log.i("yang","if   ===");
            holder.image.setImageBitmap(mDatas.get(position).getImage());
        }else{
            Log.i("yang","else   ===");
            //holder.image.setBackgroundResource(R.drawable.public_circle_icon);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.setOnItemClick(position);
                Toast.makeText(context,"position==="+position,Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        if(mDatas.size() < 9){
            Log.i("yang","<9   ===");
            return mDatas.size()+1;
        }else{
            Log.i("yang","else  ===");
            return mDatas.size();
        }
    }

    public  interface OnItemClick{
        void setOnItemClick(int position);
    }
    public void setOnClick(OnItemClick onItemClick){
        this.onItemClick = onItemClick;
    }

    class PublicCircleHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.circle_image)
        ImageView image;

        public PublicCircleHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
