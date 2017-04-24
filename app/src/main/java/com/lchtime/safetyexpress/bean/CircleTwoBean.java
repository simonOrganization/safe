package com.lchtime.safetyexpress.bean;

import android.graphics.Bitmap;

import butterknife.BindView;

/**
 * Created by yxn on 2017/4/20.
 */

public class CircleTwoBean {
    private String imageUrl;
    private Bitmap image;

    public CircleTwoBean() {
    }

    public CircleTwoBean(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public CircleTwoBean(Bitmap image) {
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
