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

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.google.gson.Gson;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.MyMsgAdapter;
import com.lchtime.safetyexpress.bean.InitInfo;
import com.lchtime.safetyexpress.ui.chat.hx.activity.protocal.GetInfoProtocal;
import com.lchtime.safetyexpress.ui.chat.hx.bean.ApplyMessageBean;
import com.lchtime.safetyexpress.ui.chat.hx.db.InviteMessgeDao;
import com.lchtime.safetyexpress.ui.chat.hx.domain.InviteMessage;
import com.lchtime.safetyexpress.ui.chat.hx.fragment.protocal.AddCommandProtocal;
import com.lchtime.safetyexpress.utils.CommonUtils;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import static com.lchtime.safetyexpress.R.id.swipeRefreshLayout;


/**
 * Application and notification
 *
 */
public class NewFriendsMsgActivity extends BaseActivity implements View.OnClickListener {

	private TextView mTitle;
	private TextView mTitleRight;
	private LinearLayout mTitleLeft;
	private LinearLayout mLlTitleRight;
	public ListView listView;
	public SwipeRefreshLayout refresh;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.em_activity_new_friends_msg);
		initTitle();

		listView = (ListView) findViewById(R.id.list);
		refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
		refresh.setColorSchemeResources(com.hyphenate.easeui.R.color.holo_blue_bright, com.hyphenate.easeui.R.color.holo_green_light,
				com.hyphenate.easeui.R.color.holo_orange_light, com.hyphenate.easeui.R.color.holo_red_light);
		refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

			@Override
			public void onRefresh() {
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						initData();

					}
				}, 300);
			}
		});
		initData();
//		InviteMessgeDao dao = new InviteMessgeDao(this);
//		List<InviteMessage> msgs = dao.getMessagesList();
//
//		dao.saveUnreadMessageCount(0);
		
	}

	private GetInfoProtocal mProtocal;
	private Gson gson = new Gson();
	public List<ApplyMessageBean.ApplyListBean> applyList = new ArrayList<>();
	private void initData() {
		if (mProtocal == null){
			mProtocal = new GetInfoProtocal();
		}
		mProtocal.getApplyMessage(InitInfo.phoneNumber, new AddCommandProtocal.NormalListener() {
			@Override
			public void normalResponse(Object response) {
				if (response == null){
					CommonUtils.toastMessage("请求未读消息失败，请手动刷新重试！");
					refresh.setRefreshing(false);
					return;
				}
				ApplyMessageBean bean = gson.fromJson((String) response,ApplyMessageBean.class);
				if ("10".equals(bean.result.code)){
					if (bean.applyList == null){
						bean.applyList = new ArrayList<ApplyMessageBean.ApplyListBean>();
					}
					applyList.clear();
					applyList.addAll(bean.applyList);
					MyMsgAdapter adapter = new MyMsgAdapter(NewFriendsMsgActivity.this,R.layout.em_row_invite_msg_2,applyList);
					listView.setAdapter(adapter);
				}else {
					CommonUtils.toastMessage("请求未读消息失败，请手动刷新重试！");
				}
				refresh.setRefreshing(false);

			}
		});
	}

	private void initTitle() {
		mTitle = (TextView) findViewById(R.id.title);
		mTitleRight = (TextView) findViewById(R.id.tv_delete);
		mTitleLeft = (LinearLayout) findViewById(R.id.ll_back);
		mLlTitleRight = (LinearLayout) findViewById(R.id.ll_right);
		mLlTitleRight.setVisibility(View.VISIBLE);
		mTitle.setText("新的朋友");
		mTitleRight.setText("加好友");
		mTitleRight.setVisibility(View.VISIBLE);
		mLlTitleRight.setOnClickListener(this);
		mTitleLeft.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.ll_right){
			//跳转加好友界面

			startActivity(new Intent(this, AddContactActivity.class).putExtra("friend","1"));

		}else if(v.getId() == R.id.ll_back){
			setResult(100,new Intent().putExtra("refresh","yes"));
			finish();
		}
	}
}
