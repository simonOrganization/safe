package com.lchtime.safetyexpress.adapter;

import android.content.Context;
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
import com.lchtime.safetyexpress.bean.AddSubscribBean;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.InitInfo;
import com.lchtime.safetyexpress.bean.res.CircleBean;
import com.lchtime.safetyexpress.ui.circle.OtherPersonSubscribeActivity;
import com.lchtime.safetyexpress.ui.circle.fragment.SubscirbeAllFragment;
import com.lchtime.safetyexpress.ui.circle.fragment.SubscirbeCommendFragment;
import com.lchtime.safetyexpress.ui.circle.protocal.CircleProtocal;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lchtime.safetyexpress.views.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yxn on 2017/4/23.
 */

public class AddSubscribeAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<AddSubscribBean.ItemBean> mDatas;
    private CircleProtocal protocal = new CircleProtocal();
    private Object fragment;

    public AddSubscribeAdapter(Context context, List<AddSubscribBean.ItemBean> mDatas, Object fragment) {
        this.context = context;
        this.mDatas = mDatas;
        this.fragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.add_subscirbe_item,parent,false);
        return new AddSubscribeHodler(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final AddSubscribeHodler myHolder = (AddSubscribeHodler) holder;
        final AddSubscribBean.ItemBean bean = mDatas.get(position);
        myHolder.add_subscribe_item_name.setText(bean.ud_nickname);
        myHolder.add_subscribe_item_content.setText(bean.user);
        myHolder.add_subscirbe_item_but.setChecked("1".equals(bean.is_dy));
        myHolder.add_subscirbe_item_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CommonUtils.isNetworkAvailable(MyApplication.getContext())) {
                 //   CommonUtils.toastMessage("您当前无网络，请联网再试");
                    myHolder.add_subscirbe_item_but.setChecked("1".equals(bean.is_dy));
                    return;
                }
                String userid = SpTools.getUserId(context);
                if (TextUtils.isEmpty(userid)){
                    CommonUtils.toastMessage("请登陆！！！");
                    myHolder.add_subscirbe_item_but.setChecked("1".equals(bean.is_dy));
                    return;
                }else {
                    protocal.changeSubscribe(userid, bean.ud_ub_id, "1".equals(bean.is_dy)?"0":"1" , new CircleProtocal.CircleListener() {
                        @Override
                        public void circleResponse(CircleBean response) {
                                if (response == null){
                                    CommonUtils.toastMessage("操作失败,请检查网络");
                                    myHolder.add_subscirbe_item_but.setChecked("1".equals(bean.is_dy));
                                    return;
                                }
                                if (fragment instanceof SubscirbeAllFragment) {
                                    ((SubscirbeAllFragment) fragment).refreshData("1");
                                } else if (fragment instanceof SubscirbeCommendFragment) {
                                    ((SubscirbeCommendFragment) fragment).initData("1");
                                } else if (fragment instanceof OtherPersonSubscribeActivity) {
                                    ((OtherPersonSubscribeActivity) fragment).prepareData();
                                }
                            if ("10".equals(response.result.code)) {
                                InitInfo.circleRefresh = true;
                            }

                            CommonUtils.toastMessage(response.result.getInfo());
                        }
                    });
                }
            }
        });
        //是否已阅读
        myHolder.add_subscribe_item_count.setText( bean.dy+"已订阅");
        if (!TextUtils.isEmpty(bean.ud_photo_fileid)) {
            Glide.with(context).load(bean.ud_photo_fileid).into(myHolder.add_subscribe_item_image);
        }

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
