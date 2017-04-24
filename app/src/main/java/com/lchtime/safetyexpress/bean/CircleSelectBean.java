package com.lchtime.safetyexpress.bean;

/**
 * Created by yxn on 2017/4/21.
 */

public class CircleSelectBean {
    private boolean isSelect;

    public CircleSelectBean(boolean isSelect) {
        this.isSelect = isSelect;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
