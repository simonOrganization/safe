package com.lchtime.safetyexpress.ui.chat.hx.bean;

import java.util.List;

/**
 * Created by Dreamer on 2017/6/7.
 */

public class SearchResultBean {

    /**
     * code : 10
     * info : 返回查找信息
     */

    public ResultBean result;
    /**
     * ud_nickname : 鹏哥
     * ud_photo_fileid : http://fcar.lchtime.cn:8001/upload/119/2017/05/24/bf2883cb83e7020551fcccfa0b5c0b90.png
     * hx_account : 18502918464
     * user : 化工 一线员工
     */

    public List<UserBean> user_base;
    public List<UserBean> qun;

    public static class ResultBean {
        public String code;
        public String info;
    }


}
