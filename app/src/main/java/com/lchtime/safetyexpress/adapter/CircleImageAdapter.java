package com.lchtime.safetyexpress.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.CircleTwoBean;

import java.sql.Array;
import java.util.ArrayList;

/**
 * Created by yxn on 2017/4/20.
 */

public class CircleImageAdapter extends RecyclerView.Adapter {
    private Context context;
    private ArrayList<CircleTwoBean> circleTwoList;

    public CircleImageAdapter(Context context, ArrayList<CircleTwoBean> circleTwoList) {
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
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;

    }

    @Override
    public int getItemCount() {
        return circleTwoList.size();
    }
    class CircleImageHolder extends RecyclerView.ViewHolder{

        public CircleImageHolder(View itemView) {
            super(itemView);
        }
    }
}
