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

import com.bumptech.glide.Glide;
import com.lchtime.safetyexpress.CircleH5Activity;
import com.lchtime.safetyexpress.H5DetailUI;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.MyCircleActiveBean;
import com.lchtime.safetyexpress.bean.QzContextBean;
import com.lchtime.safetyexpress.bean.Result;
import com.lchtime.safetyexpress.ui.circle.protocal.CirclePhone;
import com.lchtime.safetyexpress.ui.circle.protocal.CircleProtocal;
import com.lchtime.safetyexpress.ui.news.MediaActivity;
import com.lchtime.safetyexpress.ui.vip.MyCircleActiveActivity;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.ScreenUtil;
import com.lchtime.safetyexpress.utils.SpTools;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.lchtime.safetyexpress.ui.circle.CircleUI.CITY_DETAIL_CODE;

/**
 * Created by yxn on 2017/4/20.
 */

public class MyCircleActiveAdapter extends RecyclerView.Adapter {

    private Activity context;
    private List<QzContextBean> circleOneList;
    private OnDeleteListener listener;

    public MyCircleActiveAdapter(Activity context, List<QzContextBean> circleOneList, OnDeleteListener listener) {
        this.context = context;
        this.circleOneList = circleOneList;
        this.listener = listener;
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
            final QzContextBean bean = circleOneList.get(position);


            //如果是长文章
            if(!TextUtils.isEmpty(bean.qc_cwz)){
                myHodler.mLongContentLl.setVisibility(View.VISIBLE);
                myHodler.mCircleRl.setVisibility(View.GONE);
                if(bean.qc_auth.trim().equals("")){
                    myHodler.mLongAuthTv.setVisibility(View.GONE);
                }else{
                    myHodler.mLongAuthTv.setText("作者：" + bean.qc_auth);
                    myHodler.mLongAuthTv.setVisibility(View.VISIBLE);
                }
                if(bean.qc_context.trim().equals("")){
                    myHodler.mLongContentTv.setVisibility(View.GONE);
                }else{
                    myHodler.mLongContentTv.setVisibility(View.VISIBLE);
                    myHodler.mLongContentTv.setText(bean.qc_context);
                }

                if(bean.qc_title.trim().equals("")){
                    myHodler.mLongTitleTv.setVisibility(View.GONE);
                }else{
                    myHodler.mLongTitleTv.setText(bean.qc_title);
                    myHodler.mLongTitleTv.setVisibility(View.VISIBLE);
                }
                Glide.with(context).load(bean.qc_cwz).into(myHodler.mLongImg);
                myHodler.mLongContentLl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, H5DetailUI.class);
                        intent.putExtra("newsId", bean.qc_id);
                        intent.putExtra("type", "circle");
                        context.startActivity(intent);
                    }
                });
            }else {

                //如果有图片
                if (TextUtils.isEmpty(bean.qc_video) || bean.qc_video.equals("0")) {
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
                    imageAdapter.setOnItemSelectLs(new CircleImageAdapter.IOnItemSelectListener() {
                        @Override
                        public void onItemClick(View v, int pos) {
                            Intent intent = new Intent(context, CirclePhone.class);
                            intent.putExtra("url", bean.pic);
                            intent.putExtra("pos", pos);
                            context.startActivity(intent);
                        }
                    });
                    //如果没有图片
                } else {

                    //视频
                    if (bean.pic.size() > 0) {
                        ((MyCircleActiveHodler) holder).circleItemShipin.setVisibility(View.VISIBLE);
                        ((MyCircleActiveHodler) holder).circleItemImageRc.setVisibility(View.GONE);
                        Glide.with(context).load(bean.pic.get(0)).into(((MyCircleActiveHodler) holder).ivRecommendImg);
                    } else {
                        ((MyCircleActiveHodler) holder).circleItemShipin.setVisibility(View.GONE);
                        ((MyCircleActiveHodler) holder).circleItemImageRc.setVisibility(View.GONE);
                    }

                    //点击视频播放事件
                    myHodler.circleItemShipin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, MediaActivity.class);
                            intent.putExtra("url", bean.qc_video);
                            intent.putExtra("img_url", bean.pic.get(0));
                            context.startActivity(intent);
                        }
                    });
                }
            }
            final CircleProtocal protocal = new CircleProtocal();

            ((MyCircleActiveHodler) holder).circleItemContent.setText(bean.qc_context);
            ((MyCircleActiveHodler) holder).circleItemTalk.setText(bean.qc_pinglun + "评论");

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CircleH5Activity.class);
                    intent.putExtra("data", bean);
                    intent.putExtra("type", "circle");
                    context.startActivityForResult(intent , CITY_DETAIL_CODE);
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
                    deleteCircle(((MyCircleActiveHodler) holder), position, protocal);
                }
            });


        }


    }

    /**
     * 删除圈子
     *
     * @param holder
     * @param position
     * @param protocal
     */
    private void deleteCircle(MyCircleActiveHodler holder, final int position, CircleProtocal protocal) {
        //  ((MyCircleActiveActivity) context).setIsLoading(true);
        QzContextBean bean = circleOneList.get(position);
        String userid = SpTools.getUserId(context);
        if (TextUtils.isEmpty(userid)) {
            CommonUtils.toastMessage("没有登陆！！");
            holder.ivCircleItemGreat.setChecked("1".equals(bean.zan));
            return;
        }
        protocal.getDeleteCircle(userid, bean.qc_id, new CircleProtocal.NormalListener() {
            @Override
            public void normalResponse(Object response) {
                if (response == null) {
                    CommonUtils.toastMessage("删除圈子失败，请稍后再试");
                    ((MyCircleActiveActivity) context).setIsLoading(false);
                    return;
                }
                Result bean = (Result) response;
                CommonUtils.toastMessage(bean.result.info);
                circleOneList.remove(position);
                notifyDataSetChanged();
                ((MyCircleActiveActivity) context).setIsLoading(false);
                if(circleOneList.size() == 0){
                    listener.onDeleteItem();
                }
            }
        });
    }

    private boolean greate;
    private boolean down;
    private String action;

    private void setGreate(final MyCircleActiveHodler holder, final QzContextBean bean, final CircleProtocal protocal) {
        holder.circleItemGreat.setText(bean.qc_zc);
        holder.ivCircleItemGreat.setChecked("1".equals(bean.zan));
        holder.ivCircleItemGreat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                greate = !"1".equals(bean.zan);
                holder.ivCircleItemGreat.setEnabled(false);
                if (greate) {
                    action = "0";
                } else {
                    action = "1";
                }
                String userid = SpTools.getUserId(context);
                if (TextUtils.isEmpty(userid)) {
                    CommonUtils.toastMessage("没有登陆！！");
                    holder.ivCircleItemGreat.setChecked("1".equals(bean.zan));
                    return;
                } else {
                    //请求网络数据
                    protocal.updataZanOrCai(userid, bean.qc_id, "1", "0", action, new CircleProtocal.NormalListener() {
                        @Override
                        public void normalResponse(Object response) {
                            if (response == null) {
                                greate = true;
                              //  holder.ivCircleItemGreat.setEnabled(true);
                              //  holder.ivCircleItemGreat.setChecked("1".equals(bean.zan));
                                // holder.ivCircleItemGreat.setClickable(true);
                                CommonUtils.toastMessage("请求网络失败");
                                return;
                            }
                            Result result = (Result) response;
                            if (!result.result.code.equals("10")) {
                              //  holder.ivCircleItemGreat.setEnabled(true);
                                CommonUtils.toastMessage(result.result.getInfo());
                                //  holder.ivCircleItemGreat.setChecked("1".equals(bean.zan));
                            } else {
                                greate = false;
                                holder.circleItemGreat.setText(result.dzNum);
                              //  holder.ivCircleItemGreat.setEnabled(true);
                              //  holder.ivCircleItemGreat.setChecked("1".equals(bean.zan));
                                if (context instanceof MyCircleActiveActivity) {
                                    ((MyCircleActiveActivity) context).refreshItemData(bean.qc_id);
                                }

                            }
                           // holder.ivCircleItemGreat.setChecked(true);
                            CommonUtils.toastMessage(result.result.getInfo());
                        }
                    });

                }

            }
        });
    }

    private void setDown(final MyCircleActiveHodler holder, final QzContextBean bean, final CircleProtocal protocal) {
        holder.circleItemLow.setText(bean.qc_fd);
        holder.ivCircleItemLow.setChecked("1".equals(bean.cai));

        //点赞逻辑监听
        holder.ivCircleItemLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                down = !"1".equals(bean.cai);
              //  holder.ivCircleItemLow.setEnabled(false);
                if (down) {
                    action = "0";
                } else {
                    action = "1";
                }
                String userid = SpTools.getUserId(context);
                if (TextUtils.isEmpty(userid)) {
                    CommonUtils.toastMessage("没有登陆！！");
                  //  holder.ivCircleItemLow.setChecked("1".equals(bean.cai));
                    return;
                } else {
                    //请求网络数据
                    protocal.updataZanOrCai(userid, bean.qc_id, "0", "1", action, new CircleProtocal.NormalListener() {
                        @Override
                        public void normalResponse(Object response) {
                            if (response == null) {
                                down = true;
                             //   holder.ivCircleItemLow.setEnabled(true);
                              //  holder.ivCircleItemLow.setChecked("1".equals(bean.cai));
                                holder.ivCircleItemLow.setClickable(true);
                                CommonUtils.toastMessage("请求网络失败");
                                return;
                            }
                            Result result = (Result) response;
                            if (!result.result.code.equals("10")) {
                                //holder.ivCircleItemLow.setEnabled(true);
                                CommonUtils.toastMessage(result.result.getInfo());
                              //  holder.ivCircleItemLow.setChecked("1".equals(bean.cai));
                            } else {
                                down = false;
                                holder.circleItemLow.setText(result.dzNum);
                               // holder.ivCircleItemLow.setEnabled(true);
                               // holder.ivCircleItemLow.setChecked("1".equals(bean.cai));
                                if (context instanceof MyCircleActiveActivity) {
                                    ((MyCircleActiveActivity) context).refreshItemData(bean.qc_id);
                                }
                            }
                            //holder.ivCircleItemLow.setChecked(true);
                            CommonUtils.toastMessage(result.result.getInfo());
                        }
                    });


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
//        @BindView(R.id.iv_recommend_img_bg)
//        ImageView ivRecommendImgBg;
        @BindView(R.id.iv_recommend_play)
        ImageView ivRecommendPlay;
//        @BindView(R.id.tv_recommend_time1)
//        TextView tvRecommendTime1;
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
        /*@BindView(R.id.lltime)
        LinearLayout lltime;*/


        @BindView(R.id.rl_content)
        RelativeLayout mCircleRl; //正常圈子的内容

        @BindView(R.id.ll_long_circle)
        LinearLayout mLongContentLl; //长文章的内容
        @BindView(R.id.iv_image)
        ImageView mLongImg;             //长文章的图片
        @BindView(R.id.tv_title)
        TextView mLongTitleTv;
        @BindView(R.id.tv_auther)
        TextView mLongAuthTv;
        @BindView(R.id.tv_content)
        TextView mLongContentTv;


        public MyCircleActiveHodler(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    public interface OnDeleteListener{
        void onDeleteItem();
    }
}
