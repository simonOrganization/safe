package com.lchtime.safetyexpress.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lchtime.safetyexpress.H5DetailUI;
import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.VideoH5Activity;
import com.lchtime.safetyexpress.bean.NewsBean;
import com.lchtime.safetyexpress.ui.vip.fragment.VedioFragment;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.views.MyGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android-cp on 2017/4/21.
 */

public class MyConllectedVideoAdapter extends BaseAdapter {

    private Context context;
    private List<String> photos;
  //  private List<HomeNewsRecommendBean> list;
  //  private HomeNewsRecommendBean newsRecommendBean;
    private VedioFragment currentFragment;
    private List<NewsBean> newsBeenList;

    public List<NewsBean> list = new ArrayList<>();

    private boolean flag = false;

    public MyConllectedVideoAdapter(Context context, VedioFragment currentFragment,List<NewsBean> newsBeenList) {
        this.context = context;
        this.currentFragment = currentFragment;
        this.newsBeenList = newsBeenList;
        //测试

    }

    @Override
    public int getCount() {
        return newsBeenList.size();
    }

    @Override
    public Object getItem(int position) {
        return newsBeenList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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
        setCheckBox(holder.rb,position);


        final NewsBean bean = newsBeenList.get(position);
        holder.tv_from.setText(bean.getCc_from());
        holder.tv_comment.setText(bean.getCc_count());
        //时间
        holder.tv_time.setText(CommonUtils.getSpaceTime(Long.parseLong(bean.getCc_datetime())));
        holder.tv_title.setText(bean.getCc_title());
        if (bean.getMedia() != null) {
            Glide.with(MyApplication.getContext()).load(bean.getMedia().get(0)).into(holder.iv_oneimg_img);
        }else {

        }

        /*holder.iv_oneimg_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MediaActivity.class);
                intent.putExtra("url",bean.media.get(1));
                context.startActivity(intent);
            }
        });*/
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag){
                    return;
                }
                Intent intent = new Intent(context, VideoH5Activity.class);
                //intent.putExtra("type","news");
                intent.putExtra("newsId",bean.cc_id);
                intent.putExtra("type","video");
                intent.putExtra("videoUrl", bean.media.get(1));
                context.startActivity(intent);
            }
        });




        return convertView;
    }

    private void setCheckBox(CheckBox rb,int position) {
        if (flag == true){

            rb.setVisibility(View.VISIBLE);
            final NewsBean newsBean = newsBeenList.get(position);
            rb.setChecked(newsBean.isCheck);
            rb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //如果之前不是选中的状态
                    if (!newsBean.isCheck) {
                        list.add(newsBean);
                        ((VedioFragment)currentFragment).updataDeleteNum(1);

                    } else {
                        list.remove(newsBean);
                        ((VedioFragment)currentFragment).updataDeleteNum(-1);
                    }

                    newsBean.isCheck = !newsBean.isCheck;
                }

            });
        }else {
            //隐藏
            rb.setVisibility(View.GONE);
        }
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

    public List<NewsBean> getUpdataList(){
        return list;
    }

    public void setDelete(boolean isDelete){
        this.flag = isDelete;
    }
}
