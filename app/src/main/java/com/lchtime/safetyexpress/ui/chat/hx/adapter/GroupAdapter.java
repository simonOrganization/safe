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
package com.lchtime.safetyexpress.ui.chat.hx.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.Result;
import com.lchtime.safetyexpress.pop.VipInfoHintPop;
import com.lchtime.safetyexpress.ui.chat.hx.Constant;
import com.lchtime.safetyexpress.ui.chat.hx.activity.ChatActivity;
import com.lchtime.safetyexpress.ui.chat.hx.activity.GroupsActivity;
import com.lchtime.safetyexpress.ui.chat.hx.activity.protocal.GetInfoProtocal;
import com.lchtime.safetyexpress.ui.chat.hx.bean.GroupBean;
import com.lchtime.safetyexpress.ui.chat.hx.fragment.protocal.AddCommandProtocal;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lchtime.safetyexpress.views.CircleImageView;

import java.util.List;

public class GroupAdapter extends ArrayAdapter<GroupBean> {

	private LayoutInflater inflater;
	private String newGroup;
	private String addPublicGroup;
	private Activity context;
	private VipInfoHintPop popWindow;
	private GetInfoProtocal mProtocal;
	private Gson gson;
	private List<GroupBean> groups;


	public GroupAdapter(Activity context, int res, List<GroupBean> groups) {
		super(context, res, groups);
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.groups = groups;
		newGroup = "创建群组";
		addPublicGroup = "加入群组";
		gson = new Gson();
		initPop();
	}


	private void initPop() {
		if (context instanceof GroupsActivity) {
			popWindow = new VipInfoHintPop(((GroupsActivity)context).groupListView, context, R.layout.pop_delete_hint);
			popWindow.setPerfect("删除");
			popWindow.setJump("取消");
			popWindow.setContent("确定删除/退群吗？");
		}
	}


	@Override
	public int getViewTypeCount() {
		return 4;
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0) {
			return 0;
		}
		else if (position == 1) {
			//创建群组
			return 1;
		} else if (position == 2) {
			//加入群组
			return 2;
		}
		else {
			return 3;
		}
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (getItemViewType(position) == 0) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.em_search_bar_with_padding, parent, false);
			}
			final EditText query = (EditText) convertView.findViewById(R.id.query);
			final ImageButton clearSearch = (ImageButton) convertView.findViewById(R.id.search_clear);
			query.addTextChangedListener(new TextWatcher() {
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					getFilter().filter(s);
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
			clearSearch.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					query.getText().clear();
				}
			});
		} else if (getItemViewType(position) == 1) {
			//创建群组
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.em_row_add_group, parent, false);
			}
			((ImageView) convertView.findViewById(R.id.avatar)).setImageResource(R.drawable.em_create_group);
			((TextView) convertView.findViewById(R.id.name)).setText(newGroup);
			convertView.findViewById(R.id.header).setVisibility(View.VISIBLE);
			convertView.findViewById(R.id.white_view).setVisibility(View.GONE);

		} else if (getItemViewType(position) == 2) {
			//公共群条目  加入群组
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.em_row_add_group, parent, false);
			}
			((ImageView) convertView.findViewById(R.id.avatar)).setImageResource(R.drawable.em_add_public_group);
			((TextView) convertView.findViewById(R.id.name)).setText(addPublicGroup);
			convertView.findViewById(R.id.header).setVisibility(View.GONE);
			convertView.findViewById(R.id.white_view).setVisibility(View.VISIBLE);

		} else {
			if (convertView == null) {
				//convertView = inflater.inflate(R.layout.em_row_group, parent, false);
				convertView = inflater.inflate(R.layout.em_row_group_delete, parent, false);
			}
			final GroupBean groupBean = getItem(position -3);
			final CircleImageView imageView = (CircleImageView) convertView.findViewById(R.id.avatar);

			Glide.with(context)
					.load(groupBean.sq_fieldid)
					.placeholder(R.drawable.qun_list)
					.error(R.drawable.qun_list)
					.into(imageView);
			SpTools.setString(context , groupBean.sq_group_id , groupBean.sq_fieldid);
			((TextView) convertView.findViewById(R.id.name)).setText(groupBean.sq_name);
			convertView.findViewById(R.id.delete).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//删除操作
					if(popWindow != null){
						popWindow.showAtLocation();
						popWindow.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								popWindow.dismiss();
								deleteGroup(groupBean);
							}
						});
					}
				}
			});
			convertView.findViewById(R.id.rl_group).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context , ChatActivity.class);
					// it is group chat
					intent.putExtra("chatType", Constant.CHATTYPE_GROUP);
					intent.putExtra("userId", groupBean.sq_group_id);
					context.startActivityForResult(intent, 0);
				}
			});
		}

		return convertView;
	}

	/**
	 * 删除群
	 * @param groupBean
	 */
	private void deleteGroup(final GroupBean groupBean) {
		String userId = SpTools.getUserId(context);
		if(mProtocal == null){
			mProtocal = new GetInfoProtocal();
		}
		mProtocal.getDeleteGrounp(userId, groupBean.sq_group_id, new AddCommandProtocal.NormalListener() {
			@Override
			public void normalResponse(Object response) {
				if(response == null){
					CommonUtils.toastMessage("操作失败");
					return;
				}
				Result result = gson.fromJson((String) response, Result.class);
				if ("10".equals(result.result.code)){
					groups.remove(groupBean);
					notifyDataSetChanged();
				}else {
					CommonUtils.toastMessage(result.result.info);
				}

			}
		});

	}

	@Override
	public int getCount() {
		return super.getCount() + 3;
//		return super.getCount() + 1;
	}

}
