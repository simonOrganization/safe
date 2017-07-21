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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.adapter.EaseContactAdapter;
import com.hyphenate.easeui.bean.ContactBean;
import com.hyphenate.easeui.bean.EaseInitBean;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.widget.EaseSidebar;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.ui.chat.hx.Constant;
import com.lchtime.safetyexpress.ui.chat.hx.DemoHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupPickContactsActivity extends BaseActivity implements View.OnClickListener {
	/** if this is a new group */
	protected boolean isCreatingNewGroup;
	private PickContactAdapter contactAdapter;
	/** members already in the group */
	private List<String> existMembers;



	private TextView mTitle;
	private TextView mTitleRight;
	private LinearLayout mTitleLeft;
	private LinearLayout mLlTitleRight;
	private ImageButton clearSearch;
	private ListView listView;
	private Map<String,ContactBean> myMap = new HashMap<>();
	private boolean type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.em_activity_group_pick_contacts);
		String groupId = getIntent().getStringExtra("groupId");
		type = getIntent().getBooleanExtra("type" , true);
		initTitle();

		initView();

		initListener();
		if (groupId == null) {// create new group
			isCreatingNewGroup = true;
		} else {
			// get members of the group
			EMGroup group = EMClient.getInstance().groupManager().getGroup(groupId);
			existMembers = group.getMembers();
		}
		if(existMembers == null)
			existMembers = new ArrayList<String>();
		// get contact list
		final List<EaseUser> alluserList = new ArrayList<EaseUser>();
		for (EaseUser user : DemoHelper.getInstance().getContactList().values()) {
			if (!user.getUsername().equals(Constant.NEW_FRIENDS_USERNAME) & !user.getUsername().equals(Constant.GROUP_USERNAME) & !user.getUsername().equals(Constant.CHAT_ROOM) & !user.getUsername().equals(Constant.CHAT_ROBOT))
				alluserList.add(user);
		}
		// sort the list
        Collections.sort(alluserList, new Comparator<EaseUser>() {

            @Override
            public int compare(EaseUser lhs, EaseUser rhs) {
                if(lhs.getInitialLetter().equals(rhs.getInitialLetter())){
                    return lhs.getNick().compareTo(rhs.getNick());
                }else{
                    if("#".equals(lhs.getInitialLetter())){
                        return 1;
                    }else if("#".equals(rhs.getInitialLetter())){
                        return -1;
                    }
                    return lhs.getInitialLetter().compareTo(rhs.getInitialLetter());
                }
                
            }
        });

		if (EaseInitBean.map == null) {
			if (EaseInitBean.contactBean != null) {

				for (ContactBean contactBean : EaseInitBean.contactBean.friendlist) {
					myMap.put(contactBean.hx_account, contactBean);
				}
			}
			EaseInitBean.map = myMap;
		}
		contactAdapter = new PickContactAdapter(this, R.layout.em_row_contact_with_checkbox, alluserList , EaseInitBean.map);
		listView.setAdapter(contactAdapter);
		((EaseSidebar) findViewById(R.id.sidebar)).setListView(listView);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
				checkBox.toggle();

			}
		});
	}

	private EditText query;
	private void initView() {
		query = (EditText) findViewById(R.id.query);
		clearSearch = (ImageButton) findViewById(R.id.search_clear);
		listView = (ListView) findViewById(R.id.list);

	}

	private void initListener() {
		query.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				filter(s);
				if (s.length() > 0) {
					clearSearch.setVisibility(View.VISIBLE);
				} else {
					clearSearch.setVisibility(View.INVISIBLE);

				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void afterTextChanged(Editable s) {
			}
		});

		clearSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				query.getText().clear();
				hideSoftKeyboard();
			}
		});
	}


	public void filter(CharSequence str) {
		contactAdapter.getFilter().filter(str);
	}

	private void initTitle() {

		mTitle = (TextView) findViewById(R.id.title);
		mTitleRight = (TextView) findViewById(R.id.tv_delete);
		mTitleLeft = (LinearLayout) findViewById(R.id.ll_back);
		mLlTitleRight = (LinearLayout) findViewById(R.id.ll_right);
		mLlTitleRight.setVisibility(View.VISIBLE);
		mTitle.setText("添加好友");
		mTitleRight.setText("确定");
		mTitleRight.setVisibility(View.VISIBLE);
		mLlTitleRight.setOnClickListener(this);
		mTitleLeft.setOnClickListener(this);
	}


	/**
	 * get selected members
	 * 
	 * @return
	 */
	private ArrayList<EaseUser> getToBeAddMembers() {
		//List<String> members = new ArrayList<String>();
		ArrayList<EaseUser> members = new ArrayList<EaseUser>();
		int length = contactAdapter.isCheckedArray.length;
		for (int i = 0; i < length; i++) {
			String username = contactAdapter.getItem(i).getUsername();
            ContactBean bean = EaseInitBean.map.get(username);
			//if (contactAdapter.isCheckedArray[i] && !existMembers.contains(username)) {
			if (contactAdapter.isCheckedArray[i]) {
                EaseUser user = contactAdapter.getItem(i);
                user.setAvatar(bean.ud_photo_fileid);
                user.setExternalNickName(bean.ud_nickname);
				user.setNick(bean.hx_account);
				user.setNickname(bean.ud_nickname);
				members.add(user);
			}
		}

		return members;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.ll_right){
			ArrayList<EaseUser> members = getToBeAddMembers();
			if(type){
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				//bundle.putParcelableArrayList("list",(ArrayList<User>)list);// 序列化,要注意
				bundle.putParcelableArrayList("newmembers", members);
				intent.putExtras(bundle);// 发送数据
				setResult(RESULT_OK, intent);

			}else{
				NewGroupActivity.setSelectData(members);
			}
			finish();
		}else if(v.getId() == R.id.ll_back){
			finish();
		}
	}

	/**
	 * adapter
	 */
	private class PickContactAdapter extends EaseContactAdapter {

		private boolean[] isCheckedArray;

		public PickContactAdapter(Context context, int resource, List<EaseUser> users , Map<String,ContactBean> userInfo) {

			super(context, resource, users,userInfo);
			isCheckedArray = new boolean[users.size()];
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = super.getView(position, convertView, parent);

			final String username = getItem(position).getUsername();

			final CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
			ImageView avatarView = (ImageView) view.findViewById(R.id.avatar);
			TextView nameView = (TextView) view.findViewById(R.id.name);
			
			if (checkBox != null) {
//			    if(existMembers != null && existMembers.contains(username)){
//                    checkBox.setButtonDrawable(R.drawable.em_checkbox_bg_gray_selector);
//                }else{
//                    checkBox.setButtonDrawable(R.drawable.em_checkbox_bg_selector);
//                }

				checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						// check the exist members
						if (existMembers.contains(username)) {
								isChecked = true;
								checkBox.setChecked(true);
						}
						isCheckedArray[position] = isChecked;

					}
				});
				// keep exist members checked
				if (existMembers.contains(username)) {
						checkBox.setChecked(true);
						isCheckedArray[position] = true;
				} else {
					checkBox.setChecked(isCheckedArray[position]);
				}
			}

			return view;
		}
	}

	
}
