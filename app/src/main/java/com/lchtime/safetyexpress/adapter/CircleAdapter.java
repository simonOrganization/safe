package com.lchtime.safetyexpress.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.BasicResult;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.QzContextBean;
import com.lchtime.safetyexpress.bean.res.CircleBean;
import com.lchtime.safetyexpress.ui.circle.CircleUI;
import com.lchtime.safetyexpress.ui.circle.SingleInfoUI;
import com.lchtime.safetyexpress.ui.circle.protocal.CircleProtocal;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.ScreenUtil;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lchtime.safetyexpress.views.GridSpacingItemDecoration;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yxn on 2017/4/20.
 */

public class CircleAdapter extends RecyclerView.Adapter {
    private Activity context;
    private List<QzContextBean> circleOneList;

    public CircleAdapter(Activity context, List<QzContextBean> circleOneList) {
        this.context = context;
        this.circleOneList = circleOneList;
    }

    private boolean isShowDy = true;

    public void setShowDy(boolean isShowDy){
        this.isShowDy = isShowDy;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.circle_item,parent,false);
        if(!isShowDy){
            view.findViewById(R.id.circle_item_subscribe).setVisibility(View.GONE);
            view.findViewById(R.id.circle_item_subscribe_num).setVisibility(View.GONE);
        }
        return new CircleHodler(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        int screenWith = ScreenUtil.getScreenSize(context)[0];
        if(holder instanceof CircleHodler){
            CircleHodler circleHodler = (CircleHodler) holder;
            final QzContextBean bean = circleOneList.get(position);

            //如果有图片
            if (TextUtils.isEmpty(bean.qc_video)) {
                ((CircleHodler) holder).circle_item_shipin.setVisibility(View.GONE);
                ((CircleHodler) holder).circle_item_image_rc.setVisibility(View.VISIBLE);
                //一片张图
                if (bean.pic.size() == 1) {
                    ViewGroup.LayoutParams layoutParamsss = circleHodler.circle_item_image_rc.getLayoutParams();
                    layoutParamsss.width = screenWith / 3;
                    circleHodler.circle_item_image_rc.setLayoutManager(new GridLayoutManager(context, 1));
//                circleHodler.circle_item_image_rc.addItemDecoration(new GridSpacingItemDecoration(2,10,true));
                } else if (bean.pic.size() == 4) {
                    //四张图片
                    ViewGroup.LayoutParams layoutParamsss = circleHodler.circle_item_image_rc.getLayoutParams();
                    layoutParamsss.width = screenWith / 2;
                    circleHodler.circle_item_image_rc.setLayoutManager(new GridLayoutManager(context, 2));
                    //circleHodler.circle_item_image_rc.addItemDecoration(new GridSpacingItemDecoration(2, 5, true));
                } else {
                    //多张图片
                    ViewGroup.LayoutParams layoutParamsss = circleHodler.circle_item_image_rc.getLayoutParams();
                    layoutParamsss.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    circleHodler.circle_item_image_rc.setLayoutManager(new GridLayoutManager(context, 3));
                    //circleHodler.circle_item_image_rc.addItemDecoration(new GridSpacingItemDecoration(3, 5, true));
                }
                //circleHodler.circle_item_image_rc.addItemDecoration(new GridSpacingItemDecoration(3, 5, true));
                CircleImageAdapter imageAdapter = new CircleImageAdapter(context, bean.pic);
                circleHodler.circle_item_image_rc.setAdapter(imageAdapter);
                //如果没有图片
            }else {

                //视频
                if (bean.pic.size() > 0) {
                    ((CircleHodler) holder).circle_item_shipin.setVisibility(View.VISIBLE);
                    ((CircleHodler) holder).circle_item_image_rc.setVisibility(View.GONE);
                    Picasso.with(context).load(bean.pic.get(0)).into(((CircleHodler) holder).circle_item_shipin);
                }else {
                    ((CircleHodler) holder).circle_item_shipin.setVisibility(View.GONE);
                    ((CircleHodler) holder).circle_item_image_rc.setVisibility(View.GONE);
                }

                circleHodler.circle_item_shipin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }
            final CircleProtocal protocal = new CircleProtocal();
            if (!TextUtils.isEmpty(bean.ud_photo_fileid)) {
                Picasso.with(context).load(bean.ud_photo_fileid).into(((CircleHodler) holder).iv_circle_photo);
            }
            ((CircleHodler) holder).iv_circle_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SingleInfoUI.class);
                    intent.putExtra("uid",bean.qc_ub_id);
                    context.startActivity(intent);
                }
            });
            ((CircleHodler) holder).circle_item_company_name.setText(bean.user);
            ((CircleHodler) holder).circle_item_title.setText(bean.qc_auth);
            ((CircleHodler) holder).circle_item_content.setText(bean.qc_context);
            ((CircleHodler) holder).circle_item_talk.setText(bean.qc_pinglun);
            ((CircleHodler) holder).circle_item_subscribe.setChecked("1".equals(bean.is_dy));
            ((CircleHodler) holder).circle_item_subscribe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    String userid = SpTools.getString(context, Constants.userId,"");
                    if (TextUtils.isEmpty(userid)){
                        CommonUtils.toastMessage("没有登陆！！");
                        ((CircleHodler) holder).circle_item_subscribe.setChecked(!isChecked);
                        return;
                    }else {
                        protocal.changeSubscribe(userid, bean.qc_ub_id, isChecked ? "1" : "0", new CircleProtocal.CircleListener() {
                            @Override
                            public void circleResponse(CircleBean response) {
                                // CommonUtils.toastMessage(response.result.info);
                                if (context instanceof CircleUI) {
                                    ((CircleUI) context).refreshData();
                                }
                            }
                        });
                    }
                }
            });
            //点赞
            setGreate((CircleHodler) holder, bean, protocal);

            //比down
            setDown((CircleHodler) holder, bean, protocal);

            ((CircleHodler) holder).circle_item_time.setText(CommonUtils.getSpaceTime(Long.parseLong(bean.qc_date)));
            ((CircleHodler) holder).circle_item_subscribe_num.setText(bean.dyNum + "已订阅");
//            circleHodler.circle_item_image_rc.addItemDecoration(new SpacesItemDecoration(10));
        }


    }

    private void setGreate(final CircleHodler holder, final QzContextBean bean, final CircleProtocal protocal) {
        holder.circle_item_great.setText(bean.qc_zc);
        holder.iv_circle_item_great.setChecked("1".equals(bean.zan));
        //点赞逻辑监听
        holder.iv_circle_item_great.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userid = SpTools.getString(context, Constants.userId,"");
                if (TextUtils.isEmpty(userid)){
                    CommonUtils.toastMessage("没有登陆！！");
                    holder.iv_circle_item_great.setChecked("1".equals(bean.zan));
                    return;
                }else {
                    //请求网络数据
                    protocal.updataZanOrCai(userid, bean.qc_id, "1", "0", new CircleProtocal.NormalListener() {
                        @Override
                        public void normalResponse(Object response) {
                            BasicResult result = (BasicResult) response;
                            if (!result.code.equals("10")) {
                                CommonUtils.toastMessage(result.getInfo());
                                holder.iv_circle_item_great.setChecked("1".equals(bean.zan));
                            }else {
                                if (context instanceof CircleUI) {
                                    ((CircleUI) context).refreshData();
                                }
                            }
                        }
                    });
                    holder.iv_circle_item_great.setChecked(true);
                }

            }
        });
    }

    private void setDown(final CircleHodler holder, final QzContextBean bean, final CircleProtocal protocal) {
        holder.circle_item_low.setText(bean.qc_zc);
        holder.iv_circle_item_low.setChecked("1".equals(bean.cai));
        //点赞逻辑监听
        holder.iv_circle_item_low.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userid = SpTools.getString(context, Constants.userId,"");
                if (TextUtils.isEmpty(userid)){
                    CommonUtils.toastMessage("没有登陆！！");
                    holder.iv_circle_item_low.setChecked("1".equals(bean.cai));
                    return;
                }else {

                    //请求网络数据
                    protocal.updataZanOrCai(userid, bean.qc_id, "0", "1", new CircleProtocal.NormalListener() {
                        @Override
                        public void normalResponse(Object response) {
                            BasicResult result = (BasicResult) response;
                            if (!result.code.equals("10")) {
                                CommonUtils.toastMessage(result.getInfo());
                                holder.iv_circle_item_low.setChecked("1".equals(bean.zan));
                            }else {
                                if (context instanceof CircleUI) {
                                    ((CircleUI) context).refreshData();
                                }
                            }
                        }
                    });

                    holder.iv_circle_item_low.setChecked(true);
                }

            }
        });
    }


    @Override
    public int getItemCount() {
        return circleOneList.size();
    }




    class CircleHodler extends RecyclerView.ViewHolder{
        @BindView(R.id.circle_item_image_rc)
        RecyclerView circle_item_image_rc;
        @BindView(R.id.circle_item_shipin)
        ImageView circle_item_shipin;
        @BindView(R.id.iv_circle_photo)
        ImageView iv_circle_photo;
        @BindView(R.id.circle_item_company_name)
        TextView circle_item_company_name;
        @BindView(R.id.circle_item_title)
        TextView circle_item_title;
        @BindView(R.id.circle_item_content)
        TextView circle_item_content;
        @BindView(R.id.circle_item_talk)
        TextView circle_item_talk;
        @BindView(R.id.circle_item_great)
        TextView circle_item_great;
        //点赞图片
        @BindView(R.id.iv_circle_item_great)
        CheckBox iv_circle_item_great;
        @BindView(R.id.circle_item_low)
        TextView circle_item_low;
        //比down图片
        @BindView(R.id.iv_circle_item_low)
        CheckBox iv_circle_item_low;
        @BindView(R.id.circle_item_time)
        TextView circle_item_time;
        @BindView(R.id.circle_item_subscribe)
        CheckBox circle_item_subscribe;
        @BindView(R.id.circle_item_subscribe_num)
        TextView circle_item_subscribe_num;

        public CircleHodler(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }
}
