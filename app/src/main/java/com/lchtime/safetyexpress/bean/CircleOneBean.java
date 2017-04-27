package com.lchtime.safetyexpress.bean;

import java.util.ArrayList;

/**
 * Created by yxn on 2017/4/20.
 */

public class CircleOneBean {
    private ArrayList<String> images;

    public CircleOneBean(ArrayList<String> images) {
        this.images = images;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }
}
