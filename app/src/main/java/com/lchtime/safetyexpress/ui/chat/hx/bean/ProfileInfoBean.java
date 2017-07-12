package com.lchtime.safetyexpress.ui.chat.hx.bean;

import java.util.List;

/**
 * Created by Dreamer on 2017/6/10.
 */

public class ProfileInfoBean {

    /**
     * code : 10
     * info : 显示好友的详情
     */

    public ResultBean result;
    /**
     * ud_nickname : 安全快车用户1111
     * ud_photo_fileid : http://fcar.lchtime.cn:8001/upload/136/2017/06/02/394afd2af317925a00210aaec2c513ec.png
     * ud_ub_id : 136
     * ud_post : 专家顾问
     * ud_addr : 澳门
     * ud_company_name :
     * ud_sex :
     * ud_profession : 建筑
     * ud_memo :
     * is_friend : 0
     * ub_phone : 11111111111
     */

    public List<SingleDetailBean> user_detail;

    public static class ResultBean {
        public String code;
        public String info;
    }


}
