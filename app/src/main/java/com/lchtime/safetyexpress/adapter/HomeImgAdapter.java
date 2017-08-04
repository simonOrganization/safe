package com.lchtime.safetyexpress.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lchtime.safetyexpress.R;

import java.util.List;

/**
 * Created by user on 2017/4/18.
 */

public class HomeImgAdapter  extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<String> list;
    //private int[] imgs;

    public HomeImgAdapter(Context context,List<String> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        //测试
        //imgs = new int[]{R.drawable.home_test_img6, R.drawable.home_test_img6, R.drawable.home_test_img6};
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.home_img_item, null);
            holder = new ViewHolder();
            holder.iv_img = (ImageView) convertView.findViewById(R.id.iv_img_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(context).load(list.get(position)).into(holder.iv_img);
        return convertView;
    }

    class ViewHolder {
        ImageView iv_img;
    }
}
