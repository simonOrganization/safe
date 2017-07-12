package com.lchtime.safetyexpress.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lchtime.safetyexpress.H5DetailUI;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.BasicResult;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.MyCircleActiveBean;
import com.lchtime.safetyexpress.bean.Result;
import com.lchtime.safetyexpress.ui.circle.CircleUI;
import com.lchtime.safetyexpress.ui.circle.protocal.CircleProtocal;
import com.lchtime.safetyexpress.ui.vip.MyCircleActiveActivity;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.ScreenUtil;
import com.lchtime.safetyexpress.utils.SpTools;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yxn on 2017/4/20.
 */

public class MyCircleActiveAdapter extends RecyclerView.Adapter {

    private Activity context;
    private List<MyCircleActiveBean.QuanziBean> circleOneList;

    public MyCircleActiveAdapter(Activity context, List<MyCircleActiveBean.QuanziBean> circleOneList) {
        this.context = context;
        this.circleOneList = circleOneList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mycircle_item, parent, false);
        return new MyCircleActiveHodler(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        int screenWith = ScreenUtil.getScreenSize(context)[0];
        if (holder instanceof MyCircleActiveHodler) {
            MyCircleActiveHodler myHodler = (MyCircleActiveHodler) holder;
            final MyCircleActiveBean.QuanziBean bean = circleOneList.get(position);

            //如果有图片
            if (TextUtils.isEmpty(bean.qc_video)) {
                ((MyCircleActiveHodler) holder).circleItemShipin.setVisibility(View.GONE);
                ((MyCircleActiveHodler) holder).circleItemImageRc.setVisibility(View.VISIBLE);
                //一片张图
                if (bean.pic.size() == 1) {
                    ViewGroup.LayoutParams layoutParamsss = myHodler.circleItemImageRc.getLayoutParams();
                    layoutParamsss.width = screenWith / 3;
                    myHodler.circleItemImageRc.setLayoutManager(new GridLayoutManager(context, 1));
//                circleHodler.circle_item_image_rc.addItemDecoration(new GridSpacingItemDecoration(2,10,true));
                } else if (bean.pic.size() == 4) {
                    //四张图片
                    ViewGroup.LayoutParams layoutParamsss = myHodler.circleItemImageRc.getLayoutParams();
                    layoutParamsss.width = screenWith / 2;
                    myHodler.circleItemImageRc.setLayoutManager(new GridLayoutManager(context, 2));
                    //circleHodler.circle_item_image_rc.addItemDecoration(new GridSpacingItemDecoration(2, 5, true));
                } else {
                    //多张图片
                    ViewGroup.LayoutParams layoutParamsss = myHodler.circleItemImageRc.getLayoutParams();
                    layoutParamsss.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    myHodler.circleItemImageRc.setLayoutManager(new GridLayoutManager(context, 3));
                    //circleHodler.circle_item_image_rc.addItemDecoration(new GridSpacingItemDecoration(3, 5, true));
                }
                //circleHodler.circle_item_image_rc.addItemDecoration(new GridSpacingItemDecoration(3, 5, true));
                CircleImageAdapter imageAdapter = new CircleImageAdapter(context, bean.pic);
                myHodler.circleItemImageRc.setAdapter(imageAdapter);
                //如果没有图片
            } else {

                //视频
                if (bean.pic.size() > 0) {
                    ((MyCircleActiveHodler) holder).circleItemShipin.setVisibility(View.VISIBLE);
                    ((MyCircleActiveHodler) holder).circleItemImageRc.setVisibility(View.GONE);
                    Picasso.with(context).load(bean.pic.get(0)).fit().into(((MyCircleActiveHodler) holder).ivRecommendImg);
                } else {
                    ((MyCircleActiveHodler) holder).circleItemShipin.setVisibility(View.GONE);
                    ((MyCircleActiveHodler) holder).circleItemImageRc.setVisibility(View.GONE);
                }

                //点击视频播放事件
                myHodler.circleItemShipin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }
            final CircleProtocal protocal = new CircleProtocal();

            ((MyCircleActiveHodler) holder).circleItemContent.setText(bean.qc_context);
            ((MyCircleActiveHodler) holder).circleItemTalk.setText(bean.qc_pinglun + "评论");

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,H5DetailUI.class);
                    intent.putExtra("newsId",bean.qc_id);
                    intent.putExtra("type","circle");
                    context.startActivity(intent);
                }
            });
            //点赞
            setGreate((MyCircleActiveHodler) holder, bean, protocal);

            //比down
            setDown((MyCircleActiveHodler) holder, bean, protocal);

            ((MyCircleActiveHodler) holder).circleItemTime.setText(CommonUtils.getSpaceTime(Long.parseLong(bean.qc_date)));
//            circleHodler.circle_item_image_rc.addItemDecoration(new SpacesItemDecoration(10));
            ((MyCircleActiveHodler) holder).circleItemDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //删除
                    deleteCircle(((MyCircleActiveHodler) holder),position,protocal);
                }
            });


        }


    }

    private void deleteCircle(MyCircleActiveHodler holder, final int position, CircleProtocal protocal) {
        ((MyCircleActiveActivity)context).setIsLoading(true);
        MyCircleActiveBean.QuanziBean bean = circleOneList.get(position);
        String userid = SpTools.getString(context, Constants.userId, "");
        if (TextUtils.isEmpty(userid)) {
            CommonUtils.toastMessage("没有登陆！！");
            holder.ivCircleItemGreat.setChecked("1".equals(bean.zan));
            return;
        }
        protocal.getDeleteCircle(userid, bean.qc_id, new CircleProtocal.NormalListener() {
            @Override
            public void normalResponse(Object response) {
                if (response == null){
                    CommonUtils.toastMessage("删除圈子失败，请稍后再试");
                    ((MyCircleActiveActivity)context).setIsLoading(false);
                    return;
                }
                Result bean = (Result) response;
                CommonUtils.toastMessage(bean.result.info);
                circleOneList.remove(position);
                notifyDataSetChanged();
                ((MyCircleActiveActivity)context).setIsLoading(false);
            }
        });
    }

    private void setGreate(final MyCircleActiveHodler holder, final MyCircleActiveBean.QuanziBean bean, final CircleProtocal protocal) {
        holder.circleItemGreat.setText(bean.qc_zc);
        holder.ivCircleItemGreat.setChecked("1".equals(bean.zan));
        //点赞逻辑监听
        holder.ivCircleItemGreat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userid = SpTools.getString(context, Constants.userId, "");
                if (TextUtils.isEmpty(userid)) {
                    CommonUtils.toastMessage("没有登陆！！");
                    holder.ivCircleItemGreat.setChecked("1".equals(bean.zan));
                    return;
                } else {
                    //请求网络数据
                    protocal.updataZanOrCai(userid, bean.qc_id, "1", "0", new CircleProtocal.NormalListener() {
                        @Override
                        public void normalResponse(Object response) {
                            BasicResult result = (BasicResult) response;
                            if (!result.code.equals("10")) {
                                CommonUtils.toastMessage(result.getInfo());
                                holder.ivCircleItemGreat.setChecked("1".equals(bean.zan));
                            } else {
                                if (context instanceof CircleUI) {
                                    ((CircleUI) context).refreshItemData(bean.qc_id);
                                }
                            }
                        }
                    });
                    holder.ivCircleItemGreat.setChecked(true);
                }

            }
        });
    }

    private void setDown(final MyCircleActiveHodler holder, final MyCircleActiveBean.QuanziBean bean, final CircleProtocal protocal) {
        holder.circleItemLow.setText(bean.qc_zc);
        holder.ivCircleItemLow.setChecked("1".equals(bean.cai));
        //点赞逻辑监听
        holder.ivCircleItemLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userid = SpTools.getString(context, Constants.userId, "");
                if (TextUtils.isEmpty(userid)) {
                    CommonUtils.toastMessage("没有登陆！！");
                    holder.ivCircleItemLow.setChecked("1".equals(bean.cai));
                    return;
                } else {

                    //请求网络数据
                    protocal.updataZanOrCai(userid, bean.qc_id, "0", "1", new CircleProtocal.NormalListener() {
                        @Override
                        public void normalResponse(Object response) {
                            BasicResult result = (BasicResult) response;
                            if (!result.code.equals("10")) {
                                CommonUtils.toastMessage(result.getInfo());
                                holder.ivCircleItemLow.setChecked("1".equals(bean.zan));
                            } else {
                                if (context instanceof CircleUI) {
                                    ((CircleUI) context).refreshItemData(bean.qc_id);
                                }
                            }
                        }
                    });

                    holder.ivCircleItemLow.setChecked(true);
                }

            }
        });
    }


    @Override
    public int getItemCount() {
        return circleOneList.size();
    }


    class MyCircleActiveHodler extends RecyclerView.ViewHolder {

        @BindView(R.id.circle_item_content)
        TextView circleItemContent;
        @BindView(R.id.iv_recommend_img)
        ImageView ivRecommendImg;
        @BindView(R.id.iv_recommend_img_bg)
        ImageView ivRecommendImgBg;
        @BindView(R.id.iv_recommend_play)
        ImageView ivRecommendPlay;
        @BindView(R.id.tv_recommend_time1)
        TextView tvRecommendTime1;
        @BindView(R.id.circle_item_shipin)
        RelativeLayout circleItemShipin;
        @BindView(R.id.circle_item_image_rc)
        RecyclerView circleItemImageRc;
        @BindView(R.id.circle_item_talk)
        TextView circleItemTalk;
        @BindView(R.id.iv_circle_item_great)
        CheckBox ivCircleItemGreat;
        @BindView(R.id.circle_item_great)
        TextView circleItemGreat;
        @BindView(R.id.iv_circle_item_low)
        CheckBox ivCircleItemLow;
        @BindView(R.id.circle_item_low)
        TextView circleItemLow;
        @BindView(R.id.circle_item_time)
        TextView circleItemTime;
        @BindView(R.id.circle_item_delete)
        TextView circleItemDelete;
        @BindView(R.id.lltime)
        LinearLayout lltime;
        public MyCircleActiveHodler(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
