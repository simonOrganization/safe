package com.lchtime.safetyexpress.bean;

import java.util.List;

/**
 * Created by Dreamer on 2017/5/20.
 */

public class WordSerchBean {

    /**
     * code : 10
     * info : 获取热门搜索词成功
     */

    public ResultBean result;
    /**
     * result : {"code":"10","info":"获取热门搜索词成功"}
     * hot : ["人","好","哈哈","还没","嗯啊"]
     */

    public List<String> hot;

    public static class ResultBean {
        public String code;
        public String info;
    }
}
