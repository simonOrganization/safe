package com.lchtime.safetyexpress.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.MydyBean;
import com.lchtime.safetyexpress.bean.res.CircleBean;
import com.lchtime.safetyexpress.ui.circle.protocal.CircleProtocal;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lchtime.safetyexpress.views.CircleImageView;
import com.lidroid.xutils.view.annotation.ContentView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yxn on 2017/4/21.
 */

public class CircleSubscribAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<MydyBean.DyBean> dy;
    private CircleProtocal protocal = new CircleProtocal();

    public CircleSubscribAdapter(Context context,List<MydyBean.DyBean> dy) {
        this.context = context;
        this.dy = dy;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_subscrib_item,parent,false);
        return new CircleSubscribeHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final CircleSubscribeHolder myViewHolder = (CircleSubscribeHolder) holder;
        final MydyBean.DyBean bean = dy.get(position);
        if (!TextUtils.isEmpty(bean.ud_photo_fileid)) {
            Picasso.with(context).load(bean.ud_photo_fileid).fit().into(myViewHolder.raiv_hotcircle_icon);
        }
        myViewHolder.tv_hotcircle_name.setText(bean.ud_nickname);
        myViewHolder.cb_hotcircle_subscribe.setChecked(true);
        myViewHolder.cb_hotcircle_subscribe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
                    CommonUtils.toastMessage("您当前无网络，请联网再试");
                    myViewHolder.cb_hotcircle_subscribe.setChecked(!isChecked);
                    return;
                }
                protocal.changeSubscribe(SpTools.getString(context, Constants.userId,""), bean.ud_ub_id, isChecked ? "1" : "0", new CircleProtocal.CircleListener() {
                    @Override
                    public void circleResponse(CircleBean response) {
                        CommonUtils.toastMessage(response.result.info);
                    }
                });

            }
        });

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
