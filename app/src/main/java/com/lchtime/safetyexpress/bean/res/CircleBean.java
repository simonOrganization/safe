package com.lchtime.safetyexpress.bean.res;

import com.lchtime.safetyexpress.bean.BasicResult;
import com.lchtime.safetyexpress.bean.QzContextBean;

import java.util.List;

/**
 * Created by android-cp on 2017/5/8.
 */

public class CircleBean {


    /**
     * code : 10
     * info : 显示数据
     */

    public BasicResult result;
    /**
     * result : {"code":"10","info":"显示数据"}
     * qz_context : [{"qc_id":10,"qc_ub_id":2,"qc_fee":0,"qc_feevalue":0,"qc_title":"努力工作","qc_context":"努力工作","qc_zc":0,"qc_fd":0,"qc_date":"2017-04-22","qc_auth":"2334","qc_video":"","qc_mark":"B","qc_qs_id":0,"qc_pinglun":1,"qc_long":12,"qc_lat":13,"qc_high":14,"qc_lm_id":0,"is_delete":0,"pic":["http://fcar.lchtime.cn:8001/upload/2/2017/04/22/7e9cdf3be97e5a87bbac1646b58df362.png","http://fcar.lchtime.cn:8001/upload/2/2017/04/22/7e9cdf3be97e5a87bbac1646b58df362.png"],"ud_photo_fileid":false},{"qc_id":1,"qc_ub_id":1,"qc_fee":0,"qc_feevalue":0,"qc_title":"天气好阿","qc_context":"是阿是阿","qc_zc":5,"qc_fd":3,"qc_date":"2017-04-20","qc_auth":"aaaa","qc_fieldid5":" ","qc_fieldid6":" ","qc_fieldid7":" ","qc_fieldid8":" ","qc_fieldid9":" ","qc_video":"","qc_mark":"A","qc_qs_id":0,"qc_pinglun":1,"qc_long":1,"qc_lat":1,"qc_high":1,"qc_lm_id":0,"is_delete":0,"pic":["http://fcar.lchtime.cn:8001/upload/1/2017/04/24/fe6b9b0627945d2e9892b6c1ce8ec383.jpg","http://fcar.lchtime.cn:8001/upload/1/2017/04/24/fe6b9b0627945d2e9892b6c1ce8ec383.jpg","http://fcar.lchtime.cn:8001/upload/1/2017/04/24/fe6b9b0627945d2e9892b6c1ce8ec383.jpg"],"ud_photo_fileid":"http://fcar.lchtime.cn:8001/upload/92/2017/04/28/27eb23aea85e57ad4b5d4c663c81aff3."}]
     * total : 2
     */
    public int total;
    public int totalpage;

    public List<QzContextBean> qz_context;
    //收藏里面的圈子
    public List<QzContextBean> cms_context;

    /**
     * qc_id : 10
     * qc_ub_id : 2
     * qc_fee : 0
     * qc_feevalue : 0
     * qc_title : 努力工作
     * qc_context : 努力工作
     * qc_zc : 0
     * qc_fd : 0
     * qc_date : 2017-04-22
     * qc_auth : 2334
     * qc_video :
     * qc_mark : B
     * qc_qs_id : 0
     * qc_pinglun : 1
     * qc_long : 12
     * qc_lat : 13
     * qc_high : 14
     * qc_lm_id : 0
     * is_delete : 0
     * pic : ["http://fcar.lchtime.cn:8001/upload/2/2017/04/22/7e9cdf3be97e5a87bbac1646b58df362.png","http://fcar.lchtime.cn:8001/upload/2/2017/04/22/7e9cdf3be97e5a87bbac1646b58df362.png"]
     * ud_photo_fileid : false
     */

}
