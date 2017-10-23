package com.lchtime.safetyexpress.bean.res;

import com.lchtime.safetyexpress.bean.BasicResult;
import com.lchtime.safetyexpress.bean.NewTypeBean;
import com.lchtime.safetyexpress.bean.NewsBean;

import java.util.ArrayList;

/**
 * Created by android-cp on 2017/5/8.
 */

public class VideoRes {
    public BasicResult result;
    public ArrayList<NewTypeBean> cms_dir;
    //新闻列表
    public ArrayList<NewsBean> cms_context;

    public BasicResult getResult() {
        return result;
    }

    public void setResult(BasicResult result) {
        this.result = result;
    }

    public ArrayList<NewTypeBean> getCms_dir() {
        return cms_dir;
    }

    public void setCms_dir(ArrayList<NewTypeBean> cms_dir) {
        this.cms_dir = cms_dir;
    }


    @Override
    public String toString() {
        return "VideoRes{" +
                "result=" + result +
                ", cms_dir=" + cms_dir +
                ", cms_context=" + cms_context +
                '}';
    }
}
