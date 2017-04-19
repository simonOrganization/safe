package com.lchtime.safetyexpress.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;

/**
 * Created by user on 2017/4/14.
 */

public class HomeHotCircleAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private int[] imgs;
    private String[] txts;

    public HomeHotCircleAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        imgs = new int[]{R.drawable.home_test_img1, R.drawable.home_test_img2, R.drawable.home_test_img3, R.drawable.home_test_img4, R.drawable.home_test_img5};
        txts = new String[]{"BIG笑工坊", "轻松时刻", "完美红颜", "大咖秀", "万家灯火"};
    }

    @Override
    public int getCount() {
        return txts.length;
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
            convertView = inflater.inflate(R.layout.home_hotcircle_item, null);
            holder = new ViewHolder();
            holder.raiv_icon = (ImageView) convertView.findViewById(R.id.raiv_hotcircle_icon);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_hotcircle_name);
            holder.iv_subscribe = (ImageView) convertView.findViewById(R.id.iv_hotcircle_subscribe);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.raiv_icon.setBackgroundResource(imgs[position]);
        holder.tv_name.setText(txts[position]);

        return convertView;
    }

    class ViewHolder {
        ImageView raiv_icon;
        TextView tv_name;
        ImageView iv_subscribe;
    }
}