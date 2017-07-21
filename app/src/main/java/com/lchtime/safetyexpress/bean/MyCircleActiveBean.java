package com.lchtime.safetyexpress.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android-cp on 2017/5/17.
 */

public class MyCircleActiveBean {

    /**
     * code : 10
     * info : 获取我的圈子动态成功
     */

    public ResultBean result;
    /**
     * qc_id : 46
     * qc_ub_id : 82
     * qc_fee : 0
     * qc_feevalue : 0
     * qc_title :
     * qc_context : 纯文字试试
     * qc_zc : 0
     * qc_fd : 0
     * qc_date : 1495003361
     * qc_auth : 哈哈
     * qc_video :
     * qc_mark :
     * qc_qs_id : 0
     * qc_pinglun : 1
     * qc_long : 0
     * qc_lat : 0
     * qc_high : 0
     * qc_lm_id : 0
     * is_delete : 0
     * pic : []
     * zan : 0
     * cai : 0
     */

    public List<QuanziBean> quanzi;

    public static class ResultBean {
        public String code;
        public String info;
    }

    public static class QuanziBean {
        public String qc_id;
        public String qc_ub_id;
        public String qc_fee;
        public String qc_feevalue;
        public String qc_title;
        public String qc_context;
        public String qc_zc;
        public String qc_fd;
        public String qc_date;
        public String qc_auth;
        public String qc_video;
        public String qc_mark;
        public String qc_qs_id;
        public String qc_pinglun;
        public String qc_long;
        public String qc_lat;
        public String qc_high;
        public String qc_lm_id;
        public String is_delete;
        public String zan;
        public String cai;
        public ArrayList<String> pic;
    }
}
