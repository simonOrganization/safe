package com.lchtime.safetyexpress.bean;

import java.util.List;

/**
 * Created by android-cp on 2017/5/11.
 */

public class WenDaDetailBean {

    /**
     * code : 10
     * info : 问答详情
     */

    public ResultBean result;
    /**
     * q_id : 7
     * q_ub_id : 2
     * q_title : 提问阿勒
     * q_description : 这是问题描述
     * q_create : 1494485444
     * pic : []
     */

    public WentiBean wenti;
    /**
     * result : {"code":"10","info":"问答详情"}
     * wenti : {"q_id":7,"q_ub_id":2,"q_title":"提问阿勒","q_description":"这是问题描述","q_create":"1494485444","pic":[]}
     * huida : 2
     * guanzhu : 0
     * hdinfo : [{"a_id":2,"aq_id":7,"aq_title":"77777777","a_ub_id":17,"a_context":"法国恢复规划法规和","a_dz":"","a_dc":"","a_create":"1494463884","user":"安全快车用户3330   ","ud_photo_fileid":"http://fcar.lchtime.cn:8001/upload/pub/head/2c223800d88ef9f054f73ecd7e0943d6.","pic":[]},{"a_id":3,"aq_id":7,"aq_title":"88888888888","a_ub_id":82,"a_context":"fsdfdsf ","a_dz":"","a_dc":"","a_create":"1494463884","user":"哈哈 互联网/电子商务 大幅度 哈哈哈哈","ud_photo_fileid":"http://fcar.lchtime.cn:8001/upload/82/2017/05/11/d9163859b4c8bf0831f35c5ee9f2baff.png","pic":[]}]
     */

    public String huida;
    public String guanzhu;
    /**
     * a_id : 2
     * aq_id : 7
     * aq_title : 77777777
     * a_ub_id : 17
     * a_context : 法国恢复规划法规和
     * a_dz :
     * a_dc :
     * a_create : 1494463884
     * user : 安全快车用户3330
     * ud_photo_fileid : http://fcar.lchtime.cn:8001/upload/pub/head/2c223800d88ef9f054f73ecd7e0943d6.
     * pic : []
     */

    public List<HdinfoBean> hdinfo;

    public static class ResultBean {
        public String code;
        public String info;
    }

    public static class WentiBean {
        public String q_id;
        public String q_ub_id;
        public String q_title;
        public String q_description;
        public String q_create;
        public List<String> pic;
    }

    public static class HdinfoBean {
        public String a_id;
        public String aq_id;
        public String aq_title;
        public String a_ub_id;
        public String a_context;
        public String a_dz;
        public String a_dc;
        public String a_create;
        public String ud_nickname;
        public String user;
        public String ud_photo_fileid;
        public List<String> pic;
    }
}
