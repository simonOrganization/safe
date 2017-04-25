package com.lchtime.safetyexpress.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lchtime.safetyexpress.MyApplication;

import java.util.List;

/**
 * Created by android-cp on 2017/4/24.
 */

public class OpnionPicAdapter extends RecyclerView.Adapter {
    private List<Uri> list;

    public OpnionPicAdapter(List<Uri> list){
        this.list = list;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageView iv = new ImageView(MyApplication.getContext());
        return new MyViewHolder(iv);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ImageView)holder.itemView).setImageURI(list.get(position));
    }

    @Override
    public int getItemCount() {
        if (list != null && list.size() > 0){

            return list.size();
        }
        return 0;
    }


    class MyViewHolder extends RecyclerView.ViewHolder{

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
