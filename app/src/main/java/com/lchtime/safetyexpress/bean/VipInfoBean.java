package com.lchtime.safetyexpress.bean;

/**
 * Created by android-cp on 2017/4/26.
 */

public class VipInfoBean {
    public BasicResult result;//基础结果
    public String user_base;//电话号码
    public UserDetail user_detail;//用户的ID

    public static class UserDetail{
        public String ud_nickname;
        //行业
        public String ud_profession;
        //岗位
        public String ud_post;
        public String ud_addr;
        public String ud_company_name;
        public String ud_borth;
        public String ud_sex;
        //部门
        public String ud_bm;
        //备注
        public String ud_memo;
        public String ud_photo_fileid;
    }
}
