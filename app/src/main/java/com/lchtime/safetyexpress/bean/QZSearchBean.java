package com.lchtime.safetyexpress.bean;

import java.util.List;

/**
 * Created by Dreamer on 2017/5/20.
 */

public class QZSearchBean {

    /**
     * code : 10
     * info : 搜索成功
     */

    public ResultBean result;
    /**
     * qc_id : 10
     * qc_ub_id : 2
     * qc_fee : 0
     * qc_feevalue : 0
     * qc_title : 努力工作
     * qc_context : 人努力工作
     * qc_zc : 6
     * qc_fd : 3
     * qc_date : 1492790400
     * qc_auth : 安全快车用户8816
     * qc_video :
     * qc_mark : B
     * qc_qs_id : 0
     * qc_pinglun : 1
     * qc_long : 12
     * qc_lat : 13
     * qc_high : 14
     * qc_lm_id : 0
     * is_delete : 0
     * dyNum : 2
     * is_dy : 0
     * zan : 0
     * cai : 0
     * pic : ["http://fcar.lchtime.cn:8001/upload/2/2017/04/22/7e9cdf3be97e5a87bbac1646b58df362.png","http://fcar.lchtime.cn:8001/upload/2/2017/04/22/7e9cdf3be97e5a87bbac1646b58df362.png"]
     * ud_photo_fileid :
     * user : 北京 化工 一线员工
     */

    public List<QzContextBean> qz_info;

    public static class ResultBean {
        public String code;
        public String info;
    }


}
