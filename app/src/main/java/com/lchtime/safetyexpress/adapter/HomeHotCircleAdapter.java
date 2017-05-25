package com.lchtime.safetyexpress.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.HotCircleBean;
import com.lchtime.safetyexpress.bean.InitInfo;
import com.lchtime.safetyexpress.bean.res.CircleBean;
import com.lchtime.safetyexpress.ui.circle.CircleUI;
import com.lchtime.safetyexpress.ui.circle.SubscribActivity;
import com.lchtime.safetyexpress.ui.circle.protocal.CircleProtocal;
import com.lchtime.safetyexpress.ui.home.HomeUI;
import com.lchtime.safetyexpress.ui.home.protocal.HotCirclesProtocal;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.SpTools;
import com.squareup.picasso.Picasso;

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
//        imgs = new int[]{R.drawable.home_test_img1, R.drawable.home_test_img2, R.drawable.home_test_img3, R.drawable.home_test_img4, R.drawable.home_test_img5};
//        txts = new String[]{"BIG笑工坊", "轻松时刻", "完美红颜", "大咖秀", "万家灯火"};
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
            holder.iv_subscribe = (CheckBox) convertView.findViewById(R.id.iv_hotcircle_subscribe);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (!TextUtils.isEmpty(list.get(position).ud_photo_fileid)) {
            Picasso.with(context).load(list.get(position).ud_photo_fileid).fit().into(holder.raiv_icon);
        }
        holder.iv_subscribe.setChecked(list.get(position).checked);
        holder.tv_name.setText(list.get(position).ud_nickname);
        final ViewHolder finalHolder = holder;
        holder.iv_subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userid = SpTools.getString(context, Constants.userId,"");
                if (TextUtils.isEmpty(userid)){
                    CommonUtils.toastMessage("请登陆！！！");
                    finalHolder.iv_subscribe.setChecked(list.get(position).checked);
                    return;
                }else {
                    String type = finalHolder.iv_subscribe.isChecked() ? "1" : "0";
                    protocal.changeSubscribe(userid, list.get(position).ud_ub_id, type , new CircleProtocal.CircleListener() {
                        @Override
                        public void circleResponse(CircleBean response) {
                            if ("10".equals(response.result.code)){
                                list.get(position).checked =  !list.get(position).checked;
                                InitInfo.circleRefresh = true;
                            }else {
                                //订阅或者取消订阅失败了
                                notifyDataSetChanged();
                            }
                            CommonUtils.toastMessage(response.result.getInfo());
                        }
                    });
                }
            }
        });

        return convertView;
    }

    class ViewHolder {
        ImageView raiv_icon;
        TextView tv_name;
        CheckBox iv_subscribe;
    }
}