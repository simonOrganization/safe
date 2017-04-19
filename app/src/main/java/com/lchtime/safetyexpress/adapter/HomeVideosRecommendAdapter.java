package com.lchtime.safetyexpress.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;

import java.util.List;

/**
 * Created by user on 2017/4/18.
 */

public class HomeVideosRecommendAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<String> list;

    public HomeVideosRecommendAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list == null ? 10 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.home_videos_recommend_item, null);
            holder = new ViewHolder();
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_recommend_title);
            holder.ll_share = (LinearLayout) convertView.findViewById(R.id.ll_recommend_share);
            holder.iv_img = (ImageView) convertView.findViewById(R.id.iv_recommend_img);
            holder.iv_img_bg = (ImageView) convertView.findViewById(R.id.iv_recommend_img_bg);
            holder.iv_play = (ImageView) convertView.findViewById(R.id.iv_recommend_play);
            holder.tv_time1 = (TextView) convertView.findViewById(R.id.tv_recommend_time1);
            holder.tv_from = (TextView) convertView.findViewById(R.id.tv_recommend_from);
            holder.tv_comment = (TextView) convertView.findViewById(R.id.tv_recommend_comment);
            holder.tv_time2 = (TextView) convertView.findViewById(R.id.tv_recommend_time2);
            holder.tv_playnum = (TextView) convertView.findViewById(R.id.tv_recommend_playnum);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_title;
        LinearLayout ll_share;
        ImageView iv_img;
        ImageView iv_img_bg;
        ImageView iv_play;
        TextView tv_time1;
        TextView tv_from;
        TextView tv_comment;
        TextView tv_time2;
        TextView tv_playnum;
    }
}