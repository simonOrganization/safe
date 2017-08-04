package com.lchtime.safetyexpress.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lchtime.safetyexpress.R;
<<<<<<< HEAD
import com.lchtime.safetyexpress.ui.circle.protocal.CirclePhone;
import com.squareup.picasso.Picasso;
=======
>>>>>>> 717038468199931acd24852ee19d69def607f5db

import java.util.ArrayList;

/**
 * Created by user on 2017/4/18.
 */

public class HomeImgAdapter  extends BaseAdapter {

    private final ArrayList<String> lists;
    private Context context;
    private LayoutInflater inflater;

    //private int[] imgs;

    public HomeImgAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.lists  = list;
        inflater = LayoutInflater.from(context);
        //测试
        //imgs = new int[]{R.drawable.home_test_img6, R.drawable.home_test_img6, R.drawable.home_test_img6};
    }

    @Override
    public int getCount() {
        return lists == null ? 0 : lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.home_img_item, null);
            holder = new ViewHolder();
            holder.iv_img = (ImageView) convertView.findViewById(R.id.iv_img_img);

            holder.iv_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CirclePhone.class);
                    intent.putExtra("url", lists);
                    intent.putExtra("pos", position);
                    context.startActivity(intent);
                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
<<<<<<< HEAD
        Picasso.with(context).load(lists.get(position)).fit().into(holder.iv_img);
=======
        Glide.with(context).load(list.get(position)).into(holder.iv_img);
>>>>>>> 717038468199931acd24852ee19d69def607f5db
        return convertView;
    }

    class ViewHolder {
        ImageView iv_img;
    }
}
