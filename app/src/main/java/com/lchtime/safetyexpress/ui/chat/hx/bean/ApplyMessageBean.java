package com.lchtime.safetyexpress.ui.chat.hx.bean;

import com.lchtime.safetyexpress.bean.BasicResult;

import java.util.List;

/**
 * Created by Dreamer on 2017/6/12.
 */

public class ApplyMessageBean {

    /**
     * code : 10
     * info : 获取好友申请成功
     */

    public BasicResult result;
    /**
     * ud_nickname : 梁仓
     * ud_photo_fileid : http://fcar.lchtime.cn:8001/upload/98/2017/06/12/4556aaf7ddf602dfacb6c71b81957f38.png
     * ub_id : 98
     * hx_account : 13693600116
     * message : 98申请添加119
     * status : 0
     * applyid : 140
     * qun : 0
     */

    public List<ApplyListBean> applyList;

    public static class ApplyListBean {
        public String ud_nickname;
        public String ud_photo_fileid;
        public String ub_id;
        public String hx_account;
        public String message;
        public String status;
        public String applyid;
        public String qun;
        public String groupid;
        public String master;
        public String groupimg;
    }
}
