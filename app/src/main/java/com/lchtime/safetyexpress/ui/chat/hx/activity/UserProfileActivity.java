package com.lchtime.safetyexpress.ui.chat.hx.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.EaseUser;
import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.InitInfo;
import com.lchtime.safetyexpress.bean.Result;
import com.lchtime.safetyexpress.pop.VipInfoHintPop;
import com.lchtime.safetyexpress.ui.chat.hx.Constant;
import com.lchtime.safetyexpress.ui.chat.hx.activity.protocal.GetInfoProtocal;
import com.lchtime.safetyexpress.ui.chat.hx.bean.ProfileInfoBean;
import com.lchtime.safetyexpress.ui.chat.hx.bean.SingleDetailBean;
import com.lchtime.safetyexpress.ui.chat.hx.db.TopUserDao;
import com.lchtime.safetyexpress.ui.chat.hx.fragment.protocal.AddCommandProtocal;
import com.lchtime.safetyexpress.ui.chat.hx.utils.WindowUtils;
import com.lchtime.safetyexpress.ui.circle.SingleInfoUI;
import com.lchtime.safetyexpress.ui.vip.VipInfoUI;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.SpTools;

import java.util.Map;

public class UserProfileActivity extends BaseActivity implements OnClickListener{
	
	private static final int REQUESTCODE_PICK = 1;
	private static final int REQUESTCODE_CUTTING = 2;
	private ImageView headAvatar;
	private ImageView headPhotoUpdate;
	private TextView tvUsername;
	private ProgressDialog dialog;

	private TextView mTitle;
	private ImageView mTitleRight;
	private LinearLayout mTitleLeft;
	private LinearLayout mLlTitleRight;

	private TextView tvHy;
	private TextView tvGw;
	private TextView tvAddr;
	private TextView tvCompany;
	private TextView tvSingleInfo;
	private TextView tvSex;
	private CheckBox cbChatUp ;
	private RelativeLayout rlClearChat ;
	private TextView tvSendMessage ;
	private TextView tvDeleteFriends;
	private String mUserid;
	private String mUsername;
	private ProgressBar mBar;
	private VipInfoHintPop popWindow;
	private String uid;
	private String phoneNumber;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.em_activity_user_profile);
		Intent intent = getIntent();
		mUsername = intent.getStringExtra("username");
		phoneNumber = SpTools.getHXID(this);

		topMap = MyApplication.getInstance().getTopUserList();
		initView();
		setLoadding(true);
		initListener();
		initData();
	}


	private void initView() {

		mBar = (ProgressBar) findViewById(R.id.pb_progress);
		popWindow = new VipInfoHintPop(mBar, this, R.layout.pop_delete_hint);
		headAvatar = (ImageView) findViewById(R.id.user_head_avatar);
		headPhotoUpdate = (ImageView) findViewById(R.id.user_head_headphoto_update);

		tvUsername = (TextView) findViewById(R.id.user_username);
		tvSex = (TextView) findViewById(R.id.tv_profile_sex);
		tvHy = (TextView) findViewById(R.id.tv_hy);
		tvGw = (TextView) findViewById(R.id.tv_gw);
		tvAddr = (TextView) findViewById(R.id.tv_addr);
		tvCompany = (TextView) findViewById(R.id.tv_company);
		tvSingleInfo = (TextView) findViewById(R.id.tv_singleinfo);
		cbChatUp = (CheckBox) findViewById(R.id.cb_chat_up);
		rlClearChat = (RelativeLayout) findViewById(R.id.rl_clear_chat);
		tvSendMessage = (TextView) findViewById(R.id.tv_send_message);
		tvDeleteFriends = (TextView) findViewById(R.id.tv_delete_friends);
//		tvNickName = (TextView) findViewById(R.id.user_nickname);
//		rlNickName = (RelativeLayout) findViewById(R.id.rl_nickname);
//		iconRightArrow = (ImageView) findViewById(R.id.ic_right_arrow);


		cbChatUp.setChecked(topMap.containsKey(mUsername));

		initTitle();


	}

	private void initTitle() {
		mTitle = (TextView) findViewById(R.id.title);
		mTitleRight = (ImageView) findViewById(R.id.iv_right);
		mTitleLeft = (LinearLayout) findViewById(R.id.ll_back);
		mLlTitleRight = (LinearLayout) findViewById(R.id.ll_right);
		mLlTitleRight.setVisibility(View.GONE);

		mTitleRight.setVisibility(View.VISIBLE);
		mTitleRight.setImageResource(R.drawable.single_info_more);
		mTitle.setText("个人资料");
		mLlTitleRight.setOnClickListener(this);
		mTitleLeft.setOnClickListener(this);
	}

	private void initListener() {

		headPhotoUpdate.setVisibility(View.GONE);

		cbChatUp.setOnClickListener(this);
		rlClearChat.setOnClickListener(this);
		tvSendMessage.setOnClickListener(this);
		tvDeleteFriends.setOnClickListener(this);
		headAvatar.setOnClickListener(this);
	}

	private Map<String, EaseUser> topMap;
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.user_head_avatar : //头像跳转个人主页


				Intent inti = new Intent(this, SingleInfoUI.class);
				inti.putExtra("uid", uid);
				//Log.i("qaz", "onClick: "+SpTools.getString(context, Constants.userId, ""));
				this.startActivity(inti);
				break;
		case R.id.ll_back:
			finish();
			break;
		case R.id.cb_chat_up:
//			CommonUtils.toastMessage("聊天置顶");
			setUpConversation();


			break;
		case R.id.rl_clear_chat:
//			CommonUtils.toastMessage("清除数据");
			emptyHistory();
			break;
		case R.id.tv_send_message:

			if (TextUtils.isEmpty(mUsername) && type == 5){
				return;
			}

			if (type == 0){
				//不是好友  执行添加好友动作
				Intent intent = new Intent(this, ApplyMessage.class);
				intent.putExtra("type", "1");
				intent.putExtra("master",mUsername);
				//这个请求码是返回是否finish当前界面的
				startActivityForResult(intent, 102);
			}else if(type == 1) {
				//是好友 进入界面与其聊天
				Intent intent = new Intent(this,ChatActivity.class);
				intent.putExtra("userId",mUsername);
				startActivity(intent);
			}else if (type == 2){
				//自己
				Intent intent = new Intent(this, VipInfoUI.class);
				startActivity(intent);
			}
//			CommonUtils.toastMessage("发送消息");
			break;
		case R.id.tv_delete_friends:
//			CommonUtils.toastMessage("删除好友");
			popWindow.setPerfect("删除");
			popWindow.setJump("取消");
			popWindow.setContent("确定删除好友？");
			popWindow.showAtLocation();
			popWindow.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					popWindow.dismiss();
					deleteFriend();
				}
			});
//			deleteContact(DemoHelper.getInstance().getUserInfo(mUsername));
			break;
		default:
			break;
		}


	}

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

		if (topMap.containsKey(mUsername)){
//			删除置顶
			mProtocal.getUp(mUserid, "2", mUsername, new AddCommandProtocal.NormalListener() {
				@Override
				public void normalResponse(Object response) {
					if (response == null){
						CommonUtils.toastMessage("取消置顶失败，请稍后再试");
						cbChatUp.setChecked(true);
						setLoadding(false);
						return;
					}

					Result result = gson.fromJson((String) response,Result.class);
					if ("10".equals(result.result.code)){
						topMap.remove(mUsername);
						TopUserDao dao = new TopUserDao(UserProfileActivity.this);
						dao.deleteContact(mUsername);
						CommonUtils.toastMessage("取消置顶成功");
					}else {
						CommonUtils.toastMessage(result.result.info);
						cbChatUp.setChecked(true);
					}

					setLoadding(false);

				}
			});

        }else {

            //1 存入  2 删除
            mProtocal.getUp(mUserid, "1", mUsername, new AddCommandProtocal.NormalListener() {
                @Override
                public void normalResponse(Object response) {
					if (response == null) {
						CommonUtils.toastMessage("置顶失败，请稍后再试");
						cbChatUp.setChecked(false);
						setLoadding(false);
						return;
					}

					Result result = gson.fromJson((String) response,Result.class);
					if ("10".equals(result.result.code)){
						EaseUser user = new EaseUser(mUsername);
						topMap.put(mUsername,user);
						TopUserDao dao = new TopUserDao(UserProfileActivity.this);
						dao.saveContact(user);
						CommonUtils.toastMessage("置顶成功");
					}else {
						CommonUtils.toastMessage(result.result.info);
						cbChatUp.setChecked(false);
					}
					setLoadding(false);

                }
            });

        }
	}

	//删除好友的方法
	private void deleteFriend() {
		setLoadding(true);
		if (mProtocal == null){
			mProtocal = new GetInfoProtocal();
		}

		mProtocal.getDeleteFriends(phoneNumber, mUsername, new AddCommandProtocal.NormalListener() {
			@Override
			public void normalResponse(Object response) {
				if (response == null){
					CommonUtils.toastMessage("解除好友关系失败，请重新尝试");
					setLoadding(false);
					return;
				}
				Result result = gson.fromJson((String) response,Result.class);
				if ("10".equals(result.result.code)){
					CommonUtils.toastMessage(result.result.info);
					//清除聊天记录
					EMClient.getInstance().chatManager().deleteConversation(mUsername, true);

					Intent intent = new Intent();
//					EaseUser user = DemoHelper.getInstance().getUserInfo(mUsername);
					intent.putExtra("tobeDeleteUser",mUsername);
					setResult(101,intent);
					setLoadding(false);
					finish();
				}else {
					CommonUtils.toastMessage(result.result.info);
					setLoadding(false);
				}
			}
		});
	}




	private GetInfoProtocal mProtocal;
	private Gson gson = new Gson();
	private int type = 0;//0 不是好友  1  是好友  2  自己
	private void initData() {
		if (mProtocal == null){
			mProtocal = new GetInfoProtocal();
		}
		if (mUserid == null) {
			mUserid = SpTools.getUserId(this);
		}
		mProtocal.getInfo(mUserid, "1", mUsername, new AddCommandProtocal.NormalListener() {
			@Override
			public void normalResponse(Object response) {
				if (response == null){
					//CommonUtils.toastMessage("获取个人信息失败！");
					setLoadding(false);
					type = 5;
					return;
				}

				ProfileInfoBean profileInfoBean = gson.fromJson((String) response,ProfileInfoBean.class);
				if ("10".equals(profileInfoBean.result.code)){
					SingleDetailBean bean = profileInfoBean.user_detail.get(0);
					if (!TextUtils.isEmpty(bean.ud_photo_fileid)){
                        Glide.with(UserProfileActivity.this)
								.load(bean.ud_photo_fileid)
								.into(headAvatar);
					}else {
                        Glide.with(UserProfileActivity.this)
								.load(R.drawable.circle_user_image)
								.into(headAvatar);
					}
					uid = bean.ud_ub_id;
					tvUsername.setText(bean.ud_nickname);
					tvSex.setText(bean.ud_sex);
					tvHy.setText(bean.ud_profession);
					tvGw.setText(bean.ud_post);
					tvAddr.setText(bean.ud_addr);
					tvCompany.setText(bean.ud_company_name);
					tvSingleInfo.setText(bean.ud_memo);

					if ("1".equals(bean.is_friend)||TextUtils.isEmpty(bean.is_friend)){
						//如果是好友关系
						if (phoneNumber.equals(bean.ub_phone) ){
							//自己的个人资料
							type = 2;
							tvSendMessage.setText("编辑");
							tvDeleteFriends.setVisibility(View.GONE);
						}else {
							type = 1;
							tvSendMessage.setText("发消息");
							tvDeleteFriends.setVisibility(View.VISIBLE);
						}
					}else {
						type = 0;
						tvSendMessage.setText("申请好友");
						tvDeleteFriends.setVisibility(View.GONE);
					}

					setLoadding(false);
				}else {
					CommonUtils.toastMessage("获取个人信息失败!请重试！");
					setLoadding(false);
					type = 5;
				}
			}
		});




	}




	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUESTCODE_PICK:
			if (data == null || data.getData() == null) {
				return;
			}
//			startPhotoZoom(data.getData());
			break;
		case REQUESTCODE_CUTTING:
			if (data != null) {
//				setPicToView(data);
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}


	protected void emptyHistory() {
		popWindow.setPerfect("清空");
		popWindow.setJump("取消");
		popWindow.setContent("确定清空聊天记录？");

		popWindow.showAtLocation();
		popWindow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				popWindow.dismiss();
				EMClient.getInstance().chatManager().deleteConversation(mUsername, true);
			}
		});
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
