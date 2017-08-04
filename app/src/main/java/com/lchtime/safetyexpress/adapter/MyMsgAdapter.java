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
package com.lchtime.safetyexpress.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.InitInfo;
import com.lchtime.safetyexpress.bean.Result;
import com.lchtime.safetyexpress.pop.VipInfoHintPop;
import com.lchtime.safetyexpress.ui.chat.hx.activity.NewFriendsMsgActivity;
import com.lchtime.safetyexpress.ui.chat.hx.activity.protocal.GetInfoProtocal;
import com.lchtime.safetyexpress.ui.chat.hx.bean.ApplyMessageBean;
import com.lchtime.safetyexpress.ui.chat.hx.db.InviteMessgeDao;
import com.lchtime.safetyexpress.ui.chat.hx.fragment.protocal.AddCommandProtocal;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.SpTools;

import java.util.List;

public class MyMsgAdapter extends ArrayAdapter<ApplyMessageBean.ApplyListBean> {

	private Activity context;
	private InviteMessgeDao messgeDao;
	private List<ApplyMessageBean.ApplyListBean> objects;
	private GetInfoProtocal mProtocal = new GetInfoProtocal();
	private Gson gson = new Gson();
	private String userid;
	private VipInfoHintPop popWindow;

	public MyMsgAdapter(Activity context, int textViewResourceId, List<ApplyMessageBean.ApplyListBean> objects) {
		super(context, textViewResourceId, objects);
		this.objects = objects;
		this.context = context;
		messgeDao = new InviteMessgeDao(context);
		userid = SpTools.getUserId(MyApplication.getContext());
		initPop();
	}

	private void initPop() {
		if (context instanceof NewFriendsMsgActivity) {
			popWindow = new VipInfoHintPop(((NewFriendsMsgActivity)context).listView, context, R.layout.pop_delete_hint);
			popWindow.setPerfect("删除");
			popWindow.setJump("取消");
			popWindow.setContent("确定删除该条消息？");
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.em_row_invite_msg_2, null);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.avator = (ImageView) convertView.findViewById(R.id.avatar);
			holder.status = (CheckBox) convertView.findViewById(R.id.user_state);
			holder.reason = (TextView) convertView.findViewById(R.id.message);
			holder.delete = (LinearLayout) convertView.findViewById(R.id.delete);
			// holder.time = (TextView) convertView.findViewById(R.id.time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}


		final ApplyMessageBean.ApplyListBean msg = getItem(position);
		if (msg != null) {

			holder.reason.setText(msg.message);
			holder.name.setText(msg.ud_nickname);
			if (!TextUtils.isEmpty(msg.ud_photo_fileid)) {
				Glide.with(MyApplication.getContext())
				.load(msg.ud_photo_fileid)
				.into(holder.avator);
			}else {
				Glide.with(MyApplication.getContext())
						.load(R.drawable.circle_user_image)
						.into(holder.avator);
			}

			if ("1".equals(msg.status)){
				//如果是已读的消息
				holder.status.setChecked(true);
				holder.status.setText("已添加");
			}else {
				holder.status.setChecked(false);
				holder.status.setText("添加");
			}


			holder.status.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if ("1".equals(msg.status)){
						holder.status.setChecked(true);
					}else {
						//请求网络数据

						accept(msg, holder.status);

					}
				}
			});

//			holder.status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//				@Override
//				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//					if ("1".equals(msg.status)){
//						holder.status.setChecked(true);
//					}else {
//						//请求网络数据
//						if (isChecked) {
//							accept(msg, holder.status);
//						}
//					}
//				}
//			});

			holder.delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//删除操作
					popWindow.showAtLocation();
					popWindow.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							popWindow.dismiss();
							deleteMsg(msg);
						}
					});

				}
			});


		}

		return convertView;
	}

	private void deleteMsg(final ApplyMessageBean.ApplyListBean msg) {
		mProtocal.getDelapply(msg.applyid, new AddCommandProtocal.NormalListener() {
            @Override
            public void normalResponse(Object response) {
                if (response == null){
                    CommonUtils.toastMessage("删除请求失败，请稍后再试！");
                    return;
                }

                Result bean = gson.fromJson((String) response,Result.class);
                if ("10".equals(bean.result.code)){
                    CommonUtils.toastMessage(bean.result.info);
                    //刷新界面
                    if (objects != null){
                        objects.remove(msg);
                        notifyDataSetChanged();
                    }
                }else {
                    CommonUtils.toastMessage(bean.result.info);
                }
            }
        });
	}

	private void accept(final ApplyMessageBean.ApplyListBean msg, final CheckBox checkBox) {

		String phoneNumber = SpTools.getString(context , Constants.phoneNum);
		if ("0".equals(msg.qun)) {
			//如果不是群，接收好友
			mProtocal.getAccept(phoneNumber, msg.hx_account, new AddCommandProtocal.NormalListener() {
				@Override
				public void normalResponse(Object response) {
					if (response == null) {
						CommonUtils.toastMessage("网络不稳定，请稍后重试");
						checkBox.setChecked(false);
						return;
					}

					Result bean = gson.fromJson((String) response, Result.class);
					if ("10".equals(bean.result.code)) {
						CommonUtils.toastMessage(bean.result.info);
						msg.status = "1";
						notifyDataSetChanged();
					} else {
						CommonUtils.toastMessage(bean.result.info);
						checkBox.setChecked(false);
					}

				}
			});
		}else {
			//接受群
			if (TextUtils.isEmpty(userid)){
				CommonUtils.toastMessage("请重新登录后再尝试！");
				checkBox.setChecked(false);
				return;
			}
			mProtocal.getAcceptQun("1", userid, msg.groupid, phoneNumber, msg.hx_account, new AddCommandProtocal.NormalListener() {
				@Override
				public void normalResponse(Object response) {
					if (response == null){
						CommonUtils.toastMessage("网络不稳定，请稍后重试");
						checkBox.setChecked(false);
						return;
					}

					Result bean = gson.fromJson((String) response,Result.class);
					if ("10".equals(bean.result.code)) {
						CommonUtils.toastMessage(bean.result.info);
						msg.status = "1";
						notifyDataSetChanged();
					} else {
						CommonUtils.toastMessage(bean.result.info);
						checkBox.setChecked(false);
					}
				}
			});

		}

	}


	private static class ViewHolder {
		ImageView avator;
		TextView name;
		TextView reason;
		CheckBox status;
		LinearLayout delete;
		// TextView time;
	}

}
