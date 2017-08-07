package com.lchtime.safetyexpress.ui.chat.hx.bean;

/**
 * Created by Dreamer on 2017/6/7.
 */

public class UserBean {
    public String master;
    public String qun_type;
    public String ud_nickname;
    public String ud_photo_fileid;
    public String user;
    public String hx_account;
    public int is_friend = 0;





    public boolean isHave(){
        if(is_friend == 0){
            return false;
        }
        return true;
    }

}
