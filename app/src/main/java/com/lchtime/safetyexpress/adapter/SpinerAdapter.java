package com.lchtime.safetyexpress.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.lchtime.safetyexpress.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yxn on 2017/3/23.
 */

public class SpinerAdapter extends RecyclerView.Adapter<SpinerAdapter.SpinerHolder>{
    private ArrayList<String> mObjects;
    private LayoutInflater mInflater;
    private IOnItemSelectListener mListener;

    public SpinerAdapter(ArrayList<String> mObjects, Context context) {
        this.mObjects = mObjects;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void refreshData(ArrayList<String> objects, int selIndex){
        mObjects = objects;
        if (selIndex < 0){
            selIndex = 0;
        }
        if (selIndex >= mObjects.size()){
            selIndex = mObjects.size() - 1;
        }
    }


    @Override
    public SpinerAdapter.SpinerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.spiner_item,null);
        return new SpinerHolder(view);
    }

    @Override
    public void onBindViewHolder(SpinerAdapter.SpinerHolder holder, final int position) {
        holder.spiner_tv.setText(mObjects.get(position));
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mObjects.size();
    }

    class SpinerHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.spiner_item_tv)
        TextView spiner_tv;
        View view;

        public SpinerHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this,itemView);

        }
    }

    public  interface IOnItemSelectListener{
         void onItemClick(int pos);
    }
    public void setOnItemSelectLs(IOnItemSelectListener mListener){
        this.mListener = mListener;
    }

}
