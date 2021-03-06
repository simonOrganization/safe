package com.lchtime.safetyexpress.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.HotCircleBean;
import com.lchtime.safetyexpress.bean.InitInfo;
import com.lchtime.safetyexpress.bean.res.CircleBean;
import com.lchtime.safetyexpress.ui.circle.SingleInfoUI;
import com.lchtime.safetyexpress.ui.circle.protocal.CircleProtocal;
import com.lchtime.safetyexpress.ui.login.LoginUI;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lchtime.safetyexpress.weight.GlideCircleTransform;
import com.lchtime.safetyexpress.weight.LoginDialog;

import java.util.List;

/**
 * Created by user on 2017/4/14.
 */

public class HomeHotCircleAdapter extends BaseAdapter {

    private Activity context;
    private LayoutInflater inflater;
//    private int[] imgs;
//    private String[] txts;
    private List<HotCircleBean.HotBean> list;
    private CircleProtocal protocal;

    public HomeHotCircleAdapter(Activity context, List<HotCircleBean.HotBean> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;

    }

    @Override
    public int getCount() {
        return list.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (protocal == null) {
            protocal = new CircleProtocal();
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.home_hotcircle_item, null);
            holder = new ViewHolder();
            holder.raiv_icon = (ImageView) convertView.findViewById(R.id.raiv_hotcircle_icon);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_hotcircle_name);
            //holder.iv_subscribe = (CheckBox) convertView.findViewById(R.id.iv_hotcircle_subscribe);
            holder.hot_circle_item = (LinearLayout) convertView.findViewById(R.id.hot_circle_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Glide.with(context)
                .load(list.get(position).ud_photo_fileid)
                .bitmapTransform(new GlideCircleTransform(context , 8))
                .placeholder(R.drawable.icon_head_rectangle)
                .error(R.drawable.icon_head_rectangle)
                .into(holder.raiv_icon);

        //holder.iv_subscribe.setChecked(list.get(position).checked);
        holder.tv_name.setText(list.get(position).ud_nickname);
        final ViewHolder finalHolder = holder;
        holder.hot_circle_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userid = SpTools.getUserId(context);
                if (TextUtils.isEmpty(userid)){
                    LoginDialog dialog = new LoginDialog(context, new LoginDialog.onClickLogin() {
                        @Override
                        public void OnClickLogin() {
                            Intent intent = new Intent(context,LoginUI.class);
                            context.startActivity(intent);
                        }
                    });
                    dialog.show();
                    return;
                }
                Intent intent = new Intent(context, SingleInfoUI.class);
                intent.putExtra("uid",list.get(position).ud_ub_id);
                //是否订阅  热门圈子肯定都是没有订阅的
                context.startActivity(intent);
            }
        });


        return convertView;
    }

    class ViewHolder {
        ImageView raiv_icon;
        TextView tv_name;
        //CheckBox iv_subscribe;
        LinearLayout hot_circle_item;
    }
}