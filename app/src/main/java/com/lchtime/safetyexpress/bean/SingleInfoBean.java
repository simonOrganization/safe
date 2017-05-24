package com.lchtime.safetyexpress.bean;

/**
 * Created by android-cp on 2017/5/18.
 */

public class SingleInfoBean {

    /**
     * code : 10
     * info : 返回用户信息
     */

    public ResultBean result;
    /**
     * ud_photo_fileid :
     * ud_memo :
     * ud_nickname :
     * user :
     * is_dy : 0
     * is_friend : 0
     * friendNum :
     * dyNum : 3
     */

    public UserBean user;

    public static class ResultBean {
        public String code;
        public String info;
    }

    public static class UserBean {
        public String ud_photo_fileid;
        public String ud_memo;
        public String ud_nickname;
        public String user;
        public String is_dy;
        public String is_friend;
        public String friendNum;
        public String dyNum;
    }
}
