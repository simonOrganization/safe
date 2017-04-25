package com.lchtime.safetyexpress.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.CircleOneBean;
import com.lchtime.safetyexpress.bean.CircleTwoBean;
import com.lchtime.safetyexpress.utils.ScreenUtil;
import com.lchtime.safetyexpress.views.GridSpacingItemDecoration;
import com.lchtime.safetyexpress.views.SpacesItemDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yxn on 2017/4/20.
 */

public class CircleAdapter extends RecyclerView.Adapter {
    private Activity context;
    private ArrayList<CircleOneBean> circleOneList;

    public CircleAdapter(Activity context, ArrayList<CircleOneBean> circleOneList) {
        this.context = context;
        this.circleOneList = circleOneList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.circle_item,parent,false);
        return new CircleHodler(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        int screenHeight = ScreenUtil.getScreenSize(context)[0];
        if(holder instanceof CircleHodler){
            CircleHodler circleHodler = (CircleHodler) holder;
            CircleOneBean bean = circleOneList.get(position);
            if(bean.getImages().size()==1){
                ViewGroup.LayoutParams layoutParamsss = circleHodler.circle_item_image_rc.getLayoutParams();
                layoutParamsss.width = screenHeight/3;
                circleHodler.circle_item_image_rc.setLayoutManager(new GridLayoutManager(context,1));
//                circleHodler.circle_item_image_rc.addItemDecoration(new GridSpacingItemDecoration(2,10,true));
            }else if(bean.getImages().size()==4){
                ViewGroup.LayoutParams layoutParamsss = circleHodler.circle_item_image_rc.getLayoutParams();
                layoutParamsss.width = screenHeight/2;
                circleHodler.circle_item_image_rc.setLayoutManager(new GridLayoutManager(context,2));
                circleHodler.circle_item_image_rc.addItemDecoration(new GridSpacingItemDecoration(2,5,true));
            }else{
                ViewGroup.LayoutParams layoutParamsss = circleHodler.circle_item_image_rc.getLayoutParams();
                layoutParamsss.width = ViewGroup.LayoutParams.MATCH_PARENT;
                circleHodler.circle_item_image_rc.setLayoutManager(new GridLayoutManager(context,3));
                circleHodler.circle_item_image_rc.addItemDecoration(new GridSpacingItemDecoration(3,5,true));
            }
            circleHodler.circle_item_image_rc.addItemDecoration(new GridSpacingItemDecoration(3,5,true));
            CircleImageAdapter imageAdapter = new CircleImageAdapter(context,bean.getImages());
            circleHodler.circle_item_image_rc.setAdapter(imageAdapter);
            circleHodler.circle_item_shipin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
//            circleHodler.circle_item_image_rc.addItemDecoration(new SpacesItemDecoration(10));
        }


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

        public CircleHodler(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }
}
