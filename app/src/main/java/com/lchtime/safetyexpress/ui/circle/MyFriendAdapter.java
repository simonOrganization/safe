package com.lchtime.safetyexpress.ui.circle;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.hyphenate.easeui.bean.ContactBean;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.ui.chat.hx.activity.UserProfileActivity;
import com.lchtime.safetyexpress.ui.chat.hx.adapter.AddFriendsCommendAdapter;
import com.lchtime.safetyexpress.ui.chat.hx.bean.UserBean;

import java.util.List;

import static android.R.attr.type;
import static com.lchtime.safetyexpress.ui.chat.hx.adapter.AddFriendsCommendAdapter.FIND_FRIENDS;

/**
 * Created by ${Hongcha36} on 2017/11/28.
 */

public class MyFriendAdapter extends RecyclerView.Adapter<AddFriendsCommendAdapter.MyHolder> {

    private Context mContext;
    private List<ContactBean> mFriends;


    public MyFriendAdapter(Context mContext, List<ContactBean> mFriends) {
        this.mContext = mContext;
        this.mFriends = mFriends;
    }

    @Override
    public AddFriendsCommendAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.em_row_add_friend_item , null);
        AddFriendsCommendAdapter.MyHolder holder = new AddFriendsCommendAdapter.MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(AddFriendsCommendAdapter.MyHolder holder, int position) {
        final ContactBean bean = mFriends.get(position);
        Glide.with(mContext)
                .load(bean.ud_photo_fileid)
                .placeholder(R.drawable.qun_list)
                .error(R.drawable.qun_list)
                .into(holder.mAvatar);

        holder.mName.setText(bean.ud_nickname);
        holder.mMessage.setText(bean.user);
        //隐藏添加好友
        holder.mIndicator.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserProfileActivity.class);
                intent.putExtra("username", bean.hx_account);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFriends == null ? 0 : mFriends.size();
    }



}
