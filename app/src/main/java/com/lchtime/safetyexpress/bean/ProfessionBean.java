package com.lchtime.safetyexpress.bean;

import java.util.List;

/**
 * Created by android-cp on 2017/4/27.
 */

public class ProfessionBean {

    public BasicResult result;

    public List<ProfessionItemBean> hy;

    public class ProfessionItemBean{
        public String hy_id;
        public String hy_name;
    }

}
