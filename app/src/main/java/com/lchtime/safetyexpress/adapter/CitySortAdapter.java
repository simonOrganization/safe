package com.lchtime.safetyexpress.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;


import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.views.CityView.CitySortModel;

import java.util.List;

public class CitySortAdapter extends BaseAdapter implements SectionIndexer {

    private  String city;
    private List<CitySortModel> list = null;
    private Context mContext;

    public CitySortAdapter(Context mContext, List<CitySortModel> list ,String city ) {
        this.mContext = mContext;
        this.list = list;
        this.city = city;

    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        final CitySortModel mContent = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_select_city, null);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.tv_city_name);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.tv_catagory);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        int section = getSectionForPosition(position);

        if (position == getPositionForSection(section)) {
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(mContent.getSortLetters());
        } else {
            viewHolder.tvLetter.setVisibility(View.GONE);
        }
       // Log.i("----------", "clickEvent: 2" +city );

        if (list.get(position).getName().equals(city)) {
            list.get(position).isSelect = true ;
            viewHolder.tvTitle.setText(this.list.get(position).getName());
            viewHolder.tvTitle.setTextColor(mContext.getResources().getColor(R.color.red));
        }else{
            viewHolder.tvTitle.setText(this.list.get(position).getName());
            viewHolder.tvTitle.setTextColor(mContext.getResources().getColor(R.color.black));
        }


        return view;

    }


    final static class ViewHolder {
        TextView tvLetter;
        TextView tvTitle;
    }

    public int getSectionForPosition(int position) {
        return list.get(position).getSortLetters().charAt(0);
    }
    public String setCity (String city) {
      return this.city = city ;
    }

    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }


    @Override
    public Object[] getSections() {
        return null;
    }
}