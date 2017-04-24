package com.lchtime.safetyexpress.bean;

import java.util.ArrayList;

/**
 * Created by yxn on 2017/4/20.
 */

public class CircleOneBean {
    private ArrayList<CircleTwoBean> images;

    public CircleOneBean(ArrayList<CircleTwoBean> images) {
        this.images = images;
    }

    public ArrayList<CircleTwoBean> getImages() {
        return images;
    }

    public void setImages(ArrayList<CircleTwoBean> images) {
        this.images = images;
    }
}
