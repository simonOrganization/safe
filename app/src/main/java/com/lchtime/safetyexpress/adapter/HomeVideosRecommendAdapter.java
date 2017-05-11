package com.lchtime.safetyexpress.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.NewsBean;
import com.lchtime.safetyexpress.ui.home.HomeVideosDeatilUI;
import com.lchtime.safetyexpress.ui.news.MediaActivity;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by android-cp on 2017/5/8.
 */

public class HomeVideosRecommendAdapter extends RecyclerView.Adapter{
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<NewsBean> videoList;

    public HomeVideosRecommendAdapter(Context context, ArrayList<NewsBean> videoList) {
        this.context = context;
        this.videoList = videoList;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.home_videos_recommend_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        ViewHolder myHolder = (ViewHolder)holder;
        final NewsBean bean = videoList.get(position);

        myHolder.tv_title.setText(bean.cc_title);
        Picasso.with(context).load(bean.media.get(0)).fit().into(myHolder.iv_img);
        myHolder.tv_from.setText(bean.cc_from);
        myHolder.tv_comment.setText(bean.plNum);

        myHolder.tv_time2.setText( CommonUtils.getSpaceTime(Long.parseLong(bean.cc_datetime)));
        myHolder.tv_playnum.setText(bean.cc_count + "次播放");
        myHolder.rl_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MediaActivity.class);
                intent.putExtra("url",bean.media.get(1));
                context.startActivity(intent);
            }
        });
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((position - 1) <getItemCount()) {
                    Intent intent = new Intent(context, HomeVideosDeatilUI.class);
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return videoList == null ? 0 : videoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_recommend_title)
        TextView tv_title;
        @BindView(R.id.ll_recommend_share)
        LinearLayout ll_share;
        @BindView(R.id.iv_recommend_img)
        ImageView iv_img;
        @BindView(R.id.iv_recommend_img_bg)
        ImageView iv_img_bg;
        @BindView(R.id.iv_recommend_play)
        ImageView iv_play;
        @BindView(R.id.tv_recommend_time1)
        TextView tv_time1;
        @BindView(R.id.tv_recommend_from)
        TextView tv_from;
        @BindView(R.id.tv_recommend_comment)
        TextView tv_comment;
        @BindView(R.id.tv_recommend_time2)
        TextView tv_time2;
        @BindView(R.id.tv_recommend_playnum)
        TextView tv_playnum;
        @BindView(R.id.rl_play)
        RelativeLayout rl_play;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
