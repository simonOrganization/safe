package com.lchtime.safetyexpress.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lchtime.safetyexpress.R;
import com.lidroid.xutils.view.annotation.ContentView;

import butterknife.ButterKnife;

/**
 * Created by yxn on 2017/4/21.
 */

public class CircleSubscribAdapter extends RecyclerView.Adapter {
    private Context context;

    public CircleSubscribAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_subscrib_item,parent,false);
        return new CircleSubscribeHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 8;
    }
    class CircleSubscribeHolder extends RecyclerView.ViewHolder{

        public CircleSubscribeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
