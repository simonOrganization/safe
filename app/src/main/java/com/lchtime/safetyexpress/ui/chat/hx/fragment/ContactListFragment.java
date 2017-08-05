/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lchtime.safetyexpress.ui.chat.hx.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.bean.ContactBean;
import com.hyphenate.easeui.bean.ContactListBean;
import com.hyphenate.easeui.bean.EaseInitBean;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.util.EMLog;
import com.hyphenate.util.NetUtils;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.InitInfo;
import com.lchtime.safetyexpress.ui.chat.hx.DemoHelper;
import com.lchtime.safetyexpress.ui.chat.hx.activity.AddContactActivity;
import com.lchtime.safetyexpress.ui.chat.hx.activity.ChatActivity;
import com.lchtime.safetyexpress.ui.chat.hx.activity.GroupDetailsActivity;
import com.lchtime.safetyexpress.ui.chat.hx.activity.GroupsActivity;
import com.lchtime.safetyexpress.ui.chat.hx.activity.NewFriendsMsgActivity;
import com.lchtime.safetyexpress.ui.chat.hx.activity.UserProfileActivity;
import com.lchtime.safetyexpress.ui.chat.hx.activity.protocal.GetInfoProtocal;
import com.lchtime.safetyexpress.ui.chat.hx.bean.ApplyNum;
import com.lchtime.safetyexpress.ui.chat.hx.db.InviteMessgeDao;
import com.lchtime.safetyexpress.ui.chat.hx.db.UserDao;
import com.lchtime.safetyexpress.ui.chat.hx.fragment.protocal.AddCommandProtocal;
import com.lchtime.safetyexpress.ui.chat.hx.widget.ContactItemView;
import com.lchtime.safetyexpress.ui.home.protocal.HomeQuestionProtocal;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.SpTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * contact list
 * 通讯录界面
 */
public class ContactListFragment extends EaseContactListFragment {
	
    private static final String TAG = ContactListFragment.class.getSimpleName();
    private ContactSyncListener contactSyncListener;
    private BlackListSyncListener blackListSyncListener;
    private ContactInfoSyncListener contactInfoSyncListener;
    private View loadingView;
    private ContactItemView applicationItem;
    private InviteMessgeDao inviteMessgeDao;
    public static final int DEL_FRIEND = 127;
    public static final int REFRESH_DATA = 128;
    private ArrayList<ContactBean> list;
    private String uid;

    @SuppressLint("InflateParams")
    @Override
    protected void initView() {
        super.initView();
        @SuppressLint("InflateParams") View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.em_contacts_header, null);
        HeaderItemClickListener clickListener = new HeaderItemClickListener();
        applicationItem = (ContactItemView) headerView.findViewById(R.id.application_item);
        applicationItem.setOnClickListener(clickListener);
        headerView.findViewById(R.id.group_item).setOnClickListener(clickListener);
        listView.addHeaderView(headerView);
        //add loading view
        loadingView = LayoutInflater.from(getActivity()).inflate(R.layout.em_layout_loading_data, null);
        contentContainer.addView(loadingView);

        registerForContextMenu(listView);
        setRefreshListener();
        hideTitleBar();
    }

    private void setRefreshListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        refresh();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 600);
            }
        });
    }


    private HomeQuestionProtocal protocal = new HomeQuestionProtocal();
    @Override
    public void refresh() {

        //if (EaseInitBean.contactBean == null){
            protocal.getMyFriends(new HomeQuestionProtocal.QuestionListener() {
                @Override
                public void questionResponse(Object response) {

                    if (response == null){
                        CommonUtils.toastMessage("请求好友数据失败，请稍后再试！");
                        return;
                    }

                    try {
                        ContactListBean bean = gson.fromJson((String) response, ContactListBean.class);


                        if ("10".equals(bean.result.code)){

                            if (bean.friendlist == null || bean.friendlist.size() == 0){
                                return;
                            }
                            EaseInitBean.contactBean = bean;

                            Map<String, EaseUser> m = DemoHelper.getInstance().getContactList();
                            if (m instanceof Hashtable<?, ?>) {
                                //noinspection unchecked
                                m = (Map<String, EaseUser>) ((Hashtable<String, EaseUser>)m).clone();
                            }
                            setContactsMap(m);
                            ContactListFragment.super.refresh();
                            getApplyNum();


                        }else {

                            CommonUtils.toastMessage("请求好友数据失败，请稍后再试！");
                        }
                    }catch (Exception exception){

                        CommonUtils.toastMessage("请求好友数据失败，请稍后再试！");
                    }

                }
            });
       // }



        Map<String, EaseUser> m = DemoHelper.getInstance().getContactList();
        if (m instanceof Hashtable<?, ?>) {
            //noinspection unchecked
            m = (Map<String, EaseUser>) ((Hashtable<String, EaseUser>)m).clone();
        }
        setContactsMap(m);
        super.refresh();
//        if(inviteMessgeDao == null){
//            inviteMessgeDao = new InviteMessgeDao(getActivity());
//        }
//        if(inviteMessgeDao.getUnreadMessagesCount() > 0){
//            applicationItem.showUnreadMsgView();
//            applicationItem.setUnreadCount(inviteMessgeDao.getUnreadMessagesCount());
//        }else{
//            applicationItem.hideUnreadMsgView();
//        }
        getApplyNum();
    }

    private GetInfoProtocal mProtocal;
    private Gson gson = new Gson();
    private void getApplyNum() {
        if (mProtocal == null){
            mProtocal = new GetInfoProtocal();
        }
        String phoneNumber = SpTools.getString(getActivity() , Constants.phoneNum);
        mProtocal.getApplyNum(phoneNumber, new AddCommandProtocal.NormalListener() {
            @Override
            public void normalResponse(Object response) {
                if (response == null){
                    applicationItem.hideUnreadMsgView();
                    CommonUtils.toastMessage("请求未读消息数量失败，请手动刷新重试！");
                    return;
                }
                ApplyNum bean = gson.fromJson((String) response,ApplyNum.class);
                if ("10".equals(bean.result.code)){
                    if (bean.qunNum > 0){
                        applicationItem.showUnreadMsgView();
                        applicationItem.setUnreadCount(bean.qunNum);
                    }else {
                        applicationItem.hideUnreadMsgView();
                    }
                }else {
                    applicationItem.hideUnreadMsgView();
                    CommonUtils.toastMessage("请求未读消息数量失败，请手动刷新重试！");
                }
            }
        });
    }




    @SuppressWarnings("unchecked")
    @Override
    protected void setUpView() {
        titleBar.setRightImageResource(R.drawable.em_add);
        titleBar.setRightLayoutClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), AddContactActivity.class));
                NetUtils.hasDataConnection(getActivity());
            }
        });
        //设置联系人数据
        Map<String, EaseUser> m = DemoHelper.getInstance().getContactList();
        if (m instanceof Hashtable<?, ?>) {
            m = (Map<String, EaseUser>) ((Hashtable<String, EaseUser>)m).clone();
        }
        setContactsMap(m);
        if (EaseInitBean.map == null) {
            if (EaseInitBean.contactBean != null) {

                for (ContactBean contactBean : EaseInitBean.contactBean.friendlist) {
                    userInfo.put(contactBean.hx_account, contactBean);
                }
            }
            EaseInitBean.map = userInfo;
        }
        super.setUpView();

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EaseUser user = (EaseUser)listView.getItemAtPosition(position);

                if (user != null) {

                    String username = user.getUsername();
                    // demo中直接进入聊天页面，实际一般是进入用户详情页
                    Intent intent = new Intent(getActivity(),UserProfileActivity.class);
                    intent.putExtra("username",username);
                    startActivityForResult(intent,DEL_FRIEND);
                }
            }
        });

        
        // 进入添加好友页
        titleBar.getRightLayout().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddContactActivity.class));
            }
        });
        
        
        contactSyncListener = new ContactSyncListener();
        DemoHelper.getInstance().addSyncContactListener(contactSyncListener);
        
        blackListSyncListener = new BlackListSyncListener();
        DemoHelper.getInstance().addSyncBlackListListener(blackListSyncListener);
        
        contactInfoSyncListener = new ContactInfoSyncListener();
        DemoHelper.getInstance().getUserProfileManager().addSyncContactInfoListener(contactInfoSyncListener);
        
        if (DemoHelper.getInstance().isContactsSyncedWithServer()) {
            loadingView.setVisibility(View.GONE);
        } else if (DemoHelper.getInstance().isSyncingContactsWithServer()) {
            loadingView.setVisibility(View.VISIBLE);
        }
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (contactSyncListener != null) {
            DemoHelper.getInstance().removeSyncContactListener(contactSyncListener);
            contactSyncListener = null;
        }
        
        if(blackListSyncListener != null){
            DemoHelper.getInstance().removeSyncBlackListListener(blackListSyncListener);
        }
        
        if(contactInfoSyncListener != null){
            DemoHelper.getInstance().getUserProfileManager().removeSyncContactInfoListener(contactInfoSyncListener);
        }
    }
    
	
	protected class HeaderItemClickListener implements OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.application_item:
                // 进入申请与通知页面
                startActivityForResult(new Intent(getActivity(), NewFriendsMsgActivity.class),REFRESH_DATA);
                break;
            case R.id.group_item:
                // 进入群聊列表页面
                startActivity(new Intent(getActivity(), GroupsActivity.class));
                break;

            default:
                break;
            }
        }
	    
	}
	

//	@Override
//	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
//		super.onCreateContextMenu(menu, v, menuInfo);
//	    toBeProcessUser = (EaseUser) listView.getItemAtPosition(((AdapterContextMenuInfo) menuInfo).position);
//	    toBeProcessUsername = toBeProcessUser.getUsername();
//		getActivity().getMenuInflater().inflate(R.menu.em_context_contact_list, menu);
//	}

//	@Override
//	public boolean onContextItemSelected(MenuItem item) {
//		if (item.getItemId() == R.id.delete_contact) {
//			try {
//                // delete contact
//                deleteContact(toBeProcessUser);
//                // remove invitation message
//                InviteMessgeDao dao = new InviteMessgeDao(getActivity());
//                dao.deleteMessage(toBeProcessUser.getUsername());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//			return true;
//		}else if(item.getItemId() == R.id.add_to_blacklist){
//			moveToBlacklist(toBeProcessUsername);
//			return true;
//		}
//		return super.onContextItemSelected(item);
//	}


	/**
	 * delete contact
	 * 
	 * @param tobeDeleteUser
	 */
	public void deleteContact(final EaseUser tobeDeleteUser) {
		new Thread(new Runnable() {
			public void run() {
				try {
					EMClient.getInstance().contactManager().deleteContact(tobeDeleteUser.getUsername());
					// remove user from memory and database
					UserDao dao = new UserDao(getActivity());
					dao.deleteContact(tobeDeleteUser.getUsername());
//					DemoHelper.getInstance().getContactList().remove(tobeDeleteUser.getUsername());
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							contactList.remove(tobeDeleteUser);
							contactListLayout.refresh();

						}
					});
//                    InviteMessgeDao dao2 = new InviteMessgeDao(getActivity());
//                    dao2.deleteMessage(tobeDeleteUser.getUsername());
				} catch (final Exception e) {
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
						}
					});

				}

			}
		}).start();


	}
	
	class ContactSyncListener implements DemoHelper.DataSyncListener {
        @Override
        public void onSyncComplete(final boolean success) {
            if (getActivity() ==null)
                return;
            EMLog.d(TAG, "on contact list sync success:" + success);
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    getActivity().runOnUiThread(new Runnable(){

                        @Override
                        public void run() {
                            if(success){
                                loadingView.setVisibility(View.GONE);
                                refresh();
                            }else{
                                String s1 = getResources().getString(R.string.get_failed_please_check);
                                Toast.makeText(getActivity(), s1, Toast.LENGTH_LONG).show();
                                loadingView.setVisibility(View.GONE);
                            }
                        }
                        
                    });
                }
            });
        }
    }
    
    class BlackListSyncListener implements DemoHelper.DataSyncListener {

        @Override
        public void onSyncComplete(boolean success) {
            if (getActivity() ==null)
                return;
            getActivity().runOnUiThread(new Runnable(){

                @Override
                public void run() {
                    refresh();
                }
            });
        }
        
    }

    class ContactInfoSyncListener implements DemoHelper.DataSyncListener {

        @Override
        public void onSyncComplete(final boolean success) {
            EMLog.d(TAG, "on contactinfo list sync success:" + success);
            if (getActivity() ==null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                
                @Override
                public void run() {
                    loadingView.setVisibility(View.GONE);
                    if(success){
                        refresh();
                    }
                }
            });
        }
        
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DEL_FRIEND){
            //删除好友
            if (data != null) {
                String username = data.getStringExtra("tobeDeleteUser");
                EaseUser user = DemoHelper.getInstance().getUserInfo(username);
                deleteContact(user);

            }
        }else if (requestCode == REFRESH_DATA){
            //需要刷新页面
            if (data != null) {
                String refresh = data.getStringExtra("refresh");
                if ("yes".equals(refresh)){
                    refresh();
                }
            }
        }
    }
}
