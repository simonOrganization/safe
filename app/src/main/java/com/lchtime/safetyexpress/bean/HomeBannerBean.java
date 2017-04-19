package com.lchtime.safetyexpress.bean;

/**
 *
 * 主页Banner
 * Created by user on 2017/2/23.
 */

public class HomeBannerBean {
    private String imgurl;
    private String txtContent;

    public String getTxtContent() {
        return txtContent;
    }

    public void setTxtContent(String txtContent) {
        this.txtContent = txtContent;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
}
