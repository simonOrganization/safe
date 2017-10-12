package com.hyphenate.easeui.widget.chatrow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.Direct;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.adapter.EaseMessageAdapter;
import com.hyphenate.easeui.bean.ContactBean;
import com.hyphenate.easeui.bean.EaseConstants;
import com.hyphenate.easeui.bean.EaseInitBean;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.easeui.utils.SPUtils;
import com.hyphenate.easeui.widget.EaseChatMessageList;
import com.hyphenate.easeui.widget.EaseChatMessageList.MessageListItemClickListener;
import com.hyphenate.util.DateUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class EaseChatRow extends LinearLayout {
    protected static final String TAG = EaseChatRow.class.getSimpleName();

    protected LayoutInflater inflater;
    protected Context context;
    protected BaseAdapter adapter;
    protected EMMessage message;
    protected int position;

    protected TextView timeStampView;
    protected ImageView userAvatarView;
    protected View bubbleLayout;
    protected TextView usernickView;

    protected TextView percentageView;
    protected ProgressBar progressBar;
    protected ImageView statusView;
    protected Activity activity;

    protected TextView ackedView;
    protected TextView deliveredView;

    protected EMCallBack messageSendCallback;
    protected EMCallBack messageReceiveCallback;

    protected MessageListItemClickListener itemClickListener;

    public EaseChatRow(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context);
        this.context = context;
        this.activity = (Activity) context;
        this.message = message;
        this.position = position;
        this.adapter = adapter;
        inflater = LayoutInflater.from(context);

        initView();
    }

    private void initView() {
        onInflateView();
        timeStampView = (TextView) findViewById(R.id.timestamp);
        userAvatarView = (ImageView) findViewById(R.id.iv_userhead);
        bubbleLayout = findViewById(R.id.bubble);
        usernickView = (TextView) findViewById(R.id.tv_userid);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        statusView = (ImageView) findViewById(R.id.msg_status);
        ackedView = (TextView) findViewById(R.id.tv_ack);
        deliveredView = (TextView) findViewById(R.id.tv_delivered);

        onFindViewById();
    }

    /**
     * set property according message and postion
     * 
     * @param message
     * @param position
     */
    public void setUpView(EMMessage message, int position,
            EaseChatMessageList.MessageListItemClickListener itemClickListener) {
        this.message = message;
        this.position = position;
        this.itemClickListener = itemClickListener;

        setUpBaseView();
        onSetUpView();
        setClickListener();
    }

    Handler handler = new Handler();
    private void setUpBaseView() {
    	// set nickname, avatar and background of bubble
        TextView timestamp = (TextView) findViewById(R.id.timestamp);
        if (timestamp != null) {
            if (position == 0) {
                timestamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
                timestamp.setVisibility(View.VISIBLE);
            } else {
            	// show time stamp if interval with last message is > 30 seconds
                EMMessage prevMessage = (EMMessage) adapter.getItem(position - 1);
                if (prevMessage != null && DateUtils.isCloseEnough(message.getMsgTime(), prevMessage.getMsgTime())) {
                    timestamp.setVisibility(View.GONE);
                } else {
                    timestamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
                    timestamp.setVisibility(View.VISIBLE);
                }
            }
        }


        if (EaseInitBean.map == null){
            Map<String,ContactBean> userInfo = new HashMap<>();
            for (ContactBean bean:EaseInitBean.contactBean.friendlist){
                userInfo.put(bean.hx_account,bean);
            }
            EaseInitBean.map = userInfo;
        }

        //set nickname and avatar
        if(message.direct() == Direct.SEND){
            /*String currentUser = EMClient.getInstance().getCurrentUser();
            EaseUserUtils.setUserAvatar(context, currentUser, userAvatarView);
            File file = new File(context.getFilesDir(), EaseConstants.photo_name);//将要保存图片的路径
            //如果没有加载过图片了
            if (!file.exists()){
                EaseUserUtils.setUserAvatar(context, currentUser, userAvatarView);
            }else {
                Glide.with(context).load(file).into(userAvatarView);
            }*/
            Glide.with(context).load(SPUtils.getUserHead(context)).placeholder(R.drawable.circle_user_image).error(R.drawable.circle_user_image).into(userAvatarView);
        }else{
            String user = message.getFrom();
            EaseUserUtils.setUserAvatar(context, user, userAvatarView);
            EaseUserUtils.setUserNick(user, usernickView);

            if (EaseInitBean.map != null && EaseInitBean.map.size() > position && EaseInitBean.map.get(user) != null){
                if (!TextUtils.isEmpty(EaseInitBean.map.get(user).ud_photo_fileid)) {
                    Glide.with(context)
                            .load(EaseInitBean.map.get(user).ud_photo_fileid)
                            .placeholder(R.drawable.circle_user_image)
                            .error(R.drawable.circle_user_image)
                            .into(userAvatarView);
                }

                usernickView.setText(EaseInitBean.map.get(user).ud_nickname);
            }



        }
        
        if(deliveredView != null){
            if (message.isDelivered()) {
                deliveredView.setVisibility(View.VISIBLE);
            } else {
                deliveredView.setVisibility(View.INVISIBLE);
            }
        }
        
        if(ackedView != null){
            if (message.isAcked()) {
                if (deliveredView != null) {
                    deliveredView.setVisibility(View.INVISIBLE);
                }
                ackedView.setVisibility(View.VISIBLE);
            } else {
                ackedView.setVisibility(View.INVISIBLE);
            }
        }
        

        if (adapter instanceof EaseMessageAdapter) {
            if (((EaseMessageAdapter) adapter).isShowAvatar())
                userAvatarView.setVisibility(View.VISIBLE);
            else
                userAvatarView.setVisibility(View.GONE);
            if (usernickView != null) {
                if (((EaseMessageAdapter) adapter).isShowUserNick())
                    usernickView.setVisibility(View.VISIBLE);
                else
                    usernickView.setVisibility(View.GONE);
            }
            if (message.direct() == Direct.SEND) {
                if (((EaseMessageAdapter) adapter).getMyBubbleBg() != null) {
                    bubbleLayout.setBackgroundDrawable(((EaseMessageAdapter) adapter).getMyBubbleBg());
                }
            } else if (message.direct() == Direct.RECEIVE) {
                if (((EaseMessageAdapter) adapter).getOtherBuddleBg() != null) {
                    bubbleLayout.setBackgroundDrawable(((EaseMessageAdapter) adapter).getOtherBuddleBg());
                }
            }
        }
    }


    public static void getUrlBitmap(final String url, final BitmapListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL pictureUrl = new URL(url);
                    InputStream in = pictureUrl.openStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(in);
                    in.close();
                    listener.giveBitmap(bitmap);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    /**
     * 保存图片到本地缓存
     * @param bitmap
     * @param str
     */
    public void saveBitmapFile(Bitmap bitmap , String str){

        File file = new File(context.getFilesDir(),str);//将要保存图片的路径
        Log.d("------------","file="+file.getName());
        try {
            if(file.exists()){
                file.delete();
            }
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public interface BitmapListener{
        void giveBitmap(Bitmap bitmap);
    }

    /**
     * set callback for sending message
     */
    protected void setMessageSendCallback(){
        if(messageSendCallback == null){
            messageSendCallback = new EMCallBack() {
                
                @Override
                public void onSuccess() {
                    updateView();
                }
                
                @Override
                public void onProgress(final int progress, String status) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(percentageView != null)
                                percentageView.setText(progress + "%");

                        }
                    });
                }
                
                @Override
                public void onError(int code, String error) {
                    updateView();
                }
            };
        }
        message.setMessageStatusCallback(messageSendCallback);
    }
    
    /**
     * set callback for receiving message
     */
    protected void setMessageReceiveCallback(){
        if(messageReceiveCallback == null){
            messageReceiveCallback = new EMCallBack() {
                
                @Override
                public void onSuccess() {
                    updateView();
                }
                
                @Override
                public void onProgress(final int progress, String status) {
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            if(percentageView != null){
                                percentageView.setText(progress + "%");
                            }
                        }
                    });
                }
                
                @Override
                public void onError(int code, String error) {
                    updateView();
                }
            };
        }
        message.setMessageStatusCallback(messageReceiveCallback);
    }
    
    
    private void setClickListener() {
        if(bubbleLayout != null){
            bubbleLayout.setOnClickListener(new OnClickListener() {
    
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null){
                        if(!itemClickListener.onBubbleClick(message)){
                        	// if listener return false, we call default handling
                            onBubbleClick();
                        }
                    }
                }
            });
    
            bubbleLayout.setOnLongClickListener(new OnLongClickListener() {
    
                @Override
                public boolean onLongClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onBubbleLongClick(message);
                    }
                    return true;
                }
            });
        }

        if (statusView != null) {
            statusView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onResendClick(message);
                    }
                }
            });
        }

        if(userAvatarView != null){
            //头像点击事件
            userAvatarView.setOnClickListener(new OnClickListener() {
    
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        if (message.direct() == Direct.SEND) {
                            itemClickListener.onUserAvatarClick(EMClient.getInstance().getCurrentUser());
                        } else {
                            itemClickListener.onUserAvatarClick(message.getFrom());
                        }
                    }
                }
            });
            userAvatarView.setOnLongClickListener(new OnLongClickListener() {
                
                @Override
                public boolean onLongClick(View v) {
                    if(itemClickListener != null){
                        if (message.direct() == Direct.SEND) {
                            itemClickListener.onUserAvatarLongClick(EMClient.getInstance().getCurrentUser());
                        } else {
                            itemClickListener.onUserAvatarLongClick(message.getFrom());
                        }
                        return true;
                    }
                    return false;
                }
            });
        }
    }


    protected void updateView() {
        activity.runOnUiThread(new Runnable() {
            public void run() {

                if (message.status() == EMMessage.Status.FAIL) {
//                    if (message.getError() == EMError.MESSAGE_INCLUDE_ILLEGAL_CONTENT) {
//                        Toast.makeText(activity,activity.getString(R.string.send_fail) + activity.getString(R.string.error_send_invalid_content), Toast.LENGTH_SHORT).show();
//                    } else if (message.getError() == EMError.GROUP_NOT_JOINED) {
//                        Toast.makeText(activity,activity.getString(R.string.send_fail) + activity.getString(R.string.error_send_not_in_the_group), Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(activity,activity.getString(R.string.send_fail) + activity.getString(R.string.connect_failuer_toast), Toast.LENGTH_SHORT).show();
//                    }
                }

                onUpdateView();
            }
        });

    }

    protected abstract void onInflateView();

    /**
     * find view by id
     */
    protected abstract void onFindViewById();

    /**
     * refresh list view when message status change
     */
    protected abstract void onUpdateView();

    /**
     * setup view
     * 
     */
    protected abstract void onSetUpView();
    
    /**
     * on bubble clicked
     */
    protected abstract void onBubbleClick();

}
