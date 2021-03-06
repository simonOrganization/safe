package com.lchtime.safetyexpress.adapter;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.MyWenDaBean;
import com.lchtime.safetyexpress.H5DetailUI;
import com.lchtime.safetyexpress.ui.home.HomeQuewstionDetail;
import com.lchtime.safetyexpress.ui.home.fragment.GuanZhuFragment;
import com.lchtime.safetyexpress.ui.home.fragment.TiWenFragment;
import com.lchtime.safetyexpress.ui.home.fragment.WenDaFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by android-cp on 2017/5/23.
 */

public class WenDaAdapter extends RecyclerView.Adapter {
    private List<MyWenDaBean.ItemBean> item;


    private Fragment context;
    private LayoutInflater inflater;
    //private List<WenDaBean.TwBean> list;

    public WenDaAdapter(Fragment context, List<MyWenDaBean.ItemBean> item) {
        this.context = context;
        this.item = item;
        inflater = LayoutInflater.from(context.getContext());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.my_question_item, null);
        return new ViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return item.size();
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyWenDaBean.ItemBean bean = item.get(position);
        ViewHolder myHolder = ( ViewHolder)holder;

        if (context instanceof WenDaFragment) {
            myHolder.tv_wenda_content.setVisibility(View.VISIBLE);
            myHolder.tv_wenda_content.setText(bean.param);
            myHolder.tv_question.setText(bean.num + "评论");
        }else {
            myHolder.tv_question.setText(bean.num + "回答");
        }
        myHolder.tv_title.setText(bean.title);

        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //问答详情
                if (context instanceof WenDaFragment){
                    //如果是问答，就跳转h5
                    Intent intent = new Intent(context.getActivity(),H5DetailUI.class);
                    //回答id
                    intent.putExtra("a_id",bean.oid);
                    //回答的问题id
                    intent.putExtra("aq_id",bean.q_id);
                    intent.putExtra("type","wenda");
                    context.startActivity(intent);
                }else {
                    Intent intent = new Intent(context.getActivity(),HomeQuewstionDetail.class);
                    if (context instanceof GuanZhuFragment){
                        //关注的问题id
                        intent.putExtra("q_id",bean.param);
                    }else if (context instanceof TiWenFragment) {
                        //提问的问题id
                        intent.putExtra("q_id", bean.oid);
                    }
                    context.startActivity(intent);
                }
//                Intent intent = new Intent(context,HomeQuewstionDetail.class);
//                intent.putExtra("q_id",bean.q_id);
//                context.startActivity(intent);
            }
        });
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_home_question_title)
        TextView tv_title;
        @BindView(R.id.tv_home_question_num)
        TextView tv_question;
        @BindView(R.id.tv_wenda_content)
        TextView tv_wenda_content;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
