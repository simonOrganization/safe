package com.lchtime.safetyexpress.bean;

import java.util.List;

/**
 * Created by android-cp on 2017/4/27.
 */

public class PostBean {

    public BasicResult result;

    public List<PostItemBean> gw;

    public class PostItemBean{
        public String gw_id;
        public String gw_name;
        public boolean isSelect;
    }

}
