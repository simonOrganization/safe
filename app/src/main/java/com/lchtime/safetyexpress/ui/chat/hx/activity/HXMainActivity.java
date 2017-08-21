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
package com.lchtime.safetyexpress.ui.chat.hx.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMConversation.EMConversationType;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.EMLog;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.InitInfo;
import com.lchtime.safetyexpress.ui.chat.hx.Constant;
import com.lchtime.safetyexpress.ui.chat.hx.DemoHelper;
import com.lchtime.safetyexpress.ui.chat.hx.activity.protocal.GetInfoProtocal;
import com.lchtime.safetyexpress.ui.chat.hx.bean.ApplyNum;
import com.lchtime.safetyexpress.ui.chat.hx.db.InviteMessgeDao;
import com.lchtime.safetyexpress.ui.chat.hx.db.UserDao;
import com.lchtime.safetyexpress.ui.chat.hx.fragment.ContactListFragment;
import com.lchtime.safetyexpress.ui.chat.hx.fragment.ConversationListFragment;
import com.lchtime.safetyexpress.ui.chat.hx.fragment.protocal.AddCommandProtocal;
import com.lchtime.safetyexpress.ui.chat.hx.permission.PermissionsManager;
import com.lchtime.safetyexpress.ui.chat.hx.permission.PermissionsResultAction;
import com.lchtime.safetyexpress.ui.login.LoginUI;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.SpTools;

import java.util.List;

//环信会话列表的类，也就是点击聊天的界面

@SuppressLint("NewApi")
public class HXMainActivity extends BaseActivity implements View.OnClickListener {

	protected static final String TAG = "MainActivity";
	// textview for unread message count
	private TextView unreadLabel;
	// textview for unread event message
	private TextView unreadAddressLable;

//	private Button[] mTabs;
	private ContactListFragment contactListFragment;
	private Fragment[] fragments;
	private int index;
	private int currentTabIndex;
	// user logged into another device
	public boolean isConflict = false;
	// user account was removed
	private boolean isCurrentAccountRemoved = false;


	private TextView mTitle;
	private ImageView mTitleRight;
	private LinearLayout mTitleLeft;
	private LinearLayout mLlTitleRight;
	private String ub_id;
	private TextView msgNum;


	/**
	 * check if current user account was remove
	 */
	public boolean getCurrentAccountRemoved() {
		return isCurrentAccountRemoved;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
		    String packageName = getPackageName();
		    PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		    if (!pm.isIgnoringBatteryOptimizations(packageName)) {
		        Intent intent = new Intent();
		        intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
		        intent.setData(Uri.parse("package:" + packageName));
		        startActivity(intent);
		    }
		}
		
		//make sure activity will not in background if user is logged into another device or removed
		if (savedInstanceState != null && savedInstanceState.getBoolean(Constant.ACCOUNT_REMOVED, false)) {
		    DemoHelper.getInstance().logout(false,null);
			finish();
			startActivity(new Intent(this, LoginUI.class));
			return;
		} else if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false)) {
			finish();
			startActivity(new Intent(this, LoginUI.class));
			return;
		}
		setContentView(R.layout.em_activity_main);
		// runtime permission for android 6.0, just require all permissions here for simple

		ub_id = SpTools.getUserId(this);

		requestPermissions();

		initView();


		showExceptionDialogFromIntent(getIntent());

		inviteMessgeDao = new InviteMessgeDao(this);
		UserDao userDao = new UserDao(this);
		conversationListFragment = new ConversationListFragment();
		contactListFragment = new ContactListFragment();
//		SettingsFragment settingFragment = new SettingsFragment();
		fragments = new Fragment[] { conversationListFragment, contactListFragment};

		getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, conversationListFragment)
				.show(conversationListFragment)
				.commit();

		//register broadcast receiver to receive the change of group from DemoHelper
		registerBroadcastReceiver();
		
		
		EMClient.getInstance().contactManager().setContactListener(new MyContactListener());
		//debug purpose only
        registerInternalDebugReceiver();
	}




	@TargetApi(23)
	private void requestPermissions() {
		PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
			@Override
			public void onGranted() {
//				Toast.makeText(MainActivity.this, "All permissions have been granted", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onDenied(String permission) {
				//Toast.makeText(MainActivity.this, "Permission " + permission + " has been denied", Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * init views
	 */
	private void initView() {
		//提示有多少个未读
		unreadLabel = (TextView) findViewById(R.id.unread_msg_number);
		//通讯录里的未读
//		unreadAddressLable = (TextView) findViewById(R.id.unread_address_number);


		mTitle = (TextView) findViewById(R.id.title);
		mTitleRight = (ImageView) findViewById(R.id.iv_right);
		mTitleLeft = (LinearLayout) findViewById(R.id.ll_back);
		mLlTitleRight = (LinearLayout) findViewById(R.id.ll_right);
		msgNum = (TextView) findViewById(R.id.unread_msg_number);
		mLlTitleRight.setVisibility(View.VISIBLE);
		mTitleLeft.setVisibility(View.GONE);
		mTitle.setText("聊天");
		mTitle.setTextColor(getResources().getColor(R.color.black));
		mTitleRight.setVisibility(View.VISIBLE);
		mTitleRight.setImageResource(R.drawable.icon_persion_list);
		mLlTitleRight.setOnClickListener(this);




//		mTabs = new Button[2];
//		mTabs[0] = (Button) findViewById(R.id.btn_conversation);
//		mTabs[1] = (Button) findViewById(R.id.btn_address_list);
//		mTabs[2] = (Button) findViewById(R.id.btn_setting);
		// select first tab
//		mTabs[0].setSelected(true);
	}

	/**
	 * on tab clicked
	 * 
	 * @param view
	 */
//	public void onTabClicked(View view) {
//		switch (view.getId()) {
//		case R.id.btn_conversation:
//			index = 0;
//			break;
//		case R.id.btn_address_list:
//			index = 1;
//			break;
////		case R.id.btn_setting:
////			index = 2;
////			break;
//		}
//		if (currentTabIndex != index) {
//			FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
//			trx.hide(fragments[currentTabIndex]);
//			if (!fragments[index].isAdded()) {
//				trx.add(R.id.fragment_container, fragments[index]);
//			}
//			trx.show(fragments[index]).commit();
//		}
////		mTabs[currentTabIndex].setSelected(false);
//		// set current tab selected
////		mTabs[index].setSelected(true);
//		currentTabIndex = index;
//	}

	EMMessageListener messageListener = new EMMessageListener() {
		
		@Override
		public void onMessageReceived(List<EMMessage> messages) {
			// notify new message
		    for (EMMessage message : messages) {
		        DemoHelper.getInstance().getNotifier().onNewMsg(message);
		    }
			refreshUIWithMessage();
		}
		
		@Override
		public void onCmdMessageReceived(List<EMMessage> messages) {
			//red packet code : 处理红包回执透传消息
			/*for (EMMessage message : messages) {
				EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
				final String action = cmdMsgBody.action();//获取自定义action
				if (action.equals(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION)) {
					RedPacketUtil.receiveRedPacketAckMessage(message);
				}
			}*/
			//end of red packet code
			refreshUIWithMessage();
		}

		@Override
		public void onMessageRead(List<EMMessage> list) {

		}

		@Override
		public void onMessageDelivered(List<EMMessage> list) {

		}

		
		@Override
		public void onMessageChanged(EMMessage message, Object change) {}
	};

	private void refreshUIWithMessage() {
		runOnUiThread(new Runnable() {
			public void run() {
				// refresh unread count
//				updateUnreadLabel();
				if (currentTabIndex == 0) {
					// refresh conversation list
					if (conversationListFragment != null) {
						conversationListFragment.refresh();
					}
				}
			}
		});
	}

	@Override
	public void back(View view) {
		super.back(view);
	}
	
	private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(Constant.ACTION_GROUP_CHANAGED);
//		intentFilter.addAction(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION);
        broadcastReceiver = new BroadcastReceiver() {
            
            @Override
            public void onReceive(Context context, Intent intent) {
                updateUnreadLabel();
                updateUnreadAddressLable();
                if (currentTabIndex == 0) {
                    // refresh conversation list
                    if (conversationListFragment != null) {
                        conversationListFragment.refresh();
                    }
                } else if (currentTabIndex == 1) {
//                    if(contactListFragment != null) {
//                        contactListFragment.refresh();
//                    }
                }
                String action = intent.getAction();
                if(action.equals(Constant.ACTION_GROUP_CHANAGED)){
                    if (EaseCommonUtils.getTopActivity(HXMainActivity.this).equals(GroupsActivity.class.getName())) {
                        GroupsActivity.instance.onResume();
                    }
                }
				//red packet code : 处理红包回执透传消息
		/*		if (action.equals(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION)){
					if (conversationListFragment != null){
						conversationListFragment.refresh();
					}
				}*/
				//end of red packet code
			}
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }
	
	public class MyContactListener implements EMContactListener {
		@Override
		public void onContactAdded(String username) {
		}
		@Override
		public void onContactDeleted(final String username) {
			runOnUiThread(new Runnable() {
				public void run() {
					if (ChatActivity.activityInstance != null && ChatActivity.activityInstance.toChatUsername != null &&
							username.equals(ChatActivity.activityInstance.toChatUsername)) {
						String st10 = getResources().getString(R.string.have_you_removed);
						Toast.makeText(HXMainActivity.this, ChatActivity.activityInstance.getToChatUsername() + st10, Toast.LENGTH_LONG)
								.show();
						ChatActivity.activityInstance.finish();
					}
				}
			});
		}
		@Override
		public void onContactInvited(String username, String reason) {
		}

		@Override
		public void onFriendRequestAccepted(final String s) {
			//CommonUtils.toastMessage(s + "已同意您的好友申请");
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(HXMainActivity.this, s + "已同意您的好友申请" , Toast.LENGTH_LONG)
							.show();
				}
			});

		}

		@Override
		public void onFriendRequestDeclined(final String s) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(HXMainActivity.this, s + "已拒绝您的好友申请" , Toast.LENGTH_LONG)
							.show();
				}
			});

		}

	}
	
	private void unregisterBroadcastReceiver(){
	    broadcastManager.unregisterReceiver(broadcastReceiver);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();		
		
		if (exceptionBuilder != null) {
		    exceptionBuilder.create().dismiss();
		    exceptionBuilder = null;
		    isExceptionDialogShow = false;
		}
		unregisterBroadcastReceiver();

		try {
            unregisterReceiver(internalDebugReceiver);
        } catch (Exception e) {
        }
		
	}

	/**
	 * update unread message count
	 */
	public void updateUnreadLabel() {
//		int count = getUnreadMsgCountTotal();
//		if (count > 0) {
//			unreadLabel.setText(String.valueOf(count));
//			unreadLabel.setVisibility(View.VISIBLE);
//		} else {
//			unreadLabel.setVisibility(View.INVISIBLE);
//		}
	}

	/**
	 * update the total unread count 
	 */
	public void updateUnreadAddressLable() {
//		runOnUiThread(new Runnable() {
//			public void run() {
//				int count = getUnreadAddressCountTotal();
//				if (count > 0) {
//					unreadAddressLable.setVisibility(View.VISIBLE);
//				} else {
//					unreadAddressLable.setVisibility(View.INVISIBLE);
//				}
//			}
//		});

	}

	/**
	 * get unread event notification count, including application, accepted, etc
	 * 
	 * @return
	 */
	public int getUnreadAddressCountTotal() {
		int unreadAddressCountTotal = 0;
		unreadAddressCountTotal = inviteMessgeDao.getUnreadMessagesCount();
		return unreadAddressCountTotal;
	}

	/**
	 * get unread message count
	 * 
	 * @return
	 */
	public int getUnreadMsgCountTotal() {
		int unreadMsgCountTotal = 0;
		int chatroomUnreadMsgCount = 0;
		unreadMsgCountTotal = EMClient.getInstance().chatManager().getUnreadMsgsCount();
		for(EMConversation conversation: EMClient.getInstance().chatManager().getAllConversations().values()){
			if(conversation.getType() == EMConversationType.ChatRoom)
			chatroomUnreadMsgCount=chatroomUnreadMsgCount+conversation.getUnreadMsgCount();
		}
		return unreadMsgCountTotal-chatroomUnreadMsgCount;
	}

	private InviteMessgeDao inviteMessgeDao;

	@Override
	protected void onResume() {
		super.onResume();
		
		if (!isConflict && !isCurrentAccountRemoved) {
			updateUnreadLabel();
			updateUnreadAddressLable();
		}

		// unregister this event listener when this activity enters the
		// background
		DemoHelper sdkHelper = DemoHelper.getInstance();
		sdkHelper.pushActivity(this);

		EMClient.getInstance().chatManager().addMessageListener(messageListener);


		ub_id = SpTools.getUserId(this);
		if (TextUtils.isEmpty(ub_id)) {
			if (mLlTitleRight != null) {
				mLlTitleRight.setEnabled(false);
			}
		}else {
			if (mLlTitleRight != null) {
				mLlTitleRight.setEnabled(true);
			}
		}
		getApplyNum();

	}

	@Override
	protected void onStop() {
		EMClient.getInstance().chatManager().removeMessageListener(messageListener);
		DemoHelper sdkHelper = DemoHelper.getInstance();
		sdkHelper.popActivity(this);

		super.onStop();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean("isConflict", isConflict);
		outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			exit();
			return true;
		}

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			//moveTaskToBack(false);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	private android.app.AlertDialog.Builder exceptionBuilder;
	private boolean isExceptionDialogShow =  false;
    private BroadcastReceiver internalDebugReceiver;
    private ConversationListFragment conversationListFragment;
    private BroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager broadcastManager;

    private int getExceptionMessageId(String exceptionType) {
         if(exceptionType.equals(Constant.ACCOUNT_CONFLICT)) {
             return R.string.connect_conflict;
         } else if (exceptionType.equals(Constant.ACCOUNT_REMOVED)) {
             return R.string.em_user_remove;
         } else if (exceptionType.equals(Constant.ACCOUNT_FORBIDDEN)) {
             return R.string.user_forbidden;
         }
         return R.string.Network_error;
    }
	/**
	 * show the dialog when user met some exception: such as login on another device, user removed or user forbidden
	 */
	private void showExceptionDialog(String exceptionType) {
	    isExceptionDialogShow = true;
		DemoHelper.getInstance().logout(false,null);
		String st = getResources().getString(R.string.Logoff_notification);
		if (!HXMainActivity.this.isFinishing()) {
			// clear up global variables
			try {
				if (exceptionBuilder == null)
				    exceptionBuilder = new android.app.AlertDialog.Builder(HXMainActivity.this);
				    exceptionBuilder.setTitle(st);
				    exceptionBuilder.setMessage(getExceptionMessageId(exceptionType));	
				    exceptionBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						exceptionBuilder = null;
						isExceptionDialogShow = false;
						finish();
						//进入登录界面
						/*Intent intent = new Intent(HXMainActivity.this, LoginActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);*/
					}
				});
				exceptionBuilder.setCancelable(false);
				exceptionBuilder.create().show();
				isConflict = true;
			} catch (Exception e) {
				EMLog.e(TAG, "---------color conflictBuilder error" + e.getMessage());
			}
		}
	}

	private void showExceptionDialogFromIntent(Intent intent) {
	    EMLog.e(TAG, "showExceptionDialogFromIntent");
	    if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_CONFLICT, false)) {
            showExceptionDialog(Constant.ACCOUNT_CONFLICT);
        } else if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_REMOVED, false)) {
            showExceptionDialog(Constant.ACCOUNT_REMOVED);
        } else if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_FORBIDDEN, false)) {
            showExceptionDialog(Constant.ACCOUNT_FORBIDDEN);
        }   
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		showExceptionDialogFromIntent(intent);
	}
	
	/**
	 * debug purpose only, you can ignore this
	 */
	private void registerInternalDebugReceiver() {
	    internalDebugReceiver = new BroadcastReceiver() {
            
            @Override
            public void onReceive(Context context, Intent intent) {
                DemoHelper.getInstance().logout(false,new EMCallBack() {
                    
                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                finish();
//                                startActivity(new Intent(HXMainActivity.this, LoginActivity.class));
                            }
                        });
                    }
                    
                    @Override
                    public void onProgress(int progress, String status) {}
                    
                    @Override
                    public void onError(int code, String message) {}
                });
            }
        };
        IntentFilter filter = new IntentFilter(getPackageName() + ".em_internal_debug");
        registerReceiver(internalDebugReceiver, filter);
    }

	@Override 
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
			@NonNull int[] grantResults) {
		PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
	}


	/**
	 * 描述：退出程序
	 */

	private long exitTime = 0;
	protected void exit() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			finish();
			//System.exit(0);
		}
	}


	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.ll_right){
			//跳转联系人界面
			goContact();
		}
	}

	private void goContact() {
		Intent intent = new Intent(this,ContactActivity.class);
		startActivity(intent);
	}


	private GetInfoProtocal mProtocal;
	private Gson gson = new Gson();
	private void getApplyNum() {
		if (mProtocal == null) {
			mProtocal = new GetInfoProtocal();
		}
		String phoneNumber = SpTools.getHXID(this);
		if (phoneNumber != null) {
			mProtocal.getApplyNum(phoneNumber, new AddCommandProtocal.NormalListener() {
				@Override
				public void normalResponse(Object response) {
					if (response == null) {
						isShowMsgView(false);
						//CommonUtils.toastMessage("请求未读消息数量失败，请手动刷新重试！");
						return;
					}
					ApplyNum bean = gson.fromJson((String) response, ApplyNum.class);
					if ("10".equals(bean.result.code)) {
						if (bean.qunNum > 0) {
							isShowMsgView(true);
						} else {
							isShowMsgView(false);
						}
					} else {
						isShowMsgView(false);
						CommonUtils.toastMessage("请求未读消息数量失败，请手动刷新重试！");
					}
				}
			});
		}
	}

	public void isShowMsgView(boolean isShow){
		if (isShow) {
			msgNum.setVisibility(View.VISIBLE);
		}else {
			msgNum.setVisibility(View.GONE);
		}
	}


}
