package com.lchtime.safetyexpress.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by android-cp on 2017/5/18.
 */

public class HotCircleBean implements Serializable{

    /**
     * code : 10
     * info : 获取首页热点圈子成功
     */

    public ResultBean result;
    /**
     * ud_ub_id : 83
     * ud_photo_fileid :
     * ud_nickname :
     * dycount : 3
     */

    public List<List<HotBean>> hot;

    public static class ResultBean implements Serializable{
        public String code;
        public String info;
    }

    public static class HotBean implements Serializable{
        public String ud_ub_id;
        public String ud_photo_fileid;
        public String ud_nickname;
        public int dycount;
        //自己打的标记
        public boolean checked;
    }
}
