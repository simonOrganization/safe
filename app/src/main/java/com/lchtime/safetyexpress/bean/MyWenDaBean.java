package com.lchtime.safetyexpress.bean;

import java.util.List;

/**
 * Created by android-cp on 2017/5/23.
 */

public class MyWenDaBean {

    /**
     * code : 10
     * info : 我的回答
     */

    public ResultBean result;
    /**
     * result : {"code":"10","info":"我的回答"}
     * item : [{"num":0,"oid":3,"title":"88888888888","time":"1494463884","param":"fsdfdsf "},{"num":0,"oid":4,"title":"123456","time":"1494463884","param":"177777777777777"},{"num":0,"oid":5,"title":"sflgjskgfjdk","time":"1494463884","param":"177777777777777"},{"num":0,"oid":6,"title":"sflgjskgfjdk","time":"1494463884","param":"17777755555"},{"num":0,"oid":7,"title":"123456","time":"1494463884","param":"燕啊是你么"},{"num":0,"oid":8,"title":"123456","time":"1494463884","param":"燕啊是你吗"},{"num":0,"oid":10,"title":"123456","time":"1494463884","param":"sdfsdfd"},{"num":0,"oid":15,"title":"123456","time":"1494463884","param":"哈哈"},{"num":0,"oid":16,"title":"88888","time":"1494463884","param":"哈哈"},{"num":0,"oid":17,"title":"88888","time":"1494463884","param":"哈哈"}]
     * num : 13
     * totalpage : 2
     */

    public String num;
    public String totalpage;
    /**
     * num : 0
     * oid : 3
     * title : 88888888888
     * time : 1494463884
     * param : fsdfdsf
     */

    public List<ItemBean> item;

    public static class ResultBean {
        public String code;
        public String info;
    }

    public static class ItemBean {
        public String num;
        public String oid;
        public String title;
        public String time;
//        type=0   param=关注问题的id
//        type=1   param=回答的内容
//        type=2   param=问题描述
        public String param;
    }
}
