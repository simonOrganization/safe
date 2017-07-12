package com.lchtime.safetyexpress.ui.chat.hx.bean;

import java.util.List;

/**
 * Created by Dreamer on 2017/6/7.
 */

public class AddBean {

    /**
     * code : 10
     * info : 找人首页用户列表
     */

    public ResultBean result;
    /**
     * ud_nickname : 张金宇
     * ud_photo_fileid : http://fcar.lchtime.cn:8001/upload/117/2017/05/24/71d84c9fe88eb099a4e41f42063ab4f2.png
     * user : 矿山 一线员工
     * hx_account : 17710164002
     */

    public List<UserBean> user;
    public List<UserBean> qun;

    public static class ResultBean {
        public String code;
        public String info;
    }

}
