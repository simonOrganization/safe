package com.lchtime.safetyexpress.ui.chat.hx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.util.EasyUtils;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.ui.TabUI;
import com.lchtime.safetyexpress.ui.chat.hx.fragment.ChatFragment;
import com.lchtime.safetyexpress.ui.chat.hx.permission.PermissionsManager;


/**
 * chat activity，EaseChatFragment was used {@link #}
 *聊天界面
 */
public class ChatActivity extends BaseActivity implements View.OnClickListener {
    public static ChatActivity activityInstance;
    private EaseChatFragment chatFragment;
    String toChatUsername;
    private int chatType;
    private TextView mTitle;
    private ImageView mTitleRight;
    private LinearLayout mTitleLeft;
    private LinearLayout mLlTitleRight;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.em_activity_chat);

        activityInstance = this;
        //get user id or group id
        toChatUsername = getIntent().getExtras().getString("userId");
        chatType = getIntent().getExtras().getInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        initTitle(toChatUsername);
        //use EaseChatFratFragment
        chatFragment = new ChatFragment();
        //pass parameters to chat fragment
        chatFragment.setArguments(getIntent().getExtras());

        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();

    }

    private void initTitle(String title) {
        mTitle = (TextView) findViewById(R.id.title);
        mTitleRight = (ImageView) findViewById(R.id.iv_right);
        mTitleLeft = (LinearLayout) findViewById(R.id.ll_back);
        mLlTitleRight = (LinearLayout) findViewById(R.id.ll_right);
        mLlTitleRight.setVisibility(View.VISIBLE);

        mTitleRight.setVisibility(View.VISIBLE);
        mTitleRight.setImageResource(R.drawable.single_info_more);
        mLlTitleRight.setOnClickListener(this);
        mTitleLeft.setOnClickListener(this);

        mTitle.setText(title);
        if (chatType == EaseConstant.CHATTYPE_SINGLE) {
            // set title
            if(EaseUserUtils.getUserInfo(toChatUsername) != null){
                EaseUser user = EaseUserUtils.getUserInfo(toChatUsername);
                if (user != null) {
                    mTitle.setText(user.getExternalNickName());
                }
            }
        } else {
            //群聊情况
            if (chatType == EaseConstant.CHATTYPE_GROUP) {
                //group chat
                EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
                if (group != null)
                    mTitle.setText(group.getGroupName());
                // listen the event that user moved out group or group is dismissed
            }

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
    	// make sure only one chat activity is opened
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }
    
    @Override
    public void onBackPressed() {
        chatFragment.onBackPressed();
        if (EasyUtils.isSingleActivity(this)) {
            Intent intent = new Intent(this, TabUI.class);
            startActivity(intent);
        }
    }
    
    public String getToChatUsername(){
        return toChatUsername;
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_right){
            //查看详细资料
            chatFragment.goDetail();

        }else if(v.getId() == R.id.ll_back){
            finish();
        }
    }

    public void setTitle(String title){
        mTitle.setText(title);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ChatFragment.REQUEST_CODE_GROUP_DETAIL && resultCode == RESULT_OK){
            finish();
        }

    }
}
