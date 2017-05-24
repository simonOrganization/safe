package com.lchtime.safetyexpress.bean;

/**
 * Created by android-cp on 2017/5/18.
 */

public class MyAccountBean {

    /**
     * code : 10
     * info : 返回账户余额的信息
     */

    public ResultBean result;
    /**
     * ud_zfb_name : 张三
     * ud_zfb_account : 2223333
     */

    public TpartyBean tparty;
    /**
     * result : {"code":"10","info":"返回账户余额的信息"}
     * tparty : {"ud_zfb_name":"张三","ud_zfb_account":"2223333"}
     * ud_amount : 991.00
     */

    public String ud_amount;

    public static class ResultBean {
        public String code;
        public String info;
    }

    public static class TpartyBean {
        public String ud_zfb_name;
        public String ud_zfb_account;
    }
}
