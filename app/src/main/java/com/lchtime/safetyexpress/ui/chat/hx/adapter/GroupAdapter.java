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

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.hyphenate.chat.EMGroup;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.ui.chat.hx.bean.GroupBean;
import com.lchtime.safetyexpress.views.CircleImageView;

import java.util.List;

import static com.baidu.location.f.mC;

public class GroupAdapter extends ArrayAdapter<GroupBean> {

	private LayoutInflater inflater;
	private String newGroup;
	private String addPublicGroup;
	private Context context;
	public GroupAdapter(Context context, int res, List<GroupBean> groups) {
		super(context, res, groups);
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		newGroup = "创建群组";
		addPublicGroup = "加入群组";
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
	public View getView(int position, View convertView, ViewGroup parent) {
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
				convertView = inflater.inflate(R.layout.em_row_group, parent, false);
			}
			CircleImageView imageView = (CircleImageView) convertView.findViewById(R.id.avatar);
			Glide.with(context)
					.load(getItem(position -3).sq_fieldid)
					.placeholder(R.drawable.qun_list)
					.error(R.drawable.qun_list)
					.into(imageView);
			//Log.i("getMuteList==" , group.getMuteList().toString());
			//group.
			((TextView) convertView.findViewById(R.id.name)).setText(getItem(position - 3).sq_name);
		}

		return convertView;
	}

	@Override
	public int getCount() {
		return super.getCount() + 3;
//		return super.getCount() + 1;
	}

}
