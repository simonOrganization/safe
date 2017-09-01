package com.lchtime.safetyexpress.ui.vip.fragment;

import com.lchtime.safetyexpress.ui.home.fragment.GuanZhuFragment;
import com.lchtime.safetyexpress.ui.home.fragment.TiWenFragment;
import com.lchtime.safetyexpress.ui.home.fragment.WenDaFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by android-cp on 2017/4/21.
 */

public class FragmentFactory2WenDa {
    public static final int FRAGMENT_GUANZHU = 0;//关注
    public static final int FRAGMENT_HUIDA = 1;//回答
    public static final int FRAGMENT_TIWEN = 2;//提问

    public Map<Integer,BaseFragment> mCacheFragments = new HashMap<>();

    public BaseFragment createFragment(int position){
        BaseFragment fragment = null;

        if(mCacheFragments.containsKey(position)){

            fragment = mCacheFragments.get(position);

            return fragment;
        }

        switch (position) {


            case FRAGMENT_GUANZHU://返回 关注 对应的fragment
                fragment = new GuanZhuFragment();
                break;
            case FRAGMENT_HUIDA://返回 回答 对应的fragment
                fragment = new WenDaFragment();
                break;
            case FRAGMENT_TIWEN://返回 提问 对应的fragment
                fragment = new TiWenFragment();
                break;
        }

        mCacheFragments.put(position,fragment);
        return fragment;
    }
}
