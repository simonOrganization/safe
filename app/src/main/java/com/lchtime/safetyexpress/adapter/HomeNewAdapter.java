package com.lchtime.safetyexpress.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lchtime.safetyexpress.H5DetailUI;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.NewsBean;
import com.lchtime.safetyexpress.ui.circle.protocal.CirclePhone;
import com.lchtime.safetyexpress.ui.news.MediaActivity;
import com.lchtime.safetyexpress.ui.vip.fragment.NewsFragment;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.views.NoTouchRecycler;

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
    private static final int ITEM_VIEW_TYPE_VIDEO = 2;
    private ArrayList<NewsBean> mDatas;

    public List<NewsBean> list = new ArrayList<>();
    private Fragment fragment;

    private boolean flag = false;

    public HomeNewAdapter(Context context, ArrayList<NewsBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_IMAGE) {
            View view = LayoutInflater.from(context).inflate(R.layout.home_new_item, parent, false);
            return new HomeNewHolder(view);
        } else if (viewType == ITEM_VIEW_TYPE_ONE) {
            View view = LayoutInflater.from(context).inflate(R.layout.home_new_no_item, parent, false);
            return new HomeNewNoHolder(view);
        } else if (viewType == ITEM_VIEW_TYPE_VIDEO) {
            //View view = LayoutInflater.from(context).inflate(R.layout.home_news_video_item, parent, false);
            View view = LayoutInflater.from(context).inflate(R.layout.home_hotvideo_item , parent, false);
            return new HomeNewVideoHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        int itemViewType = -1;
        String beanType = mDatas.get(position).getCc_mark();
        if (!TextUtils.isEmpty(beanType)) {
            if (beanType.contains("A")) {
                //一个图
                itemViewType = ITEM_VIEW_TYPE_ONE;
            } else if (beanType.contains("B")) {
                //多图
                itemViewType = ITEM_VIEW_TYPE_IMAGE;
            } else if (beanType.contains("C")) {
                //视频
                itemViewType = ITEM_VIEW_TYPE_VIDEO;
            } else if (beanType.contains("D")) {
                //文字
                itemViewType = ITEM_VIEW_TYPE_IMAGE;
            } else if (beanType.contains("V")) {
                //视频
                itemViewType = ITEM_VIEW_TYPE_VIDEO;
            } else {
                itemViewType = ITEM_VIEW_TYPE_IMAGE;
            }
        } else {
            itemViewType = ITEM_VIEW_TYPE_IMAGE;
        }
        return itemViewType;
    }

    /*--------------------------------------------------------------------------------------------------------------------------------------------*/

    public List<NewsBean> getUpdataList() {
        return list;
    }

    public void setCheckBoxShow(Fragment fragment, boolean isShow) {
        this.fragment = fragment;
        flag = isShow;
    }


    public void setCheckBox(CheckBox rb, final int position) {
        //设置删除按钮
        if (flag == true) {

            //设置checkbox的自定义背景

            /*Drawable drawable = fragment.getActivity().getResources().getDrawable(R.drawable.rb_delete);

            //设置drawable对象的大小
            drawable.setBounds(0,0,20,20);

            //设置CheckBox对象的位置，对应为左、上、右、下
            rb.setCompoundDrawables(drawable,null,null,null);
            */
            //显示
            rb.setVisibility(View.VISIBLE);
            final NewsBean newsBean = mDatas.get(position);
            rb.setChecked(newsBean.isCheck);
            rb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fragment instanceof NewsFragment) {
                        //如果之前不是选中的状态
                        if (!newsBean.isCheck) {
                            list.add(newsBean);
                            ((NewsFragment) fragment).updataDeleteNum(1);

                        } else {
                            list.remove(newsBean);
                            ((NewsFragment) fragment).updataDeleteNum(-1);
                        }
                    }
                    newsBean.isCheck = !newsBean.isCheck;
                }
            });
        } else {
            //隐藏
            rb.setVisibility(View.GONE);
        }
    }

    /*--------------------------------------------------------------------------------------------------------------------------------------------*/
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newsItemInterface.setNewOnItem(position);
            }
        });

        final NewsBean bean = mDatas.get(position);
        if (holder instanceof HomeNewHolder) {
            setCheckBox(((HomeNewHolder) holder).rb, position);
            HomeNewHolder homeNewHolder = (HomeNewHolder) holder;
            homeNewHolder.textViews.get(0).setText(bean.getCc_title());
            if (bean.getMedia().size() == 0) {
                homeNewHolder.home_new_item_rc.setVisibility(View.GONE);
            } else {
                homeNewHolder.home_new_item_rc.setVisibility(View.VISIBLE);
            }
            homeNewHolder.home_new_item_rc.setLayoutManager(new GridLayoutManager(context, 3));
//            homeNewHolder.home_new_item_rc.addItemDecoration(new GridSpacingItemDecoration(3,0,true));
              homeNewHolder.home_new_item_rc.setClickable(false);
              homeNewHolder.home_new_item_rc.setPressed(false);
               homeNewHolder.home_new_item_rc.setEnabled(false);
            if (bean.getMedia() != null && bean.getMedia().size() > 0) {
                CircleImageAdapter imageAdapter = new CircleImageAdapter(context, bean.getMedia());
                homeNewHolder.home_new_item_rc.setAdapter(imageAdapter);

                imageAdapter.setOnItemSelectLs(new CircleImageAdapter.IOnItemSelectListener() {
                    @Override
                    public void onItemClick(View v, int pos) {

                        Intent intent = new Intent(context, H5DetailUI.class);
                        intent.putExtra("newsId", bean.cc_id);
                        intent.putExtra("type", "news");
                        context.startActivity(intent);
                    }
                });
            }
            homeNewHolder.textViews.get(1).setText(bean.getCc_from());
            homeNewHolder.textViews.get(2).setText(bean.getPlNum() + "评论");
            if (!TextUtils.isEmpty(bean.getCc_datetime())) {
                homeNewHolder.textViews.get(3).setText(CommonUtils.getSpaceTime(Long.parseLong(bean.getCc_datetime())));
            }

        } else if (holder instanceof HomeNewNoHolder) {
            setCheckBox(((HomeNewNoHolder) holder).rb, position);
            HomeNewNoHolder homeNewNoHolder = (HomeNewNoHolder) holder;
            homeNewNoHolder.textViews.get(0).setText(bean.getCc_title());
            if (bean.getMedia().size() > 0) {
                Glide.with(context).load(bean.getMedia().get(0)).into(homeNewNoHolder.home_new_no_item_image);
            } else {
                Glide.with(context).load(R.drawable.home_banner).into(homeNewNoHolder.home_new_no_item_image);
            }
            homeNewNoHolder.textViews.get(1).setText(bean.getCc_from());
            homeNewNoHolder.textViews.get(2).setText(bean.getPlNum() + "评论");
            homeNewNoHolder.playNumTv.setText(bean.cc_count + "次播放");
            if (!TextUtils.isEmpty(bean.getCc_datetime())) {
                homeNewNoHolder.textViews.get(3).setText(CommonUtils.getSpaceTime(Long.parseLong(bean.getCc_datetime())));
            }

            String beanType = bean.getCc_mark();
            if (beanType.contains("V")) {
                /*homeNewNoHolder.home_new_no_item_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, MediaActivity.class);
                        intent.putExtra("url",bean.media.get(1));
                        context.startActivity(intent);
                    }
                });*/
                //首页视频借用此adapter，因此需要将隐藏的半透明和视频时长显示出来
                //homeNewNoHolder.video_model.setVisibility(View.VISIBLE);
                homeNewNoHolder.video_time.setVisibility(View.VISIBLE);
                homeNewNoHolder.video_time.setText(bean.video_time);

            }
            if (position + 1 == getItemCount()) {
                homeNewNoHolder.line.setVisibility(View.GONE);
            } else {
                homeNewNoHolder.line.setVisibility(View.VISIBLE);
            }

        } else if (holder instanceof HomeNewVideoHolder) {
            //setCheckBox(((HomeNewVideoHolder) holder).rb, position);
            Log.i("yang", "HomeNewVideoHolder----");
            HomeNewVideoHolder homeNewVideoHolder = (HomeNewVideoHolder) holder;
           /* homeNewVideoHolder.textViews.get(0).setText(bean.getCc_title());
            homeNewVideoHolder.textViews.get(1).setText(bean.getCc_from());
            homeNewVideoHolder.textViews.get(2).setText(bean.getPlNum() + "评论");*/
            /*if (!TextUtils.isEmpty(bean.getCc_datetime())) {
                homeNewVideoHolder.textViews.get(3).setText(CommonUtils.getSpaceTime(Long.parseLong(bean.getCc_datetime())));
            }*/
            homeNewVideoHolder.titleTv.setText(bean.getCc_title());
            homeNewVideoHolder.playNumTv.setText(bean.getPlNum() + "次播放");
            if (mDatas.get(position).getMedia().size() > 0) {
                Glide.with(context).load(mDatas.get(position).getMedia().get(0)).into(homeNewVideoHolder.home_new_video_item_video);
            }
            homeNewVideoHolder.home_new_video_item_video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    newsItemInterface.setVideoPlay(bean.getMedia().get(1));
                }
            });


        }
    }

    @Override
    public int getItemCount() {
        if (mDatas != null && mDatas.size() > 0) {
            return mDatas.size();
        } else {
            return 0;
        }
    }

    class HomeNewHolder extends RecyclerView.ViewHolder {
        @BindViews({R.id.home_new_item_title, R.id.home_new_item_from, R.id.home_new_item_comment, R.id.home_new_item_time})
        List<TextView> textViews;
        @BindView(R.id.home_new_item_rc)
        NoTouchRecycler home_new_item_rc;
        @BindView(R.id.rb_delete)
        CheckBox rb;

        public HomeNewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class HomeNewNoHolder extends RecyclerView.ViewHolder {
        @BindViews({R.id.home_new_no_item_title, R.id.home_new_no_item_from, R.id.home_new_no_item_comment, R.id.home_new_no_item_time})
        List<TextView> textViews;
        @BindView(R.id.home_new_no_item_image)
        ImageView home_new_no_item_image;
        @BindView(R.id.rb_delete)
        CheckBox rb;
        @BindView(R.id.item_line)
        View line;
        @BindView(R.id.video_model)
        View video_model;
        @BindView(R.id.video_time)
        TextView video_time;
        @BindView(R.id.tv_play_num)
        TextView playNumTv;

        public HomeNewNoHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class HomeNewVideoHolder extends RecyclerView.ViewHolder {
        /*@BindViews({R.id.home_new_video_item_title, R.id.home_new_video_item_from, R.id.home_new_video_item_comment, R.id.home_new_video_item_time})
        List<TextView> textViews;
        @BindView(R.id.home_new_video_item_video)
        ImageView home_new_video_item_video;
        @BindView(R.id.rb_delete)
        CheckBox rb;*/
        @BindView(R.id.iv_hotvideo_img)
        ImageView home_new_video_item_video;
        @BindView(R.id.tv_hotvideo_title)
        TextView titleTv;
        @BindView(R.id.tv_hotvideo_play)
        TextView playNumTv;

        public HomeNewVideoHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private NewsItemInterface newsItemInterface;

    public interface NewsItemInterface {
        void setNewOnItem(int position);

        void setVideoPlay(String url);
    }

    public void setNewItemInterface(NewsItemInterface newsItemInterface) {
        this.newsItemInterface = newsItemInterface;

    }
}
