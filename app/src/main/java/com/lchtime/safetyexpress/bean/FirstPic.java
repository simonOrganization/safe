package com.lchtime.safetyexpress.bean;

import java.util.List;

/**
 * Created by Dreamer on 2017/6/19.
 */

public class FirstPic {

    /**
     * code : 10
     * info : 搜索轮播图成功
     */

    public BasicResult result;
    /**
     * img : http://fcar.lchtime.cn:8001/upload/pub/2017/06/16/20864d65566abbcd4e5293ad614f293f.png
     * url : sss
     */

    public List<LunboBean> lunbo;

    public static class LunboBean {
        public String img;
        public String url;
    }
}
