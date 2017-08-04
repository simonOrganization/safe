package com.lchtime.safetyexpress.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.ui.chat.hx.bean.ContactBean;
import com.lchtime.safetyexpress.bean.Result;
import com.lchtime.safetyexpress.ui.home.protocal.HomeQuestionProtocal;
import com.lchtime.safetyexpress.utils.CommonUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by android-cp on 2017/5/24.
 */

public class InviteFriendAdapter extends RecyclerView.Adapter {

    private List<ContactBean> friendlist;
    private String q_id;
    public InviteFriendAdapter(List<ContactBean> friendlist,String q_id){
        this.friendlist = friendlist;
        this.q_id = q_id;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(MyApplication.getContext(), R.layout.item_invite_friends, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    private HomeQuestionProtocal protocal = new HomeQuestionProtocal();
    private Gson gson = new Gson();
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder holder1 = (ViewHolder) holder;
        final ContactBean bean = friendlist.get(position);
        holder1.detail.setText(bean.user);
        holder1.invite.setChecked(bean.isCheck);
        holder1.nickName.setText(bean.ud_nickname);
        if (!TextUtils.isEmpty(bean.ud_photo_fileid)) {
            Glide.with(MyApplication.getContext())
                    .load(bean.ud_photo_fileid)
                    .error(R.drawable.circle_user_image)
                    .into(holder1.photo);
        }else {
            Glide.with(MyApplication.getContext())
                    .load(R.drawable.circle_user_image)
                    .error(R.drawable.circle_user_image)
                    .into(holder1.photo);
        }
        holder1.invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!holder1.invite.isChecked()){
                    holder1.invite.setChecked(true);
                    return;
                }


                //邀请好友
                protocal.postInviteFriends(q_id, bean.hx_account, new HomeQuestionProtocal.QuestionListener() {
                    @Override
                    public void questionResponse(Object response) {
                        if (response == null){
                            CommonUtils.toastMessage("请求失败，请稍后再试！");
                            holder1.invite.setChecked(false);
                            return;
                        }

                        Result result = gson.fromJson((String) response, Result.class);
                        if ("10".equals(result.result.code)){
                            bean.isCheck = true;
                            CommonUtils.toastMessage(result.result.info);
                        }else {
                            holder1.invite.setChecked(false);
                            CommonUtils.toastMessage(result.result.info);
                        }
                    }
                });


            }
        });

    }

    @Override
    public int getItemCount() {
        return friendlist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_zhifubao_img)
        ImageView photo;
        @BindView(R.id.tv_nickname)
        TextView nickName;
        @BindView(R.id.tv_detail)
        TextView detail;
        @BindView(R.id.tv_invite)
        CheckBox invite;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
