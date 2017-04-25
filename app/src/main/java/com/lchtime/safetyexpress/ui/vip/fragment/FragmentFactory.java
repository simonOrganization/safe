package com.lchtime.safetyexpress.ui.vip.fragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by android-cp on 2017/4/21.
 */

public class FragmentFactory {
    public static final int FRAGMENT_NEWS = 0;//新闻
    public static final int FRAGMENT_VEDIO = 1;//视频
    public static final int FRAGMENT_CIRCLE = 2;//圈子

    public static Map<Integer,BaseFragment> mCacheFragments = new HashMap<>();

    public static BaseFragment createFragment(int position){
        BaseFragment fragment = null;

        if(mCacheFragments.containsKey(position)){

            fragment = mCacheFragments.get(position);

            return fragment;
        }

        switch (position) {


            case FRAGMENT_NEWS://返回 新闻 对应的fragment
                fragment = new NewsFragment();
                break;
            case FRAGMENT_VEDIO://返回 视频 对应的fragment
                fragment = new VedioFragment();
                break;
            case FRAGMENT_CIRCLE://返回 圈子 对应的fragment
                fragment = new CircleFragment();
                break;
        }

        mCacheFragments.put(position,fragment);
        return fragment;
    }
}
