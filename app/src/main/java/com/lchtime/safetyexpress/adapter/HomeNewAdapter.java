package com.lchtime.safetyexpress.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.CircleTwoBean;
import com.lchtime.safetyexpress.bean.NewsBean;
import com.lchtime.safetyexpress.views.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * Created by yxn on 2017/4/25.
 */

public class HomeNewAdapter extends RecyclerView.Adapter {
    private Context context;
    private static final int ITEM_VIEW_TYPE_IMAGE = 0;
    private static final int ITEM_VIEW_TYPE_ONE = 1;
    private static final int ITEM_VIEW_TYPE_VIDEO = 1;
    private ArrayList<NewsBean> mDatas;

    public HomeNewAdapter(Context context,ArrayList<NewsBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == ITEM_VIEW_TYPE_IMAGE){
            View view = LayoutInflater.from(context).inflate(R.layout.home_new_item,parent,false);
            return  new HomeNewHolder(view);
        }else if(viewType == ITEM_VIEW_TYPE_ONE){
            View view = LayoutInflater.from(context).inflate(R.layout.home_new_no_item,parent,false);
            return  new HomeNewNoHolder(view);
        }else if(viewType == ITEM_VIEW_TYPE_VIDEO){
            Log.i("yang","ITEM_VIEW_TYPE_VIDEO----");
            View view = LayoutInflater.from(context).inflate(R.layout.home_news_video_item,parent,false);
            return  new HomeNewVideoHolder(view);
        }
        return null;
    }
    @Override
    public int getItemViewType(int position) {
        int itemViewType ;
        if(position%2 ==0){
            itemViewType = ITEM_VIEW_TYPE_IMAGE ;
        }else if(position%3 ==0){
            itemViewType = ITEM_VIEW_TYPE_VIDEO ;
            Log.i("yang","position%3 ==0----");
        }else{
            Log.i("yang","else----");
            itemViewType = ITEM_VIEW_TYPE_ONE ;
        }
        return itemViewType;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newsItemInterface.setNewOnItem(position);
            }
        });
        final NewsBean bean = mDatas.get(position);
        if(holder instanceof HomeNewHolder){
            HomeNewHolder homeNewHolder = (HomeNewHolder) holder;
            ArrayList<CircleTwoBean> mlist = new ArrayList<CircleTwoBean>();
            mlist.add(new CircleTwoBean());
            mlist.add(new CircleTwoBean());
            mlist.add(new CircleTwoBean());
            homeNewHolder.textViews.get(0).setText(bean.getCc_title());
            homeNewHolder.home_new_item_rc.setLayoutManager(new GridLayoutManager(context,3));
            homeNewHolder.home_new_item_rc.addItemDecoration(new GridSpacingItemDecoration(3,10,true));
            CircleImageAdapter imageAdapter = new CircleImageAdapter(context,mlist);
            homeNewHolder.home_new_item_rc.setAdapter(imageAdapter);
        }else if(holder instanceof HomeNewNoHolder){
            HomeNewNoHolder homeNewNoHolder = (HomeNewNoHolder) holder;
            homeNewNoHolder.textViews.get(0).setText(bean.getCc_title());
        }else if(holder instanceof HomeNewVideoHolder){
            Log.i("yang","HomeNewVideoHolder----");
            HomeNewVideoHolder homeNewVideoHolder = (HomeNewVideoHolder) holder;
            homeNewVideoHolder.textViews.get(0).setText(bean.getCc_title());
            homeNewVideoHolder.home_new_video_item_video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    newsItemInterface.setVideoPlay(bean.getIs_media());
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
    class HomeNewHolder extends RecyclerView.ViewHolder{
        @BindViews({R.id.home_new_item_title,R.id.home_new_item_from,R.id.home_new_item_comment,R.id.home_new_item_time})
        List<TextView> textViews;
        @BindView(R.id.home_new_item_rc)
        RecyclerView home_new_item_rc;

        public HomeNewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    class HomeNewNoHolder extends RecyclerView.ViewHolder{
        @BindViews({R.id.home_new_no_item_title,R.id.home_new_no_item_from,R.id.home_new_no_item_comment,R.id.home_new_no_item_time})
        List<TextView> textViews;
        @BindView(R.id.home_new_no_item_image)
        ImageView home_new_no_item_image;

        public HomeNewNoHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    class HomeNewVideoHolder extends RecyclerView.ViewHolder{
        @BindViews({R.id.home_new_video_item_title,R.id.home_new_video_item_from,R.id.home_new_video_item_comment,R.id.home_new_video_item_time})
        List<TextView> textViews;
        @BindView(R.id.home_new_video_item_video)
        ImageView home_new_video_item_video;

        public HomeNewVideoHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    private NewsItemInterface newsItemInterface;
    public interface NewsItemInterface{
        void setNewOnItem(int position);
        void setVideoPlay(String url);
    }
    public  void setNewItemInterface(NewsItemInterface newsItemInterface){
        this.newsItemInterface = newsItemInterface;

    }
}
