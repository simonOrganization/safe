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
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.ui.chat.hx.bean.InfoBean;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.value;

public class AllPeopleAdapter extends ArrayAdapter<InfoBean.QunersBean> {

	private LayoutInflater inflater;
	private List<InfoBean.QunersBean> copyConversationList;
	public AllPeopleAdapter(Context context, int res, List<InfoBean.QunersBean> groups) {
		super(context, res, groups);
		this.inflater = LayoutInflater.from(context);
//		this.groups = groups;
		copyConversationList = new ArrayList<InfoBean.QunersBean>();
		copyConversationList.addAll(groups);
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0) {
			return 0;
		}

		else {
			return 1;
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
		}  else {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.em_row_group, parent, false);
			}
			((TextView) convertView.findViewById(R.id.name)).setText(getItem(position - 1).ud_nickname);
			if (TextUtils.isEmpty(getItem(position - 1).ud_photo_fileid)){

				Picasso.with(MyApplication
						.getContext()).load(R.drawable.circle_user_image)
						.into((ImageView)convertView.findViewById(R.id.avatar));

			}else {

				Picasso.with(MyApplication
						.getContext()).load(getItem(position - 1).ud_photo_fileid)
						.into((ImageView)convertView.findViewById(R.id.avatar));
			}

		}

		return convertView;
	}

	@Override
	public int getCount() {
		return super.getCount() + 1;
	}



}
