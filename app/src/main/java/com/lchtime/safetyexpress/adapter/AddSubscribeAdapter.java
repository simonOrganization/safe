package com.lchtime.safetyexpress.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lchtime.safetyexpress.R;

/**
 * Created by yxn on 2017/4/23.
 */

public class AddSubscribeAdapter extends RecyclerView.Adapter {
    private Context context;

    public AddSubscribeAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.add_subscirbe_item,parent,false);
        return new AddSubscribeHodler(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 9;
    }
    class AddSubscribeHodler extends RecyclerView.ViewHolder{

        public AddSubscribeHodler(View itemView) {
            super(itemView);
        }
    }
}
