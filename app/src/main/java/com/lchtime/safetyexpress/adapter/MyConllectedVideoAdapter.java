package com.lchtime.safetyexpress.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.HomeNewsRecommendBean;
import com.lchtime.safetyexpress.ui.vip.fragment.BaseFragment;
import com.lchtime.safetyexpress.ui.vip.fragment.VedioFragment;
import com.lchtime.safetyexpress.views.MyGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android-cp on 2017/4/21.
 */

public class MyConllectedVideoAdapter extends BaseAdapter {

    private Context context;
    private List<String> photos;
    private List<HomeNewsRecommendBean> list;
    private HomeNewsRecommendBean newsRecommendBean;
    private VedioFragment currentFragment;

    private boolean flag = false;

    public MyConllectedVideoAdapter(Context context, VedioFragment currentFragment) {
        this.context = context;
        this.currentFragment = currentFragment;
        //测试
        list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            newsRecommendBean = new HomeNewsRecommendBean();
            list.add(newsRecommendBean);
        }
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int itemViewType = getItemViewType(position);
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
                //单张图片

            convertView = View.inflate(context, R.layout.myconllected_video_item_oneimg, null);
            holder.iv_oneimg_img = (ImageView)convertView.findViewById(R.id.iv_recommend_oneimg_img);

            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_recommend_title);
            holder.tv_from = (TextView) convertView.findViewById(R.id.tv_recommend_from);
            holder.tv_comment = (TextView) convertView.findViewById(R.id.tv_recommend_comment);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_recommend_time);
            holder.rb = (CheckBox) convertView.findViewById(R.id.rb_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (flag == true){

            //设置checkbox的自定义背景

            Drawable drawable = currentFragment.getActivity().getResources().getDrawable(R.drawable.rb_delete);

        //设置drawable对象的大小
            drawable.setBounds(10,10,10,10);

        //设置CheckBox对象的位置，对应为左、上、右、下
           // holder.rb.setCompoundDrawables(drawable,null,null,null);

           // holder.rb.setBackgroundDrawable(drawable);

            //显示
            holder.rb.setVisibility(View.VISIBLE);
            holder.rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //如果选中就加一，否则减一
                    if (isChecked) {
                        currentFragment.updataDeleteNum(1);
                    }else {
                        currentFragment.updataDeleteNum(-1);
                    }
                }
            });
        }else {
            //隐藏
            holder.rb.setVisibility(View.GONE);
        }

        holder.tv_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "来源", Toast.LENGTH_SHORT).show();
            }
        });
        holder.tv_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "评论", Toast.LENGTH_SHORT).show();
            }
        });
        holder.tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "时间", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    class ViewHolder {
        LinearLayout ll_imgbody;
        LinearLayout ll_moneybody;
        //多张图片
//        MultiImageView miv_imgs;
        MyGridView mgv_imgs;
        //标题
        TextView tv_title;
        //来源
        TextView tv_from;
        //评论数
        TextView tv_comment;
        //时间
        TextView tv_time;
        //视频图片
        ImageView iv_img;
        //视频播放
        ImageView iv_play;
        //单张图片
        ImageView iv_oneimg_img;

        CheckBox rb;
    }

    public void setDelete(boolean isDelete){
        this.flag = isDelete;
    }
}
