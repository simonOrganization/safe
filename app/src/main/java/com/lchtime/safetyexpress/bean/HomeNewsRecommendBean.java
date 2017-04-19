package com.lchtime.safetyexpress.bean;

import java.io.Serializable;

/**
 * Created by user on 2017/4/17.
 */

public class HomeNewsRecommendBean implements Serializable {

    private String type;// 1:图片 2:视频 3:单张图片

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
