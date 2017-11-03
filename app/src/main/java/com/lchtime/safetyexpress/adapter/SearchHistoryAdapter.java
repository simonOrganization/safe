package com.lchtime.safetyexpress.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${Hongcha36} on 2017/8/18.
 *
 */

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.SearchHistoryHolder> {

    private Context context;
    private ArrayList<String> history;

    public SearchHistoryAdapter(Context context, ArrayList<String> history) {
        this.context = context;
        this.history = history;
    }

    @Override
    public SearchHistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // if use "parent", only first line will show up
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_history , null , false);
        SearchHistoryHolder holder = new SearchHistoryHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(SearchHistoryHolder holder, final int position) {
        holder.contentTv.setText(history.get(position));
        holder.contentTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(position , history.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return history == null ? 0 : history.size();
    }

    class SearchHistoryHolder extends RecyclerView.ViewHolder{
        TextView contentTv;
        public SearchHistoryHolder(View itemView) {
            super(itemView);

            contentTv = (TextView) itemView.findViewById(R.id.tv_content);

        }
    }


    protected OnItemClickListener mItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public interface OnItemClickListener{
        void onItemClick(int position , String content);
    }
}
