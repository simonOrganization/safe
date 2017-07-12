package com.lchtime.safetyexpress.bean;

/**
 * Created by Dreamer on 2017/6/19.
 */

public class Third2Bean {


    /**
     * code : 10
     * info : 第三方登陆在线
     */

    public ResultBean result;
    /**
     * ud_ub_id : 151
     * ud_pwd : 123456
     * ud_borth :
     * ud_idcard :
     * ud_sex : 男
     * ud_nickname : 不解释
     * ud_status : 1
     * ud_amount : 0
     * ud_mount : 0
     * ud_last_ip : 218.244.44.22
     * ud_last_datetime : 2017-06-19 18:28:43
     * ud_login_count : 3
     * ud_mark :
     * ud_pay :
     * ud_photo_fileid : http://wx.qlogo.cn/mmopen/1CHHx9Yq4nG3mh5icL4ibEslpiaGzSic7zriaFCsiamtGreXsenibvicGtS1ZPwuCcsHgfZrvMOSZn16Klu02iceJmHNmKw/0
     * ud_name :
     * ud_memo :
     * ud_tj_ub_id : 0
     * ud_jifen : 0
     * ud_ul_level : 1
     * ud_ul_name :
     * ud_profession :
     * ud_post :
     * ud_addr :
     * ud_company_name :
     * ud_share : 0
     */

    public UserBean user;
    /**
     * ub_id : 151
     * phone : 18646885222
     * openid : ojCPvwOwLc0_RimHHVuj2dyxG7pM
     * username :
     * tp_head : http://wx.qlogo.cn/mmopen/1CHHx9Yq4nG3mh5icL4ibEslpiaGzSic7zriaFCsiamtGreXsenibvicGtS1ZPwuCcsHgfZrvMOSZn16Klu02iceJmHNmKw/0
     * tp_gender : 男
     */

    public UseridBean userid;

    public static class ResultBean {
        public String code;
        public String info;
    }

    public static class UserBean {
        public int ud_ub_id;
        public String ud_pwd;
        public String ud_borth;
        public String ud_idcard;
        public String ud_sex;
        public String ud_nickname;
        public int ud_status;
        public int ud_amount;
        public int ud_mount;
        public String ud_last_ip;
        public String ud_last_datetime;
        public int ud_login_count;
        public String ud_mark;
        public String ud_pay;
        public String ud_photo_fileid;
        public String ud_name;
        public String ud_memo;
        public int ud_tj_ub_id;
        public int ud_jifen;
        public int ud_ul_level;
        public String ud_ul_name;
        public String ud_profession;
        public String ud_post;
        public String ud_addr;
        public String ud_company_name;
        public int ud_share;
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
