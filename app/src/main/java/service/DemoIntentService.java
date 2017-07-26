package service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.Tag;
import com.igexin.sdk.message.FeedbackCmdMessage;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.igexin.sdk.message.SetTagCmdMessage;
import com.lchtime.safetyexpress.H5DetailUI;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.MessageBean;
import com.lchtime.safetyexpress.ui.Splash;
import com.lchtime.safetyexpress.ui.TabUI;
import com.lchtime.safetyexpress.ui.home.HomeQuewstionDetail;


/**
 * 继承 GTIntentService 接收来自个推的消息, 所有消息在线程中回调, 如果注册了该服务, 则务必要在 AndroidManifest中声明, 否则无法接受消息<br>
 * onReceiveMessageData 处理透传消息<br>
 * onReceiveClientId 接收 cid <br>
 * onReceiveOnlineState cid 离线上线通知 <br>
 * onReceiveCommandResult 各种事件处理回执 <br>
 */
public class DemoIntentService extends GTIntentService {

    private static final String TAG = "GetuiSdkDemo";

    /**
     * 为了观察透传数据变化.
     */
    private static int cnt;

    public DemoIntentService() {

    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
        Log.d("qaz", "onReceiveServicePid -> " + pid);
    }

    private Gson gson = new Gson();
    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        String appid = msg.getAppid();
        String taskid = msg.getTaskId();
        String messageid = msg.getMessageId();
        byte[] payload = msg.getPayload();
        String pkg = msg.getPkgName();
        String cid = msg.getClientId();
        // 第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
        boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
        Log.d("qaz", "call sendFeedbackMessage = " + (result ? "success" : "failed"));

        Log.d("qaz", "onReceiveMessageData -> " + "appid = " + appid + "\ntaskid = " + taskid + "\nmessageid = " + messageid + "\npkg = " + pkg
                + "\ncid = " + cid);

        if (payload == null) {
            Log.e("qaz", "receiver payload = null");
        } else {
            String data = new String(payload);
            Log.d("qaz", "receiver payload = " + data);

            //接到通知 跳转到指定的activity
            jumpActivity(data);


            // 测试消息为了观察数据变化
            if (data.equals("收到一条透传测试消息")) {
                data = data + "-" + cnt;
                cnt++;
            }
//            sendMessage(data, 0);
        }
//        sendNotification(new String(payload));
        Log.d("qaz", "----------------------------------------------------------------------------------------------");
    }

    private void jumpActivity(String data) {
        try {
            MessageBean bean = gson.fromJson(data,MessageBean.class);
            Log.i("qaz", "jumpActivity: " + bean.locate);
            if ("1".equals(bean.locate)){
                if (!TextUtils.isEmpty(bean.q_id)){

                    //跳转到自己写的界面
                    //跳转到问答详情
                    Intent intentHome = new Intent(this, TabUI.class);
                    intentHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Intent intentChild = new Intent(this, HomeQuewstionDetail.class);
                    intentChild.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intentChild.putExtra("q_id",bean.q_id);
                    Intent[] intents = new Intent[2];
                    intents[0] = intentHome;
                    intents[1] = intentChild;
                    startActivities(intents);
                }
            }


            if ("6".equals(bean.locate)){
                if (!TextUtils.isEmpty(bean.url)) {
                    Intent intentHome = new Intent(this, Splash.class);
                    intentHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Intent intentChild = new Intent(this, H5DetailUI.class);
                    intentChild.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intentChild.putExtra("urls", bean.url);
                    intentChild.putExtra("type", "url");
                    Intent[] intents = new Intent[2];
                    intents[0] = intentHome;
                    intents[1] = intentChild;
                    startActivities(intents);
                }
            }


            if ("2".equals(bean.locate)||"3".equals(bean.locate)||"4".equals(bean.locate)){
                //新闻推送 圈子 视频
                Intent intentHome = new Intent(this, TabUI.class);
                intentHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Intent intentChild = new Intent(this, H5DetailUI.class);
                intentChild.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intentChild.putExtra("urls",bean.url);
                intentChild.putExtra("type","url");
                Intent[] intents = new Intent[2];
                intents[0] = intentHome;
                intents[1] = intentChild;
                startActivities(intents);
            }



        }catch (Exception exception){
//            sendNotification(data);
        }

    }

    int i = 0;
    private void sendNotification(String content) {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("安全快车")//设置通知栏标题
                .setContentText(content)
                .setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL)) //设置通知栏点击意图
//  .setNumber(number) //设置通知集合的数量
                .setTicker(content) //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                .setSmallIcon(R.drawable.push);//设置通知小ICON

        mNotificationManager.notify(i++, mBuilder.build());
    }

    public PendingIntent getDefalutIntent(int flags){

        PendingIntent pendingIntent= PendingIntent.getActivity(this, 1, new Intent(this,TabUI.class), flags);
        return pendingIntent;
    }



    @Override
    public void onReceiveClientId(Context context, String clientid) {
        Log.e("qaz", "onReceiveClientId -> " + "clientid = " + clientid);

        Tag t = new Tag();
        //name 字段只支持：中文、英文字母（大小写）、数字、除英文逗号以外的其他特殊符号, 具体请看代码示例
        t.setName("Android");
        PushManager.getInstance().setTag(this,new Tag[]{t},
                System.currentTimeMillis() +"");


    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
        Log.d("qaz", "onReceiveOnlineState -> " + (online ? "online" : "offline"));
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
        Log.d("qaz", "onReceiveCommandResult -> " + cmdMessage);

        int action = cmdMessage.getAction();

        if (action == PushConsts.SET_TAG_RESULT) {
            setTagResult((SetTagCmdMessage) cmdMessage);
        } else if ((action == PushConsts.THIRDPART_FEEDBACK)) {
            feedbackResult((FeedbackCmdMessage) cmdMessage);
        }
    }

    private void setTagResult(SetTagCmdMessage setTagCmdMsg) {
        String sn = setTagCmdMsg.getSn();
        String code = setTagCmdMsg.getCode();

        String text = "设置标签失败, 未知异常";
        switch (Integer.valueOf(code)) {
            case PushConsts.SETTAG_SUCCESS:
                text = "设置标签成功";
                break;

            case PushConsts.SETTAG_ERROR_COUNT:
                text = "设置标签失败, tag数量过大, 最大不能超过200个";
                break;

            case PushConsts.SETTAG_ERROR_FREQUENCY:
                text = "设置标签失败, 频率过快, 两次间隔应大于1s且一天只能成功调用一次";
                break;

            case PushConsts.SETTAG_ERROR_REPEAT:
                text = "设置标签失败, 标签重复";
                break;

            case PushConsts.SETTAG_ERROR_UNBIND:
                text = "设置标签失败, 服务未初始化成功";
                break;

            case PushConsts.SETTAG_ERROR_EXCEPTION:
                text = "设置标签失败, 未知异常";
                break;

            case PushConsts.SETTAG_ERROR_NULL:
                text = "设置标签失败, tag 为空";
                break;

            case PushConsts.SETTAG_NOTONLINE:
                text = "还未登陆成功";
                break;

            case PushConsts.SETTAG_IN_BLACKLIST:
                text = "该应用已经在黑名单中,请联系售后支持!";
                break;

            case PushConsts.SETTAG_NUM_EXCEED:
                text = "已存 tag 超过限制";
                break;

            default:
                break;
        }

        Log.d("qaz", "settag result sn = " + sn + ", code = " + code + ", text = " + text);
    }

    private void feedbackResult(FeedbackCmdMessage feedbackCmdMsg) {
        String appid = feedbackCmdMsg.getAppid();
        String taskid = feedbackCmdMsg.getTaskId();
        String actionid = feedbackCmdMsg.getActionId();
        String result = feedbackCmdMsg.getResult();
        long timestamp = feedbackCmdMsg.getTimeStamp();
        String cid = feedbackCmdMsg.getClientId();

        Log.d("qaz", "onReceiveCommandResult -> " + "appid = " + appid + "\ntaskid = " + taskid + "\nactionid = " + actionid + "\nresult = " + result
                + "\ncid = " + cid + "\ntimestamp = " + timestamp);
    }

//    private void sendMessage(String data, int what) {
//        Message msg = Message.obtain();
//        msg.what = what;
//        msg.obj = data;
//        MyApplication.sendMessage(msg);
//    }
}
