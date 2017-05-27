package com.lchtime.safetyexpress.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android-cp on 2017/5/27.
 */

public class CircleItemUpBean {

    /**
     * code : 10
     * info : 查询成功
     */

    public ResultBean result;
    /**
     * qc_id : 101
     * qc_ub_id : 105
     * qc_fee : 0
     * qc_feevalue : 0
     * qc_title :
     * qc_context : 我们试试看
     * qc_zc : 5
     * qc_fd : 3
     * qc_date : 1495704870
     * qc_auth : 许文翰
     * qc_video :
     * qc_mark :
     * qc_qs_id : 0
     * qc_pinglun : 1
     * qc_long : 0
     * qc_lat : 0
     * qc_high : 0
     * qc_lm_id : 0
     * is_delete : 0
     * dyNum : 1
     * is_dy : 0
     * zan : 1
     * cai : 1
     * pic : ["http://fcar.lchtime.cn:8001/upload/105/2017/05/25/241d05df6dcd3be4651acd6978ac1099."]
     * ud_photo_fileid : http://fcar.lchtime.cn:8001/upload/105/2017/05/16/838f8da6aa9073d2748bd1ac502c44b2.png
     * user : 北京 燃气 管理人员
     */

    public ArrayList<QzContextBean> qz_info;

    public static class ResultBean {
        public String code;
        public String info;
    }


}
