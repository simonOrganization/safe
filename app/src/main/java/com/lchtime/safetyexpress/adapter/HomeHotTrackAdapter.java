package com.lchtime.safetyexpress.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.NewsBean;

import java.util.List;

/**
 * Created by user on 2017/4/14.
 */

public class HomeHotTrackAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<NewsBean> list;

    public HomeHotTrackAdapter(Context context,List<NewsBean> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list == null ? 3 : list.size();
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
            convertView = inflater.inflate(R.layout.home_hottrack_item, null);
            holder = new ViewHolder();
            holder.iv_img = (ImageView) convertView.findViewById(R.id.iv_hottrack_img);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_hottrack_title);
            holder.tv_from = (TextView) convertView.findViewById(R.id.tv_hottrack_from);
            holder.tv_zan = (TextView) convertView.findViewById(R.id.tv_hottrack_zan);
            holder.tv_comment = (TextView) convertView.findViewById(R.id.tv_hottrack_comment);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    class ViewHolder {
        ImageView iv_img;
        TextView tv_title;
        TextView tv_from;
        TextView tv_zan;
        TextView tv_comment;
    }
}