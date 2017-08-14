package com.lchtime.safetyexpress.bean;

import android.text.TextUtils;

/**
 * Created by android-cp on 2017/4/26.
 */

public class VipInfoBean {
    public BasicResult result;//基础结果
   // public String user_base;//电话号码
    public UserDetail user_detail;//用户的ID

    public VipInfoBean() {
        result = new BasicResult();
        user_detail = new UserDetail();
    }

    public VipInfoBean(UserDetail user_detail) {
        this.user_detail = user_detail;
    }

    public static class UserDetail{
        public String ud_nickname;
        //行业
        public String ud_profession;
        //岗位
        public String ud_post;
        //地址
        public String ud_addr;
        public String ud_company_name;
        public String ud_borth;
        public String ud_sex;
        //部门
        public String ud_bm;
        //备注
        public String ud_memo;
        public String ud_photo_fileid;
        public String ub_phone;//电话号码
        public String ub_id;
        //是否分享  未分享  已分享,未领奖  已领奖
        public String share;


        public String getHXId(){
            if(TextUtils.isEmpty(ub_id)){
                return "";
            }
            return "aqkc" + ub_id;
        }

    }
}
