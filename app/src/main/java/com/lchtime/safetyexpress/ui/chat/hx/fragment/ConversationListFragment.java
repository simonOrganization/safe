package com.lchtime.safetyexpress.ui.chat.hx.fragment;

import android.content.Intent;
import android.util.Pair;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMConversation.EMConversationType;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.bean.ContactBean;
import com.hyphenate.easeui.bean.ContactListBean;
import com.hyphenate.easeui.bean.EaseInitBean;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.EaseAtMessageHelper;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.easeui.widget.EaseConversationList.EaseConversationListHelper;
import com.hyphenate.util.NetUtils;
import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.InitInfo;
import com.lchtime.safetyexpress.bean.MyAccountBean;
import com.lchtime.safetyexpress.ui.TabUI;
import com.lchtime.safetyexpress.ui.chat.hx.Constant;
import com.lchtime.safetyexpress.ui.chat.hx.DemoHelper;
import com.lchtime.safetyexpress.ui.chat.hx.activity.ChatActivity;
import com.lchtime.safetyexpress.ui.chat.hx.activity.HXMainActivity;
import com.lchtime.safetyexpress.ui.chat.hx.db.InviteMessgeDao;
import com.lchtime.safetyexpress.ui.home.protocal.HomeQuestionProtocal;
import com.lchtime.safetyexpress.utils.CommonUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


public class ConversationListFragment extends MyConversationListFragment {

    private TextView errorText;
    private Map<String, EaseUser> topMap;


    @Override
    protected void initView() {
        super.initView();
        View errorView = (LinearLayout) View.inflate(getActivity(), R.layout.em_chat_neterror_item, null);
        errorItemContainer.addView(errorView);
        errorText = (TextView) errorView.findViewById(R.id.tv_connect_errormsg);



        hideTitleBar();
    }

    @Override
    public void onResume() {
        super.onResume();

        setUpView();
    }

    private HomeQuestionProtocal protocal = new HomeQuestionProtocal();
    private Gson gson = new Gson();
    @Override
    protected void setUpView() {
    //初始化本地资料  使其显示本地的相关信息

        if (EaseInitBean.contactBean == null){

            protocal.getMyFriends(new HomeQuestionProtocal.QuestionListener() {
                @Override
                public void questionResponse(Object response) {
                    if (response == null){
                        CommonUtils.toastMessage("请求好友数据失败，请稍后再试！");
                        return;
                    }
                    try {
                        ContactListBean bean = gson.fromJson((String) response, ContactListBean.class);
                        if ("10".equals(bean.result.code)){
                            if (bean.friendlist == null || bean.friendlist.size() == 0){
                                return;
                            }
                            EaseInitBean.contactBean= bean;
                            if (EaseInitBean.map == null) {
                                if (EaseInitBean.contactBean != null) {

                                    for (ContactBean contactBean : EaseInitBean.contactBean.friendlist) {
                                        userInfo.put(contactBean.hx_account, contactBean);
                                    }
                                }
                                EaseInitBean.map = userInfo;
                            }

                            ConversationListFragment.super.setUpView();
                        }else {
                            CommonUtils.toastMessage("请求好友数据失败，请稍后再试！");
                        }
                    }catch (Exception exception){
                        CommonUtils.toastMessage("请求好友数据失败，请稍后再试！");
                    }

                }
            });

        }

        if (EaseInitBean.map == null) {
            if (EaseInitBean.contactBean != null) {

                for (ContactBean contactBean : EaseInitBean.contactBean.friendlist) {
                    userInfo.put(contactBean.hx_account, contactBean);
                }
            }
            EaseInitBean.map = userInfo;
        }

        super.setUpView();
        // register context menu
        registerForContextMenu(conversationListView);
        conversationListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EMConversation conversation = conversationListView.getItem(position);
                String username = conversation.conversationId();
                if (username.equals(EMClient.getInstance().getCurrentUser()))
                    Toast.makeText(getActivity(), R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
                else {
                    // start chat acitivity
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    if(conversation.isGroup()){
                        if(conversation.getType() == EMConversationType.ChatRoom){
                            // it's group chat
                            intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_CHATROOM);
                        }else{
                            intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
                        }
                        
                    }
                    // it's single chat
                    intent.putExtra(Constant.EXTRA_USER_ID, username);
                    startActivity(intent);
                }
            }
        });

        super.setUpView();
        //end of red packet code
    }

    @Override
    protected void onConnectionDisconnected() {
        super.onConnectionDisconnected();
        if (NetUtils.hasNetwork(getActivity())){
         errorText.setText(R.string.can_not_connect_chat_server_connection);
        } else {
          errorText.setText(R.string.the_current_network);
        }
    }
    
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.em_delete_message, menu); 
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean deleteMessage = false;
        if (item.getItemId() == R.id.delete_message) {
            deleteMessage = true;
        } else if (item.getItemId() == R.id.delete_conversation) {
            deleteMessage = false;
        }
    	EMConversation tobeDeleteCons = conversationListView.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
        if (tobeDeleteCons == null) {
    	    return true;
    	}
        if(tobeDeleteCons.getType() == EMConversationType.GroupChat){
            EaseAtMessageHelper.get().removeAtMeGroup(tobeDeleteCons.conversationId());
        }
        try {
            // delete conversation
            EMClient.getInstance().chatManager().deleteConversation(tobeDeleteCons.conversationId(), deleteMessage);
            InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
            inviteMessgeDao.deleteMessage(tobeDeleteCons.conversationId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        refresh();

        // update unread count
        ((HXMainActivity) getActivity()).updateUnreadLabel();
        TabUI.getTabUI().updateUnreadLabel();
        return true;
    }


    /**
     * sort conversations according time stamp of last message
     *
     * @param conversationList
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {
                if (con1.first.equals(con2.first)) {
                    return 0;
                } else if (con2.first.longValue() > con1.first.longValue()) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

}
