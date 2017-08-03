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
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.lchtime.safetyexpress.H5DetailUI;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.ui.chat.hx.Constant;
import com.lchtime.safetyexpress.ui.chat.hx.activity.protocal.GetInfoProtocal;
import com.lchtime.safetyexpress.ui.chat.hx.adapter.GroupAdapter;
import com.lchtime.safetyexpress.ui.chat.hx.bean.GroupBean;
import com.lchtime.safetyexpress.ui.chat.hx.bean.JoinGroups;
import com.lchtime.safetyexpress.ui.chat.hx.fragment.protocal.AddCommandProtocal;
import com.lchtime.safetyexpress.utils.SpTools;

import java.util.ArrayList;
import java.util.List;

/**
 * 群聊界面
 */
public class GroupsActivity extends BaseActivity implements View.OnClickListener {
	public static final String TAG = "GroupsActivity";
	public ListView groupListView;
	//protected List<EMGroup> grouplist;
	private GroupAdapter groupAdapter;
	private InputMethodManager inputMethodManager;
	public static GroupsActivity instance;
	private View progressBar;
	private SwipeRefreshLayout swipeRefreshLayout;
	protected List<GroupBean> mGroupList = new ArrayList<>();

	private TextView mTitle;
	private TextView mTitleRight;
	private LinearLayout mTitleLeft;
	private LinearLayout mLlTitleRight;
	private GetInfoProtocal protocal;


	Handler handler = new Handler(){
	    public void handleMessage(android.os.Message msg) {
	        swipeRefreshLayout.setRefreshing(false);
	        switch (msg.what) {
            case 0:
                refresh();
                break;
            case 1:
                Toast.makeText(GroupsActivity.this, R.string.Failed_to_get_group_chat_information, Toast.LENGTH_LONG).show();
                break;

            default:
                break;
            }
	    }
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.em_fragment_groups);
		initTitle();
		instance = this;
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		getGroupsData();
		//grouplist = EMClient.getInstance().groupManager().getAllGroups();
		groupListView = (ListView) findViewById(R.id.list);
		//show group list
        groupAdapter = new GroupAdapter(this, 1, mGroupList);
        groupListView.setAdapter(groupAdapter);
		
		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
		swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
		                R.color.holo_orange_light, R.color.holo_red_light);
		//pull down to refresh
		swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				new Thread(){
					@Override
					public void run(){
						try {
							EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
							handler.sendEmptyMessage(0);
						} catch (HyphenateException e) {
							e.printStackTrace();
							handler.sendEmptyMessage(1);
						}
					}
				}.start();
			}
		});
		
		groupListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position == 1) {
					// create a new group
					startActivityForResult(new Intent(GroupsActivity.this, NewGroupActivity.class), 0);
				} else if (position == 2) {
					// join a public group
					startActivityForResult(new Intent(GroupsActivity.this, AddContactActivity.class).putExtra("group","1"), 0);
				} else {
					// enter group chat
					Intent intent = new Intent(GroupsActivity.this, ChatActivity.class);
					// it is group chat
					intent.putExtra("chatType", Constant.CHATTYPE_GROUP);
					intent.putExtra("userId", groupAdapter.getItem(position - 3).sq_group_id);
					startActivityForResult(intent, 0);
				}
			}

		});
		groupListView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
					if (getCurrentFocus() != null)
						inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);

				}
				return false;
			}
		});
		
	}

	/**
	 * 获取群组信息
	 */
	private void getGroupsData() {
		String ub_id = SpTools.getUserId(GroupsActivity.this);
		if(TextUtils.isEmpty(ub_id)) return;
		if(protocal == null){
			protocal = new GetInfoProtocal();
		}
		final Gson gson = new Gson();

		protocal.getJoinGroups(ub_id, new AddCommandProtocal.NormalListener() {
			@Override
			public void normalResponse(Object response) {
				JoinGroups groups = gson.fromJson((String) response, JoinGroups.class);
				mGroupList.clear();
				if(groups != null){
					if(groups.add_qun!= null)
						mGroupList.addAll(groups.add_qun);
					if(groups.create_qun != null)
						mGroupList.addAll(groups.create_qun);
				}
				groupAdapter.notifyDataSetChanged();
			}
		});



	}

	private void initTitle() {
		mTitle = (TextView) findViewById(R.id.title);
		mTitleRight = (TextView) findViewById(R.id.tv_delete);
		mTitleLeft = (LinearLayout) findViewById(R.id.ll_back);
		mLlTitleRight = (LinearLayout) findViewById(R.id.ll_right);
		mLlTitleRight.setVisibility(View.GONE);
		mTitle.setText("群聊");
		mTitleRight.setText("建群");
		mTitleRight.setVisibility(View.VISIBLE);
		mLlTitleRight.setOnClickListener(this);
		mTitleLeft.setOnClickListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onResume() {
        refresh();
		super.onResume();
	}
	
	private void refresh(){
	    //grouplist = EMClient.getInstance().groupManager().getAllGroups();
		getGroupsData();
        groupAdapter = new GroupAdapter(this, 1, mGroupList);
        groupListView.setAdapter(groupAdapter);
        groupAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		instance = null;
	}


	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.ll_right){
			//跳转创建群界面
			startActivityForResult(new Intent(GroupsActivity.this, NewGroupActivity.class), 0);
		}else if(v.getId() == R.id.ll_back){
			finish();
		}
	}
}
