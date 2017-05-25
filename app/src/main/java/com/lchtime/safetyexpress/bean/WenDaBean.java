package com.lchtime.safetyexpress.bean;

import java.util.List;

/**
 * Created by android-cp on 2017/5/11.
 */

public class WenDaBean {

    /**
     * code : 10
     * info : 问答首页
     */

    public ResultBean result;
    /**
     * result : {"code":"10","info":"问答首页"}
     * tw : [{"q_id":6,"q_ub_id":2,"q_title":"提问阿勒","q_description":"这是问题描述","q_create":"1494486666","pic":[],"hd_num":0},{"q_id":7,"q_ub_id":2,"q_title":"提问阿勒","q_description":"这是问题描述","q_create":"1494485444","pic":[],"hd_num":2},{"q_id":4,"q_ub_id":2,"q_title":"提问阿勒","q_description":"这是问题描述","q_create":"1494485333","pic":[],"hd_num":0},{"q_id":8,"q_ub_id":81,"q_title":"开始的疯狂减肥","q_description":"水电费水电费对方打开了法规及贷款","q_create":"1494485222","pic":[],"hd_num":0},{"q_id":2,"q_ub_id":2,"q_title":"提问阿勒","q_description":"这是问题描述","q_create":"1494485199","pic":[],"hd_num":0},{"q_id":1,"q_ub_id":2,"q_title":"克鲁塞德缴费克鲁塞德","q_description":"斯蒂芬斯蒂芬","q_create":"1494485101","pic":[],"hd_num":1},{"q_id":3,"q_ub_id":2,"q_title":"提问阿勒","q_description":"这是问题描述","q_create":"1494485000","pic":[],"hd_num":0},{"q_id":5,"q_ub_id":2,"q_title":"提问阿勒","q_description":"这是问题描述","q_create":"1494482222","pic":[],"hd_num":0}]
     * totalpage : 1
     */

    public int totalpage;
    /**
     * q_id : 6
     * q_ub_id : 2
     * q_title : 提问阿勒
     * q_description : 这是问题描述
     * q_create : 1494486666
     * pic : []
     * hd_num : 0
     */

    public List<TwBean> tw;

    public static class ResultBean {
        public String code;
        public String info;
    }

    public static class TwBean {
        public String q_id;
        public String q_ub_id;
        public String q_title;
        public String q_description;
        public String q_create;
        public String hdNum ;
        public List<String> pic;
    }
}
