package com.lchtime.safetyexpress.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.AcountDetailBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by android-cp on 2017/5/18.
 */

public class AccountDetailAdapter extends RecyclerView.Adapter {
    private List<AcountDetailBean.ListBean> detailList;
    private Context context;
    public AccountDetailAdapter(Context context,List<AcountDetailBean.ListBean> detailList){
        this.context = context;
        this.detailList = detailList;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(MyApplication.getContext(), R.layout.item_account_detail, null);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myHolder = (MyViewHolder) holder;
        AcountDetailBean.ListBean bean = detailList.get(position);
        //提现还是领红包
        myHolder.tvGetType.setText(bean.uc_at);
        myHolder.tvTime.setText(bean.time);
        if (bean.uc_amount.startsWith("-")){
            myHolder.tvSingleMoney.setTextColor(context.getResources().getColor(R.color.title_999));
        }else {
            myHolder.tvSingleMoney.setTextColor(context.getResources().getColor(R.color.commen_reg));
        }
        myHolder.tvSingleMoney.setText(bean.uc_amount);
        myHolder.tvTotalMoney.setText("余额：" + bean.balance);
        myHolder.statusTv.setText(bean.uc_status);
    }

    @Override
    public int getItemCount() {
        return detailList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_get_type)
        TextView tvGetType;
        @BindView(R.id.tv_single_money)
        TextView tvSingleMoney;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_total_money)
        TextView tvTotalMoney;
        @BindView(R.id.tv_status)
        TextView statusTv;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
