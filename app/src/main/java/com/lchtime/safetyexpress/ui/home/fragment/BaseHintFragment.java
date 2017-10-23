package com.lchtime.safetyexpress.ui.home.fragment;

import android.support.v4.app.Fragment;

/**
 * Created by ${Hongcha36} on 2017/10/20.
 */

public abstract class BaseHintFragment extends Fragment{

    /** Fragment当前状态是否可见 */
    protected boolean isVisible;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(getUserVisibleHint()) { //当前fragment可见
            isVisible = true;
            //onVisible();*/
            lazyLoad();
        } else {
            isVisible = false;
            //onInvisible();*/
        }

    }


    /**
     * 延迟加载
     * 子类必须重写此方法
     */
    protected abstract void lazyLoad();

}
