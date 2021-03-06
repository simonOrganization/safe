package com.lchtime.safetyexpress.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lchtime.safetyexpress.H5DetailUI;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.WenDaDetailBean;
import com.lchtime.safetyexpress.ui.circle.protocal.CirclePhone;
import com.lchtime.safetyexpress.ui.home.HomeQuewstionDetail;
import com.lchtime.safetyexpress.ui.home.MyQuestion;
import com.lchtime.safetyexpress.views.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yxn on 2017/4/20.
 */

public class QuetionDetailAdapter extends RecyclerView.Adapter {

    private Activity context;
    private List<WenDaDetailBean.HdinfoBean> list;

    public QuetionDetailAdapter(Activity context, List<WenDaDetailBean.HdinfoBean> circleOneList) {
        this.context = context;
        this.list = circleOneList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.question_pinglun_item, parent, false);
        return new CircleHodler(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        final WenDaDetailBean.HdinfoBean bean = list.get(position);
        final ArrayList<String> picList = (ArrayList<String>) bean.pic;
        if (holder instanceof CircleHodler) {
            CircleHodler myHolder = (CircleHodler) holder;

            /*ViewGroup.LayoutParams lp =  myHolder.circleItemShipin.getLayoutParams();
            lp.width = 600;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            myHolder.circleItemShipin.setLayoutParams(lp);
            myHolder.circleItemShipin.setMaxWidth(600);
            //context.getWindowManager().getDefaultDisplay().getWidth() - 100
            myHolder.circleItemShipin.setMaxHeight(1200);*/

            //如果有图片
            if (picList.size() != 0 && !TextUtils.isEmpty(picList.get(0))) {
                myHolder.circle_item_image_rc.setVisibility(View.VISIBLE);
                CircleImageAdapter imageAdapter = new CircleImageAdapter(context, bean.pic);
                myHolder.circle_item_image_rc.setLayoutManager(new GridLayoutManager(context, 3));
                myHolder.circle_item_image_rc.setAdapter(imageAdapter);
                //Picasso.with(context).load(bean.pic.get(0)).into(myHolder.circleItemShipin);
                imageAdapter.setOnItemSelectLs(new CircleImageAdapter.IOnItemSelectListener() {
                    @Override
                    public void onItemClick(View v, int pos) {
                        Intent intent = new Intent(context, CirclePhone.class);
                        intent.putExtra("url",  picList);
                        intent.putExtra("pos", pos);
                        context.startActivity(intent);
                    }
                });
            } else {
                //如果没有图片
                ((CircleHodler) holder).circle_item_image_rc.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(bean.ud_photo_fileid)) {
                Glide.with(context).load(bean.ud_photo_fileid).into(((CircleHodler) holder).ivCirclePhoto);
            }else {
                Glide.with(context).load(R.drawable.circle_user_image).into(((CircleHodler) holder).ivCirclePhoto);
            }
            ((CircleHodler) holder).ivCirclePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MyQuestion.class);
                    //进入他人的他的问答界面
                    intent.putExtra("otherid",bean.a_ub_id);
                    context.startActivity(intent);
                }
            });

            ((CircleHodler) holder).circleItemTitle.setText(bean.ud_nickname);
            ((CircleHodler) holder).circleItemCompanyName.setText(bean.user);
            ((CircleHodler) holder).circleItemContent.setText(bean.a_context);

            myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, H5DetailUI.class);
                    intent.putExtra("type","wenda");
                    intent.putExtra("a_id",bean.a_id);
                    intent.putExtra("aq_id",bean.aq_id);
                    intent.putExtra("num",list.size() + "");
                    context.startActivityForResult(intent , HomeQuewstionDetail.QUEWSTION_DETAIL_REQUEST);
                }
            });
        }


    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    class CircleHodler extends RecyclerView.ViewHolder {

        @BindView(R.id.circle_item_image_rc)
        RecyclerView circle_item_image_rc;
        @BindView(R.id.iv_circle_photo)
        CircleImageView ivCirclePhoto;
        @BindView(R.id.circle_item_title)
        TextView circleItemTitle;
        @BindView(R.id.circle_item_company_name)
        TextView circleItemCompanyName;
        @BindView(R.id.circle_item_content)
        TextView circleItemContent;
        /*@BindView(R.id.circle_item_shipin)
        ImageView circleItemShipin;*/
        public CircleHodler(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
