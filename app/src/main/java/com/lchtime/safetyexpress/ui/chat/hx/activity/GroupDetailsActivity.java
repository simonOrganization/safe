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
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMContact;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMConversation.EMConversationType;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.easeui.widget.EaseExpandGridView;
import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.InitInfo;
import com.lchtime.safetyexpress.bean.Result;
import com.lchtime.safetyexpress.bean.UpdataBean;
import com.lchtime.safetyexpress.pop.VipInfoHintPop;
import com.lchtime.safetyexpress.ui.chat.hx.activity.protocal.GetInfoProtocal;
import com.lchtime.safetyexpress.ui.chat.hx.bean.InfoBean;
import com.lchtime.safetyexpress.ui.chat.hx.db.TopUserDao;
import com.lchtime.safetyexpress.ui.chat.hx.fragment.protocal.AddCommandProtocal;
import com.lchtime.safetyexpress.ui.chat.hx.utils.CardUtils;
import com.lchtime.safetyexpress.ui.chat.hx.utils.WindowUtils;
import com.lchtime.safetyexpress.ui.vip.SelectCityActivity;
import com.lchtime.safetyexpress.utils.BitmapUtils;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.DialogUtil;
import com.lchtime.safetyexpress.utils.JsonUtils;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lchtime.safetyexpress.utils.UpdataImageUtils;
import com.lchtime.safetyexpress.weight.GlideCircleTransform;
import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;
import com.yalantis.ucrop.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.R.attr.button;
import static com.lchtime.safetyexpress.R.id.ll_more_member;
import static com.lchtime.safetyexpress.R.id.rl_change_group_invite;
import static com.lchtime.safetyexpress.ui.chat.hx.activity.NewGroupActivity.members;
import static java.lang.System.load;

public class GroupDetailsActivity extends BaseActivity implements OnClickListener, PopupWindow.OnDismissListener {
	private static final String TAG = "GroupDetailsActivity";
	private static final int REQUEST_CODE_ADD_USER = 0;
	private static final int REQUEST_CODE_DEL_USER = 10;
	private static final int REQUEST_CODE_EXIT = 1;
	private static final int REQUEST_CODE_EXIT_DELETE = 2;
	private static final int REQUEST_CODE_EDIT_GROUPNAME = 5;

	private static final int CITY_CODE = 588;


	private String groupId;
	private Button exitBtn;
	private GridAdapter adapter;

	public static GroupDetailsActivity instance;
	
	String st = "";


	private TextView mTitle;
	private ImageView mTitleRight;
	private LinearLayout mTitleLeft;
	private LinearLayout mLlTitleRight;

	private TextView qunName;
	private LinearLayout llMoreMember;
	private RelativeLayout invite;
	private RelativeLayout hy;
	private RelativeLayout addr;
	private RelativeLayout groupUp;
	private CheckBox chatUp;
	private int mType = 1;
	private GetInfoProtocal mProtocal;
	private TextView mInviteContent;
	private TextView mProfessionContent;
	private TextView mAddrContent;
	private TextView nullPic;
	private RelativeLayout mClearAllHistory;
	private EaseExpandGridView mUserGridview;
	private RelativeLayout mChangeGroupNameLayout;
	private InfoBean mBean;
	private ImageView mIvNameArrow;
	private ImageView mIvInviteArrow;
	private ImageView mIvHyArrow;
	private ImageView mIvAddrArrow;
	private String mUserid;
	private RelativeLayout mPhotoItem;
	private ImageView mCivPhotoArrow;
	private ImageView mCivPhoto;
	private List<InfoBean.QunersBean> mAdapterList = new ArrayList<>();
	private String ub_id;
	private ProgressBar mBar;
	private VipInfoHintPop popWindow;

	private DialogUtil mDialog;
	private String phoneNumber = "";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
        groupId = getIntent().getStringExtra("groupId");
		topMap = MyApplication.getInstance().getTopUserList();
		setContentView(R.layout.em_activity_group_details);
		ub_id = SpTools.getUserId(this);
		phoneNumber = SpTools.getHXID(this);
		if (mProtocal == null){
			mProtocal = new GetInfoProtocal();
		}
		instance = this;
		st = getResources().getString(R.string.people);
		mBar = (ProgressBar) findViewById(R.id.pb_progress);
		setLoadding(true);
		popWindow = new VipInfoHintPop(mBar, this, R.layout.pop_delete_hint);
		mClearAllHistory = (RelativeLayout) findViewById(R.id.clear_all_history);
		mUserGridview = (EaseExpandGridView) findViewById(R.id.gridview);
		nullPic = (TextView) findViewById(R.id.null_pic);
//		loadingPB = (ProgressBar) findViewById(R.id.progressBar);
		exitBtn = (Button) findViewById(R.id.btn_exit_grp);
		qunName = (TextView) findViewById(R.id.qun_name);
		llMoreMember = (LinearLayout) findViewById(ll_more_member);
		invite = (RelativeLayout) findViewById(rl_change_group_invite);
		mPhotoItem = (RelativeLayout) findViewById(R.id.photo_item);
		hy = (RelativeLayout) findViewById(R.id.rl_change_group_hy);
		addr = (RelativeLayout) findViewById(R.id.rl_change_group_addr);
		groupUp = (RelativeLayout) findViewById(R.id.rl_change_group_up);
		chatUp = (CheckBox) findViewById(R.id.cb_chat_up);

		chatUp.setChecked(topMap.containsKey(groupId));

		mChangeGroupNameLayout = (RelativeLayout) findViewById(R.id.rl_change_group_name);
		mInviteContent = (TextView) findViewById(R.id.invite_content);
		mProfessionContent = (TextView) findViewById(R.id.profession_content);
		mAddrContent = (TextView) findViewById(R.id.addr_content);
		mCivPhoto = (ImageView) findViewById(R.id.civ_photo);

		mIvNameArrow = (ImageView) findViewById(R.id.iv_arrow);
		mIvInviteArrow = (ImageView) findViewById(R.id.iv_group_name_arrow);
		mIvHyArrow = (ImageView) findViewById(R.id.iv_arrow_hy);
		mIvAddrArrow = (ImageView) findViewById(R.id.iv_arrow_addr);
		mCivPhotoArrow = (ImageView) findViewById(R.id.photo_arrow);

		mDialog = new DialogUtil(GroupDetailsActivity.this);
		//设置最下面按键的显示字


		initTitle();
		initData();

	}

	private void initData() {
		if (mProtocal == null){
			mProtocal = new GetInfoProtocal();
		}

		initPopWindow();
		String ub_id = SpTools.getUserId(this);
		mProtocal.getInfo(ub_id, "0", groupId, new AddCommandProtocal.NormalListener() {
			@Override
			public void normalResponse(Object response) {
				if (response == null){
					setLoadding(false);
					CommonUtils.toastMessage("获取群信息失败！");
					return;
				}
				mBean = (InfoBean) JsonUtils.stringToObject((String) response, InfoBean.class);
				if ("10".equals(mBean.result.code)){
					qunName.setText(mBean.qun.ud_nickname);
					if (mBean.quners != null){
						mTitle.setText(mBean.qun.ud_nickname + "(" + mBean.quners.size() + st );
					}else {
						mTitle.setText(mBean.qun.ud_nickname);
					}
					mInviteContent.setText(mBean.qun.sq_info);
					mProfessionContent.setText(mBean.qun.sq_profession);
					mAddrContent.setText(mBean.qun.sq_addr);

					Glide.with(GroupDetailsActivity.this)
							.load(mBean.qun.ud_photo_fileid)
							.placeholder(R.drawable.qun_list)
							.error(R.drawable.qun_list)
							.into(mCivPhoto);

					//如果不在群里面，在这里设置
					setType();
					initListener();
					setLoadding(false);
				}else {
					CommonUtils.toastMessage("获取群消息失败");
					setLoadding(false);
				}
			}
		});
	}



	private void initListener() {

		GroupChangeListener groupChangeListener = new GroupChangeListener();
		EMClient.getInstance().groupManager().addGroupChangeListener(groupChangeListener);

//		((TextView) findViewById(R.id.group_name)).setText(group.getGroupName() + "(" + group.getAffiliationsCount() + st);


//		List<String> members = new ArrayList<String>();
//		members.addAll(group.getMembers());
		if (mBean.quners != null) {
			mAdapterList.clear();
			mAdapterList.addAll(mBean.quners);
			adapter = new GridAdapter(this, R.layout.em_grid, mAdapterList);
			mUserGridview.setAdapter(adapter);
		}

		// 保证每次进详情看到的都是最新的group
//		updateGroup();


		mClearAllHistory.setOnClickListener(this);
//		blacklistLayout.setOnClickListener(this);
		mChangeGroupNameLayout.setOnClickListener(this);
//		rl_switch_block_groupmsg.setOnClickListener(this);
//        searchLayout.setOnClickListener(this);
		//更多成员点击事件
		llMoreMember.setOnClickListener(this);
		invite.setOnClickListener(this);
		hy.setOnClickListener(this);
		addr.setOnClickListener(this);
		chatUp.setOnClickListener(this);
		exitBtn.setOnClickListener(this);
		mPhotoItem.setOnClickListener(this);
	}

	private void initTitle() {

		mTitle = (TextView) findViewById(R.id.title);
		mTitleRight = (ImageView) findViewById(R.id.iv_right);
		mTitleLeft = (LinearLayout) findViewById(R.id.ll_back);
		mLlTitleRight = (LinearLayout) findViewById(R.id.ll_right);
		mLlTitleRight.setVisibility(View.VISIBLE);

		mTitleRight.setVisibility(View.GONE);
		mTitleRight.setImageResource(R.drawable.single_info_more);


		mLlTitleRight.setOnClickListener(this);
		mTitleLeft.setOnClickListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		String st1 = getResources().getString(R.string.being_added);
		String st2 = getResources().getString(R.string.is_quit_the_group_chat);
		String st3 = getResources().getString(R.string.chatting_is_dissolution);
		String st4 = getResources().getString(R.string.are_empty_group_of_news);
		String st5 = getResources().getString(R.string.is_modify_the_group_name);
		final String st6 = getResources().getString(R.string.Modify_the_group_name_successful);
		final String st7 = getResources().getString(R.string.change_the_group_name_failed_please);
		if (resultCode == RESULT_OK) {
			ArrayList<EMContact> selectedMembers;
			//String[] newmembers = null;
			switch (requestCode) {
			case REQUEST_CODE_ADD_USER:
				// 添加群成员
				//newmembers = data.getStringArrayExtra("newmembers");
				selectedMembers = data.getParcelableArrayListExtra("newmembers");
				setLoadding(true);
				addMembersToGroup(selectedMembers);
				break;
			case REQUEST_CODE_DEL_USER: //删除群成员
				selectedMembers = data.getParcelableArrayListExtra("newmembers");
				//setLoadding(true);
				if(selectedMembers != null && selectedMembers.size() > 0){
					String sns_quners = "";
					for (int i = 0; i < selectedMembers.size() ; i++){
						if ( i == 0){
							//因为传输数据问题 这里将环信id设置给 Username 这个变量。取出来的时候 Username 实际上是环信id
							sns_quners = sns_quners + selectedMembers.get(i).getUsername();
						}else {
							sns_quners = sns_quners + "," + selectedMembers.get(i).getUsername();
						}
					}

					if (!TextUtils.isEmpty(sns_quners)) {
						deleteMembers(sns_quners);
					}
				}
				break;
			default:
				break;
			}
		}else if (requestCode == CITY_CODE){
			if (data != null) {
				String city = data.getStringExtra("city");
				mAddrContent.setText(city);
			}

		}

		if (requestCode == 100){
			//修改名称
			if (data != null){
				qunName.setText(data.getStringExtra("name"));
			}
		}else if (requestCode == 101){
			//修改简介
			if (data != null) {
				mInviteContent.setText(data.getStringExtra("invite"));
			}
		}else if (requestCode == 102){
			if (data != null){
				String finish = data.getStringExtra("finish");
				if ("1".equals(finish)){
					finish();
				}
			}
		}
	}


	/**
	 * 增加群成员
	 *
	 * @param members
	 */
	private void addMembersToGroup(ArrayList<EMContact> members) {
		if (mProtocal == null){
			mProtocal = new GetInfoProtocal();
		}
		if (TextUtils.isEmpty(ub_id)){
			CommonUtils.toastMessage("请重新登录后重新操作！");
			return;
		}
		/*mAdapterList.clear();
		mAdapterList.addAll(mBean.quners);*/
		String sns_qunser = "";
		for (int i = 0 ; i < members.size();i++){
			/*InfoBean.QunersBean bean = new InfoBean.QunersBean();
			EaseUser user = EaseUserUtils.getUserInfo(newmembers[i]);
			if(user != null && user.getNick() != null){
				bean.ud_nickname = user.getNick();
			}else{
				bean.ud_nickname = newmembers[i];
			}

			if(user != null && user.getAvatar() != null){

				bean.ud_photo_fileid = user.getAvatar();

			}else{
				bean.pic_resource = 0;
			}*/

//			mAdapterList.add(bean);
			if (i == 0){
				sns_qunser = sns_qunser + members.get(i).getNick();
			}else {
				sns_qunser = sns_qunser + "," +members.get(i).getNick();
			}
		}
		//adapter.notifyDataSetChanged();

		//执行网络添加群成员操作

		if (!TextUtils.isEmpty(sns_qunser)) {

			String message = "";
			mProtocal.getApply(phoneNumber, groupId, "0", message, sns_qunser, new AddCommandProtocal.NormalListener() {
				@Override
				public void normalResponse(Object response) {
					if (response == null){
						CommonUtils.toastMessage("添加群成员失败！");
						mAdapterList.clear();
						mAdapterList.addAll(mBean.quners);
						adapter.notifyDataSetChanged();
						setLoadding(false);
						return;
					}
					Result result = gson.fromJson((String) response, Result.class);
					if ("10".equals(result.result.code)){
						refreshMembers();

						CommonUtils.toastMessage(result.result.info);
					}else {
						CommonUtils.toastMessage(result.result.info);
						mAdapterList.clear();
						mAdapterList.addAll(mBean.quners);
						adapter.notifyDataSetChanged();
					}
					setLoadding(false);
				}
			});

		}else {

			setLoadding(false);
		}

	}



	private void refreshMembers(){
	    initData();
	}
	
	/**
	 * 点击退出群组按钮
	 * 
	 * @param view
	 */
	public void exitGroup(View view) {
		startActivityForResult(new Intent(this, ExitGroupDialog.class), REQUEST_CODE_EXIT);

	}

//	/**
//	 * 点击解散群组按钮
//	 *
//	 * @param view
//	 */
//	public void exitDeleteGroup(View view) {
//		startActivityForResult(new Intent(this, ExitGroupDialog.class).putExtra("deleteToast", getString(R.string.dissolution_group_hint)),
//				REQUEST_CODE_EXIT_DELETE);
//
//	}

	/**
	 * 清空群聊天记录
	 */
	private void clearGroupHistory() {

		EMConversation conversation = EMClient.getInstance().chatManager().getConversation(groupId, EMConversationType.GroupChat);
		if (conversation != null) {
			conversation.clearAllMessages();
		}

	}


	private FunctionOptions options;
	private Intent picData;
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
		switch (v.getId()) {
		case R.id.clear_all_history: // 清空聊天记录

			popWindow.setPerfect("删除");
			popWindow.setJump("取消");
			popWindow.setContent("确定清空此群的聊天记录吗？");
			popWindow.showAtLocation();
			popWindow.setOnClickListener(new View.OnClickListener() {
					 @Override
					 public void onClick(View v) {
						 popWindow.dismiss();
						 clearGroupHistory();
					 }
				 }
			);

			break;
		case R.id.rl_change_group_name://修改群名称
			String name = qunName.getText().toString().trim();
			if (TextUtils.isEmpty(name)){
				name = "";
			}
			Intent intent1 = new Intent(this,QunName.class);
			intent1.putExtra("name",name);
			startActivityForResult(intent1,100);
			break;
		case ll_more_member:
//			CommonUtils.toastMessage("查看更多群成员");
			Intent intent2 = new Intent(this,AllPeopleActivity.class);
			intent2.putExtra("groupId", groupId);
			startActivity(intent2);
			break;
		case rl_change_group_invite:
//			CommonUtils.toastMessage("群介绍");
			String invite = mInviteContent.getText().toString().trim();
			if (TextUtils.isEmpty(invite)){
				invite = "";
			}
			Intent intent3 = new Intent(this,QunInvite.class);
			intent3.putExtra("invite",invite);
			startActivityForResult(intent3,101);
			break;
		case R.id.rl_change_group_hy:
//			CommonUtils.toastMessage("行业");
			CardUtils.show(mProfessionContent,this);
			break;
		case R.id.rl_change_group_addr:
//			CommonUtils.toastMessage("公司地址");
			Intent intent = new Intent(this,SelectCityActivity.class);
			startActivityForResult(intent,CITY_CODE);
			break;
		case R.id.cb_chat_up:
//			CommonUtils.toastMessage("聊天置顶");
			setUpConversation();
			break;
		case R.id.tv_picture_list:
			//选择图片
			PictureConfig.getPictureConfig().init(options).openPhoto(this, resultCallback);
			popupWindow.dismiss();
			break;

		case R.id.tv_takepic:
			//选择拍摄
			PictureConfig.getPictureConfig().init(options).startOpenCamera(this, resultCallback);
			popupWindow.dismiss();
			break;
		case R.id.photo_item:
//			CommonUtils.toastMessage("更换群头像");
			//弹框 更换群头像
			getPicture();
			break;
		case R.id.btn_exit_grp: //
			if (mProtocal == null){
				mProtocal = new GetInfoProtocal();
			}

			if (TextUtils.isEmpty(mUserid)) {
				mUserid = SpTools.getUserId(this);
			}

			if (mType == 0) {
//				CommonUtils.toastMessage("申请加群");
				Intent aintent = new Intent(this,ApplyMessage.class);
				aintent.putExtra("type","0");
				aintent.putExtra("groupid",groupId);
				aintent.putExtra("master",mBean.qun.master);
				startActivityForResult(aintent,102);

			}else if (mType == 1){
//				CommonUtils.toastMessage("删除退群");
				deleteGroup();
			}else if(mType == 2){
//				CommonUtils.toastMessage("修改");
				upDataInfo();
			}
			break;
		case R.id.ll_back: // 后退键
			setResult(RESULT_OK);
			finish();
			break;
		default:
			break;
		}

	}

	private Map<String, EaseUser> topMap;
	private void setUpConversation() {
		setLoadding(true);
		if (mProtocal == null){
			mProtocal = new GetInfoProtocal();
		}
		if (mUserid == null) {
			mUserid = SpTools.getUserId(this);
		}
		if (TextUtils.isEmpty(mUserid)){
			CommonUtils.toastMessage("请重新登录后再尝试！");
			setLoadding(false);
			return;
		}

		if (topMap.containsKey(groupId)){
//			删除置顶
			mProtocal.getUp(mUserid, "2", groupId, new AddCommandProtocal.NormalListener() {
				@Override
				public void normalResponse(Object response) {
					if (response == null){
						CommonUtils.toastMessage("取消置顶失败，请稍后再试");
						chatUp.setChecked(true);
						setLoadding(false);
						return;
					}

					Result result = gson.fromJson((String) response,Result.class);
					if ("10".equals(result.result.code)){
						topMap.remove(groupId);
						TopUserDao dao = new TopUserDao(GroupDetailsActivity.this);
						dao.deleteContact(groupId);
						CommonUtils.toastMessage("取消置顶成功");
					}else {
						CommonUtils.toastMessage(result.result.info);
						chatUp.setChecked(true);
					}

					setLoadding(false);

				}
			});

		}else {

			//1 存入  2 删除
			mProtocal.getUp(mUserid, "1", groupId, new AddCommandProtocal.NormalListener() {
				@Override
				public void normalResponse(Object response) {
					if (response == null) {
						CommonUtils.toastMessage("置顶失败，请稍后再试");
						chatUp.setChecked(false);
						setLoadding(false);
						return;
					}

					Result result = gson.fromJson((String) response,Result.class);
					if ("10".equals(result.result.code)){
						EaseUser user = new EaseUser(groupId);
						topMap.put(groupId,user);
						TopUserDao dao = new TopUserDao(GroupDetailsActivity.this);
						dao.saveContact(user);
						CommonUtils.toastMessage("置顶成功");
					}else {
						CommonUtils.toastMessage(result.result.info);
						chatUp.setChecked(false);
					}
					setLoadding(false);

				}
			});

		}
	}

	/**
	 * 退群
	 */
	private void deleteGroup() {
		popWindow.setPerfect("删除");
		popWindow.setJump("取消");
		popWindow.setContent("确定退出群聊？");
		popWindow.showAtLocation();
		popWindow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				popWindow.dismiss();
				if (TextUtils.isEmpty(mUserid)){
					CommonUtils.toastMessage("账号出现故障");
					return;
				}
				mProtocal.getDeleteGrounp(mUserid, groupId, new AddCommandProtocal.NormalListener() {
					@Override
					public void normalResponse(Object response) {
						if (response == null){
							CommonUtils.toastMessage("退出群组失败！请稍后重试");
							return;
						}
						Result result = gson.fromJson((String) response, Result.class);
						if ("10".equals(result.result.code)){
							CommonUtils.toastMessage(result.result.info);
							//删除聊天记录
							clearGroupHistory();
							finish();
						}else {
							CommonUtils.toastMessage(result.result.info);
						}
					}
				});
			}
		});


	}

	//处理照片或者拍摄的回调
	private String phtotoPath;
	private UpdataImageUtils updataImageUtils;
	private String photoId = "";
	private Gson gson = new Gson();
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
				mDialog.show();
				updataImageUtils.upDataPic(phtotoPath, mDialog ,new UpdataImageUtils.UpdataPicListener() {
					//上传头像的回调e
					@Override
					public void onResponse(String response) {
						UpdataBean updataBean = gson.fromJson(response, UpdataBean.class);
						if (updataBean == null){
							Toast.makeText(GroupDetailsActivity.this,"获取图片资源失败！请检查网络",Toast.LENGTH_SHORT).show();
							return;
						}

						if (!TextUtils.isEmpty(phtotoPath)) {
							mCivPhoto.setImageBitmap(BitmapUtils.getBitmap(phtotoPath));
						}

						if (updataBean != null&& updataBean.file_ids != null) {
							photoId = updataBean.file_ids.get(0);

						}
					}
				});
			}
		}
	};

	private void upDataInfo() {
		if (TextUtils.isEmpty(mUserid)){
            CommonUtils.toastMessage("账号出现故障");
            return;
        }
		String qunNameContent = qunName.getText().toString().trim();
		String describe = mInviteContent.getText().toString().trim();
		String hy = mProfessionContent.getText().toString().trim();
		String addr = mAddrContent.getText().toString().trim();
		mProtocal.getUpdateGrounp(mUserid, qunNameContent, describe, photoId, hy, groupId,addr, new AddCommandProtocal.NormalListener() {
			@Override
			public void normalResponse(Object response) {
				if (response == null){
					CommonUtils.toastMessage("更改群消息失败！请稍后重试");
					return;
				}

				Result result = gson.fromJson((String) response, Result.class);

				if ("10".equals(result.result.code)){
					CommonUtils.toastMessage(result.result.info);
					finish();
				}else {
					CommonUtils.toastMessage(result.result.info);
				}
			}
		});
	}

	@Override
	public void onDismiss() {
		backgroundAlpha(1f);
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

	/**
	 * 群组成员gridadapter
	 * @author admin_new
	 */
	public boolean isInDeleteMode;
	private class GridAdapter extends ArrayAdapter<InfoBean.QunersBean> {

		private int res;

		private List<InfoBean.QunersBean> objects;
		//private ArrayList<String> members = new ArrayList<>();

		public GridAdapter(Context context, int textViewResourceId, List<InfoBean.QunersBean> objects) {
			super(context, textViewResourceId, objects);
			this.objects = objects;
			res = textViewResourceId;
			isInDeleteMode = false;
			/*for(InfoBean.QunersBean bean : objects){
				members.add(bean.hx_account);
			}*/
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
			if (phoneNumber.equals(mBean.qun.master)) {  //如果是群主
				// 最后一个item，减人按钮
				if (position == getCount() - 1) {
					holder.textView.setText("");
					// 设置成删除按钮
					holder.imageView.setImageResource(R.drawable.em_smiley_minus_btn);
					// 如果不是创建者或者没有相应权限，不提供加减人按钮
//				if (!phoneNumber.equals(mBean.qun.master)) {
//					// if current user is not group admin, hide add/remove btn
//					convertView.setVisibility(View.GONE);
//				} else { // 显示删除按钮
					button.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(GroupDetailsActivity.this, GroupPickDeleteContactsActivity.class);
							intent.putExtra("groupId", groupId);
							startActivityForResult(intent , REQUEST_CODE_DEL_USER);
						}
					});
					//}
				} else if (position == getCount() - 2) { // 添加群组成员按钮
					holder.textView.setText("");
					holder.imageView.setImageResource(R.drawable.em_smiley_add_btn);
					// 如果不是创建者或者没有相应权限
//				if (!phoneNumber.equals(mBean.qun.master)) {
//					// if current user is not group admin, hide add/remove btn
//					convertView.setVisibility(View.GONE);
//				} else {
					button.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// 进入选人页面
							Intent intent = new Intent(GroupDetailsActivity.this, GroupPickContactsActivity.class);
							intent.putExtra("groupId", groupId);
							intent.putExtra("type" , true);
							//intent.putExtra("members" , members);
							startActivityForResult(intent , REQUEST_CODE_ADD_USER);
						}
					});
					//}
				} else { // 普通item，显示群组成员
					final InfoBean.QunersBean bean = getItem(position);
					convertView.setVisibility(View.VISIBLE);
					button.setVisibility(View.VISIBLE);
					EaseUserUtils.setUserNick(bean.ud_nickname, holder.textView);
					String photoSrc = objects.get(position).ud_photo_fileid;
					int localPhotoSrc = objects.get(position).pic_resource;
					if (!TextUtils.isEmpty(photoSrc)){
						Glide.with(GroupDetailsActivity.this)
								.load(photoSrc)
								.bitmapTransform(new GlideCircleTransform(GroupDetailsActivity.this , 8))
								.into(holder.imageView);
					}else if(localPhotoSrc != 0){
						//如果有本地资源，那么就设置本地资源
						Glide.with(GroupDetailsActivity.this).load(localPhotoSrc).into(holder.imageView);
					}else {
						Glide.with(GroupDetailsActivity.this).load(R.drawable.circle_user_image).into(holder.imageView);
					}

//				final String st12 = getResources().getString(R.string.not_delete_myself);
//				final String st13 = getResources().getString(R.string.Are_removed);
//				final String st14 = getResources().getString(R.string.Delete_failed);
					button.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(GroupDetailsActivity.this,UserProfileActivity.class);
							intent.putExtra("username",objects.get(position).hx_account);
							startActivity(intent);

						}


					});

				}
			}else{// 如果并不是群主
				final InfoBean.QunersBean bean = getItem(position);
				convertView.setVisibility(View.VISIBLE);
				button.setVisibility(View.VISIBLE);
				EaseUserUtils.setUserNick(bean.ud_nickname, holder.textView);
				String photoSrc = objects.get(position).ud_photo_fileid;
				int localPhotoSrc = objects.get(position).pic_resource;
				if (!TextUtils.isEmpty(photoSrc)){
					Glide.with(GroupDetailsActivity.this)
							.load(photoSrc)
							.bitmapTransform(new GlideCircleTransform(GroupDetailsActivity.this , 8))
							.into(holder.imageView);
				}else if(localPhotoSrc != 0){
					//如果有本地资源，那么就设置本地资源
					Glide.with(GroupDetailsActivity.this).load(localPhotoSrc).into(holder.imageView);
				}else {
					Glide.with(GroupDetailsActivity.this).load(R.drawable.circle_user_image).into(holder.imageView);
				}

//				final String st12 = getResources().getString(R.string.not_delete_myself);
//				final String st13 = getResources().getString(R.string.Are_removed);
//				final String st14 = getResources().getString(R.string.Delete_failed);
				button.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(GroupDetailsActivity.this,UserProfileActivity.class);
						intent.putExtra("username",objects.get(position).hx_account);
						startActivity(intent);

					}


				});

			}

			return convertView;
		}

		@Override
		public int getCount() {
			if(!phoneNumber.equals(mBean.qun.master)){
				return super.getCount();
			}
			return super.getCount() + 2;
		}
	}

	/**
	 * 删除群成员
	 *
	 * @param sns_quners
	 */

	private void deleteMembers(final String sns_quners) {
		final String st13 = getResources().getString(R.string.Are_removed);
		if (!TextUtils.isEmpty(ub_id)) {
			setLoadding(true);
			mProtocal.getDelMember(ub_id, groupId, sns_quners, new AddCommandProtocal.NormalListener() {
				@Override
				public void normalResponse(Object response) {
					if (response == null) {
						CommonUtils.toastMessage("删除群成员失败！请重试");
						isInDeleteMode = false;
						setLoadding(false);
						return;
					}
					Result result = gson.fromJson((String) response, Result.class);
					if ("10".equals(result.result.code)) {
						isInDeleteMode = false;
						mTitle.setText(mBean.qun.ud_nickname + "(" + mAdapterList.size() + st);
						CommonUtils.toastMessage(result.result.info);
						refreshMembers();
						setLoadding(false);
					} else {
						isInDeleteMode = false;
						CommonUtils.toastMessage(result.result.info);
						setLoadding(false);
					}
				}
			});
		}
	}

	@Override
	public void onBackPressed() {
		setResult(RESULT_OK);
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		instance = null;
	}
	
	private static class ViewHolder{
	    ImageView imageView;
	    TextView textView;
	    ImageView badgeDeleteView;
	}

	public void setItemClickable(boolean click){
		if (click){
			mIvNameArrow.setVisibility(View.VISIBLE);
			mIvInviteArrow.setVisibility(View.VISIBLE);
			mIvHyArrow.setVisibility(View.VISIBLE);
			mIvAddrArrow.setVisibility(View.VISIBLE);
			mCivPhotoArrow.setVisibility(View.VISIBLE);
			mChangeGroupNameLayout.setEnabled(true);
			invite.setEnabled(true);
			hy.setEnabled(true);
			addr.setEnabled(true);
			mPhotoItem.setEnabled(true);
		}else {
			mIvNameArrow.setVisibility(View.INVISIBLE);
			mIvInviteArrow.setVisibility(View.INVISIBLE);
			mIvHyArrow.setVisibility(View.INVISIBLE);
			mIvAddrArrow.setVisibility(View.INVISIBLE);
			mCivPhotoArrow.setVisibility(View.INVISIBLE);
			mChangeGroupNameLayout.setEnabled(false);
			invite.setEnabled(false);
			hy.setEnabled(false);
			addr.setEnabled(false);
			mPhotoItem.setEnabled(false);
		}



	}

	/**
	 * 设置 删除 或者加群 按钮样式
	 */
	private void setType() {
		if ("0".equals(mBean.qun.inQun)) {
			mType = 0;
			exitBtn.setText("申请加群");
			groupUp.setVisibility(View.GONE);
			mClearAllHistory.setVisibility(View.GONE);
			setItemClickable(false);
			//群聊名称是否显示，注释掉代表都显示
//			changeGroupNameLayout.setVisibility(View.GONE);
		}
		//如果是群主
		if (phoneNumber.equals(mBean.qun.master)){
			mType = 2;
			exitBtn.setText("修改并保存");
			groupUp.setVisibility(View.VISIBLE);
			mClearAllHistory.setVisibility(View.VISIBLE);
			setItemClickable(true);
		}


		//如果在群里并且不是群主
		if ("1".equals(mBean.qun.inQun) && !phoneNumber.equals(mBean.qun.master)) {
			mType = 1;
			exitBtn.setText("删除并退群");
			groupUp.setVisibility(View.VISIBLE);
			mClearAllHistory.setVisibility(View.VISIBLE);
			setItemClickable(false);
		}

		if (mBean.quners != null){
			nullPic.setVisibility(View.GONE);
			mUserGridview.setVisibility(View.VISIBLE);
			llMoreMember.setVisibility(View.VISIBLE);
		}
	}


	private View contentView;
	private PopupWindow popupWindow;
	private void initPopWindow() {

		contentView = LayoutInflater.from(this).inflate(
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
		contentView.findViewById(R.id.tv_cancel).setOnClickListener(this);
	}


	private class GroupChangeListener implements EMGroupChangeListener{

		@Override
		public void onInvitationReceived(String s, String s1, String s2, String s3) {

		}

		@Override
		public void onRequestToJoinReceived(String s, String s1, String s2, String s3) {

		}

		@Override
		public void onRequestToJoinAccepted(String s, String s1, String s2) {

		}

		@Override
		public void onRequestToJoinDeclined(String s, String s1, String s2, String s3) {

		}

		@Override
		public void onInvitationAccepted(String s, String s1, String s2) {

		}

		@Override
		public void onInvitationDeclined(String s, String s1, String s2) {

		}

		@Override
		public void onUserRemoved(String s, String s1) {

		}

		@Override
		public void onGroupDestroyed(String s, String s1) {

		}

		@Override
		public void onAutoAcceptInvitationFromGroup(String s, String s1, String s2) {

		}

		@Override
		public void onMuteListAdded(String s, List<String> list, long l) {

		}

		@Override
		public void onMuteListRemoved(String s, List<String> list) {

		}

		@Override
		public void onAdminAdded(String s, String s1) {

		}

		@Override
		public void onAdminRemoved(String s, String s1) {

		}

		@Override
		public void onOwnerChanged(String s, String s1, String s2) {

		}
	}

	public void setLoadding(boolean isLoadding){
		if (isLoadding){
			WindowUtils.backgroundAlpha(this,0.5f);
			mBar.setVisibility(View.VISIBLE);
		}else {
			WindowUtils.backgroundAlpha(this,1f);
			mBar.setVisibility(View.GONE);
		}
	}


}
