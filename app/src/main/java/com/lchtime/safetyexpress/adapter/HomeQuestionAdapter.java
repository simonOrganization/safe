package com.lchtime.safetyexpress.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.views.MyGridView;

import java.util.List;

/**
 * Created by user on 2017/4/18.
 */

public class HomeQuestionAdapter  extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<String> list;

    public HomeQuestionAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 3;
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
            convertView = inflater.inflate(R.layout.home_question_item, null);
            holder = new ViewHolder();
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_home_question_title);
            holder.mgv_question = (MyGridView) convertView.findViewById(R.id.mgv_home_question);
            holder.tv_question = (TextView) convertView.findViewById(R.id.tv_home_question_num);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HomeImgAdapter homeImgAdapter = new HomeImgAdapter(context);
        holder.mgv_question.setAdapter(homeImgAdapter);

        return convertView;
    }

    class ViewHolder {
        TextView tv_title;
        MyGridView mgv_question;
        TextView tv_question;
    }
}
