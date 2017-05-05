package com.lchtime.safetyexpress.bean.res;

import com.lchtime.safetyexpress.bean.BasicResult;
import com.lchtime.safetyexpress.bean.NewTypeBean;
import com.lchtime.safetyexpress.bean.NewsBean;

import java.util.ArrayList;

/**
 * Created by yxn on 2017/4/25.
 */

public class NewsRes {
    private BasicResult result;
    private ArrayList<NewTypeBean> cms_dir;
    public ArrayList<NewsBean> tj;
    public ArrayList<NewsBean> hot;

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

    public ArrayList<NewsBean> getTj() {
        return tj;
    }

    public void setTj(ArrayList<NewsBean> tj) {
        this.tj = tj;
    }
}
