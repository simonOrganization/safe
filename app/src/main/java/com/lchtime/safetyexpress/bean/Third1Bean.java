package com.lchtime.safetyexpress.bean;

/**
 * Created by Dreamer on 2017/6/19.
 */

public class Third1Bean {

    /**
     * code : 10
     * info : 暂无该用户第三方登陆信息
     */

    public BasicResult result;
    /**
     * ud_ub_id : 0
     * ud_pwd : 0
     * ud_borth : 0
     * ud_idcard : 0
     * ud_sex : 0
     * ud_nickname : 0
     * ud_status : 0
     * ud_amount : 0
     * ud_mount : 0
     * ud_last_ip : 0
     * ud_last_datetime : 0
     * ud_login_count : 0
     * ud_mark : 0
     * ud_pay : 0
     * ud_photo_fileid : 0
     * ud_name : 0
     * ud_memo : 0
     * ud_tj_ub_id : 0
     * ud_jifen : 0
     * ud_ul_level : 0
     * ud_ul_name : 0
     * ud_profession : 0
     * ud_post : 0
     * ud_addr : 0
     * ud_company_name : 0
     */

    public UserBean user;
    /**
     * ub_id : 0
     * phone : 0
     * openid : 0
     * username : 0
     * tp_head : 0
     * tp_gender : 0
     */

    public UseridBean userid;


    public static class UserBean {
        public String ud_ub_id;
        public String ud_pwd;
        public String ud_borth;
        public String ud_idcard;
        public String ud_sex;
        public String ud_nickname;
        public String ud_status;
        public String ud_amount;
        public String ud_mount;
        public String ud_last_ip;
        public String ud_last_datetime;
        public String ud_login_count;
        public String ud_mark;
        public String ud_pay;
        public String ud_photo_fileid;
        public String ud_name;
        public String ud_memo;
        public String ud_tj_ub_id;
        public String ud_jifen;
        public String ud_ul_level;
        public String ud_ul_name;
        public String ud_profession;
        public String ud_post;
        public String ud_addr;
        public String ud_company_name;
    }

    public static class UseridBean {
        public String ub_id;
        public String phone;
        public String openid;
        public String username;
        public String tp_head;
        public String tp_gender;
    }
}
