package com.lchtime.safetyexpress.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.AddSubscribBean;
import com.lchtime.safetyexpress.views.CircleImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yxn on 2017/4/23.
 */

public class AddSubscribeAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<AddSubscribBean.ItemBean> mDatas;

    public AddSubscribeAdapter(Context context,List<AddSubscribBean.ItemBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.add_subscirbe_item,parent,false);
        return new AddSubscribeHodler(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AddSubscribeHodler myHolder = (AddSubscribeHodler) holder;
        AddSubscribBean.ItemBean bean = mDatas.get(position);
        myHolder.add_subscribe_item_name.setText(bean.ud_nickname);
        myHolder.add_subscribe_item_content.setText(bean.user);
        myHolder.add_subscirbe_item_but.setChecked("1".equals(bean.is_dy));
        //是否已阅读
        myHolder.add_subscribe_item_count.setText( bean.dy+"已订阅");
        Picasso.with(context).load(bean.ud_photo_fileid).fit().into(myHolder.add_subscribe_item_image);

    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }
    class AddSubscribeHodler extends RecyclerView.ViewHolder{

        @BindView(R.id.add_subscribe_item_image)
        CircleImageView add_subscribe_item_image;
        @BindView(R.id.add_subscribe_item_name)
        TextView add_subscribe_item_name;
        @BindView(R.id.add_subscribe_item_content)
        TextView add_subscribe_item_content;
        @BindView(R.id.add_subscribe_item_count)
        TextView add_subscribe_item_count;
        @BindView(R.id.add_subscirbe_item_but)
        CheckBox add_subscirbe_item_but;

        public AddSubscribeHodler(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
