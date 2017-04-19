package com.lchtime.safetyexpress.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;

import java.util.List;

/**
 * Created by user on 2017/4/18.
 */

public class HomeImgAdapter  extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<String> list;
    private int[] imgs;

    public HomeImgAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        //测试
        imgs = new int[]{R.drawable.home_test_img6, R.drawable.home_test_img6, R.drawable.home_test_img6};
    }

    @Override
    public int getCount() {
        return imgs.length;
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
            convertView = inflater.inflate(R.layout.home_img_item, null);
            holder = new ViewHolder();
            holder.iv_img = (ImageView) convertView.findViewById(R.id.iv_img_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.iv_img.setBackgroundResource(imgs[position]);
        return convertView;
    }

    class ViewHolder {
        ImageView iv_img;
    }
}
