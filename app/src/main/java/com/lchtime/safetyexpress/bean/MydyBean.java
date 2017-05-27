package com.lchtime.safetyexpress.bean;

import java.util.List;

/**
 * Created by android-cp on 2017/5/9.
 */

public class MydyBean {

    /**
     * code : 10
     * info : 获取我的订阅首页成功
     */

    public ResultBean result;
    /**
     * ud_ub_id : 1
     * ud_nickname : 安全快车用户8814
     * ud_photo_fileid : http://fcar.lchtime.cn:8001/upload/92/2017/04/28/27eb23aea85e57ad4b5d4c663c81aff3.
     * ud_profession : 互联网/电子商务
     * ud_post : 打杂
     * ud_company_name :
     */

    public List<DyBean> dy;
    /**
     * qc_id : 13
     * qc_ub_id : 1
     * qc_fee : 0
     * qc_feevalue : 0
     * qc_title : 考虑时间的反馈了
     * qc_context : 斯蒂芬斯蒂芬多少
     * qc_zc : 0
     * qc_fd : 0
     * qc_date : 1492617700
     * qc_auth : 安全快车用户8814
     * qc_video :
     * qc_mark :
     * qc_qs_id : 0
     * qc_pinglun : 0
     * qc_long : 0
     * qc_lat : 0
     * qc_high : 0
     * qc_lm_id : 0
     * is_delete : 0
     * ud_ub_id : 1
     * ud_nickname : 安全快车用户8814
     * ud_photo_fileid : http://fcar.lchtime.cn:8001/upload/92/2017/04/28/27eb23aea85e57ad4b5d4c663c81aff3.
     * user : 互联网/电子商务 打杂
     * zan : 0
     * cai : 0
     * pic : []
     */

    public List<QzContextBean> quanzi;
    public String totalpage;

    public static class ResultBean {
        public String code;
        public String info;
    }

    public static class DyBean {
        public String ud_ub_id;
        public String ud_nickname;
        public String ud_photo_fileid;
        public String ud_profession;
        public String ud_post;
        public String ud_company_name;
    }

}
