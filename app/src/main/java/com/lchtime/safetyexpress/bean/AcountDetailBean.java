package com.lchtime.safetyexpress.bean;

import java.util.List;

/**
 * Created by android-cp on 2017/5/19.
 */

public class AcountDetailBean {

    /**
     * code : 10
     * info : 返回账目明细记录
     */

    public ResultBean result;
    /**
     * result : {"code":"10","info":"返回账目明细记录"}
     * list : [{"uc_amount":"-1.00","uc_type":"支付宝","uc_at":"提现","uc_status":"待处理","time":"2017-05-18 15:59:10","balance":"￥984.00"},{"uc_amount":"-2.00","uc_type":"支付宝","uc_at":"提现","uc_status":"待处理","time":"2017-05-17 11:13:55","balance":"￥984.00"},{"uc_amount":"-1.00","uc_type":"支付宝","uc_at":"提现","uc_status":"待处理","time":"2017-05-17 11:12:20","balance":"￥984.00"}]
     * totalpage : 1
     */

    public int totalpage;
    /**
     * uc_amount : -1.00
     * uc_type : 支付宝
     * uc_at : 提现
     * uc_status : 待处理
     * time : 2017-05-18 15:59:10
     * balance : ￥984.00
     */

    public List<ListBean> list;

    public static class ResultBean {
        public String code;
        public String info;
    }

    public static class ListBean {
        public String uc_amount;
        public String uc_type;
        public String uc_at;
        public String uc_status;
        public String time;
        public String balance;
    }
}
