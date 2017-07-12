package com.lchtime.safetyexpress.bean;

/**
 * Created by Dreamer on 2017/6/5.
 */

public class HXInfo {

    /**
     * code : 10
     * info : 返回环信账号
     */

    public ResultBean result;
    /**
     * result : {"code":"10","info":"返回环信账号"}
     * ub_id : 119
     * hx_account : 18502918464
     */

    public String ub_id;
    public String hx_account;

    public static class ResultBean {
        public String code;
        public String info;
    }
}
