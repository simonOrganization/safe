package com.lchtime.safetyexpress.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yxn on 2017/4/26.
 */

public class NewSearchStringAdapter extends RecyclerView.Adapter {
    private ArrayList<String> mDatas;
    private Context context;

    public NewSearchStringAdapter(ArrayList<String> mDatas, Context context) {
        this.mDatas = mDatas;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.new_search_string_item,parent,false);
        return new NewSearchStringHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof NewSearchStringHolder){
            NewSearchStringHolder stringHolder = (NewSearchStringHolder) holder;
            stringHolder.new_search_string_tv.setText(mDatas.get(position));
        }

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
    class NewSearchStringHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.new_search_string_tv)
        TextView new_search_string_tv;

        public NewSearchStringHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
