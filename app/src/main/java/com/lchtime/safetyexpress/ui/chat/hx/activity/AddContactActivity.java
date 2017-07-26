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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.widget.EaseAlertDialog;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.ui.chat.hx.DemoHelper;
import com.lchtime.safetyexpress.ui.chat.hx.adapter.AddFriendsCommendAdapter;
import com.lchtime.safetyexpress.ui.chat.hx.bean.SearchResultBean;
import com.lchtime.safetyexpress.ui.chat.hx.bean.UserBean;
import com.lchtime.safetyexpress.ui.chat.hx.fragment.FindFriendsFragment;
import com.lchtime.safetyexpress.ui.chat.hx.fragment.FindGroupsFragment;
import com.lchtime.safetyexpress.ui.chat.hx.fragment.protocal.AddCommandProtocal;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.SpTools;


import java.util.List;

import static com.lchtime.safetyexpress.R.id.add_subscirbe_rg;

public class AddContactActivity extends BaseActivity implements View.OnClickListener {
	private static final int REQUEST_CODE_GROUP_DETAIL = 13;
	private static final int REQUEST_CODE_CONTEXT_MENU = 14;
	private static final int REQUEST_CODE_SELECT_VIDEO = 11;
	private static final int REQUEST_CODE_SELECT_AT_USER = 15;

	private EditText editText;
	private FrameLayout searchedUserLayout;
	private TextView nameText;
	private Button searchBtn;
	private String toAddUsername;
	private ProgressDialog progressDialog;
	private AddCommandProtocal mProtocal;

	private TextView mTitle;
	private TextView mTitleRight;
	private LinearLayout mTitleLeft;
	private LinearLayout mLlTitleRight;
	private View mLeftLine;
	private View mRightLine;
	private RadioGroup mRg;
	private RelativeLayout mRlSearchButton;
	private TextView mTvSearchContent;
	private RelativeLayout mSearchResult;
	private String mUb_id;
	private ImageView mAvatar;
	private TextView mName;
	private TextView mMessage;
	private TextView mTvBtAdd;
	private LinearLayout mIndicator;
	private RecyclerView mSearchSuccess;
	private RelativeLayout mLoading;
	private RelativeLayout mError;
	private TextView mEmpty;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.em_activity_add_contact);

		if (mProtocal == null){
			mProtocal = new AddCommandProtocal();
		}
		mUb_id = SpTools.getString(this, Constants.userId,"");
		initTitle();
//		TextView mTextView = (TextView) findViewById(R.id.add_list_friends);
		

//		String strAdd = getResources().getString(R.string.add_friend);
//		mTextView.setText(strAdd);
		//设置搜索框暗示
//		String strUserName = getResources().getString(R.string.user_name);
//		editText.setHint(strUserName);
		initView();

//		searchBtn = (Button) findViewById(R.id.search);
	}

	private void initView() {

		mSearchSuccess = (RecyclerView) findViewById(R.id.search_result_success);
		mLoading = (RelativeLayout) findViewById(R.id.loading);
		mError = (RelativeLayout) findViewById(R.id.error);
		mEmpty = (TextView) findViewById(R.id.empty);

		mAvatar = (ImageView) findViewById(R.id.avatar);
		mName = (TextView) findViewById(R.id.name);
		mMessage = (TextView) findViewById(R.id.message);
		mTvBtAdd = (TextView) findViewById(R.id.tv_bt_add);
		mIndicator = (LinearLayout) findViewById(R.id.indicator);

		editText = (EditText) findViewById(R.id.edit_note);
		searchedUserLayout = (FrameLayout) findViewById(R.id.ll_user);
		nameText = (TextView) findViewById(R.id.name);
		mLeftLine = findViewById(R.id.add_subscirbe_line_left);
		mRightLine = findViewById(R.id.add_subscirbe_line_right);
		mRg = (RadioGroup) findViewById(add_subscirbe_rg);
		mRlSearchButton = (RelativeLayout) findViewById(R.id.search_button);
		mTvSearchContent = (TextView) findViewById(R.id.search_content);
		mSearchResult = (RelativeLayout) findViewById(R.id.search_result);
		String group = getIntent().getStringExtra("group");
		String friend = getIntent().getStringExtra("friend");
		if (!TextUtils.isEmpty(group)){
			//如果是通过群组进入的，那么就显示群组界面，并且将引导器隐藏
			mLeftLine.setVisibility(View.GONE);
			mRightLine.setVisibility(View.GONE);
			mRg.setVisibility(View.GONE);
			mIndex = 1;
			mTitle.setText("加群组");
			initFragment(1);
		}else if (!TextUtils.isEmpty(friend)){
			//如果是通过加好友进入的，那么就显示群组界面，并且将引导器隐藏
			mLeftLine.setVisibility(View.GONE);
			mRightLine.setVisibility(View.GONE);
			mRg.setVisibility(View.GONE);
			mIndex = 0;
			mTitle.setText("加好友");
			initFragment(0);
		}else {
			initFragment(0);
		}

		mRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, int i) {
				switch (i){
					case R.id.add_subscirbe_line_rb_all:
						setIndexSelected(0);
						break;
					case R.id.add_subscirbe_line_rb_comm:
						setIndexSelected(1);
						break;
				}
			}
		});
		initListener();
	}



	private int mIndex = 0;
	private Fragment[] fragments;
	private void setIndexSelected(int index) {
		if (mIndex == index) {
			return;
		}
		if(index == 0){
			mLeftLine.setBackgroundColor(Color.parseColor("#ea553f"));
			mRightLine.setBackgroundColor(Color.parseColor("#f5f5f5"));
		}else if(index == 1){
			mRightLine.setBackgroundColor(Color.parseColor("#ea553f"));
			mLeftLine.setBackgroundColor(Color.parseColor("#f5f5f5"));
		}
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		//隐藏
		ft.hide(fragments[mIndex]);
		//判断是否添加
		if (!fragments[index].isAdded()) {
			ft.add(R.id.ll_user, fragments[index]).show(fragments[index]);
		} else {
			ft.show(fragments[index]);
		}
		ft.commit();
		//再次赋值
		mIndex = index;
	}

	private void initFragment(int selected) {
		FindFriendsFragment fff = new FindFriendsFragment();
		FindGroupsFragment fgf = new FindGroupsFragment();
		fragments = new Fragment[]{fff,fgf};
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(R.id.ll_user,fragments[selected]).commit();
		setIndexSelected(selected);
	}

	private void initTitle() {
		mTitle = (TextView) findViewById(R.id.title);
		mTitleRight = (TextView) findViewById(R.id.tv_delete);
		mTitleLeft = (LinearLayout) findViewById(R.id.ll_back);
		mLlTitleRight = (LinearLayout) findViewById(R.id.ll_right);
		mLlTitleRight.setVisibility(View.GONE);
		mTitle.setText("添加好友/群");
		mTitleRight.setVisibility(View.VISIBLE);
		mLlTitleRight.setOnClickListener(this);
		mTitleLeft.setOnClickListener(this);
	}


	private void initListener() {
		editText.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
//				conversationListView.filter(s);
				if (s.length() > 0) {
//					clearSearch.setVisibility(View.VISIBLE);
					mRlSearchButton.setVisibility(View.VISIBLE);
				} else {
//					clearSearch.setVisibility(View.INVISIBLE);
					mRlSearchButton.setVisibility(View.GONE);
					mSearchResult.setVisibility(View.GONE);
					searchedUserLayout.setVisibility(View.VISIBLE);
				}
				mTvSearchContent.setText(s);

			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void afterTextChanged(Editable s) {
			}
		});

		mRlSearchButton.setOnClickListener(this);
	}
	/**
	 * search contact
	 * @param v
	 */
	public void searchContact(View v) {
		//执行搜索操作
		final String name = editText.getText().toString();
//		String saveText = searchBtn.getText().toString();
		
		/*if (getString(R.string.button_search).equals(saveText)) {
			toAddUsername = name;
			if(TextUtils.isEmpty(name)) {
				new EaseAlertDialog(this, R.string.Please_enter_a_username).show();
				return;
			}
			
			// TODO you can search the user from your app server here.
			
			//show the userame and add button if user exist
			searchedUserLayout.setVisibility(View.VISIBLE);
			nameText.setText(toAddUsername);
			
		} */
	}	
	
	/**
	 *  add contact
	 * @param view
	 */
	public void addContact(View view){
		if(EMClient.getInstance().getCurrentUser().equals(nameText.getText().toString())){
			new EaseAlertDialog(this, R.string.not_add_myself).show();
			return;
		}

		if(DemoHelper.getInstance().getContactList().containsKey(nameText.getText().toString())){
			//let the user know the contact already in your contact list
			if(EMClient.getInstance().contactManager().getBlackListUsernames().contains(nameText.getText().toString())){
				new EaseAlertDialog(this, R.string.user_already_in_contactlist).show();
				return;
			}
			new EaseAlertDialog(this, R.string.This_user_is_already_your_friend).show();
			return;
		}

		progressDialog = new ProgressDialog(this);
		String stri = getResources().getString(R.string.Is_sending_a_request);
		progressDialog.setMessage(stri);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();

		new Thread(new Runnable() {
			public void run() {

				try {
					//demo use a hardcode reason here, you need let user to input if you like
					String s = getResources().getString(R.string.Add_a_friend);
					EMClient.getInstance().contactManager().addContact(toAddUsername, s);
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							String s1 = getResources().getString(R.string.send_successful);
							Toast.makeText(getApplicationContext(), s1, Toast.LENGTH_LONG).show();
						}
					});
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							String s2 = getResources().getString(R.string.Request_add_buddy_failure);
							Toast.makeText(getApplicationContext(), s2 + e.getMessage(), Toast.LENGTH_LONG).show();
						}
					});
				}
			}
		}).start();
	}


	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.ll_back){
			finish();
		}else if (v.getId() == R.id.search_button){
			if(TextUtils.isEmpty(mUb_id)){
				CommonUtils.toastMessage("登陆才能搜索哦！！！");
				return;
			}

			setSearchLoadingVisiblity();
			mSearchSuccess.setLayoutManager(new LinearLayoutManager(this));
			String searchWord = editText.getText().toString().trim();
			//先将按钮隐藏起来
			mRlSearchButton.setVisibility(View.GONE);
			//隐藏推荐，显示搜索结果
			setSearchLoadingVisiblity();
			mSearchResult.setVisibility(View.VISIBLE);
			searchedUserLayout.setVisibility(View.GONE);
			String type = "";
			//进行搜索操作
			if (mIndex == 0){
				//好友
				type = "1";
			}else {
				//群
				type = "0";
			}
			final String finalType = type;
			mProtocal.getSearchFriends(mUb_id, type, searchWord, new AddCommandProtocal.NormalListener() {
				@Override
				public void normalResponse(Object response) {
					if (response == null){
						CommonUtils.toastMessage("网络错误!");
						setSearchErrorVisiblity();
						return;
					}

					SearchResultBean bean = (SearchResultBean) response;
					List<UserBean> list = null;
					if (finalType == "1") {
						list = bean.user_base;
					}else {
						list = bean.qun;
					}
					if ("10".equals(bean.result.code) && list != null && list.size() > 0 ) {

						AddFriendsCommendAdapter adapter = new AddFriendsCommendAdapter(fragments[mIndex],list,mIndex);
						mSearchSuccess.setAdapter(adapter);
						setSearchSuccessVisiblity();
					}else if ("20".equals(bean.result.code)){
						setSearchEmptyVisiblity();
//						CommonUtils.toastMessage("用户不存在!");
					}else {
						setSearchErrorVisiblity();
//						CommonUtils.toastMessage("用户不存在!");
					}
				}
			});
		}
	}


	public void setSearchSuccessVisiblity(){
		mSearchSuccess.setVisibility(View.VISIBLE);
		mLoading.setVisibility(View.GONE);
		mError.setVisibility(View.GONE);
		mEmpty.setVisibility(View.GONE);

	}

	public void setSearchLoadingVisiblity(){
		mLoading.setVisibility(View.VISIBLE);
		mSearchSuccess.setVisibility(View.GONE);
		mError.setVisibility(View.GONE);
		mEmpty.setVisibility(View.GONE);
	}

	public void setSearchErrorVisiblity(){
		mError.setVisibility(View.VISIBLE);
		mLoading.setVisibility(View.GONE);
		mSearchSuccess.setVisibility(View.GONE);
		mEmpty.setVisibility(View.GONE);
	}

	public void setSearchEmptyVisiblity(){
		mEmpty.setVisibility(View.VISIBLE);
		mError.setVisibility(View.GONE);
		mLoading.setVisibility(View.GONE);
		mSearchSuccess.setVisibility(View.GONE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_CONTEXT_MENU) {
			switch (resultCode) {
//				case ContextMenuActivity.RESULT_CODE_COPY: // copy
//					clipboard.setPrimaryClip(ClipData.newPlainText(null,
//							((EMTextMessageBody) contextMenuMessage.getBody()).getMessage()));
//					break;
				case ContextMenuActivity.RESULT_CODE_DELETE: // delete
//					conversation.removeMessage(contextMenuMessage.getMsgId());
//					messageList.refresh();
					break;

				case ContextMenuActivity.RESULT_CODE_FORWARD: // forward
//					Intent intent = new Intent(getActivity(), ForwardMessageActivity.class);
//					intent.putExtra("forward_msg_id", contextMenuMessage.getMsgId());
//					startActivity(intent);

					break;

				default:
					break;
			}
		}
		if(resultCode == Activity.RESULT_OK){
			switch (requestCode) {
				case REQUEST_CODE_SELECT_VIDEO: //send the video
//					if (data != null) {
//						int duration = data.getIntExtra("dur", 0);
//						String videoPath = data.getStringExtra("path");
//						File file = new File(PathUtil.getInstance().getImagePath(), "thvideo" + System.currentTimeMillis());
//						try {
//							FileOutputStream fos = new FileOutputStream(file);
//							Bitmap ThumbBitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
//							ThumbBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//							fos.close();
//							sendVideoMessage(videoPath, file.getAbsolutePath(), duration);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
					break;
//                case REQUEST_CODE_SELECT_FILE: //send the file
//                    if (data != null) {
//                        Uri uri = data.getData();
//                        if (uri != null) {
//                            sendFileByUri(uri);
//                        }
//                    }
//                    break;
				case REQUEST_CODE_SELECT_AT_USER:
//					if(data != null){
//						String username = data.getStringExtra("username");
//						inputAtUsername(username, false);
//					}
					break;
				//red packet code : 发送红包消息到聊天界面

				//end of red packet code
				default:
					break;
			}
		}


//		if (requestCode == 102){
//			if (data != null){
//				String finish = data.getStringExtra("finish");
//				if ("1".equals(finish)){
//					finish();
//				}
//			}
//		}
	}
}
