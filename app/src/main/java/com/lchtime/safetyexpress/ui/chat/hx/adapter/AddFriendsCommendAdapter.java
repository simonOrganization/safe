package com.lchtime.safetyexpress.ui.chat.hx.adapter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.bean.ContactBean;
import com.hyphenate.easeui.bean.EaseInitBean;
import com.hyphenate.easeui.widget.EaseAlertDialog;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.ui.chat.hx.Constant;
import com.lchtime.safetyexpress.ui.chat.hx.DemoHelper;
import com.lchtime.safetyexpress.ui.chat.hx.activity.ApplyMessage;
import com.lchtime.safetyexpress.ui.chat.hx.activity.GroupDetailsActivity;
import com.lchtime.safetyexpress.ui.chat.hx.activity.UserProfileActivity;
import com.lchtime.safetyexpress.ui.chat.hx.bean.AddBean;
import com.lchtime.safetyexpress.ui.chat.hx.bean.UserBean;
import com.lchtime.safetyexpress.ui.chat.hx.fragment.FindFriendsFragment;
import com.lchtime.safetyexpress.ui.chat.hx.fragment.FindGroupsFragment;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.type;
import static com.lchtime.safetyexpress.utils.UIUtils.getResources;

/**
 * Created by Dreamer on 2017/6/7.
 */

public class AddFriendsCommendAdapter extends RecyclerView.Adapter {

    private ProgressDialog progressDialog;

    public static final int FIND_FRIENDS = 0;
    public static final int FIND_GROUP = 1;
    private Fragment mFragment;
    private List<UserBean> user;
    private int type;
    private static final int REQUEST_CODE_GROUP_DETAIL = 13;
    private ArrayList<ContactBean> list;

    public AddFriendsCommendAdapter(Fragment fragment, List<UserBean> user,int type) {
        this.mFragment = fragment;
        this.user = user;
        this.type = type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mFragment.getActivity(), R.layout.em_row_add_friend_item, null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        final UserBean bean = user.get(position);

            Picasso.with(mFragment.getActivity())
                    .load(bean.ud_photo_fileid)
                    .placeholder(R.drawable.circle_user_image)
                    .error(R.drawable.circle_user_image)
                    .into(myHolder.mAvatar);


        myHolder.mName.setText(bean.ud_nickname);
        myHolder.mMessage.setText(bean.user);
        if (type == FIND_FRIENDS){
            //如果是找朋友
            myHolder.mTvBtAdd.setText("好友");
        }else {
            //找群
            myHolder.mTvBtAdd.setText("加群");
        }

        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //条目点击事件，跳到用户详情
                if (type == FIND_FRIENDS) {
                    //跳到详情
                    toSingleDetails(bean);
                } else {
                    //调到群详情
                    toGroupDetails(bean);
                }

            }
        });
        myHolder.mIndicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //加群
                if (mFragment instanceof FindGroupsFragment) {
                    Intent intent = new Intent(mFragment.getActivity(), ApplyMessage.class);
                    intent.putExtra("groupid", bean.hx_account);
                    intent.putExtra("master",bean.master);
                    intent.putExtra("type", "0");
                    mFragment.getActivity().startActivityForResult(intent, 102);
                }else if (mFragment instanceof FindFriendsFragment){
                    Intent intent = new Intent(mFragment.getActivity(), ApplyMessage.class);
                    intent.putExtra("type", "1");
                    intent.putExtra("master",bean.hx_account);
                    mFragment.getActivity().startActivityForResult(intent, 102);
                }
                /*if(EMClient.getInstance().getCurrentUser().equals(bean.hx_account)){
                    new EaseAlertDialog(mFragment.getActivity(), R.string.not_add_myself).show();
                    return;
                }

                if(DemoHelper.getInstance().getContactList().containsKey(bean.hx_account)){
                    //let the user know the contact already in your contact list
                    if(EMClient.getInstance().contactManager().getBlackListUsernames().contains(bean.hx_account)){
                        new EaseAlertDialog(mFragment.getActivity(), R.string.user_already_in_contactlist).show();
                        return;
                    }
                    new EaseAlertDialog(mFragment.getActivity(), R.string.This_user_is_already_your_friend).show();
                    return;
                }

                //显示请求数据的显示
                progressDialog = new ProgressDialog(mFragment.getActivity());
                String stri = getResources().getString(R.string.Is_sending_a_request);
                progressDialog.setMessage(stri);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                new Thread(new Runnable() {
                    public void run() {

                        try {
                            //demo use a hardcode reason here, you need let user to input if you like
                            String s = getResources().getString(R.string.Add_a_friend);
                            EMClient.getInstance().contactManager().addContact(bean.hx_account, s);
                            mFragment.getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    progressDialog.dismiss();
                                    String s1 = getResources().getString(R.string.send_successful);
                                    Toast.makeText(mFragment.getActivity().getApplicationContext(), s1, Toast.LENGTH_LONG).show();
                                }
                            });
                        } catch (final Exception e) {
                            mFragment.getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    progressDialog.dismiss();
                                    String s2 = getResources().getString(R.string.Request_add_buddy_failure);
                                    Toast.makeText(mFragment.getActivity().getApplicationContext(), s2 + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }).start();

                */
            }
        });






    }


    @Override
    public int getItemCount() {
        return user == null ? 0 : user.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.avatar)
        ImageView mAvatar;
        @BindView(R.id.name)
        TextView mName;
        @BindView(R.id.msg_state)
        ImageView mMsgState;
        @BindView(R.id.message)
        TextView mMessage;
        @BindView(R.id.tv_bt_add)
        TextView mTvBtAdd;
        @BindView(R.id.indicator)
        LinearLayout mIndicator;
        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void toSingleDetails(UserBean bean) {

        Intent intent = new Intent(mFragment.getActivity(), UserProfileActivity.class);
        intent.putExtra("username", bean.hx_account);
        mFragment.getActivity().startActivity(intent);
    }

    protected void toGroupDetails(UserBean bean) {

        mFragment.getActivity().startActivityForResult(
                (new Intent(mFragment.getActivity(), GroupDetailsActivity.class)
                        .putExtra("groupId", bean.hx_account)),
                REQUEST_CODE_GROUP_DETAIL);

    }
}
