package com.lchtime.safetyexpress.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dreamer on 2017/5/20.
 */

public class QJSearchBean {

    /**
     * code : 10
     * info : 搜索成功
     */

    public ResultBean result;
    /**
     * cc_id : 174
     * cc_cd_id : 109
     * cc_cd_parent_id_dir : |0|
     * cc_title : 柬埔寨一名18岁女孩触电身亡，发现有人触电后该如何去做
     * cc_description :
     * cc_from :
     * cc_datetime : 1495170000
     * cc_auth :
     * cc_web_keyword :
     * cc_web_description : 电力安全生产
     * cc_pos : 0
     * cc_agr : 0
     * cc_aga : 0
     * cc_count : 0
     * cc_mark : N|D|T
     * cc_fielid :
     * cc_fee : 0
     * is_delete : 0
     * media : []
     * plNum : 0
     */

    public ArrayList<NewsBean> news;
    /**
     * result : {"code":"10","info":"搜索成功"}
     * news : [{"cc_id":174,"cc_cd_id":109,"cc_cd_parent_id_dir":"|0|","cc_title":"柬埔寨一名18岁女孩触电身亡，发现有人触电后该如何去做","cc_description":"","cc_from":"","cc_datetime":"1495170000","cc_auth":"","cc_web_keyword":"","cc_web_description":"电力安全生产","cc_pos":0,"cc_agr":0,"cc_aga":0,"cc_count":0,"cc_mark":"N|D|T","cc_fielid":"","cc_fee":0,"is_delete":0,"media":[],"plNum":0},{"cc_id":175,"cc_cd_id":106,"cc_cd_parent_id_dir":"|0|","cc_title":"溜煤眼水煤伤人事故","cc_description":"溜煤眼水煤伤人事故","cc_from":"煤矿安全网","cc_datetime":"1495123200","cc_auth":"煤矿安全网","cc_web_keyword":"","cc_web_description":"","cc_pos":0,"cc_agr":0,"cc_aga":0,"cc_count":0,"cc_mark":"N|D|T","cc_fielid":"","cc_fee":0,"is_delete":0,"media":[],"plNum":0}]
     * sp_info : []
     */

    public ArrayList<NewsBean> sp_info;

    public static class ResultBean {
        public String code;
        public String info;
    }



}
