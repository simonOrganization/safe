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

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.widget.EaseAlertDialog;
import com.hyphenate.easeui.widget.EaseExpandGridView;
import com.hyphenate.util.NetUtils;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.CardBean;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.InitInfo;
import com.lchtime.safetyexpress.bean.ProfessionBean;
import com.lchtime.safetyexpress.bean.Result;
import com.lchtime.safetyexpress.bean.UpdataBean;
import com.lchtime.safetyexpress.bean.VipInfoBean;
import com.lchtime.safetyexpress.ui.chat.hx.activity.protocal.GetInfoProtocal;
import com.lchtime.safetyexpress.ui.chat.hx.fragment.protocal.AddCommandProtocal;
import com.lchtime.safetyexpress.ui.vip.SelectCityActivity;
import com.lchtime.safetyexpress.utils.BitmapUtils;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.DialogUtil;
import com.lchtime.safetyexpress.utils.LoginInternetRequest;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lchtime.safetyexpress.utils.UpdataImageUtils;
import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;
import com.yalantis.ucrop.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;



public class NewGroupActivity extends BaseActivity implements View.OnClickListener, PopupWindow.OnDismissListener {
	private ProgressDialog progressDialog;

	private static final int CITY_CODE = 588;
	private TextView mTitle;
	private TextView mTitleRight;
	private LinearLayout mTitleLeft;
	private LinearLayout mLlTitleRight;

	private TextView mGroupName;
	private TextView mInviteContent;
	private TextView mHyContent;
	private TextView mAddrContent;
	private CheckBox mCbPublic;
	private CheckBox mCbInviter;
	private TextView mInviteText;
	private TextView mPrivateText;
	private ImageView civPhoto;
	private TextView tvSave;
	private EaseExpandGridView mUserGridview;
	private RelativeLayout photoItem;
	private RelativeLayout nameItem;
	private RelativeLayout inviteItem;
	private RelativeLayout hyItem;
	private RelativeLayout addrItem;
	private RelativeLayout loading;
	private static GridAdapter adapter;
	public static ArrayList<String> memberList = new ArrayList<>();
	public static List<EaseUser> members = new ArrayList<>();
	private List<ProfessionBean.ProfessionItemBean> professionList = new ArrayList<>();
	private ArrayList<CardBean> cardItem = new ArrayList<>();
	private GetInfoProtocal mProtocal;
	private String mUserid;

	private String type;
	private DialogUtil mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.em_activity_new_group);
		initTitle();
//		groupNameEditText = (EditText) findViewById(R.id.edit_group_name);
//		introductionEditText = (EditText) findViewById(R.id.edit_group_introduction);
//		publibCheckBox = (CheckBox) findViewById(R.id.cb_public);
//		memberCheckbox = (CheckBox) findViewById(R.id.cb_member_inviter);
//		secondTextView = (TextView) findViewById(R.id.second_desc);

		mGroupName = (TextView) findViewById(R.id.name_content);
		mInviteContent = (TextView) findViewById(R.id.invite_content);
		mHyContent = (TextView) findViewById(R.id.hy_content);
		mAddrContent = (TextView) findViewById(R.id.addr_content);
		mCbPublic = (CheckBox) findViewById(R.id.cb_public);
		mCbInviter = (CheckBox) findViewById(R.id.cb_member_inviter);
		civPhoto = (ImageView) findViewById(R.id.civ_photo);
		mPrivateText = (TextView) findViewById(R.id.private_text);
		mInviteText = (TextView) findViewById(R.id.invite_text);
		mUserGridview = (EaseExpandGridView) findViewById(R.id.gridview);
		photoItem = (RelativeLayout) findViewById(R.id.photo_item);
		nameItem = (RelativeLayout) findViewById(R.id.name_item);
		inviteItem = (RelativeLayout) findViewById(R.id.invite_item);
		hyItem = (RelativeLayout) findViewById(R.id.hy_item);
		addrItem = (RelativeLayout) findViewById(R.id.addr_item);
		loading = (RelativeLayout) findViewById(R.id.loading);
		tvSave = (TextView) findViewById(R.id.tv_save);
		adapter = new GridAdapter(this, R.layout.em_grid, members);
		mUserGridview.setAdapter(adapter);
		type = "0";

		mDialog = new DialogUtil(NewGroupActivity.this);

		initData();
		initListener();
		initPopWindow();

	}



	private void initListener() {
		photoItem.setOnClickListener(this);
		nameItem.setOnClickListener(this);
		inviteItem.setOnClickListener(this);
		hyItem.setOnClickListener(this);
		addrItem.setOnClickListener(this);
		tvSave.setOnClickListener(this);
		mUserGridview.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						if (adapter.isInDeleteMode) {
							adapter.isInDeleteMode = false;
							adapter.notifyDataSetChanged();
							return true;
						}
						break;
					default:
						break;
				}
				return false;
			}
		});

		mCbPublic.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					mPrivateText.setText("公有群");
					if (mCbInviter.isChecked()){
						mInviteText.setText("随便加入");
						type = "3";
					}else {
						mInviteText.setText("加入群组需要管理员同意");
						type = "2";
					}

				}else{
					mPrivateText.setText("私有群");
					if (mCbInviter.isChecked()){
						mInviteText.setText("允许群成员邀请其他人");
						type = "1";
					}else {
						mInviteText.setText("不允许群成员邀请其他人");
						type = "0";
					}

				}
			}
		});

		mCbInviter.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					if (mCbPublic.isChecked()){
						mInviteText.setText("随便加入");
						type = "3";
					}else {
						mInviteText.setText("允许群成员邀请其他人");
						type = "1";
					}
				}else{
					if (mCbPublic.isChecked()){
						mInviteText.setText("加入群组需要管理员同意");
						type = "2";
					}else {
						mInviteText.setText("不允许群成员邀请其他人");
						type = "0";
					}
				}
			}
		});
	}

	private void initTitle() {
		mTitle = (TextView) findViewById(R.id.title);
		mTitleRight = (TextView) findViewById(R.id.tv_delete);
		mTitleLeft = (LinearLayout) findViewById(R.id.ll_back);
		mLlTitleRight = (LinearLayout) findViewById(R.id.ll_right);
		mLlTitleRight.setVisibility(View.GONE);
		mTitle.setText("创建群组");
		mTitleRight.setVisibility(View.VISIBLE);
		mLlTitleRight.setOnClickListener(this);
		mTitleLeft.setOnClickListener(this);
	}

	private void initData() {
		//初始化行业
		initHangYe();

		//初始化自定义选项卡
		initCustomOptionPicker();

	}

	/**
	 * 设置成员的信息
	 * @param datas
	 */
	public static void setSelectData(ArrayList<EaseUser> datas){
		members.clear();
		members.addAll(datas);
		memberList.clear();
		for(EaseUser user : members){
			memberList.add(user.getUsername());
		}
		if(adapter != null)
		adapter.notifyDataSetChanged();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
//		String st1 = getResources().getString(R.string.Is_to_create_a_group_chat);
//		final String st2 = getResources().getString(R.string.Failed_to_create_groups);
		if (resultCode == RESULT_OK) {

		}else if (requestCode == CITY_CODE){
			if (data != null) {
				String city = data.getStringExtra("city");
				mAddrContent.setText(city);
			}

		}

		if (requestCode == 100){
			//选择名字回来的
			if (data != null){
				mGroupName.setText(data.getStringExtra("name"));
			}

		}else if (requestCode == 101){
			//选择介绍回来的
			if (data != null) {
				mInviteContent.setText(data.getStringExtra("invite"));
			}
		}
	}



	private class GridAdapter extends ArrayAdapter<EaseUser> {

		private int res;
		public boolean isInDeleteMode;
		private List<EaseUser> objects;

		public GridAdapter(Context context, int textViewResourceId, List<EaseUser> objects) {
			super(context, textViewResourceId, objects);
			this.objects = objects;
			res = textViewResourceId;
			isInDeleteMode = false;
			//int getPaysum = 100;
			//String transData = "{\"amt\":" + getPaysum + "}" ;
		}

		@Override
		public View getView(final int position, View convertView, final ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(getContext()).inflate(res, null);
				holder.imageView = (ImageView) convertView.findViewById(R.id.iv_avatar);
				holder.textView = (TextView) convertView.findViewById(R.id.tv_name);
				holder.badgeDeleteView = (ImageView) convertView.findViewById(R.id.badge_delete);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			final LinearLayout button = (LinearLayout) convertView.findViewById(R.id.button_avatar);
			// 最后一个item，减人按钮
            /*Log.i("eeeeee" , "getCount==" + getCount());
			Log.i("eeeeee" , "position==" + position);*/
			if (position == getCount() - 1) {
				Log.i("eeeeee" , "设置减号");
				holder.textView.setText("");
				// 设置成删除按钮
				holder.imageView.setImageResource(R.drawable.em_smiley_minus_btn);
				// 如果不是创建者或者没有相应权限，不提供加减人按钮
				// 显示删除按钮
				if (isInDeleteMode) {
					// 正处于删除模式下，隐藏删除按钮
					convertView.setVisibility(View.INVISIBLE);
				} else {
					// 正常模式
					convertView.setVisibility(View.VISIBLE);
					convertView.findViewById(R.id.badge_delete).setVisibility(View.INVISIBLE);
				}
				final String st10 = getResources().getString(R.string.The_delete_button_is_clicked);
				button.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						isInDeleteMode = true;
						notifyDataSetChanged();
					}
				});

			} else if (position == getCount() - 2) { // 添加群组成员按钮
				Log.i("eeeeee" , "设置加号");
				holder.textView.setText("");
				/*Glide.with(NewGroupActivity.this).load(R.drawable.em_smiley_add_btn)
						.into(holder.imageView);*/
				holder.imageView.setImageResource(R.drawable.em_smiley_add_btn);
//				button.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.smiley_add_btn, 0, 0);
				// 如果不是创建者或者没有相应权限

				// 正处于删除模式下,隐藏添加按钮
				if (isInDeleteMode) {
					convertView.setVisibility(View.INVISIBLE);
				} else {
					convertView.setVisibility(View.VISIBLE);
					convertView.findViewById(R.id.badge_delete).setVisibility(View.INVISIBLE);
				}
				final String st11 = getResources().getString(R.string.Add_a_button_was_clicked);
				button.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// 进入选人页面
						Intent intent = new Intent(NewGroupActivity.this, GroupPickContactsActivity.class);
						intent.putExtra("groupName" , mGroupName.getText().toString());
						intent.putExtra("type" , false);
						intent.putExtra("members" , memberList);
						startActivityForResult(intent , 0);
					}
				});

			} else { // 普通item，显示群组成员
				EaseUser easeUser = objects.get(position);
				Log.i("eeeeee" , "设置组成员");
				final String username = easeUser.getExternalNickName();
				String headUrl = easeUser.getAvatar();
				convertView.setVisibility(View.VISIBLE);
				button.setVisibility(View.VISIBLE);
				holder.textView.setText(username);
				Glide.with(NewGroupActivity.this).load(headUrl)
						.error(R.drawable.circle_user_image).into(holder.imageView);
				//EaseUserUtils.setUserNick(username, holder.textView);
				//EaseUserUtils.setUserAvatar(getContext(), headUrl, holder.imageView);

				if (isInDeleteMode) {
					// 如果是删除模式下，显示减人图标
					convertView.findViewById(R.id.badge_delete).setVisibility(View.VISIBLE);
				} else {
					convertView.findViewById(R.id.badge_delete).setVisibility(View.INVISIBLE);
				}
				final String st12 = getResources().getString(R.string.not_delete_myself);
				final String st13 = getResources().getString(R.string.Are_removed);
				final String st14 = getResources().getString(R.string.Delete_failed);
				final String st15 = getResources().getString(R.string.confirm_the_members);
				button.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (isInDeleteMode) {
							// 如果是删除自己，return
							if (EMClient.getInstance().getCurrentUser().equals(username)) {
								new EaseAlertDialog(NewGroupActivity.this, st12).show();
								return;
							}
							if (!NetUtils.hasNetwork(getApplicationContext())) {
								Toast.makeText(getApplicationContext(), getString(R.string.network_unavailable), Toast.LENGTH_SHORT).show();
								return;
							}
//							EMLog.d("group", "remove user from group:" + bean.ud_nickname);
							deleteMembersFromGroup(username);
						} else {
							// 正常情况下点击user，可以进入用户详情或者聊天页面等等
							// startActivity(new
							// Intent(GroupDetailsActivity.this,
							// ChatActivity.class).putExtra("userId",
							// user.getUsername()));

						}
					}

					/**
					 * 删除群成员
					 *
					 * @param username
					 */
					protected void deleteMembersFromGroup(final String username) {
						final ProgressDialog deleteDialog = new ProgressDialog(NewGroupActivity.this);
						deleteDialog.setMessage(st13);
						deleteDialog.setCanceledOnTouchOutside(false);
						deleteDialog.show();
						new Thread(new Runnable() {

							@Override
							public void run() {
								try {
									// 删除被选中的成员
//									EMClient.getInstance().groupManager().removeUserFromGroup(groupId, username);
									isInDeleteMode = false;
									runOnUiThread(new Runnable() {

										@Override
										public void run() {
											for (int i = 0 ; i < objects.size() ; i++){
												String s = objects.get(i).getExternalNickName();
												if (s.equals(username)){
													objects.remove(i);
													break;
												}
											}

											notifyDataSetChanged();
											deleteDialog.dismiss();
											//减少成员操作
											//refreshMembers();
//											((TextView) findViewById(R.id.group_name)).setText(group.getGroupName() + "(" + group.getAffiliationsCount() + st);
										}
									});
								} catch (final Exception e) {
									deleteDialog.dismiss();
									runOnUiThread(new Runnable() {
										public void run() {
											Toast.makeText(getApplicationContext(), st14 + e.getMessage(), Toast.LENGTH_LONG).show();
										}
									});
								}

							}
						}).start();
					}
				});

			}
			return convertView;
		}

		@Override
		public int getCount() {
			return super.getCount() + 2;
		}
	}

	private View contentView;
	private PopupWindow popupWindow;
	private void initPopWindow() {

		contentView = LayoutInflater.from(NewGroupActivity.this).inflate(
				R.layout.activity_pic_pop, null);
		//设置弹出框的宽度和高度
		popupWindow = new PopupWindow(contentView,
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		popupWindow.setOnDismissListener(this);
		ColorDrawable dw = new ColorDrawable(55000000);
		popupWindow.setBackgroundDrawable(dw);
		popupWindow.setFocusable(true);// 取得焦点
		//进入退出的动画
		popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
		//注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的

		//点击外部消失
		popupWindow.setOutsideTouchable(true);
		//设置可以点击
		popupWindow.setTouchable(true);


	}

	private void getPicture(){
		backgroundAlpha(0.5f);
		popupWindow.showAtLocation(contentView, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);

		contentView.findViewById(R.id.tv_picture_list).setOnClickListener(this);
		contentView.findViewById(R.id.tv_takepic).setOnClickListener(this);
	}

	/**
	 * 设置添加屏幕的背景透明度
	 * @param bgAlpha
	 */
	public void backgroundAlpha(float bgAlpha)
	{
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = bgAlpha; //0.0-1.0
		getWindow().setAttributes(lp);
	}

	@Override
	public void onDismiss() {
		backgroundAlpha(1f);
	}

	private static class ViewHolder{
		ImageView imageView;
		TextView textView;
		ImageView badgeDeleteView;
	}

	private FunctionOptions options;
	//private Intent picData;
	@Override
	public void onClick(View v) {

		if(options == null){
			// 可选择图片的数量
			// 是否打开剪切选项
			options = new FunctionOptions.Builder()
					.setType(FunctionConfig.TYPE_IMAGE) // 图片or视频 FunctionConfig.TYPE_IMAGE  TYPE_VIDEO
					.setSelectMode(FunctionConfig.MODE_SINGLE) // 可选择图片的数量
					.setEnableCrop(true) // 是否打开剪切选项
					.setShowCamera(false)
					.setCropMode(FunctionConfig.CROP_MODEL_1_1)
					.setCompress(true)
					.setCompressFlag(1)
					.create();
		}

		if (v.getId() == R.id.ll_back){
			finish();
		}else if (v.getId() == R.id.photo_item){
			//选择照片
			getPicture();

		}else if(v.getId() == R.id.name_item){
			//群名称
			Intent intent = new Intent(this,QunName.class);
			intent.putExtra("name",mGroupName.getText());
			startActivityForResult(intent,100);
		}else if(v.getId() == R.id.invite_item){
			//群介绍
			Intent intent = new Intent(this,QunInvite.class);
			intent.putExtra("invite",mInviteContent.getText());
			startActivityForResult(intent,101);

		}else if(v.getId() == R.id.hy_item){
			//行业
			if (cardItem.size() == 0 && professionList != null && professionList.size() != 0) {
				for (int i = 0; i < professionList.size(); i++) {

					cardItem.add(new CardBean(i, professionList.get(i).hy_name));

				}
			}

			pvCustomOptions.show(); //弹出自定义条件选择器

		}else if(v.getId() == R.id.addr_item){
			//地区
			Intent intent = new Intent(this,SelectCityActivity.class);
			startActivityForResult(intent,CITY_CODE);
		}else if(v.getId() == R.id.tv_save){
			//保存
			save();
		}else if(v.getId() == R.id.tv_picture_list){
			//选择图片
			PictureConfig.getPictureConfig().init(options).openPhoto(this, resultCallback);
			popupWindow.dismiss();
		}else if(v.getId() == R.id.tv_takepic){
			//选择拍摄
			PictureConfig.getPictureConfig().init(options).startOpenCamera(this, resultCallback);
			popupWindow.dismiss();
		}
	}


	//处理照片或者拍摄的回调
	private String phtotoPath;
	private UpdataImageUtils updataImageUtils;
	private String photoId = "";

	private PictureConfig.OnSelectResultCallback resultCallback = new PictureConfig.OnSelectResultCallback() {
		@Override
		public void onSelectSuccess(List<LocalMedia> resultList) {
			Log.i("callBack_result", resultList.size() + "");
			LocalMedia media = resultList.get(0);
			if (media.isCut() && media.isCompressed()) {
				// 裁剪过
				phtotoPath = media.getCompressPath();
				if (updataImageUtils == null){
					updataImageUtils = new UpdataImageUtils();
				}
				updataImageUtils.upDataPic(phtotoPath, mDialog , new UpdataImageUtils.UpdataPicListener() {
					//上传头像的回调
					@Override
					public void onResponse(String response) {
						UpdataBean updataBean = gson.fromJson(response, UpdataBean.class);
						if (updataBean == null){
							Toast.makeText(NewGroupActivity.this,"获取图片资源失败！请检查网络",Toast.LENGTH_SHORT).show();
							return;
						}

						if (!TextUtils.isEmpty(phtotoPath)) {
							civPhoto.setImageBitmap(BitmapUtils.getBitmap(phtotoPath));
						}

						if (updataBean != null&& updataBean.file_ids != null) {
							photoId = updataBean.file_ids.get(0);

						}
					}
				});
			}
		}
	};

//	/**
//	 * @param v
//	 */
//	public void save(View v) {
//		//保存按钮
//		String name = mGroupName.getText().toString();
//		if (TextUtils.isEmpty(name)) {
//			new EaseAlertDialog(this, R.string.Group_name_cannot_be_empty).show();
//		} else {
//			// select from contact list
//			startActivityForResult(new Intent(this, GroupPickContactsActivity.class).putExtra("groupName", name), 0);
//		}
//	}

	private Gson gson = new Gson();
	private void initHangYe() {
		VipInfoBean  vipInfoBean = SpTools.getUser(this);
		if (vipInfoBean != null) {
			if (InitInfo.professionBean == null || InitInfo.professionBean.hy == null|| InitInfo.professionBean.hy.size() == 0) {
				LoginInternetRequest.getProfession(SpTools.getUserId(this), new LoginInternetRequest.ForResultListener() {
					@Override
					public void onResponseMessage(String code) {
						if (!TextUtils.isEmpty(code)) {
							ProfessionBean professionBean = gson.fromJson(code, ProfessionBean.class);
							if (professionBean != null) {
								professionList.addAll(professionBean.hy);

							}
						}else {
							CommonUtils.toastMessage("获取岗位失败！");
						}
					}
				});
			}else {
				professionList.addAll(InitInfo.professionBean.hy);
			}
		}
	}

	//自定义选项卡
	private OptionsPickerView pvCustomOptions;
//	private HashMap<String, String> map = new HashMap<>();

	private void initCustomOptionPicker() {//条件选择器初始化，自定义布局
		/**
		 * @description
		 *
		 * 注意事项：
		 * 自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针。
		 * 具体可参考demo 里面的两个自定义layout布局。
		 */
		pvCustomOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
			@Override
			public void onOptionsSelect(int options1, int option2, int options3, View v) {
				//返回的分别是三个级别的选中位置
				String tx = cardItem.get(options1).getPickerViewText();

				//选中条目回显到当前条目
//				map.put("ud_profession",tx);
				mHyContent.setText(tx);
			}
		})
				.setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
					@Override
					public void customLayout(View v) {
						final TextView tvSure = (TextView) v.findViewById(R.id.tv_sure);
						final TextView tvCancel = (TextView) v.findViewById(R.id.tv_cancel);
						tvSure.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								pvCustomOptions.returnData();
							}
						});
						tvCancel.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								pvCustomOptions.dismiss();
							}
						});

					}
				})
				.isDialog(false)
				.build();

		pvCustomOptions.setPicker(cardItem);//添加数据

	}

	/**
	 *保存
	 */
	public void save(){
		loading.setVisibility(View.VISIBLE);
		if (mProtocal == null){
			mProtocal = new GetInfoProtocal();
		}
		if (TextUtils.isEmpty(mUserid)) {
			mUserid = SpTools.getUserId(this);
		}
		String name = mGroupName.getText().toString().trim();
		if (TextUtils.isEmpty(name)){
			CommonUtils.toastMessage("群名不能为空！");
			loading.setVisibility(View.GONE);
			return;
		}
		backgroundAlpha(0.5f);
		String qunMember = "";
		if (members != null && members.size() > 0){
			for (int i = 0; i < members.size(); i ++){
				if (i == 0 ){
					qunMember = qunMember + members.get(i).getNickname();
				}else {
					qunMember = qunMember  + "," + members.get(i).getNickname();
				}
			}
		}
		String invite = mInviteContent.getText().toString().trim();
		String hy = mHyContent.getText().toString().trim();
		String addr = mAddrContent.getText().toString().trim();
		String qunType = type;
		String photo = photoId;
		mProtocal.getCreateGrounp(mUserid, name, invite, qunType, qunMember, photo, hy, addr, new AddCommandProtocal.NormalListener() {
			@Override
			public void normalResponse(Object response) {
				if (response == null){
					CommonUtils.toastMessage("创建失败");
					loading.setVisibility(View.GONE);
					backgroundAlpha(1f);
					return;
				}


				Result result = gson.fromJson((String) response,Result.class);
				if ("10".equals(result.result.code)) {
					//如果创建成功
					CommonUtils.toastMessage(result.result.info);
					loading.setVisibility(View.GONE);
					backgroundAlpha(1f);
					finish();
				}else {
					CommonUtils.toastMessage(result.result.info);
					loading.setVisibility(View.GONE);
					backgroundAlpha(1f);
				}

			}
		});

	}

	/**
	 * 销毁得时候
	 */
	@Override
	protected void onDestroy() {
		memberList.clear();
		members.clear();
		super.onDestroy();
	}
}
