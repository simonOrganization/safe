package com.lchtime.safetyexpress.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.HomeNewsRecommendBean;
import com.lchtime.safetyexpress.views.MultiImageView;
import com.lchtime.safetyexpress.views.MyGridView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by user on 2017/4/17.
 */

public class HomeNewsRecommendAdapter extends BaseAdapter {

    private static final int ITEM_VIEW_TYPE_DEFAULT = 0;
    private static final int ITEM_VIEW_TYPE_IMAGE = 1;
    private static final int ITEM_VIEW_TYPE_VIDEO = 2;
    private static final int ITEM_VIEW_TYPE_ONEIMG = 3;

    private static final String ITEM_TYPE_IMAGE = "1";
    private static final String ITEM_TYPE_VIDEO = "2";
    private static final String ITEM_TYPE_ONEIMG = "3";
    private static final int ITEM_VIEW_TYPE_COUNT = 4;

    private Context context;
    private List<String> photos;
    private List<HomeNewsRecommendBean> list;
    private HomeNewsRecommendBean newsRecommendBean;

    public HomeNewsRecommendAdapter(Context context) {
        this.context = context;
        //测试
        list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            newsRecommendBean = new HomeNewsRecommendBean();
            if (i % 2 == 0) {
                newsRecommendBean.setType("1");// 图片
            } else if (i == 1) {
                newsRecommendBean.setType("3");// 单张图片
            } else {
                newsRecommendBean.setType("2");//视频
            }
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
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        int itemViewType = ITEM_VIEW_TYPE_DEFAULT;
        if (ITEM_TYPE_IMAGE.equals(list.get(position).getType())) {
            itemViewType = ITEM_VIEW_TYPE_IMAGE;
        } else if (ITEM_TYPE_VIDEO.equals(list.get(position).getType())) {
            itemViewType = ITEM_VIEW_TYPE_VIDEO;
        } else if (ITEM_TYPE_ONEIMG.equals(list.get(position).getType())) {
            itemViewType = ITEM_VIEW_TYPE_ONEIMG;
        } else {
            itemViewType = ITEM_VIEW_TYPE_DEFAULT;
        }
        return itemViewType;
    }

    @Override
    public int getViewTypeCount() {
        return ITEM_VIEW_TYPE_COUNT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int itemViewType = getItemViewType(position);
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.home_news_recommend_item, null);
            ViewStub vs_imgorvideo = (ViewStub) convertView.findViewById(R.id.vs_imgorvideo);
            switch (itemViewType) {
                //多张图片
                case ITEM_VIEW_TYPE_IMAGE:
                    vs_imgorvideo.setLayoutResource(R.layout.home_news_recommend_item_imgbody);
                    vs_imgorvideo.inflate();
                    LinearLayout imgbody = (LinearLayout) convertView.findViewById(R.id.ll_imgbody);
                    if (imgbody != null) {
                        holder.ll_imgbody = imgbody;
//                        holder.miv_imgs = (MultiImageView) convertView.findViewById(R.id.miv_recommend_imgbody);
                        holder.mgv_imgs = (MyGridView)convertView.findViewById(R.id.mgv_recommend_imgbody);
                    }
                    break;
                //视频
                case ITEM_VIEW_TYPE_VIDEO:
                    vs_imgorvideo.setLayoutResource(R.layout.home_news_recommend_item_videobody);
                    vs_imgorvideo.inflate();
                    LinearLayout moneybody = (LinearLayout) convertView.findViewById(R.id.ll_videobody);
                    if (moneybody != null) {
                        holder.ll_moneybody = moneybody;
                        holder.iv_img = (ImageView) convertView.findViewById(R.id.iv_recommend_videobody_img);
                        holder.iv_play = (ImageView) convertView.findViewById(R.id.iv_recommend_videobody_play);
                    }
                    break;
                //单张图片
                case ITEM_VIEW_TYPE_ONEIMG:
                    convertView = View.inflate(context, R.layout.home_news_recommend_item_oneimg, null);
                    holder.iv_oneimg_img = (ImageView)convertView.findViewById(R.id.iv_recommend_oneimg_img);
                    break;
            }
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_recommend_title);
            holder.tv_from = (TextView) convertView.findViewById(R.id.tv_recommend_from);
            holder.tv_comment = (TextView) convertView.findViewById(R.id.tv_recommend_comment);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_recommend_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
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
        switch (itemViewType) {
            case ITEM_VIEW_TYPE_IMAGE:
//                if (photos != null && photos.size() > 0) {
//                    holder.miv_imgs.setVisibility(View.VISIBLE);
//                    holder.miv_imgs.setList(photos);
//                } else {
//                    holder.miv_imgs.setVisibility(View.GONE);
//                }
                HomeImgAdapter homeImgAdapter = new HomeImgAdapter(context);
                holder.mgv_imgs.setAdapter(homeImgAdapter);
                break;
            case ITEM_VIEW_TYPE_VIDEO:
                break;
            case ITEM_VIEW_TYPE_ONEIMG:
                break;
        }
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
    }
}
