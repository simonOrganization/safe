package com.lchtime.safetyexpress.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.InitInfo;
import com.lchtime.safetyexpress.bean.MydyBean;
import com.lchtime.safetyexpress.bean.res.CircleBean;
import com.lchtime.safetyexpress.pop.VipInfoHintPop;
import com.lchtime.safetyexpress.ui.circle.SingleInfoUI;
import com.lchtime.safetyexpress.ui.circle.SubscribActivity;
import com.lchtime.safetyexpress.ui.circle.protocal.CircleProtocal;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lchtime.safetyexpress.views.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yxn on 2017/4/21.
 */

public class CircleSubscribAdapter extends RecyclerView.Adapter {
    private Activity context;
    private List<MydyBean.DyBean> dy;
    private CircleProtocal protocal = new CircleProtocal();
    private VipInfoHintPop vipInfoHintPop;

    public CircleSubscribAdapter(Activity context,List<MydyBean.DyBean> dy) {
        this.context = context;
        this.dy = dy;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_subscrib_item,parent,false);
        return new CircleSubscribeHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final CircleSubscribeHolder myViewHolder = (CircleSubscribeHolder) holder;
        final MydyBean.DyBean bean = dy.get(position);
        if (!TextUtils.isEmpty(bean.ud_photo_fileid)) {
            Glide.with(context).load(bean.ud_photo_fileid).into(myViewHolder.raiv_hotcircle_icon);
        }else {
            Glide.with(context).load(R.drawable.circle_user_image).into(myViewHolder.raiv_hotcircle_icon);
        }
        myViewHolder.tv_hotcircle_name.setText(bean.ud_nickname);
        myViewHolder.cb_hotcircle_subscribe.setChecked(true);

        myViewHolder.raiv_hotcircle_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SingleInfoUI.class);
                intent.putExtra("uid",bean.ud_ub_id);
                //是否订阅  热门圈子肯定都是没有订阅的
                context.startActivity(intent);
            }
        });

        myViewHolder.cb_hotcircle_subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof SubscribActivity){
                    //弹框
                    if (vipInfoHintPop == null) {
                        vipInfoHintPop = new VipInfoHintPop(myViewHolder.cb_hotcircle_subscribe, context, R.layout.pop_confirm_unsubscribe);
                    }
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            vipInfoHintPop.showAtLocation();
                            vipInfoHintPop.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    removeSubscribe(myViewHolder, bean, position);
                                    vipInfoHintPop.dismiss();
                                }
                            });
                            vipInfoHintPop.tv_jump.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    myViewHolder.cb_hotcircle_subscribe.setChecked(true);
                                    vipInfoHintPop.dismiss();
                                }
                            });
                        }
                    });
                }


            }
        });



    }



    private void removeSubscribe(final CircleSubscribeHolder myViewHolder, MydyBean.DyBean bean, final int position) {
        if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
           // CommonUtils.toastMessage("您当前无网络，请联网再试");
            myViewHolder.cb_hotcircle_subscribe.setChecked(true);
            return;
        }
        String userid = SpTools.getUserId(context);
        if (TextUtils.isEmpty(userid)){
            CommonUtils.toastMessage("请登陆！！！");
            myViewHolder.cb_hotcircle_subscribe.setChecked(true);
            return;
        }else {
            protocal.changeSubscribe(userid, bean.ud_ub_id, "0" , new CircleProtocal.CircleListener() {
                @Override
                public void circleResponse(CircleBean response) {

                    if (response == null){
                        //订阅或者取消订阅失败了
                        myViewHolder.cb_hotcircle_subscribe.setChecked(true);
                        CommonUtils.toastMessage("请重试！");
                        return;
                    }
                    if ("10".equals(response.result.code)){
                        //移除操作
                        dy.remove(position);
                        if (dy.size() == 0){
                            if (context instanceof SubscribActivity){
                                ((SubscribActivity) context).setEmptyVisiblity();
                            }
                        }
                        if (context instanceof SubscribActivity){
                            ((SubscribActivity) context).refreshData("1");
                        }
                        //notifyDataSetChanged();
                        InitInfo.circleRefresh = true;
                    }else {
                        //订阅或者取消订阅失败了
                        myViewHolder.cb_hotcircle_subscribe.setChecked(true);
                    }
                    CommonUtils.toastMessage(response.result.getInfo());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dy == null ? 0 : dy.size();
    }
    class CircleSubscribeHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.raiv_hotcircle_icon)
        CircleImageView raiv_hotcircle_icon;
        @BindView(R.id.tv_hotcircle_name)
        TextView tv_hotcircle_name;
        @BindView(R.id.iv_hotcircle_subscribe)
        CheckBox cb_hotcircle_subscribe;
        public CircleSubscribeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
